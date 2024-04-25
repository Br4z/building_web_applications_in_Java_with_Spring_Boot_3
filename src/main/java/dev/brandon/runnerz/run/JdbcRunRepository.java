package dev.brandon.runnerz.run;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository
public class JdbcRunRepository implements RunRepository {
	private final JdbcClient jdbcClient;


	public JdbcRunRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}


	public List <Run> find_all() {
		return jdbcClient
				.sql("SELECT * FROM run")
				.query(Run.class)
				.list();
	}

	public Optional <Run> findById(Integer id) {
		return jdbcClient
				.sql("SELECT id,title,started_on,completed_on,miles,location,version FROM Run WHERE id = :id")
				.param("id", id)
				.query(Run.class)
				.optional();
	}

	public void create(Run run) {
		var updated = jdbcClient
				.sql("INSERT INTO Run(id,title,started_on,completed_on,miles,location,version) values(?,?,?,?,?,?,?)")
				.params(List.of(run.id(), run.title(), run.started_on(), run.completed_on(), run.miles(),
						run.location().toString(), 0))
				.update();

		Assert.state(updated == 1, "Failed to create run " + run.title());
	}

	public void update(Run run, Integer id) {
		var updated = jdbcClient
				.sql("update run set title = ?, started_on = ?, completed_on = ?, miles = ?, location = ? where id = ?")
				.params(List.of(run.title(), run.started_on(), run.completed_on(), run.miles(), run.location().toString(),
						id))
				.update();

		Assert.state(updated == 1, "Failed to update run " + run.title());
	}

	public void delete(Integer id) {
		var updated = jdbcClient.sql("DELETE FROM run WHERE id = :id")
				.param("id", id)
				.update();

		Assert.state(updated == 1, "Failed to delete run " + id);
	}

	public Integer count() {
		return jdbcClient.sql("SELECT * FROM run").query().listOfRows().size();
	}

	public void save_all(List <Run> runs) {
		runs.stream().forEach(this::create);
	}

	public List <Run> find_by_location(String location) {
		return jdbcClient
				.sql("SELECT * FROM run WHERE location = :location")
				.param("location", location)
				.query(Run.class)
				.list();
	}
}
