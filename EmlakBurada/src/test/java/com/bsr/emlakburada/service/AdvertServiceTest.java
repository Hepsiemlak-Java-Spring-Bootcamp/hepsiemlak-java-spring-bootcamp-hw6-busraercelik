package com.bsr.emlakburada.service;

import com.bsr.emlakburada.client.request.AdvertRequest;
import com.bsr.emlakburada.client.BannerClientFeign;
import com.bsr.emlakburada.client.response.BannerResponse;
import com.bsr.emlakburada.dto.response.AdvertResponseDTO;
import com.bsr.emlakburada.exception.UserNotFoundException;
import com.bsr.emlakburada.model.Advert;
import com.bsr.emlakburada.model.Person;
import com.bsr.emlakburada.model.enums.PersonType;
import com.bsr.emlakburada.queue.QueueService;
import com.bsr.emlakburada.repository.AdvertRepository;
import com.bsr.emlakburada.repository.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
class AdvertServiceTest {

	@InjectMocks
	private AdvertService advertService;

	@Mock
	private AdvertRepository advertRepository;

	@Mock
	private BannerClientFeign bannerClientFeign;

	@Mock
	private QueueService queueService;

	@Mock
	private PersonRepository personRepository;

	@Test()
	@DisplayName("when user not found throw exception. "
			+ "method name can be: should_throw_exception_when_user_not_found")
	void saveAdvertWithoutUserTest() {

		assertThrows(Exception.class, () -> {
			advertService.saveAdvert(prepareAdvertRequest());
		}, "İlan kaydedilemedi");

		// assertEquals("İlan kaydedilemedi", thrown.getMessage());

	}

	@Test
	void saveAdvertWithUserTest() throws Exception {
		AdvertRequest request = prepareAdvertRequest();

		Optional<Person> user = Optional.of(prepareUser());
		//// @formatter:off
		Mockito
		.when(personRepository.findById(request.getUserId()))
		.thenReturn(user);
		
		Mockito
		.when(advertRepository.save(any()))
		.thenReturn(prepareAdvert("başlık"));
		
		Mockito
		.when(bannerClientFeign.saveBanner(any()))
		.thenReturn(new ResponseEntity<BannerResponse>(new BannerResponse(), HttpStatus.OK));
		
		// @formatter:on

		assertDoesNotThrow(() -> {
			AdvertResponseDTO response = advertService.saveAdvert(request);
			assertEquals("başlık", response.getTitle());
			
			response.getCost();
			verify(queueService).sendMessage(any());
			verify(bannerClientFeign).saveBanner(any());

		});

	}

	@Test
	void saveAdvertWithUserTest1() throws Exception {
		AdvertRequest request = prepareAdvertRequest();

		Optional<Person> user = Optional.of(prepareUser());
		//// @formatter:off
		Mockito
		.when(personRepository.findById(request.getUserId()))
		.thenReturn(user);
		
		Mockito
		.when(advertRepository.save(any()))
		.thenReturn(prepareAdvert(""));
		
		Mockito
		.when(bannerClientFeign.saveBanner(any()))
		.thenReturn(new ResponseEntity<BannerResponse>(new BannerResponse(), HttpStatus.OK));
		
		// @formatter:on

		AdvertResponseDTO response = advertService.saveAdvert(request);

		assertEquals("başlık", response.getTitle());
		verify(queueService).sendMessage(any());
		verify(bannerClientFeign).saveBanner(any());

	}

	@Test
	void getAllAdvertsTest() {
		// when
		// given
		// verify

		Mockito.when(advertRepository.findAll()).thenReturn(prepareAdvertList());

		List<AdvertResponseDTO> responseList = advertService.getAllAdverts();

		assertNotEquals(0, responseList.size());

		assertThat(responseList.size()).isNotZero();

		for (AdvertResponseDTO response : responseList) {
			assertThat(response.getAdvertNo()).isEqualTo(0);

			assertEquals(new BigDecimal(12345), response.getCost());

		}

	}

	private List<Advert> prepareAdvertList() {
		List<Advert> adverts = new ArrayList<Advert>();
		adverts.add(prepareAdvert("başlık1"));
		adverts.add(prepareAdvert("başlık2"));
		adverts.add(prepareAdvert("başlık3"));
		return adverts;
	}

	private Advert prepareAdvert(String baslik) {
		Advert advert = new Advert();
		advert.setAdNo(0);
		advert.setTitle(baslik);
		advert.setCost(new BigDecimal(12345));
		return advert;
	}

	private Person prepareUser() {
		Person user = new Person("mock name","surname", "email", PersonType.CORPORATE);
		return user;
	}

	private AdvertRequest prepareAdvertRequest() {
		AdvertRequest request = new AdvertRequest();
		request.setUserId(5);
		request.setTitle("başlık");
		request.setDuration(3);
		request.setCost(new BigDecimal(12345));
		return request;
	}

	@Test
	void getAdvertByAdvertIdTest() {

		// when
		// given
		// verify

		//// @formatter:off
		Mockito
		.when(advertRepository.findById(0L))
		.thenReturn(Optional.of(prepareAdvert("başlık")));
		// @formatter:on

		AdvertResponseDTO response = advertService.getAdvertByAdvertId(0);
		assertNotNull(response);
		assertEquals("başlık", response.getTitle());
		assertNotNull(response.getUser());

	}

	@Test
	@DisplayName("should throw UserNotFoundException.class")
	void getAllFavoriteAdvertsByUserIdNotFoundUserTest() {

		assertThrows(UserNotFoundException.class, () -> {
			List<AdvertResponseDTO> response = advertService.getAllFavouriteAdverts(1);
			assertNull(response);
		}, "User Not Found");

	}

	@Test
	@DisplayName("should return List<AdvertResponse>")
	void getAllFavoriteAdvertsByUserIdTest() {

		Person user = prepareUser();
		user.getFavouriteAdverts().add(prepareAdvert("başlık"));

		Mockito.when(personRepository.findById(1L)).thenReturn(Optional.of(user));

		assertDoesNotThrow(() -> {
			List<AdvertResponseDTO> response = advertService.getAllFavouriteAdverts(1);

			assertNotNull(user.getFavouriteAdverts());

			assertThat(user.getFavouriteAdverts().size()).isNotZero();

			assertNotNull(response);

			assertThat(response.size()).isNotZero();
		});

	}

}
