package dev.brandon.runnerz.run;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;


@Repository
public class InMemoryRunRepository implements RunRepository {
	private List <Run> runs = new ArrayList <>();


	public List <Run> find_all() {
		return runs;
	}

	public Optional <Run> findById(Integer id) {
		return Optional.ofNullable(runs.stream()
				.filter(run -> run.id() == id)
				.findFirst()
				.orElseThrow(RunNotFoundException::new));
	}

	public void create(Run run) {
		runs.add(run);
	}

	public void update(Run run, Integer id) {
		Optional <Run> existing_run = findById(id);

		if (existing_run.isPresent())
			runs.set(runs.indexOf(existing_run.get()), run);
	}

	public void delete(Integer id) {
		runs.removeIf(run -> run.id() == id);
	}

	public Integer count() {
		return runs.size();
	}

	public void save_all(List<Run> runs) {
		runs.stream().forEach(run -> create(run));
	}

	public List <Run> find_by_location(String location) {
		return runs
				.stream()
				.filter(run -> Objects.equals(run.location(), location))
				.toList();
	}
}
