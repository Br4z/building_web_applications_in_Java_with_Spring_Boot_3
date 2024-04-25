package dev.brandon.runnerz.run;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;


public record Run(
		@Id
		Integer id,
		@NotEmpty String title,
		LocalDateTime started_on,
		LocalDateTime completed_on,
		@Positive Integer miles,
		Location location,
		@Version
		Integer version
) {
	public Run {
		if (!completed_on.isAfter(started_on))
			throw new IllegalArgumentException("\"completed_on\" must be after \"started_on\"");
	}
}
