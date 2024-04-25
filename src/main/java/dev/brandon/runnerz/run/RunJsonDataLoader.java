package dev.brandon.runnerz.run;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;


@Component
public class RunJsonDataLoader implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(RunJsonDataLoader.class);
	private final ObjectMapper objectMapper;
	private final JdbcRunRepository runRepository;


	public RunJsonDataLoader(ObjectMapper objectMapper, JdbcRunRepository jdbcRunRepository) {
		this.objectMapper = objectMapper;
		this.runRepository = jdbcRunRepository;
	}


	@Override
	public void run(String... args) throws Exception {
		if (runRepository.count() == 0)
			try (InputStream input_stream = TypeReference.class.getResourceAsStream("/runs.json")) {
				Runs all_runs = objectMapper.readValue(input_stream, Runs.class);
				log.info("Reading {} runs from JSON data and saving to in-memory collection", all_runs.runs().size());
				runRepository.save_all(all_runs.runs());
			} catch (IOException e) {
				throw new RuntimeException("Failed to read JSON data", e);
			}
		else
			log.info("Not loading Runs from JSON data because the collection contains data");
	}
}
