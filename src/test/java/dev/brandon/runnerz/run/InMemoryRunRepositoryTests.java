package dev.brandon.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class InMemoryRunRepositoryTest {
	InMemoryRunRepository inMemoryRunRepository;


	@BeforeEach
	void set_up() {
		inMemoryRunRepository = new InMemoryRunRepository();
		inMemoryRunRepository.create(new Run(
												1,
												"Monday Morning Run",
												LocalDateTime.now(),
												LocalDateTime.now().plusMinutes(30),
												3,
												Location.INDOOR,
												null
											)
									);

		inMemoryRunRepository.create(new Run(
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
		List <Run> runs = inMemoryRunRepository.find_all();
		assertEquals(2, runs.size());
	}

	@Test
	void should_find_run_with_valid_id() {
		var run = inMemoryRunRepository.findById(1).get();
		assertEquals("Monday Morning Run", run.title());
		assertEquals(3, run.miles());
	}

	@Test
	void should_not_find_run_with_invalid_id() {
		RunNotFoundException notFoundException = assertThrows(
				RunNotFoundException.class,
				() -> inMemoryRunRepository.findById(3).get());

		assertEquals("Run not found", notFoundException.getMessage());
	}

	@Test
	void should_create_new_run() {
		inMemoryRunRepository.create(new Run(
												3,
												"Friday Morning Run",
												LocalDateTime.now(),
												LocalDateTime.now().plusMinutes(30),
												3,
												Location.INDOOR,
												null
											)
									);
		List <Run> runs = inMemoryRunRepository.find_all();
		assertEquals(3, runs.size());
	}

	@Test
	void should_update_run() {
		inMemoryRunRepository.update(new Run(
												1,
												"Monday Morning Run",
												LocalDateTime.now(),
												LocalDateTime.now().plusMinutes(30),
												5,
												Location.OUTDOOR,
												null
											),
									1);
		var run = inMemoryRunRepository.findById(1).get();
		assertEquals("Monday Morning Run", run.title());
		assertEquals(5, run.miles());
		assertEquals(Location.OUTDOOR, run.location());
	}

	@Test
	void should_delete_run() {
		inMemoryRunRepository.delete(1);
		List <Run> runs = inMemoryRunRepository.find_all();
		assertEquals(1, runs.size());
	}
}
