package dev.brandon.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@Import(JdbcRunRepository.class)
// @AutoConfigureTestDatabase(replace=Replace.NONE)
class JdbcRunRepositoryTests {
	@Autowired
	JdbcRunRepository repository;


	@BeforeEach
	void set_up() {
		repository.create(new Run(
									1,
									"Monday Morning Run",
									LocalDateTime.now(),
									LocalDateTime.now().plusMinutes(30),
									3,
									Location.INDOOR,
									null
								)
						);

		repository.create(new Run(
										2,
										"Wednesday Evening Run",
										LocalDateTime.now(),
										LocalDateTime.now().plusHours(1),
										6,
										Location.INDOOR,
										null
								)
						);
	}

	@Test
	void should_find_all_runs() {
		List <Run> runs = repository.find_all();
		assertEquals(2, runs.size());
	}

	@Test
	void should_find_run_with_vaalid_id() {
		var run = repository.findById(1).get();
		assertEquals("Monday Morning Run", run.title());
		assertEquals(3, run.miles());
	}

	@Test
	void should_not_find_run_with_invalid_id() {
		var run = repository.findById(3);
		assertTrue(run.isEmpty());
	}

	@Test
	void should_create_new_run() {
		repository.create(new Run(
									3,
									"Friday Morning Run",
									LocalDateTime.now(),
									LocalDateTime.now().plusMinutes(30),
									3,
									Location.INDOOR,
									null
								)
						);
		List <Run> runs = repository.find_all();
		assertEquals(3, runs.size());
	}

	@Test
	void should_update_run() {
		repository.update(new Run(
									1,
								"Monday Morning Run",
									LocalDateTime.now(),
									LocalDateTime.now().plusMinutes(30),
									5,
									Location.OUTDOOR,
									null
								),
						1);
		var run = repository.findById(1).get();
		assertEquals("Monday Morning Run", run.title());
		assertEquals(5, run.miles());
		assertEquals(Location.OUTDOOR, run.location());
	}

	@Test
	void should_delete_run() {
		repository.delete(1);
		List <Run> runs = repository.find_all();
		assertEquals(1, runs.size());
	}
}
