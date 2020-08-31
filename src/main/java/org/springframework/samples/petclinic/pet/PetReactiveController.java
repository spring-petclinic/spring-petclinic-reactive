package org.springframework.samples.petclinic.pet;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.conf.MappingUtils;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.reflist.ReferenceListReactiveDao;
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
 * Reactive CRUD operation (WEbFlux) for entity Vet.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@RestController
@RequestMapping("/petclinic/api/pets")
@CrossOrigin(
 methods = {PUT, POST, GET, OPTIONS, DELETE, PATCH},
 maxAge = 3600,
 allowedHeaders = {"x-requested-with", "origin", "content-type", "accept"},
 origins = "*"
)
@Api(value="/api/pets", tags = {"Pets Api"})
public class PetReactiveController {
    
    /** Implementation of Crud for repo. */
    private PetReactiveDao petDao;
    
    /** List available lists. */
    private final ReferenceListReactiveDao refList;
    
    /**
     * Injection with controller
     */
    public PetReactiveController(CqlSession cqlSession, ReferenceListReactiveDao refList) {
        this.petDao = new PetReactiveDaoMapperBuilder(cqlSession).build()
                            .petDao(cqlSession.getKeyspace().get());
        this.refList = refList;
    }
    
    /**
     * Read all pets from database.
     *
     * @return
     *   a {@link Flux} containing {@link Pet}
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Read all pets in database", response=WebBeanPet.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "List of pets (even if empty)"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Flux<WebBeanPet> findAllPets() {
        return Flux.from(petDao.findAllReactive())
                   .map(MappingUtils::fromPetEntityToWebBean);
    }
    
    /**
     * Retrieve pets information from its unique identifier (even if not PK)
     *
     * @param ownerId
     *      unique identifer as a String, to be converted in {@link UUID}.
     * @return
     *      a {@link Mono} of {@link Owner} or empty response with not found (404) code
     */
    @GetMapping(value = "/{petId}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Retrieve pet information from its unique identifier", response=WebBeanPet.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "the identifier exists and related pet is returned"), 
        @ApiResponse(code = 400, message= "The uid was not a valid UUID"), 
        @ApiResponse(code = 404, message= "the identifier does not exists in DB"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<WebBeanPet>> findPetById(@PathVariable("petId") @Parameter(
               required = true,example = "1ff2fbd9-bbb0-4cc1-ba37-61966aa7c5e6",
               description = "Unique identifier of a Owner") String petId) {
        return Mono.from(petDao.findByIdReactive(UUID.fromString(petId)))
                   .map(MappingUtils::fromPetEntityToWebBean) // <-- TODO : probably populating Visits
                   .map(ResponseEntity::ok)
                   .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    /**
     * TODO: This is a duplications from PetTypeController.
     * - (copied behaviour from existing REST implementation)
     */
    @GetMapping(value = "/pettypes", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Read all pet typesfrom database", 
                  response=WebBeanPetType.class)
    @ApiResponses({
      @ApiResponse(code = 200, message= "List of pet types"), 
      @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Set<WebBeanPetType>>> getAllPetTypes() {
        return refList.listPetType()
                .map(Set::stream)
                .map(s -> s.map(WebBeanPetType::new).collect(Collectors.toSet()))
                .map(ResponseEntity::ok);
    }
    
    /**
     * Create a {@link Pet} when we don't know the identifier.
     *
     * @param request
     *      current http request
     * @param vetRequest
     *      fields required to create pet (no uid)
     * @return
     *      the created owner.
     */
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes=APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Create a new Pet, an unique identifier is generated and returned", 
                  response=WebBeanPet.class)
    @ApiResponses({
        @ApiResponse(code = 201, message= "The pet has been created, uuid is provided in header"), 
        @ApiResponse(code = 400, message= "Invalid Dto provided"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<WebBeanPet>> createPet(
            UriComponentsBuilder ucBuilder,
            @RequestBody WebBeanPetCreation dto) {
      Pet pet = MappingUtils.fromPetWebBeanCreationToEntity(dto);
      pet.setPetId(UUID.randomUUID());
      return petDao.save(pet)
              .map(MappingUtils::fromPetEntityToWebBean)
              .map(created -> ResponseEntity.created(
                      ucBuilder.path("/api/owners/{id}")
                               .buildAndExpand(created.getId().toString())
                               .toUri())
              .body(created));
    }
    
    /**
     * Create or update a {@link Pet}. We do not throw exception is already exist
     * or check existence as this is the behavirous in a cassandra table to read
     * before write.
     *
     * @param ownerId
     *      unique identifier for pet
     * @param owner
     *      person as owner
     * @return
     *      the created owner.
     */
    @PutMapping(value="/{petId}",  
                consumes=APPLICATION_JSON_VALUE,
                produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Upsert a pet (no read before write as for Cassandra)", 
                  response=Pet.class)
    @ApiResponses({
        @ApiResponse(code = 201, message= "The owner has been created, uuid is provided in header"), 
        @ApiResponse(code = 400, message= "The owner bean was not OK"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<WebBeanPet>> upsertPet(
            UriComponentsBuilder ucBuilder, 
            @PathVariable("petId") String petId, @RequestBody WebBeanPet pet) {
      Objects.requireNonNull(pet);
      Assert.isTrue(UUID.fromString(petId).equals(pet.getId()), 
              "Pet identifier provided in pet does not match the value if path");
      return petDao.save(MappingUtils.fromPetWebBeanToEntity(pet)) // change to entity
                   .map(MappingUtils::fromPetEntityToWebBean)      // back to web
                   .map(created -> ResponseEntity.created(
                      ucBuilder.path("/api/owners/{id}")
                               .buildAndExpand(created.getId().toString())
                               .toUri())
                   .body(created));
    }
    
    /**
     * Delete a pet from its unique identifier.
     *
     * @param vetId
     *      vetirinian identifier
     * @return
     */
    @DeleteMapping("/{petId}")
    @ApiOperation(value= "Delete a pet from its unique identifier", response=Void.class)
    @ApiResponses({
        @ApiResponse(code = 204, message= "The pet has been deleted"), 
        @ApiResponse(code = 400, message= "The uid was not a valid UUID"),
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("ownerId") @Parameter(
            required = true,example = "1ff2fbd9-bbb0-4cc1-ba37-61966aa7c5e6",
            description = "Unique identifier of a pet") String petId) {
        // We need the owner id to delete
        return petDao.delete(new Pet(UUID.fromString(petId)))
                     .map(v -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
    }
}
