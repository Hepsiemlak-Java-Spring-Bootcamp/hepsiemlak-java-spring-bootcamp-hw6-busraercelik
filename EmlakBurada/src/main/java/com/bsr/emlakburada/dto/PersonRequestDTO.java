package com.bsr.emlakburada.dto;

import com.bsr.emlakburada.model.enums.PersonType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PersonRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private PersonType userType;
}
