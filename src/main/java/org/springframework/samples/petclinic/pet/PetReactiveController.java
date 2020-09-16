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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owner.db.OwnerEntity;
import org.springframework.samples.petclinic.pet.db.PetEntity;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive CRUD operation (WEbFlux) for pet entity.
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
    
    private PetReactiveServices petServices;
    
    /** Injection with controller. */
    public PetReactiveController(PetReactiveServices petServices) {
        this.petServices = petServices;
    }
    
    /**
     * Read all pets from database.
     *
     * @return
     *   a {@link Flux} containing {@link PetEntity}
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Read all pets in database", response=Pet.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "List of pets (even if empty)"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Flux<Pet> findAllPets() {
        return petServices.findAllPets();
    }

    // TODO: I don't understand the significance of "(even if not PK)".
    //  Why is this important to know? Are we taking advantage of an index in some circumstances but not others?
    /**
     * Retrieve a pet's information by its unique identifier (even if not PK)
     *
     * @param ownerId
     *      unique identifer as a String, to be converted in {@link UUID}.
     * @return
     *      a {@link Mono} of {@link OwnerEntity} or empty response with not found (404) code
     */
    @GetMapping(value = "/{petId}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Retrieve pet information by its unique identifier", response=Pet.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "the identifier exists and related pet is returned"), 
        @ApiResponse(code = 400, message= "The uid was not a valid UUID"), 
        @ApiResponse(code = 404, message= "the identifier does not exist in DB"),
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Pet>> findPetById(@PathVariable("petId") @Parameter(
               required = true,example = "1ff2fbd9-bbb0-4cc1-ba37-61966aa7c5e6",
               description = "Unique identifier of a Pet") String petId) {
        return petServices.findPetByPetId(UUID.fromString(petId))
                          .map(ResponseEntity::ok)
                          .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    // TODO: Not sure why this duplication is needed.
    //  And I don't see a "PetTypeController" only a "PetTypeReactiveController"
    /**
     * This is a duplication from PetTypeController.
     * - (copied behavior from existing REST implementation)
     */
    @GetMapping(value = "/pettypes", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Read all pet typesfrom database", 
                  response=PetType.class)
    @ApiResponses({
      @ApiResponse(code = 200, message= "List of pet types"), 
      @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Set<PetType>>> getAllPetTypes() {
        return petServices.findAllPetTypes().map(ResponseEntity::ok);
    }
    
    /**
     * Create a {@link PetEntity} when we don't know the identifier.
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
                  response=Pet.class)
    @ApiResponses({
        @ApiResponse(code = 201, message= "The pet has been created, uuid is provided in header"), 
        @ApiResponse(code = 400, message= "Invalid Dto provided"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Pet>> createPet(UriComponentsBuilder uc, @RequestBody Pet pet) {
        Objects.requireNonNull(pet);
        pet.setId(UUID.randomUUID());
        return petServices.createPet(pet)
                          .map(created -> mapPetAsHttpResponse(uc, created));
    }
    
    /**
     * Create or update a {@link PetEntity}. We do not throw an exception if the entity already exists
     * or check existence, as this would require a read before write, and Cassandra supports an
     * upsert style of interaction.
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
                  response=PetEntity.class)
    @ApiResponses({
        @ApiResponse(code = 201, message= "The owner has been created, uuid is provided in header"), 
        @ApiResponse(code = 400, message= "The owner bean was not OK"), // TODO: what does "not OK" mean"? malformed?
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Pet>> upsertPet(
            UriComponentsBuilder uc, 
            @PathVariable("petId") String petId, @RequestBody Pet pet) {
      Objects.requireNonNull(pet);
      Assert.isTrue(UUID.fromString(petId).equals(pet.getId()), 
              "Pet identifier provided in pet does not match the value if path");
      return petServices.createPet(pet)
                        .map(created -> mapPetAsHttpResponse(uc, created));
    }
    
    /**
     * Delete a pet by its unique identifier.
     *
     * @param vetId
     *      veterinarian identifier
     * @return
     */
    @DeleteMapping("/{petId}")
    @ApiOperation(value= "Delete a pet by its unique identifier", response=Void.class)
    @ApiResponses({
        @ApiResponse(code = 204, message= "The pet has been deleted"), 
        @ApiResponse(code = 400, message= "The uid was not a valid UUID"),
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("petId") @Parameter(
            required = true,example = "1ff2fbd9-bbb0-4cc1-ba37-61966aa7c5e6",
            description = "Unique identifier of a pet") String petId) {
        // We need the owner id first to delete
        return petServices.deletePetById(UUID.fromString(petId))
                     .map(v -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
    }
    
    protected ResponseEntity<Pet> mapPetAsHttpResponse(UriComponentsBuilder ucBuilder, Pet created) {
        return ResponseEntity.created(ucBuilder.path("/api/pets/{id}")
                        .buildAndExpand(created.getId().toString())
                        .toUri()).body(created);
    }
    
}
