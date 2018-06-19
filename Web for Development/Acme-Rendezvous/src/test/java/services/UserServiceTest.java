
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccountService;
import utilities.AbstractTest;
import domain.User;
import forms.UserRegisterForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class UserServiceTest extends AbstractTest {

	// Service under test ------------------------
	@Autowired
	private UserService			userService;
	/**
	 * Positive: A non authenticated registers as a user
	 */
	@Test
	public void registerUserPositiveTest() {
		final int actual = this.userService.findAll().size();
		this.unauthenticate();
		final UserRegisterForm registerForm = new UserRegisterForm();
		registerForm.setName("Name Test");
		registerForm.setSurname("Surname Test");
		registerForm.setPhone("652956526");
		registerForm.setEmail("test@gmail.com");
		registerForm.setAddress("Address Test");
		registerForm.setAdult(true);
		registerForm.setUsername("Username Test");
		registerForm.setPassword("Password Test");
		final User user = this.userService.reconstruct(registerForm);
		this.userService.save(user);
		final int expected = this.userService.findAll().size();
		Assert.isTrue(expected == actual + 1);
	}

	/**
	 * Negative: An non authenticated attempts to register as a user with wrong fields
	 */
	@Test(expected = IllegalArgumentException.class)
	public void registerUserWrongFieldsNegativeTest() {
		this.unauthenticate();
		final UserRegisterForm registerForm = new UserRegisterForm();
		registerForm.setName("");
		registerForm.setSurname("Surname Test");
		registerForm.setPhone("652956526");
		registerForm.setEmail("test@gmail.com");
		registerForm.setAddress("Address Test");
		registerForm.setAdult(true);
		registerForm.setUsername("Username Test");
		registerForm.setPassword("Password Test");
		final User user = this.userService.reconstruct(registerForm);
		final User user2 = this.userService.save(user);
		Assert.isNull(user2);
	}

}
