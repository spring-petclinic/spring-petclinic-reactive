package org.springframework.samples.petclinic.vet;

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
@RequestMapping("/petclinic/api/specialties")
@CrossOrigin(
 methods = {PUT, POST, GET, OPTIONS, DELETE, PATCH},
 maxAge = 3600,
 allowedHeaders = {"x-requested-with", "origin", "content-type", "accept"},
 origins = "*"
)
@Api(value="/petclinic/api/specialties", tags = {"Veterinarian Specialties Api"})
public class VetSpecialtyController {
    
    /** List available lists. */
    private final ReferenceListReactiveDao refList;
    
    /**
     * Injection with controller
     */
    public VetSpecialtyController(CqlSession cqlSession, ReferenceListReactiveDao refList) {
        this.refList = refList;
    }
    
    /**
     * Read all veterinarians specialties from database.
     *
     * @return
     *   a {@link Flux} containing {@link VetSpecialtyWebBean}
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Read all veterinarians specialties from database", 
                  response=VetSpecialtyWebBean.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "List of veterinarians specialties"), 
        @ApiResponse(code = 500, message= "Internal technical error") })
    public Mono<ResponseEntity<Set<VetSpecialtyWebBean>>> getAllVetsSpecialties() {
        return refList.listVeretinianSpecialties()
                      .map(Set::stream)
                      .map(s -> s.map(VetSpecialtyWebBean::new).collect(Collectors.toSet()))
                      .map(ResponseEntity::ok);
    }
    
    @GetMapping(value = "/{specialtyId}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value= "Retrieve veterinarian information from its unique identifier", response=VetSpecialtyWebBean.class)
    @ApiResponses({
        @ApiResponse(code = 200, message= "the identifier exists and related veterinarian specialty is returned"), 
        @ApiResponse(code = 400, message= "The name was not valid"), 
        @ApiResponse(code = 500, message= "Internal technical error") })        
    public Mono<ResponseEntity<VetSpecialtyWebBean>> getSpecialty(
            @PathVariable("specialtyId") 
            @Parameter(required = true,example = "surgery",
            description = "Unique identifier of a Veterinarian specialty") String name) {
        return refList.listVeretinianSpecialties()
                      .map(set -> {
                          if (set.contains(name)) {
                              return new ResponseEntity<VetSpecialtyWebBean>(new VetSpecialtyWebBean(name), HttpStatus.OK);
                          }
                          return new ResponseEntity<VetSpecialtyWebBean>(HttpStatus.NOT_FOUND);
                      });
    }
    
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<VetSpecialtyWebBean>> addSpecialty(
            @RequestBody VetSpecialtyWebBean specialty) {
        return refList.addVeretinianSpecialty(specialty.getName())
                      .thenReturn(specialty)
                      .map(ResponseEntity::ok);
    }
    
    @PutMapping(value = "/{specialtyId}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<VetSpecialtyWebBean>> updateSpecialty(
            @PathVariable("specialtyId") String name, 
            @RequestBody VetSpecialtyWebBean specialty) {
        specialty.setId(specialty.getName());
        return refList.replaceVeretinianSpecialty(name, specialty.getName())
                      .map(VetSpecialtyWebBean::new)
                      .map(ResponseEntity::ok);
    }
    
    @DeleteMapping(value = "/{specialtyId}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> deleteSpecialty(@PathVariable("specialtyId") String name){
        return refList.removeFromVeretinianSpecialty(name)
                      .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }

}
