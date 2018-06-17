/*
 * ManagerService.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Rendezvous;
import domain.User;
import forms.UserRegisterForm;

@Service
@Transactional
public class UserService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private UserRepository		userRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserAccountService	userAccountService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private RendezvousService	rendezvousService;


	// Constructors -----------------------------------------------------------

	public UserService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public User create() {
		UserAccount useraccount;
		final User result = new User();
		final Authority authority = new Authority();

		authority.setAuthority(Authority.USER);
		useraccount = this.userAccountService.createAsUser();

		result.setUserAccount(useraccount);

		return result;
	}

	public Collection<User> findAll() {
		Collection<User> result;

		result = this.userRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public User findOne(final int userId) {
		User result;

		result = this.userRepository.findOne(userId);
		Assert.notNull(result);

		return result;
	}

	public User save(final User user) {
		Assert.notNull(user);
		User result;

		if (user.getId() == 0) {
			Md5PasswordEncoder encoder;

			encoder = new Md5PasswordEncoder();

			user.getUserAccount().setPassword(encoder.encodePassword(user.getUserAccount().getPassword(), null));
		}

		result = this.userRepository.save(user);

		return result;
	}

	public void delete(final User user) {
		Assert.notNull(user);
		Assert.isTrue(user.getId() != 0);

		this.userRepository.delete(user);
	}

	public void flush() {
		this.userRepository.flush();
	}

	// Other business methods -------------------------------------------------

	public User findByPrincipal() {
		User result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findByUserAccount(userAccount);
		Assert.isTrue(actor instanceof User);
		result = (User) actor;
		Assert.notNull(result);

		return result;
	}

	public Collection<User> findAttendantsByRendezvous(final int rendezvousId) {
		Collection<User> attendants = new ArrayList<User>();
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);

		attendants = this.userRepository.findAttendantsByRendezvous(rendezvousId);

		return attendants;

	}

	public User reconstruct(final UserRegisterForm userForm) {
		User res;
		res = this.create();

		res.setName(userForm.getName());
		res.setPhone(userForm.getPhone());
		res.setSurname(userForm.getSurname());
		res.setEmail(userForm.getEmail());
		res.setAddress(userForm.getAddress());
		res.getUserAccount().setUsername(userForm.getUsername());
		res.getUserAccount().setPassword(userForm.getPassword());
		res.setAdult(userForm.isAdult());

		return res;
	}

	public User findByRendezvous(final Integer rendezvousId) {
		final User user = this.userRepository.findByRendezvous(rendezvousId);
		Assert.notNull(user);
		return user;
	}

}
