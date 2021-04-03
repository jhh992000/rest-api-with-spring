package me.hhjeong.restapiwithspring.events;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}