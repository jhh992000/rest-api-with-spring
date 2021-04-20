package me.hhjeong.restapiwithspring.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import me.hhjeong.restapiwithspring.commons.ErrorsSerializer;
import me.hhjeong.restapiwithspring.index.IndexController;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	private final EventValidator eventValidator;

	public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
		this.eventRepository = eventRepository;
		this.modelMapper = modelMapper;
		this.eventValidator = eventValidator;
	}

	@PostMapping()
	public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		//유효성검증
		eventValidator.validation(eventDto, errors);
		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		Event event = modelMapper.map(eventDto, Event.class);
		event.update();
		Event newEvent = this.eventRepository.save(event);

		URI createdUri = EventResource.getCreatedUri(event);

		EntityModel<Event> em = EventResource.of(event, "/docs/index.html#resources-events-create");
		em.add(linkTo(EventController.class).withRel("query-events"));
		em.add(linkTo(EventController.class).withRel("update-events"));
		return ResponseEntity.created(createdUri).body(em);
	}

	@GetMapping
	public ResponseEntity queryEvent(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		Page<Event> page = this.eventRepository.findAll(pageable);

		PagedModel pageModel = assembler.toModel(page, EventResource::of); //각 이벤트마다 self
		pageModel.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
		return ResponseEntity.ok(pageModel);
	}

	@GetMapping("/{id}")
	public ResponseEntity getEvent(@PathVariable Integer id) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		if (optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Event event = optionalEvent.get();
		EntityModel<Event> eventEntityModel = EventResource.of(event, "/docs/index.html#resources-events-get");
		return ResponseEntity.ok(eventEntityModel);
	}

	@PutMapping("/{id}")
	public ResponseEntity updateEvent(@PathVariable Integer id,
	                                  @RequestBody @Valid EventDto eventDto,
	                                  Errors errors) {
		Optional<Event> optionalEvent = this.eventRepository.findById(id);
		if (optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		this.eventValidator.validation(eventDto, errors);
		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		Event existingEvent = optionalEvent.get();
		this.modelMapper.map(eventDto, existingEvent);
		Event savedEvent = this.eventRepository.save(existingEvent);
		EntityModel<Event> eventEntityModel = EventResource.of(savedEvent, "/docs/index.html#resources-events-update");
		return ResponseEntity.ok(eventEntityModel);
	}

	private ResponseEntity badRequest(Errors errors) {

		Link indexLink = linkTo(methodOn(IndexController.class).index()).withRel("index");

		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Errors.class, new ErrorsSerializer(indexLink));

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(simpleModule);

		String str = null;
		try {
			str = objectMapper.writeValueAsString(errors);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			str = "{}";
		}
		return ResponseEntity.badRequest().body(str);
	}

}
