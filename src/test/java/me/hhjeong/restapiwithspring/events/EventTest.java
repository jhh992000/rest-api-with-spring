package me.hhjeong.restapiwithspring.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventTest {

	@Test
	public void builder() {
		Event event = Event.builder()
				.name("인프런 Spring REST API")
				.description("REST API development with Spring")
				.build();
		assertNotNull(event);
	}

	@Test
	public void javaBean() {
		var name = "정훈희";
		String description = "설명";

		Event event = new Event();
		event.setName("정훈희");
		event.setDescription(description);

		assertEquals(event.getName(), name);
		assertEquals(event.getDescription(), description);
	}


	static Stream<Arguments> testFreeParams() {
		return Stream.of(
				Arguments.of(0, 0, true),
				Arguments.of(100, 0, false),
				Arguments.of(100, 0, true)
		);
	}

	// Junit 5 기준
	@ParameterizedTest
	@MethodSource("testFreeParams")
	public void testFree(int basePrice, int maxPrice, boolean isFree){
		// Given
		Event event = Event.builder()
				.basePrice(basePrice)
				.maxPrice(maxPrice)
				.build();
		//When
		event.update();

		//Then
		assertEquals(event.isFree(), isFree);
	}

	@Test
	public void testOffline() {

		//Given
		Event event = Event.builder()
				.location("서울상공회의소 개방형 클라우드 플랫폼 센터")
				.build();

		//When
		event.update();

		//Then
		assertEquals(event.isOffline(), true);


		//Given
		event = Event.builder()
				.build();
		//When
		event.update();

		//Then
		assertEquals(event.isOffline(), false);
	}

}