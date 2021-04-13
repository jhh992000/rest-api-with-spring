package me.hhjeong.restapiwithspring.index;

import me.hhjeong.restapiwithspring.common.RestDocsConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import({RestDocsConfiguration.class})
@DisplayName("index 통합 테스트")
@ActiveProfiles("test") // application.properties + application-test.properties(override)
public class IndexControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	public void index() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("_links.events").exists())
		;
	}


}
