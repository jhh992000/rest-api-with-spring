package me.hhjeong.restapiwithspring.index;

import me.hhjeong.restapiwithspring.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("index 통합 테스트")
public class IndexControllerTest extends BaseControllerTest {

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
