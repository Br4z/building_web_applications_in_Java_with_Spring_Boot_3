package dev.brandon.runnerz.run;

import java.util.List;
import java.util.Optional;


public interface RunRepository {
	List <Run> find_all();
	Optional <Run> findById(Integer id);
	void create(Run run);
	void update(Run run, Integer id);
	void delete(Integer id);
	Integer count();
	void save_all(List <Run> runs);
	List <Run> find_by_location(String location);
}
