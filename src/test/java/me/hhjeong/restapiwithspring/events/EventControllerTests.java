package me.hhjeong.restapiwithspring.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hhjeong.restapiwithspring.common.RestDocsConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import({RestDocsConfiguration.class})
@DisplayName("이벤트 생성 통합 테스트")
@ActiveProfiles("test")
public class EventControllerTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	EventRepository eventRepository;

	@Test
	@DisplayName("이벤트 생성")
	public void createEvent() throws Exception {

		//이벤트 DTO 생성
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

		//mockMvc (controller 테스트) 를 이용해서 API 호출
		mockMvc.perform(MockMvcRequestBuilders.post("/api/events/")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8")
				.content(objectMapper.writeValueAsString(event)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("id").exists())
				.andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.LOCATION))
				.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(Matchers.not(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("free").value(false))
				.andExpect(MockMvcResultMatchers.jsonPath("offline").value(true))
				.andExpect(MockMvcResultMatchers.jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
				.andExpect(MockMvcResultMatchers.jsonPath("_links.self").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("_links.query-events").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("_links.update-events").exists())
				.andDo(document("create-event",
						HypermediaDocumentation.links(
								linkWithRel("self").description("link to self"),
								linkWithRel("query-events").description("link to query events"),
								linkWithRel("update-events").description("link to update an existing"),
								linkWithRel("profile").description("link to profile an existing")
						),
						requestHeaders(
								headerWithName(HttpHeaders.ACCEPT).description("accept header"),
								headerWithName(HttpHeaders.CONTENT_TYPE).description("content-type header")
						),
						requestFields(
								fieldWithPath("name").description("Name of new event"),
								fieldWithPath("description").description("description of new event"),
								fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
								fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
								fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
								fieldWithPath("endEventDateTime").description("date time of end of new event"),
								fieldWithPath("location").description("location of new event"),
								fieldWithPath("basePrice").description("base price of new event"),
								fieldWithPath("maxPrice").description("max price of new event"),
								fieldWithPath("limitOfEnrollment").description("limit of enrollment")

						),
						responseHeaders(
								headerWithName(HttpHeaders.LOCATION).description("Locaton header"),
								headerWithName(HttpHeaders.CONTENT_TYPE).description("content-type header")
						),
						responseFields(
								fieldWithPath("id").description("id of new event"),
								fieldWithPath("name").description("Name of new event"),
								fieldWithPath("description").description("description of new event"),
								fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
								fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
								fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
								fieldWithPath("endEventDateTime").description("date time of end of new event"),
								fieldWithPath("location").description("location of new event"),
								fieldWithPath("basePrice").description("base price of new event"),
								fieldWithPath("maxPrice").description("max price of new event"),
								fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
								fieldWithPath("free").description("it tells if this event is free or not"),
								fieldWithPath("offline").description("it tells if this event is offline meeting or not"),
								fieldWithPath("eventStatus").description("event status"),
								fieldWithPath("_links.self.href").description("link to self"),
								fieldWithPath("_links.query-events.href").description("link to query event"),
								fieldWithPath("_links.update-events.href").description("link to update existing event"),
								fieldWithPath("_links.profile.href").description("link to profile")

						)
				))
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
				.andExpect(jsonPath("content[0].objectName").exists())
				.andExpect(jsonPath("content[0].defaultMessage").exists())
				.andExpect(jsonPath("content[0].code").exists())
				.andExpect(jsonPath("_link.index").exists())
		;

	}

	@Test
	@DisplayName("30개의 이벤트를 10개씩 페이징하여 조회하기")
	public void queryEvents() throws Exception {
		//Given
		IntStream.range(0, 30).forEach(this::generateEvent);

		//When
		this.mockMvc.perform(get("/api/events")
				.param("page", "0")
				.param("size", "10")
				.param("sort", "id,DESC") // id, 내림차순
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("page").exists())
				.andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.profile").exists())
				.andDo(document("query-events"))
		;
	}

	private void generateEvent(int i) {
		Event event = Event.builder()
				.name("event" + i)
				.description("test event")
				.build();

		this.eventRepository.save(event);
	}

}
