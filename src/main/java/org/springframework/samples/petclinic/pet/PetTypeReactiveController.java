package org.springframework.samples.petclinic.pet;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Mono;

/**
 * Reactive CRUD operations for Pet Types using Spring WebFlux
 */
@RestController
@RequestMapping("/petclinic/api/pettypes")
@CrossOrigin(
 methods = {PUT, POST, GET, OPTIONS, DELETE, PATCH},
 maxAge = 3600,
 allowedHeaders = {"x-requested-with", "origin", "content-type", "accept"},
 origins = "*"
)
@Api(value="/petclinic/api/pettypes", tags = {"Pet Types Api"})
public class PetTypeReactiveController {
    
    /** Inject service implementation layer. */
    private PetReactiveServices petServices;
    
    /** Injection with controller. */
    public PetTypeReactiveController(PetReactiveServices petServices) {
        this.petServices = petServices;
    }
    
    /**
     * List all pet types from reference tables.
     *
     * @return
     *      A set of all pets
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Read all pet types from database", 
                  response=PetType.class)
    @ApiResponses({
      @ApiResponse(code = 200, message= "List of pet types"), 
      @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Set<PetType>>> getAllPetTypes() {
        return petServices.findAllPetTypes().map(ResponseEntity::ok);
    }
    
    @GetMapping(value = "/{petTypeId}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Retrieve pet information information by its unique identifier", response=PetType.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "the identifier exists and related pet type is returned"), 
        @ApiResponse(code = 400, message= "The name was not valid"), 
        @ApiResponse(code = 500, message= "Internal technical error") })        
    public Mono<ResponseEntity<PetType>> getType(
            @PathVariable("petTypeId") 
            @Parameter(required = true,example = "surgery",
            description = "Unique identifier of a Pet Type") String name) {
        return petServices.listPetType()
                          .map(set -> (set.contains(name) ? 
                        new ResponseEntity<PetType>(new PetType(name), HttpStatus.OK) :
                        new ResponseEntity<PetType>(HttpStatus.NOT_FOUND)));
    }
    
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PetType>> addType(@RequestBody PetType petType) {
        return petServices.addPetType(petType).map(ResponseEntity::ok);
    }
    
    @PutMapping(value = "/{petTypeId}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PetType>> updatePetType(
            @PathVariable("petTypeId") String name, 
            @RequestBody PetType petType) {
        petType.setId(petType.getName());
        return petServices.replacePetType(name, petType.getName())
                          .map(ResponseEntity::ok);
    }
    
    @DeleteMapping(value = "/{petTypeId}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> deletePetType(@PathVariable("petTypeId") String name){
        return petServices.removePetType(name)
                          .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }

}
