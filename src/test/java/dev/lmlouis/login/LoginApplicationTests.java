package dev.lmlouis.login;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

import dev.lmlouis.login.controller.LoginController;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class LoginApplicationTests {

	@Autowired
	private LoginController loginController;

	@Test
	void contextLoads() {
		assertThat(loginController).isNotNull(); // tester si le controller est bien instancié 
	}

}
