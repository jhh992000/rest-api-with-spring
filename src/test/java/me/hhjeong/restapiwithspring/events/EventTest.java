package me.hhjeong.restapiwithspring.events;

import me.hhjeong.restapiwithspring.test.QuickTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;


class EventTest {

	@Test
	public void builder() {
		Event event = Event.builder()
				.name("인프런 Spring REST API")
				.description("REST API development with Spring")
				.build();

		//assertj 코드
		assertThat(event).isNotNull();

		//junit 코드
		//assertNotNull(event);
	}

	@Test
	@DisplayName("이벤트 정보 비교 테스트")
	@QuickTag
	public void javaBean() {

		//조건이 맞을때만 테스트 진행, false인 경우 테스트 중지
		assumeTrue(true);

		//조건이 맞을때 하위 메서드 실행
		assumingThat(true, () -> {
			assertThat("1").isEqualTo("2");
		});


		var name = "정훈희";
		String description = "설명";

		Event event = new Event();
		event.setName("정훈희");
		event.setDescription(description);

		assertThat(event.getName()).as("이름이 다릅니다.").isEqualTo(name);
		//assertEquals(event.getName(), name, "이름이 다릅니다.");
		//assertEquals(event.getDescription(), description, "내용이 다릅니다.");
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