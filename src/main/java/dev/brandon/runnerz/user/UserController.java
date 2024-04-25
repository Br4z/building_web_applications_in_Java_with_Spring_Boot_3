package dev.brandon.runnerz.user;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserHttpClient userHttpClient;


	UserController(UserHttpClient userHttpClient) {
		this.userHttpClient = userHttpClient;
	}


	@GetMapping("")
	List <User> findAll() {
		return userHttpClient.findAll();
	}

	@GetMapping("/{id}")
	User findById(@PathVariable Integer id) {
		return userHttpClient.findById(id);
	}
}
