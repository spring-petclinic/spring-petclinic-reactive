package org.springframework.samples.petclinic.visit;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.conf.MappingUtils;
import org.springframework.samples.petclinic.owner.db.OwnerEntity;
import org.springframework.samples.petclinic.pet.PetReactiveDaoMapperBuilder;
import org.springframework.samples.petclinic.pet.db.PetEntity;
import org.springframework.samples.petclinic.pet.db.PetReactiveDao;
import org.springframework.samples.petclinic.reflist.ReferenceListReactiveDao;
import org.springframework.samples.petclinic.visit.db.VisitEntity;
import org.springframework.samples.petclinic.visit.db.VisitReactiveDao;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.datastax.oss.driver.api.core.CqlSession;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive CRUD operation (WEbFlux) for entity Visits.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@RestController
@RequestMapping("/petclinic/api/visits")
@CrossOrigin(
 methods = {PUT, POST, GET, OPTIONS, DELETE, PATCH},
 maxAge = 3600,
 allowedHeaders = {"x-requested-with", "origin", "content-type", "accept"},
 origins = "*"
)
@Api(value="/api/visits", tags = {"Visit Api"})
public class VisitReactiveController {
    
    /** Implementation of Crud for repo. */
    private VisitReactiveDao visitDao;
    
    /** Implementation of Crud for repo. */
    private PetReactiveDao petDao;
    
    /**
     * Injection with controller
     */
    public VisitReactiveController(CqlSession cqlSession, ReferenceListReactiveDao refList) {
        this.visitDao = new VisitReactiveDaoMapperBuilder(cqlSession).build()
                            .visitDao(cqlSession.getKeyspace().get());
        this.petDao = new PetReactiveDaoMapperBuilder(cqlSession).build()
                            .petDao(cqlSession.getKeyspace().get());
    }
    
    /**
     * Read all pets from database.
     *
     * @return
     *   a {@link Flux} containing {@link PetEntity}
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Read all visits in database", response=Visit.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "List of visits (even if empty)"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Flux<Visit> findAllVisits() {
        return Flux.from(visitDao.findAllReactive())
                   .map(MappingUtils::mapEntityToVisit)
                   .flatMap(petDao::populatePetForVisit);
    }
    
    /**
     * Retrieve visit information from its unique identifier (even if not PK)
     *
     * @param ownerId
     *      unique identifer as a String, to be converted in {@link UUID}.
     * @return
     *      a {@link Mono} of {@link OwnerEntity} or empty response with not found (404) code
     */
    @GetMapping(value = "/{visitId}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Retrieve visit information from its unique identifier", response=Visit.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "the identifier exists and related visit is returned"), 
        @ApiResponse(code = 400, message= "The uid was not a valid UUID"), 
        @ApiResponse(code = 404, message= "the identifier does not exists in DB"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Visit>> findVisitById(@PathVariable("visitId") @Parameter(
               required = true,example = "1ff2fbd9-bbb0-4cc1-ba37-61966aa7c5e6",
               description = "Unique identifier of a Owner") String visitId) {
        return visitDao.findByVisitIdReactive((UUID.fromString(visitId)))
                   .map(MappingUtils::mapEntityToVisit)
                   .flatMap(petDao::populatePetForVisit)
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * Create a {@link VisitEntity} when we don't know the visit identifier.
     *
     * @param request
     *      current http request
     * @param vetRequest
     *      fields required to create visit (no uid)
     * @return
     *      the created owner.
     */
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes=APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Create a new visit, an unique identifier is generated and returned", 
                  response=Visit.class)
    @ApiResponses({
        @ApiResponse(code = 201, message= "The visit has been created, uuid is provided in header"), 
        @ApiResponse(code = 400, message= "Invalid Dto provided"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Visit>> createVisit(
            UriComponentsBuilder ucBuilder,
            @RequestBody WebBeanVisitCreation dto) {
      VisitEntity v = MappingUtils.fromVisitWebBeanCreationToEntity(dto);
      v.setVisitId(UUID.randomUUID());
      return visitDao.save(v)
              .map(MappingUtils::mapEntityToVisit)
              .map(created -> ResponseEntity.created(
                      ucBuilder.path("/api/owners/{id}")
                               .buildAndExpand(created.getId().toString())
                               .toUri())
              .body(created));
    }
    
    /**
     * Create or update a {@link VisitEntity}. We do not throw exception is already exist
     * or check existence as this is the behavirous in a cassandra table to read
     * before write.
     *
     * @param visitid
     *      unique identifier for visit
     * @param visit
     *      current visit
     * @return
     *      the created visit.
     */
    @PutMapping(value="/{visitId}",  
                consumes=APPLICATION_JSON_VALUE,
                produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Upsert a pet (no read before write as for Cassandra)", 
                  response=Visit.class)
    @ApiResponses({
        @ApiResponse(code = 201, message= "The visit has been created, uuid is provided in header"), 
        @ApiResponse(code = 400, message= "The visit bean was not OK"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Visit>> upsertVisit(
            UriComponentsBuilder ucBuilder, 
            @PathVariable("visitId") String visitId, 
            @RequestBody Visit visit) {
      Objects.requireNonNull(visit);
      Assert.isTrue(UUID.fromString(visitId).equals(visit.getId()), 
              "Visit");
      return visitDao.save(MappingUtils.mapVisitToEntity(visit)) // change to entity
                   .map(MappingUtils::mapEntityToVisit)          // back to web
                   .flatMap(petDao::populatePetForVisit)
                   .map(created -> ResponseEntity.created(
                      ucBuilder.path("/api/owners/{id}")
                               .buildAndExpand(created.getId().toString())
                               .toUri())
                   .body(created));
    }
    
    /**
     * Delete a visit from its unique identifier.
     *
     * @param visitid
     *      visit identifier
     * @return
     */
    @DeleteMapping("/{visitId}")
    @ApiOperation(value= "Delete a visit from its unique identifier", response=Void.class)
    @ApiResponses({
        @ApiResponse(code = 204, message= "The pet has been deleted"), 
        @ApiResponse(code = 400, message= "The uid was not a valid UUID"),
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("visitId") @Parameter(
            required = true,example = "1ff2fbd9-bbb0-4cc1-ba37-61966aa7c5e6",
            description = "Unique identifier of a visit") String visitId) {
        // We need the owner id first to delete
        return visitDao.findByVisitIdReactive(UUID.fromString(visitId))
                       .flatMap(visitDao::delete)
                       .map(v -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
    }
}
