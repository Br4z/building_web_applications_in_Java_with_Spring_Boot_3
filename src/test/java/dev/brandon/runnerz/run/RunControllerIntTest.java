package dev.brandon.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RunControllerIntTest {
	@LocalServerPort
	int random_server_port;

	RestClient restClient;


	@BeforeEach
	void set_up() {
		restClient = RestClient.create("http://localhost:" + random_server_port);
	}

	@Test
	void should_find_all_runs() {
		List <Run> runs = restClient.get()
				.uri("/api/runs")
				.retrieve()
				.body(new ParameterizedTypeReference <>() {
				});
		assertEquals(10, runs.size());
	}

	@Test
	void should_find_run_by_id() {
		Run run = restClient.get()
				.uri("/api/runs/1")
				.retrieve()
				.body(Run.class);

		assertAll(
				() -> assertEquals(1, run.id()),
				() -> assertEquals("Noon Run", run.title()),
				() -> assertEquals("2024-02-20T06:05", run.started_on().toString()),
				() -> assertEquals("2024-02-20T10:27", run.completed_on().toString()),
				() -> assertEquals(24, run.miles()),
				() -> assertEquals(Location.INDOOR, run.location()));
	}

	@Test
	void should_create_new_run() {
		Run run = new Run(
							11,
							"Evening Run",
							LocalDateTime.now(),
							LocalDateTime.now().plusHours(2),
							10,
							Location.OUTDOOR,
							null
						);

		ResponseEntity <Void> new_run = restClient.post()
										.uri("/api/runs")
										.body(run)
										.retrieve()
										.toBodilessEntity();

		assertEquals(201, new_run.getStatusCode().value());
	}

	@Test
	void should_update_existing_run() {
		Run run = restClient.get().uri("/api/runs/1").retrieve().body(Run.class);

		ResponseEntity <Void> updated_run = restClient.put()
											.uri("/api/runs/1")
											.body(run)
											.retrieve()
											.toBodilessEntity();

		assertEquals(204, updated_run.getStatusCode().value());
	}

	@Test
	void should_delete_run() {
		ResponseEntity <Void> run = restClient.delete()
									.uri("/api/runs/1")
									.retrieve()
									.toBodilessEntity();

		assertEquals(204, run.getStatusCode().value());
	}
}
