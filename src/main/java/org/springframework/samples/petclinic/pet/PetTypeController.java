package org.springframework.samples.petclinic.pet;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.reflist.ReferenceListReactiveDao;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.oss.driver.api.core.CqlSession;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/petclinic/api/pettypes")
@CrossOrigin(
 methods = {PUT, POST, GET, OPTIONS, DELETE, PATCH},
 maxAge = 3600,
 allowedHeaders = {"x-requested-with", "origin", "content-type", "accept"},
 origins = "*"
)
@Api(value="/petclinic/api/pettypes", tags = {"Pet Types Api"})
public class PetTypeController {
    
    /** List available lists. */
    private final ReferenceListReactiveDao refList;
    
    /**
     * Injection with controller
     */
    public PetTypeController(CqlSession cqlSession, ReferenceListReactiveDao refList) {
        this.refList = refList;
    }
    
    /**
     * Read all pet types from database.
     *
     * @return
     *   a {@link Flux} containing {@link PetTypeWebBean}
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Read all pet types from database", 
                  response=PetTypeWebBean.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "List of pet types"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Set<PetTypeWebBean>>> getAllPetTypes() {
        return refList.listPetType()
                      .map(Set::stream)
                      .map(s -> s.map(PetTypeWebBean::new).collect(Collectors.toSet()))
                      .map(ResponseEntity::ok);
    }
    
    @GetMapping(value = "/{petTypeId}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Retrieve pet information information from its unique identifier", response=PetTypeWebBean.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "the identifier exists and related pet type is returned"), 
        @ApiResponse(code = 400, message= "The name was not valid"), 
        @ApiResponse(code = 500, message= "Internal technical error") })        
    public Mono<ResponseEntity<PetTypeWebBean>> getType(
            @PathVariable("petTypeId") 
            @Parameter(required = true,example = "surgery",
            description = "Unique identifier of a Pet Type") String name) {
        return refList.listPetType()
                      .map(set -> {
                          if (set.contains(name)) {
                              return new ResponseEntity<PetTypeWebBean>(new PetTypeWebBean(name), HttpStatus.OK);
                          }
                          return new ResponseEntity<PetTypeWebBean>(HttpStatus.NOT_FOUND);
                      });
    }
    
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PetTypeWebBean>> addType(
            @RequestBody PetTypeWebBean petType) {
        return refList.addPetType(petType.getName())
                      .thenReturn(petType)
                      .map(ResponseEntity::ok);
    }
    
    @PutMapping(value = "/{petTypeId}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<PetTypeWebBean>> updatePetType(
            @PathVariable("petTypeId") String name, 
            @RequestBody PetTypeWebBean petType) {
        petType.setId(petType.getName());
        return refList.replacePetType(name, petType.getName())
                      .map(PetTypeWebBean::new)
                      .map(ResponseEntity::ok);
    }
    
    @DeleteMapping(value = "/{petTypeId}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> deletePetType(@PathVariable("petTypeId") String name){
        return refList.removePetType(name)
                      .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }

}
