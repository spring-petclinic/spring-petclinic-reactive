package org.springframework.samples.petclinic.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNull;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.db.OwnerEntity;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetType;
import org.springframework.samples.petclinic.pet.db.PetEntity;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetSpecialty;
import org.springframework.samples.petclinic.vet.db.VetEntity;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.db.VisitEntity;

/**
 * The objects used in the DAOs are not the same as the one exposed in the REST API.
 * The purpose is to decouple exposition layer from implementation.
 * 
 * We enforce data conversion using a {@link DateTimeFormatter} instead of delegating 
 * data conversion to the Jackson framework.
 */
public class MappingUtils {
    
    /** Date format to be used in the REST API. (which can only use Strings). */
    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /** Hide constructor to enforce static usage. */
    private MappingUtils() {}
    
    /**
     * Convert a {@link LocalDate} to a String using the forma yyyy/MM/dd.
     */
    public static String localDate2String(@NonNull LocalDate source) {
        return FORMATTER.format(source);
    }
    
    /**
     * Parse a String as a {@link LocalDate} expecting formay yyyy/MM/dd.
     */
    public static LocalDate string2LocalDate(@NotBlank String source) {
        return LocalDate.from(FORMATTER.parse(source));
    }
    
    /**
     * Working with PET business domain.
     *
     * Map the Entity (dao layer) to web bean (exposition layer).
     */
    public static Pet mapEntityAsPet(@NotNull PetEntity entity) {
        Pet wb = new Pet();
        wb.setId(entity.getPetId());
        wb.setOwner(new Owner(entity.getOwnerId()));
        wb.setName(entity.getName());
        wb.setType(new PetType(entity.getPetType()));
        wb.setBirthDate(localDate2String(entity.getBirthDate()));
        return wb;
    }
    
    public static Vet mapEntityAsVet(@NotNull VetEntity dto) {
        Vet v = new Vet();
        v.setId(dto.getId());
        v.setFirstName( dto.getFirstName());
        v.setLastName(dto.getLastName());
        v.setSpecialties(dto.getSpecialties()
                   .stream()
                   .map(VetSpecialty::new)
                   .collect(Collectors.toSet()));
        return v;
    }
    
    public static VetEntity mapVetAsEntity(@NotNull Vet vet) {
       return new VetEntity(vet.getId(), vet.getFirstName(), vet.getLastName(), 
                vet.getSpecialties().stream()
                          .map(VetSpecialty::getName)
                          .collect(Collectors.toSet()));
    }
    
    /**
     * Working with PET business domain.
     *
     * Map the web bean (exposition layer) to Entity (dao layer).
     */
    public static PetEntity mapPetAsEntity(@NonNull Pet wb) {
        PetEntity entity = new PetEntity();
        entity.setPetId(wb.getId());
        entity.setOwnerId(wb.getOwner().getId());
        entity.setName(wb.getName());
        entity.setBirthDate(string2LocalDate(wb.getBirthDate()));
        entity.setPetType(wb.getType().getName());
        return entity;
    }
    
    /**
     * Working with OWNER business domain.
     *
     * Map the Entity (dao layer) to web bean (exposition layer).
     */
    public static Owner mapEntityAsOwner(@NotNull OwnerEntity o) {
        Objects.requireNonNull(o);
        Owner wb = new Owner();
        wb.setAddress(o.getAddress());
        wb.setCity(o.getCity());
        wb.setFirstName(o.getFirstName());
        wb.setLastName(o.getLastName());
        wb.setTelephone(o.getTelephone());
        wb.setPets(new HashSet<>());
        wb.setId(o.getId());
        return wb;
    }
    
    /**
     * Working with OWNER business domain.
     *
     * Map the web bean (exposition layer) to Entity (dao layer).
     */
    public static OwnerEntity mapOwnerAsEntity(@NotNull Owner wb) {
        OwnerEntity o = new OwnerEntity();
        o.setId(wb.getId());
        o.setAddress(wb.getAddress());
        o.setCity(wb.getCity());
        o.setFirstName(wb.getFirstName());
        o.setLastName(wb.getLastName());
        o.setTelephone(wb.getTelephone());
        return o;
    }
    
    /**
     * Working with VISIT business domain.
     *
     * Map the Entity (dao layer) to web bean (exposition layer).
     */
    public static Visit mapEntityAsVisit(@NotNull VisitEntity entity) {
        Objects.requireNonNull(entity);
        Visit wb = new Visit();
        wb.setId(entity.getVisitId());
        wb.setDescription(entity.getDescription());
        wb.setDate(localDate2String(entity.getVisitDate()));
        wb.setPet(new Pet(entity.getPetId()));
        return wb;
    }
    
    /**
     * Working with VISIT business domain.
     *
     * Map the web bean (exposition layer) to Entity (dao layer).
     */
    public static VisitEntity mapVisitAsEntity(@NotNull Visit wb) {
        Objects.requireNonNull(wb);
        VisitEntity v = new VisitEntity();
        v.setVisitId(wb.getId());
        v.setPetId(wb.getPet().getId());
        v.setDescription(wb.getDescription());
        v.setVisitDate(string2LocalDate(wb.getDate()));
        return v;
    }
    
}
