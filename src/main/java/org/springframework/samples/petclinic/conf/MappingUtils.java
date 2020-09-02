package org.springframework.samples.petclinic.conf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.db.OwnerEntity;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.WebBeanPet;
import org.springframework.samples.petclinic.pet.WebBeanPetCreation;
import org.springframework.samples.petclinic.pet.WebBeanPetType;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.WebBeanVisit;
import org.springframework.samples.petclinic.visit.WebBeanVisitCreation;

/**
 * No custom Jackson serializer of Spring Convert as the data entities
 * are quite different from exposed web beans.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class MappingUtils {
    
    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private MappingUtils() {}
    
    public static String localDate2String(@NonNull LocalDate source) {
        return FORMATTER.format(source);
    }
    
    public static LocalDate string2LocalDate(@NonNull String source) {
        return LocalDate.from(FORMATTER.parse(source));
    }
    
    public static WebBeanPet fromPetEntityToWebBean(Pet entity) {
        Objects.requireNonNull(entity);
        WebBeanPet wb = new WebBeanPet();
        wb.setId(entity.getPetId());
        wb.setOwner(new Owner(entity.getOwnerId()));
        wb.setName(entity.getName());
        wb.setType(new WebBeanPetType(entity.getPetType()));
        wb.setBirthDate(localDate2String(entity.getBirthDate()));
        return wb;
    }
    
    public static Pet fromPetWebBeanToEntity(WebBeanPet wb) {
        Objects.requireNonNull(wb);
        Pet entity = fromPetWebBeanCreationToEntity(wb);
        entity.setPetId(wb.getId());
        return entity;
    }
    
    public static Pet fromPetWebBeanCreationToEntity(WebBeanPetCreation wb) {
        Objects.requireNonNull(wb);
        Pet entity = new Pet();
        entity.setOwnerId(wb.getOwner().getId());
        entity.setName(wb.getName());
        entity.setBirthDate(string2LocalDate(wb.getBirthDate()));
        entity.setPetType(wb.getType().getName());
        return entity;
    }
    
    public static WebBeanVisit fromVisitEntityToWebBean(Visit entity) {
        Objects.requireNonNull(entity);
        WebBeanVisit wb = new WebBeanVisit();
        wb.setId(entity.getVisitId());
        wb.setDescription(entity.getDescription());
        wb.setDate(localDate2String(entity.getVisitDate()));
        wb.setPet(new WebBeanPet(entity.getPetId()));
        return wb;
    }
    
    public static Visit fromVisitWebBeanToEntity(WebBeanVisit wb) {
        Objects.requireNonNull(wb);
        Visit v = fromVisitWebBeanCreationToEntity(wb);
        v.setVisitId(wb.getId());
        return v;
    }
    
    public static Visit fromVisitWebBeanCreationToEntity(WebBeanVisitCreation wbc) {
        Objects.requireNonNull(wbc);
        Visit v = new Visit();
        v.setPetId(wbc.getPet().getId());
        v.setDescription(wbc.getDescription());
        v.setVisitDate(string2LocalDate(wbc.getDate()));
        return v;
    }
    
    public static Owner fromOwnerToEntity(OwnerEntity o) {
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
    
    public static OwnerEntity fromEntityToOwner(Owner wb) {
        OwnerEntity o = new OwnerEntity();
        o.setId(wb.getId());
        o.setAddress(wb.getAddress());
        o.setCity(wb.getCity());
        o.setFirstName(wb.getFirstName());
        o.setLastName(wb.getLastName());
        o.setTelephone(wb.getTelephone());
        return o;
    }
    
}
