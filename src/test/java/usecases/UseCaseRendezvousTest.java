
package usecases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import services.AdministratorService;
import services.RendezvousService;
import services.UserService;
import utilities.AbstractTest;
import domain.Administrator;
import domain.Coordinate;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UseCaseRendezvousTest extends AbstractTest {

	@Autowired
	private RendezvousService		rendezvousService;
	@Autowired
	private UserService				userService;
	@Autowired
	private AdministratorService	administratorService;


	/*
	 * Requerimientos:
	 * 2. Users can create rendezvouses. For each rendezvous, the system must store its name, its
	 * description, the moment when it's going to be organised, an optional picture, optional GPS
	 * co-ordinates, and the creator and the list of attendants.
	 * 5. An actor who is authenticated as a user must be able to:
	 * 1. Do the same as an actor who is not authenticated, but register to the system.
	 * 2. Create a rendezvous, which he's implicitly assumed to attend. Note that a user may
	 * edit his or her rendezvouses as long as they aren't saved them in final mode. Once a
	 * rendezvous is saved in final mode, it cannot be edited or deleted by the creator.
	 * 3. Update or delete the rendezvouses that he or she's created. Deletion is virtual, that
	 * is: the information is not removed from the database, but the rendezvous cannot be
	 * updated. Deleted rendezvouses are flagged as such when they are displayed
	 * 6. An actor who is authenticated as an administrator must be able to:
	 * 2. Remove a rendezvous that he or she thinks is inappropriate.
	 * 14. Some rendezvouses may be flagged as "adult only", in which case every attempt to RSVP
	 * them by users who are under 18 must be prohibited. Such rendezvouses must not be dis-
	 * played unless the user who is browsing them is at least 18 year old. Obviously, they must not
	 * be shown to unauthenticated users.
	 * 16. An actor who is authenticated as a user must be able to:
	 * 4. Link one of the rendezvouses that he or she's created to other similar rendezvouses.
	 */

	/*
	 * Caso de uso:
	 * Usuario anonimo listando citas agrupadas por categoria
	 */
	@Test
	public void listRendezvousByCategoryTest() {

		System.out.println("-----List rendezvous by category test. Positive 0 to 1, Negative 2 to 3.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: Anonymous: category without rendezvouses
				"P1", "category1", null
			}, {// P2: Anonymous: category with rendezvouses
				"P2", "category2", null
			},

			//Negative test cases
			{// N1: Anonymous: category does not exist
				"N1", "category4", NumberFormatException.class
			}, {//N2: Anonymous: category does not exist
				"N2", "category5", NumberFormatException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListRendezvousByCategoryTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Category
				(Class<?>) testingData[i][2]); //Exception class
	}

	protected void templateListRendezvousByCategoryTest(final Integer i, final String nameTest, final String categoryId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findByCategory(super.getEntityId(categoryId));

			Assert.notNull(rendezvouses);

			System.out.println(i + " Create Question: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Create Question: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario logado listando citas creadas, eligiendo una y editandola.
	 */
	@Test
	public void editRendezvousTest() throws ParseException {

		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		final Coordinate coor = new Coordinate();
		coor.setLongitude("40.256");
		coor.setLatitude("-30.256");

		System.out.println("-----Edit rendezvous test. Positive 0 to 2, Negative 3 to 14.");

		final Object testingData[][] = {
			//Positive cases
			//P0: Only Name, Description and moment
			{
				"rendezvous7", "user1", "Cita en Madrid con tildes áéíóúññññççççççç", "Visitamos Madrid en 48h", sdf.parse("1/12/2018 20:00"), null, null, true, false, false, null
			},
			//P1: All fields
			{
				"rendezvous7", "user1", "Kayak en el rio %%%%%???????''''''''", "Ruta en kayak, estais listos?", sdf.parse("1/12/2018 20:00"), "https://barrapunto.com", coor, false, false, false, null
			},
			//P2: Without Picture
			{
				"rendezvous7", "user1", "Visita al Museo del Prado", "Para los que les gusta vivir al límite.", sdf.parse("1/12/2018 20:00"), null, coor, true, false, false, null
			},
			//P3: Without Location
			{
				"rendezvous7", "user1", "De compras por Sevilla", "Adictos a las compras venid a mi!", sdf.parse("1/12/2018 20:00"), "https://wallapop.com", null, true, false, false, null
			},
			//P4: With adult true
			{
				"rendezvous7", "user1", "Feria de Abril", "Nada de beber ni bailar chic@s", sdf.parse("1/12/2018 20:00"), "https://www.elotrolado.com", null, false, true, false, null
			},
			//P5: With draft true
			{
				"rendezvous7", "user1", "Jueves Santo en Málaga", "Ánimo valientes!!! AahhhEstaaaEeehh!!!", sdf.parse("1/12/2018 20:00"), "https://www.aliexpress.com", null, true, false, false, null
			},

			//Negative cases
			//N0: Without user
			{
				"rendezvous7", null, "Cita en Madrid", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://yahoo.com", coor, true, false, false, NullPointerException.class
			},
			//N1: Without name
			{
				"rendezvous7", "user1", null, "Kayak en el rio", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, true, false, false, ConstraintViolationException.class
			},
			//N2: Without description
			{
				"rendezvous7", "user1", "Jueves Santo en Málaga", null, sdf.parse("1/12/2018 20:00"), "https://informatica.us.es", coor, true, false, false, ConstraintViolationException.class
			},
			//N3: Without moment
			{
				"rendezvous7", "user1", "Feria de Abril", "Description Rendezvous Test 1", null, "https://www.elpais.com", coor, true, false, false, ConstraintViolationException.class
			},
			//N4: Without draft
			{
				"rendezvous7", "user1", "Visita al Museo del Prado", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://www.elotrolado.com", coor, null, false, false, ConstraintViolationException.class
			},
			//N5: Without deleted
			{
				"rendezvous7", "user1", "Name Rendezvous Test 11", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, false, null, false, ConstraintViolationException.class
			},
			//N6: Without adult
			{
				"rendezvous7", "user1", "De compras por Sevilla", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://barrapunto.com", coor, false, false, null, ConstraintViolationException.class
			},
			//N7: With no future moment
			{
				"rendezvous7", "user1", "Tour turistico por la Cartuja", "Nos hartamos de ver becarios", sdf.parse("1/12/2017 20:00"), "https://www.androidpc.es", coor, true, false, false, ConstraintViolationException.class
			},
			//N8: With a user that is not the creator
			{
				"rendezvous7", "user2", "¿Que he hecho con mi vida?", "Grupo de autoayuda para informáticos", sdf.parse("1/12/2018 20:00"), "https://www.ebay.com", coor, true, false, false, ConstraintViolationException.class
			},
			//N9: With picture not url
			{
				"rendezvous7", "user1", "Otra cita mas", "Lorem ipsum", sdf.parse("1/12/2018 20:00"), "picture", coor, true, false, false, ConstraintViolationException.class
			},
			//N10: With draft is false
			{
				"rendezvous1", "user1", "Visita a la Giralda", "Ven a ver uno de los monumentos mas antiguos de Sevilla.", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, false, false, false, ConstraintViolationException.class
			},
			//N11: With deleted is true
			{
				"rendezvous5", "user2", "Nos conocemos en la plaza?", "Quedada para darnos a conocer todo el grupo.", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, true, false, true, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditTest(i, (String) testingData[i][0], //rendezvousId
				(String) testingData[i][1],  //username
				(String) testingData[i][2],	 //name
				(String) testingData[i][3],  //description
				(Date) testingData[i][4],    //moment
				(String) testingData[i][5],  //picture
				(Coordinate) testingData[i][6],	 //location
				(Boolean) testingData[i][7],	 //draft
				(Boolean) testingData[i][8],	 //delete
				(Boolean) testingData[i][9],	 //adult
				(Class<?>) testingData[i][10]);
	}
	protected void templateEditTest(final Integer i, final String rendezvousId, final String username, final String name, final String description, final Date moment, final String picture, final Coordinate location, final Boolean draft,
		final Boolean deleted, final Boolean adult, final Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findCreatedByUser(this.getEntityId(username));
			final Rendezvous rendezvous = this.rendezvousService.findOne(super.getEntityId(rendezvousId));
			for (final Rendezvous r : rendezvouses)
				if (r.equals(rendezvous)) {
					rendezvous.setName(name);
					rendezvous.setDescription(description);
					rendezvous.setMoment(moment);
					rendezvous.setPicture(picture);
					rendezvous.setLocation(location);
					rendezvous.setDraft(draft);
					rendezvous.setDeleted(deleted);
					rendezvous.setAdult(adult);
					this.rendezvousService.save(rendezvous);
					this.rendezvousService.flush();
				}

			System.out.println(i + " Edit Rendezvous: " + name + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Edit Rendezvous: " + name + " " + oops.getClass().toString());
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario logado creando una cita
	 */
	@Test
	public void userCreatingRendezvousTest() throws ParseException {

		System.out.println("Test 4: Usuario logado creando una cita.");

		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		final Object testingData[][] = {
			//Positive cases
			{
				"user1", "cita en madrid", "nos vamos para madrid!", sdf.parse("01/09/2018"), null
			},

			//Negative cases
			//Without name
			{
				"", "cita en madrid", "nos vamos para madrid!", sdf.parse("01/09/2018"), IllegalArgumentException.class
			},
		};

		Class<?> caught = null;
		for (int i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.authenticate((String) testingData[i][0]);
				final Rendezvous r = this.rendezvousService.create();
				r.setName((String) testingData[i][1]);
				r.setDescription((String) testingData[i][2]);
				r.setMoment((Date) testingData[i][3]);

				final Rendezvous saved = this.rendezvousService.save(r);

				Assert.isTrue(this.rendezvousService.findAll().contains(saved));

				System.out.println(i + " Test 4: ok");

			} catch (final Throwable oops) {
				caught = oops.getClass();
				System.out.println(i + " Test 4: " + oops.getClass().toString());
			}

			this.checkExceptions((Class<?>) testingData[i][4], caught);
		}
	}

	/*
	 * Caso de uso:
	 * Usuario logado listando las citas que ha creado y editando o eliminando una
	 */
	@Test
	public void userListingHisRendezvousesAndEditingTest() throws ParseException {

		System.out.println("Test 5: Usuario logado listando las citas que ha creado y editando o eliminando una.");

		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		final Object testingData[][] = {
			//Positive cases
			{
				"user1", "cita en madrid", "nos vamos para madrid!", sdf.parse("01/09/2018"), null
			},

			//Negative cases
			//Without name
			{
				"", "cita en madrid", "nos vamos para madrid!", sdf.parse("01/09/2018"), IllegalArgumentException.class
			},
		};

		Class<?> caught = null;
		for (int i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.authenticate((String) testingData[i][0]);
				final Collection<Rendezvous> rs = this.rendezvousService.findCreatedByUser(this.userService.findByPrincipal().getId());
				final Rendezvous r = rs.toArray(new Rendezvous[rs.size()])[0];
				r.setName((String) testingData[i][1]);
				r.setDescription((String) testingData[i][2]);
				r.setMoment((Date) testingData[i][3]);

				final Rendezvous saved = this.rendezvousService.save(r);

				Assert.isTrue(this.rendezvousService.findAll().contains(saved));

				System.out.println(i + " Test 5: ok");

			} catch (final Throwable oops) {
				caught = oops.getClass();
				System.out.println(i + " Test 5: " + oops.getClass().toString());
			}

			this.checkExceptions((Class<?>) testingData[i][4], caught);
		}
	}

	/*
	 * Caso de uso:
	 * Usuario admin listando citas, y eliminando una inapropiada
	 */
	@Test
	public void adminListingRendezvousesAndDeletingTest() throws ParseException {

		System.out.println("Test 10: Usuario admin listando citas, y eliminando una inapropiada.");

		final Object testingData[][] = {
			//Positive cases
			{
				"admin", null
			},

			//Negative cases
			//Usuario no es el administrador
			{
				"user1", ClassCastException.class
			},
		};

		Class<?> caught = null;
		for (int i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.authenticate((String) testingData[i][0]);
				final Administrator admin = this.administratorService.findByPrincipal();

				Collection<Rendezvous> rs = null;
				rs = this.rendezvousService.findAll();

				final Rendezvous r = rs.toArray(new Rendezvous[rs.size()])[0];
				this.rendezvousService.delete(r);

				System.out.println(i + " Test 10: ok");

			} catch (final Throwable oops) {
				caught = oops.getClass();
				System.out.println(i + " Test 10: " + oops.getClass().toString());
			}

			this.checkExceptions((Class<?>) testingData[i][1], caught);
		}
	}

}
