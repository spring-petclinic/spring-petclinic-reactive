package org.springframework.samples.petclinic.vet;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.vet.db.VetEntity;
import org.springframework.samples.petclinic.vet.db.VetReactiveDao;
import org.springframework.samples.petclinic.vet.db.VetReactiveDaoMapperBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * Reactive CRUD operation (WEbFlux) for vet entity.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@RestController
@RequestMapping("/petclinic/api/vets")
@CrossOrigin(
 methods = {PUT, POST, GET, OPTIONS, DELETE, PATCH},
 maxAge = 3600,
 allowedHeaders = {"x-requested-with", "origin", "content-type", "accept"},
 origins = "*"
)
@Api(value="/petclinic/api/vets", tags = {"Veterinarians Api"})
public class VetReactiveController {
    
    /** Implementation of Crud operations for vets. */
    private final VetReactiveDao vetRepo;
    
    /**
     * Injection with controller
     */
    public VetReactiveController(CqlSession cqlSession) {
        this.vetRepo = new VetReactiveDaoMapperBuilder(cqlSession)
                .build().vetDao(cqlSession.getKeyspace().get());
    }
    
    /**
     * Read all veterinarians from database.
     *
     * @return
     *   a {@link Flux} containing {@link VetEntity}
     */
    @PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Read all veterinarians in database", response=Vet.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "List of veterinarians"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Flux<Vet> getAllVets() {
        return vetRepo.findAll().map(Vet::new);
    }
    
    /**
     * Retrieve veterinarian information by its unique identifier.
     *
     * @param vetId
     *      unique identifier as a String, to be converted in {@link UUID}.
     * @return
     *      a {@link Mono} of {@link VetEntity} or empty response with not found (404) code
     */
    @GetMapping(value = "/{vetId}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Retrieve veterinarian information by its unique identifier", response=Vet.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "the identifier exists and related veterinarian is returned"), 
        @ApiResponse(code = 400, message= "The uid was not a valid UUID"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Vet>> getVet(@PathVariable("vetId") @Parameter(
               required = true,example = "1ff2fbd9-bbb0-4cc1-ba37-61966aa7c5e6",
               description = "Unique identifier of a Veterinarian") String vetId) {
        return Mono.from(vetRepo.findByIdReactive(UUID.fromString(vetId)))
                      .map(Vet::new)
                      .map(ResponseEntity::ok)
                      .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Create a {@link VetEntity} when we don't know the identifier.
     *
     * @param request
     *      current http request
     * @param vetRequest
     *      fields required to create Vet (no uid)
     * @return
     *      the create vet.
     */
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes=APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Create a new veterinarian, an unique identifier is generated and returned", response=Vet.class)
    @ApiResponses({
        @ApiResponse(code = 201, message= "The veterinarian has been created, uuid is provided in header"),
        @ApiResponse(code = 400, message= "The uid was not a valid UUID"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Vet>> createVet(UriComponentsBuilder ucBuilder, @RequestBody Vet vetRequest) {
      Objects.requireNonNull(vetRequest);
      VetEntity input = new VetEntity(UUID.randomUUID(), vetRequest.getFirstName(), vetRequest.getLastName(), 
              vetRequest.getSpecialties().stream()
                        .map(VetSpecialty::getName)
                        .collect(Collectors.toSet()));
      return vetRepo.save(input)
                    .map(Vet::new)
                    .map(created -> ResponseEntity
                    .created(ucBuilder.path("/api/vets/{id}").buildAndExpand(created.getId()).toUri())
                    .body(created));
    }
    
    /**
     * Create or update a {@link VetEntity}. We do not throw an exception if the entity already exists
     * or check existence, as this would require a read before write, and Cassandra supports an
     * upsert style of interaction.
     *
     * @param request
     *      current http request
     * @param vetRequest
     *      fields required to create Vet (no uid)
     * @return
     *      the create vet.
     */
    @PutMapping(value="/{vetId}", produces = APPLICATION_JSON_VALUE, consumes=APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Upsert a veterinarian (no read before write as for Cassandra)", response=Vet.class)
    @ApiResponses({
        @ApiResponse(code = 201, message= "The veterinarian has been created, uuid is provided in header"),
        @ApiResponse(code = 400, message= "The uid was not a valid UUID"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Vet>> upsert(
            UriComponentsBuilder ucBuilder, 
            @PathVariable("vetId") String vetId, @RequestBody Vet vetRequest) {
      Objects.requireNonNull(vetRequest);
      VetEntity input = new VetEntity(vetRequest.getId(), vetRequest.getFirstName(), vetRequest.getLastName(), 
              vetRequest.getSpecialties().stream()
                        .map(VetSpecialty::getName)
                        .collect(Collectors.toSet()));
      Assert.isTrue(UUID.fromString(vetId).equals(vetRequest.getId()), "Vet identifier provided in vet does not match the value if path");
      return vetRepo.save(input)
                    .map(Vet::new)
                    .map(created -> ResponseEntity
                                .created(ucBuilder.path("/api/vets/{id}").buildAndExpand(created.getId()).toUri())
                                .body(created));
    }
    
    /**
     * Delete a veterinarian by its unique identifier.
     *
     * @param vetId
     *      veterinarian identifier
     * @return
     */
    @DeleteMapping("/{vetId}")
    @ApiOperation(value= "Delete a veterinarian by its unique identifier", response=Void.class)
    @ApiResponses({
        @ApiResponse(code = 204, message= "The veterinarian has been deleted"),
        @ApiResponse(code = 400, message= "The uid was not a valid UUID"),
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("vetId") @Parameter(
            required = true,example = "1ff2fbd9-bbb0-4cc1-ba37-61966aa7c5e6",
            description = "Unique identifier of a Veterinarian") String vetId) {
        return vetRepo.delete(new VetEntity(vetId)).map(v -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
    }
}
