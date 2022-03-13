package com.bsr.emlakburada;

import com.bsr.emlakburada.dto.PersonRequestDTO;
import com.bsr.emlakburada.model.Advert;
import com.bsr.emlakburada.model.Message;
import com.bsr.emlakburada.model.Person;
import com.bsr.emlakburada.model.enums.PersonType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockDataUtil {
    
    public static final String EMAIL_POSTER = "demo_poster@gmail.com";
    public static final String EMAIL_VIEWER = "demo_viever@gmail.com";

    public static List<Message> getMockMessages() {
        Message message = new Message();
        message.setId(1);
        message.setSentAt(LocalDateTime.now());
        message.setSentFrom(getPosterMockPerson());
        message.setSentTo(getMockViewerPerson());
        message.setText("Hey, how much discount do you give?");

        return Collections.singletonList(message);
    }
    
    public static Person getPosterMockPerson() {
        Person person = new Person();
        person.setPersonType(PersonType.INDIVIDUAL);
        person.setId(1);
        person.setEmail(EMAIL_POSTER);
        person.setFirstName("john");
        person.setLastName("smith");
        person.setMobileNo("0555555555555");
        person.setPublishedAdverts(getMockAdverts2());
        
        return person;
    }

    public static Person getMockViewerPerson() {
        Person person = new Person();
        person.setPersonType(PersonType.INDIVIDUAL);
        person.setId(1);
        person.setEmail(EMAIL_VIEWER);
        person.setFirstName("johnny");
        person.setLastName("smithy");
        person.setMobileNo("0555555555566");
        person.setFavouriteAdverts(getMockAdverts1());

        return person;
    }
    
    public static List<Advert> getMockAdverts1() {
        List<Advert> adverts = new ArrayList<>();
        Advert advert = new Advert();
        advert.setAdNo(2);
        advert.setTitle("cozy house");
        advert.setCost(BigDecimal.valueOf(1000000));
        advert.setDuration(2);
        advert.setPostedBy(getPosterMockPerson());
        advert.setPostedDate(LocalDate.now());
        adverts.add(advert);

        return adverts;
    }

    public static List<Advert> getMockAdverts2() {
        List<Advert> adverts = new ArrayList<>();
        Advert advert = new Advert();
        advert.setAdNo(45);
        advert.setTitle("near metro apartment");
        advert.setCost(BigDecimal.valueOf(1000056));
        advert.setDuration(2);
        advert.setPostedBy(null);
        advert.setPostedDate(LocalDate.now());
        adverts.add(advert);

        return adverts;
    }

    public static PersonRequestDTO prepareUser() {
        return new PersonRequestDTO("cem", "", null,PersonType.INDIVIDUAL);
    }

}
