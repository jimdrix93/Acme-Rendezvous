
package usecases;

import java.text.ParseException;
import java.util.Collection;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import security.UserAccountService;
import services.AnnouncementService;
import services.RendezvousService;
import services.UserService;
import utilities.AbstractTest;
import domain.Announcement;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UseCaseUserTest extends AbstractTest {


	@Autowired
	private UserService			userService;
	@Autowired
	private UserAccountService	userAccountService;
	@Autowired
	private RendezvousService	rendezvousService;
	
	
	/*
	 * Requerimientos:
	 * 1. The system must support two kinds of actors, namely: administrators and users. It must
	 * store the following information regarding them: their names, surnames, postal addresses
	 * (optional), phone numbers (optional), and email addresses.
	 * 4. An actor who is not authenticated must be able to:
	 * 		1. Register to the system as a user.
	 * 		2. List the users of the system and navigate to their profiles, which include personal da-
	 * 		   ta and the list of rendezvouses that they've attended or are going to attend
	 * 5. An actor who is authenticated as a user must be able to:
	 * 		1. Do the same as an actor who is not authenticated, but register to the system.
	 */
	
	
	/*
	 * Caso de uso:
	 * Usuario anónimo registrandose en el sistema
	 */
	@Test
	public void createUserTest() {
		final Object testingData[][] = {
			{// Positive: Anonymous user can create an user
				"Pedro", "Dominguez Lopez", "652956526", "test@gmail.com", "Address Test", true, "Username1", "Password1", null
			}, {//Positive: Anonymous user can create an user
				"Amaia", "Fernandez Rodriguezáéíóú", "652956526", "test@gmail.com", "Address Test", false, "Username2", "Password2", null
			}, {// Negative: Anonymous user can create an user
				"", "Surname Test", "652956526", "emailTest", "Address Test", false, "Username3", "Password3", ConstraintViolationException.class
			//IllegalArgumentException.class
			}, {// Negative: Anonymous user can create an user
				null, "Surname Test", "652956526", "emailTest", "Address Test", false, "Username4", "Password4", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateUser((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Boolean) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}


	protected void templateCreateUser(final String name, final String surname, final String phone, final String email, final String address, final Boolean adult, final String username, final String password, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			final UserAccount ua = this.userAccountService.createAsUser();
			ua.setUsername(username);
			ua.setPassword(password);

			final User user = this.userService.create();

			user.setAddress(address);
			user.setAdult(adult);
			user.setEmail(email);
			user.setName(name);
			user.setSurname(surname);
			user.setUserAccount(ua);

			this.userService.save(user);
			this.userService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario anónimo logandose y editando sus datos
	 */
	@Test
	public void loginAndEditUserTest() {
		final Object testingData[][] = {
			{
				//Positivo
				null, "user1", null
			}, {
				//Positivo
				null, "user2", null
			}, {
				//Negativo: usuario inexistente
				null, "administrator1", IllegalArgumentException.class
			}, {
				//Negativo: usuario inexistente
				null, "manager1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListUsers((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}


	protected void templateListUsers(final String username, final int userId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);
			final User user = this.userService.findOne(userId);
			this.userService.save(user);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	


	
	/*
	 * Caso de uso: 
	 * Usuario anonimo registrandose en el sistema
	*/
	@Test
	public void anonymousUserRegisterTest() throws ParseException {

		System.out.println("Test 1: Usuario anonimo registrandose en el sistema.");

		final Object testingData[][] = {
			//Positive cases
			{
				"María", "Fernández", "maria@gmail.com", "664636363", "calle sin nombre", "cirdan", "pass", true, null
			},

			//Negative cases
			//Without username
			{
				"Nuria", "García", "nuria@gmail.com", "664636363", "calle sin nombre", "", "pass", true, ConstraintViolationException.class
			},
		};

		Class<?> caught = null;
		for (int i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.unauthenticate();
				final User user = this.userService.create();

				user.setName((String) testingData[i][0]);
				user.setSurname((String) testingData[i][1]);
				user.setEmail((String) testingData[i][2]);
				user.setPhone((String) testingData[i][3]);
				user.setAddress((String) testingData[i][4]);

				user.getUserAccount().setUsername((String) testingData[i][5]);
				user.getUserAccount().setPassword((String) testingData[i][6]);

				user.setAdult((Boolean) testingData[i][7]);

				this.userService.save(user);
				super.authenticate((String) testingData[i][5]);
				super.unauthenticate();

				System.out.println(i + " Test 1: ok");

			} catch (final Throwable oops) {
				caught = oops.getClass();
				System.out.println(i + " Test 1: " + oops.getClass().toString());
			}

			this.checkExceptions((Class<?>) testingData[i][8], caught);
		}
	}

	/*
	 * Caso de uso: 
	 * Usuario anonimo obteniendo los usuarios del sistema, eligiendo uno, navegando
	 * a su perfil, y a las citas a las que va a asistir
	 */
	@Test
	public void anonymousUserLookingUserDataTest() throws ParseException {

		System.out.println("Test 2: Usuario anonimo obteniendo los usuarios del sistema, eligiendo uno, navegando a su perfil, y a las citas a las que va a asistir.");

		final Object testingData[][] = {
			//Positive cases
			{
				2, null
			},

			//Negative cases
			//Eligiendo usuario inexistente
			{
				99999, ArrayIndexOutOfBoundsException.class
			},
		};

		Class<?> caught = null;
		for (int i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.unauthenticate();
				final Collection<User> users = this.userService.findAll();
				final User user = users.toArray(new User[users.size()])[(Integer) testingData[i][0]];
				final Collection<Rendezvous> rs = this.rendezvousService.findReservedAndNotCanceledByUserId(user.getId());

				System.out.println(i + " Test 2: " + rs.size() + " ok");

			} catch (final Throwable oops) {
				caught = oops.getClass();
				System.out.println(i + " Test 2: " + oops.getClass().toString());
			}

			this.checkExceptions((Class<?>) testingData[i][1], caught);
		}
	}

	/*
	 * Caso de uso: 
	 * Usuario anonimo listando las citas, eligiendo una, listando asistentes, eligiendo uno y mostrando su perfil
	 */
	@Test
	public void anonymousUserLookingAssistants() throws ParseException {

		System.out.println("Test 3: Usuario anonimo listando las citas, eligiendo una, listando asistentes, eligiendo uno y mostrando su perfil.");

		final Object testingData[][] = {
			//Positive cases
			{
				0, 1, null
			},

			//Negative cases
			//Asistente incorrecto
			{
				2, 999999, null
			},
		};

		Class<?> caught = null;
		for (int i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.unauthenticate();
				final Collection<Rendezvous> rs = this.rendezvousService.findAll();
				final Rendezvous r = rs.toArray(new Rendezvous[rs.size()])[(Integer) testingData[i][0]];
				final Collection<User> users = this.userService.findAttendantsByRendezvous(r.getId());
				users.toArray(new User[users.size()]);

				System.out.println(i + " Test 3: " + rs.size() + " ok");

			} catch (final Throwable oops) {
				caught = oops.getClass();
				System.out.println(i + " Test 3: " + " " + oops.getClass().toString());
			}

			this.checkExceptions((Class<?>) testingData[i][2], caught);
		}
	}
}
