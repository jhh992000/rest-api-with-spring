package me.hhjeong.restapiwithspring.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("이벤트 생성 통합 테스트")
public class EventControllerTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("이벤트 생성")
	public void createEvent() throws Exception {
		EventDto event = EventDto.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2021,4, 3, 16, 37))
				.closeEnrollmentDateTime(LocalDateTime.of(2021, 4, 4, 16, 37))
				.beginEventDateTime(LocalDateTime.of(2021, 4, 14, 16, 37))
				.endEventDateTime(LocalDateTime.of(2021, 4, 15, 16, 37))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("서울상공회의소 개방형 클라우드 플랫폼 센터")
				.build();

		mockMvc.perform(post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON_VALUE)
				.content(objectMapper.writeValueAsString(event)))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").exists())
				.andExpect(header().exists(HttpHeaders.LOCATION))
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
				.andExpect(jsonPath("id").value(Matchers.not(100)))
				.andExpect(jsonPath("free").value(false))
				.andExpect(jsonPath("offline").value(true))
				.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.query-events").exists())
				.andExpect(jsonPath("_links.update-events").exists())
		;
	}

	@Test
	@DisplayName("이벤트 생성 (알수없는 입력값이 존재할 경우 에러 발생)")
	public void createEvent_Bad_Request() throws Exception {
		Event event = Event.builder()
				.id(100)
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2021,4, 3, 16, 37))
				.closeEnrollmentDateTime(LocalDateTime.of(2021, 4, 4, 16, 37))
				.beginEventDateTime(LocalDateTime.of(2021, 4, 14, 16, 37))
				.endEventDateTime(LocalDateTime.of(2021, 4, 15, 16, 37))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("서울상공회의소 개방형 클라우드 플랫폼 센터")
				.free(true)
				.offline(false)
				.eventStatus(EventStatus.PUBLISHED)
				.build();

		mockMvc.perform(post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(event)))
				.andDo(print())
				.andExpect(status().isBadRequest())
		;
	}

	@Test
	@DisplayName("이벤트 생성 (입력값이 비어있을 경우 예외 발생)")
	public void createEvent_Bad_Request_Empty_Input() throws Exception {

		EventDto eventDto = EventDto.builder().build();

		this.mockMvc.perform(
					post("/api/events")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaTypes.HAL_JSON)
					.content(objectMapper.writeValueAsString(eventDto))
				)
				.andExpect(status().isBadRequest())
		;

	}

	@Test
	@DisplayName("이벤트 생성 (입력값이 잘못 들어올 경우 예외 발생)")
	public void createEvent_Bad_Request_Wrong_Input() throws Exception {

		EventDto eventDto = EventDto.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2021,4, 3, 16, 37))
				.closeEnrollmentDateTime(LocalDateTime.of(2021, 4, 4, 16, 37))
				.beginEventDateTime(LocalDateTime.of(2021, 4, 14, 16, 37))
				.endEventDateTime(LocalDateTime.of(2021, 4, 13, 16, 37))
				.basePrice(10000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("서울상공회의소 개방형 클라우드 플랫폼 센터")
				.build();


		this.mockMvc.perform(
					post("/api/events")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaTypes.HAL_JSON)
					.content(objectMapper.writeValueAsString(eventDto))
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$[0].objectName").exists())
				.andExpect(jsonPath("$[0].defaultMessage").exists())
				.andExpect(jsonPath("$[0].code").exists())
		;

	}

}
