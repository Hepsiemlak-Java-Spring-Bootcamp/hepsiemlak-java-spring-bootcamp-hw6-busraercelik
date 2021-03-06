package com.bsr.emlakburada.service;

import com.bsr.emlakburada.dto.PersonRequestDTO;
import com.bsr.emlakburada.dto.response.PersonResponseDTO;
import com.bsr.emlakburada.repository.PersonRepository;
import com.bsr.emlakburada.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonResponseDTO> getAllUsers() {
        return personRepository.findAll()
                .stream()
                .map(Response::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public PersonResponseDTO getPersonById(long id) {
        return Response.convertToResponseDTO(personRepository.
                findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("no person with given id %s found", id))));
    }

    public void savePerson(PersonRequestDTO personRequestDTO) {
        personRepository.save(Response.convertToPerson(personRequestDTO));
    }

}
