package org.springframework.samples.petclinic.conf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.WebBeanOwner;
import org.springframework.samples.petclinic.owner.WebBeanOwnerCreation;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.WebBeanPet;
import org.springframework.samples.petclinic.pet.WebBeanPetCreation;
import org.springframework.samples.petclinic.pet.WebBeanPetType;

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
        wb.setOwner(new WebBeanOwner(entity.getOwnerId()));
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
    
    public static Owner fromOwnerWebBeanCreationToEntity(WebBeanOwnerCreation wb) {
        Owner o = new Owner();
        o.setAddress(wb.getAddress());
        o.setCity(wb.getCity());
        o.setFirstName(wb.getFirstName());
        o.setLastName(wb.getLastName());
        o.setTelephone(wb.getTelephone());
        return null;
    }
    public static Owner fromOwnerWebBeanToEntity(WebBeanOwner wb) {
        Objects.requireNonNull(wb);
        Owner o = fromOwnerWebBeanCreationToEntity(wb);
        o.setId(wb.getId());
        return o;
    }
    public static WebBeanOwner fromOwnerEntityToWebBean(Owner o) {
        Objects.requireNonNull(o);
        WebBeanOwner wb = new WebBeanOwner();
        wb.setAddress(o.getAddress());
        wb.setCity(o.getCity());
        wb.setFirstName(o.getFirstName());
        wb.setLastName(o.getLastName());
        wb.setTelephone(o.getTelephone());
        wb.setPets(new HashSet<>());
        wb.setId(o.getId());
        return wb;
    }
    
    
}
