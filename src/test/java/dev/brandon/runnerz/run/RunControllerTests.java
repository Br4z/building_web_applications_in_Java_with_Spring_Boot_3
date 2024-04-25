package dev.brandon.runnerz.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RunController.class)
class RunControllerTest {
	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	JdbcRunRepository repository;

	private final List <Run> runs = new ArrayList <>();


	@BeforeEach
	void set_up() {
		runs.add(new Run(
						1,
						"Monday Morning Run",
						LocalDateTime.now(),
						LocalDateTime.now().plusMinutes(30),
						3,
						Location.INDOOR,
						0
					)
				);
	}

	@Test
	void should_find_all_runs() throws Exception {
		when(repository.find_all()).thenReturn(runs);
		mvc.perform(get("/api/runs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(runs.size())));
	}

	@Test
	void should_find_one_run() throws Exception {
		Run run = runs.get(0);
		when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(run));
		mvc.perform(get("/api/runs/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(run.id())))
				.andExpect(jsonPath("$.title", is(run.title())))
				.andExpect(jsonPath("$.miles", is(run.miles())))
				.andExpect(jsonPath("$.location", is(run.location().toString())));
	}

	@Test
	void should_return_not_found_with_invalid_id() throws Exception {
		mvc.perform(get("/api/runs/99"))
				.andExpect(status().isNotFound());
	}

	@Test
	void should_create_new_run() throws Exception {
		var run = new Run(
							null,
							"test",
							LocalDateTime.now(),
							LocalDateTime.now().plusHours(2),
							1,
							Location.INDOOR,
							0
						);
		mvc.perform(post("/api/runs")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(run)))
				.andExpect(status().isCreated());
	}

	@Test
	void should_update_run() throws Exception {
		var run = new Run(
							null,
							"test",
							LocalDateTime.now(),
							LocalDateTime.now().plusHours(1),
							1,
							Location.INDOOR,
							0
						);
		mvc.perform(put("/api/runs/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(run)))
				.andExpect(status().isNoContent());
	}

	@Test
	public void should_delete_run() throws Exception {
		mvc.perform(delete("/api/runs/1"))
				.andExpect(status().isNoContent());
	}
}
