package com.bsr.emlakburada.service;

import com.bsr.emlakburada.MockDataUtil;
import com.bsr.emlakburada.dto.PersonRequestDTO;
import com.bsr.emlakburada.dto.response.PersonResponseDTO;
import com.bsr.emlakburada.model.Person;
import com.bsr.emlakburada.model.enums.PersonType;
import com.bsr.emlakburada.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class PersonServiceTest {

	@InjectMocks
	private PersonService userService;

	@Mock
	private PersonRepository userRepository;

	@BeforeEach
	void setup() {
		Mockito
		.when(userRepository.findAll())
		.thenReturn(prepareMockUserList());
	}

	private List<Person> prepareMockUserList() {
		List<Person> userList = new ArrayList<Person>();
		userList.add(new Person("cem", "bla", "cem@patika.com",PersonType.INDIVIDUAL));
		userList.add(new Person("emre", "bla","emre@patika.com",PersonType.INDIVIDUAL));
		return userList;
	}

	@Test
	void getAllUserTest() {
		List<PersonResponseDTO> allUser = userService.getAllUsers();
		assertNotNull(allUser);
		assertThat(allUser.size()).isNotZero();
	}

	@Test
	void saveUserTest() {
		userService.savePerson(MockDataUtil.prepareUser());
		Mockito.verify(userRepository).save(any());
	}

	@Test
	void getPersonByIdTest_throwsException() {
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			userService.getPersonById(1L);
		});
		assertTrue(exception.getMessage().startsWith("no person with given id") );
	}

	@Test
	@DisplayName("get person by id test with user")
	void getPersonByIdTest_userFound() {
		Person mockViewerPerson = MockDataUtil.getMockViewerPerson();
		Mockito.when(userRepository.findById(1L))
				.thenReturn(Optional.of(mockViewerPerson));
		PersonResponseDTO responseDTO = userService.getPersonById(1L);
		assertNotNull(responseDTO);
		assertEquals(responseDTO.getEmail(), mockViewerPerson.getEmail());
		assertEquals(responseDTO.getFirstName(), mockViewerPerson.getFirstName());
		assertEquals(responseDTO.getLastName(), mockViewerPerson.getLastName());
	}

}
