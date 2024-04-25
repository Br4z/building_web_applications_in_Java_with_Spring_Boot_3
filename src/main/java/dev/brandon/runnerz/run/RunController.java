package dev.brandon.runnerz.run;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/runs")
public class RunController {
	private final RunRepository runRepository;


	public RunController(JdbcRunRepository jdbcRunRepository) {
		this.runRepository = jdbcRunRepository;
	}


	@GetMapping("")
	List <Run> findAll() {
		return runRepository.find_all();
	}

	@GetMapping("/{id}")
	public Run findById(@PathVariable Integer id) {
		Optional <Run> run = runRepository.findById(id);

		if (!run.isPresent())
			throw new RunNotFoundException();

		return run.get();
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("")
	public void create(@Valid @RequestBody Run run) {
		runRepository.create(run);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PutMapping("/{id}")
	public void update(@Valid @RequestBody Run run, @PathVariable Integer id) {
		runRepository.update(run, id);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Integer id) {
		runRepository.delete(id);
	}
}
