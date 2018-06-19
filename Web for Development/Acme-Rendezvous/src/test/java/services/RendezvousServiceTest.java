
package services;

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

import utilities.AbstractTest;
import domain.Coordinate;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class RendezvousServiceTest extends AbstractTest {

	// Service under test ------------------------
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;


	/* *****************************************************
	 * *An actor who is authenticated as a user must be able to:
	 * *** 2. Create a rendezvous, which he’s implicitly assumed to attend.
	 * *** Note that a user may edit his or her rendezvouses as long as they
	 * *** aren’t saved them in final mode. Once a rendezvous is saved in
	 * *** final mode, it cannot be edited or deleted by the creator.
	 * ******************************************************
	 */

	@Test
	public void createRendezvousTest() throws ParseException {

		System.out.println("-----Create rendezvous test. Positive 0 to 1, Negative 2 to 5.");

		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		final Object testingData[][] = {
			//Positive cases
			{
				"user1", "rendezvous 1", sdf.parse("21/12/2018"), null
			}, {
				"user2", "rendezvous 2", sdf.parse("21/12/2018"), null
			},

			//Negative cases
			//Without user
			{
				null, "rendezvous 3", sdf.parse("21/12/2016"), IllegalArgumentException.class
			},
			//Without moment
			{
				"user1", null, null, NullPointerException.class
			},
			//With past moment
			{
				"user2", "rendezvous 6", sdf.parse("21/12/2016"), IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateTest(i, (String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void templateCreateTest(final Integer i, final String username, final String name, final Date moment, final Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			final Rendezvous r = this.rendezvousService.create();
			r.setName(name);
			r.setDescription("this is the " + name + " rendezvous.");
			r.setMoment(moment);
			this.rendezvousService.save(r);

			System.out.println(i + " Create Rendezvous: " + name + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Create Rendezvous: " + name + " " + oops.getClass().toString());
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * Note that a user may edit his or her rendezvouses as long as they aren’t saved
	 * them in final mode. Once a rendezvous is saved in final mode, it cannot be edited
	 * or deleted by the creator.
	 */

	@Test
	public void editRendezvousTest() throws ParseException {

		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		final Coordinate coor = new Coordinate();
		coor.setLongitude("40.256");
		coor.setLatitude("-30.256");

		System.out.println("-----Edit rendezvous test. Positive 0 to 5, Negative 3 to 15.");

		final Object testingData[][] = {
			//Positive cases
			//P0: Only Name, Description and moment
			{
				"rendezvous1", "user1", "Name Rendezvous Test 0", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), null, null, true, false, false, null
			},
			//P1: All fields
			{
				"rendezvous1", "user1", "Name Rendezvous Test 1", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, false, false, false, null
			},
			//P2: Without Picture
			{
				"rendezvous1", "user1", "Name Rendezvous Test 2", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), null, coor, true, false, false, null
			},
			//P3: Without Location
			{
				"rendezvous1", "user1", "Name Rendezvous Test 3", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://picture.com", null, true, false, false, null
			},
			//P4: With adult true
			{
				"rendezvous1", "user1", "Name Rendezvous Test 4", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://picture.com", null, false, true, false, null
			},
			//P5: With draft true
			{
				"rendezvous1", "user1", "Name Rendezvous Test 5", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://picture.com", null, true, false, false, null
			},

			//Negative cases
			//N0: Without user
			{
				"rendezvous1", null, "Name Rendezvous Test 6", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, true, false, false, IllegalArgumentException.class
			},
			//N1: Without name
			{
				"rendezvous1", "user1", null, "Description Rendezvous Test 7", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, true, false, false, ConstraintViolationException.class
			},
			//N2: Without description
			{
				"rendezvous1", "user1", "Name Rendezvous Test 8", null, sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, true, false, false, ConstraintViolationException.class
			},
			//N3: Without moment
			{
				"rendezvous1", "user1", "Name Rendezvous Test 9", "Description Rendezvous Test 1", null, "https://picture.com", coor, true, false, false, NullPointerException.class
			},
			//N4: Without draft
			{
				"rendezvous1", "user1", "Name Rendezvous Test 10", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, null, false, false, NullPointerException.class
			},
			//N5: Without deleted
			{
				"rendezvous1", "user1", "Name Rendezvous Test 11", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, false, null, false, NullPointerException.class
			},
			//N6: Without adult
			{
				"rendezvous1", "user1", "Name Rendezvous Test 12", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, false, false, null, NullPointerException.class
			},
			//N7: With no future moment
			{
				"rendezvous1", "user1", "Name Rendezvous Test 13", "Description Rendezvous Test 1", sdf.parse("1/12/2017 20:00"), "https://picture.com", coor, true, false, false, IllegalArgumentException.class
			},
			//N8: With a user that is not the creator
			{
				"rendezvous1", "user2", "Name Rendezvous Test 14", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "https://picture.com", coor, true, false, false, IllegalArgumentException.class
			},
			//N9: With picture not url
			{
				"rendezvous1", "user1", "Name Rendezvous Test 15", "Description Rendezvous Test 1", sdf.parse("1/12/2018 20:00"), "picture", coor, true, false, false, ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditTest(i, 
				(Integer) super.getEntityId((String) testingData[i][0]), //rendezvousId
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
	protected void templateEditTest(final Integer i, final Integer rendezvousId, final String username, final String name, final String description, final Date moment, final String picture, final Coordinate location, final Boolean draft,
		final Boolean deleted, final Boolean adult, final Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
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

			System.out.println(i + " Edit Rendezvous: " + name + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Edit Rendezvous: " + name + " " + oops.getClass().toString());
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * Update or delete the rendezvouses that he or she’s created. Deletion is virtual, that is:
	 * the information is not removed from the database, but the rendezvous cannot be updated.
	 * Deleted rendezvouses are flagged as such when they are displayed.
	 */

	@Test
	public void deleteRendezvousTest() throws ParseException {

		System.out.println("-----Delete rendezvous test. Positive 0, Negative 1-4.");

		final Object testingData[][] = {
			//Positive cases
			//P1: With draft true
			{
				"P1", "rendezvous6", "user3", null
			},


			//Negative cases
			//N1: With draft false
			{
				"N1", "rendezvous2", "user1", IllegalArgumentException.class
			},
			//N2: With user that is not the creator
			{
				"N2", "rendezvous3", "user2", IllegalArgumentException.class
			},
			//N3: With user anonymous
			{
				"N3", "rendezvous1", "", IllegalArgumentException.class
			},
			//N5: With deleted true
			{
				"N5", "rendezvous1", "user1", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteRendezvousTest(
				i, 
				(String) testingData[i][0], 
				(Integer) this.getEntityIdNullable((String) testingData[i][1]), //rendezvousId
				(String) testingData[i][2],  //username
				(Class<?>) testingData[i][3]);
	}
	protected void templateDeleteRendezvousTest(final Integer i, final String index, final Integer rendezvousId, final String username, final Class<?> expected) {

		Class<?> caught = null;

		try {

			super.authenticate(username);
			final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
			this.rendezvousService.deleteByUser(rendezvous);

			System.out.println(i + " Delete Rendezvous: " + index + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Delete Rendezvous: " + index + " " + oops.getClass().toString());
		}

		super.checkExceptions(expected, caught);
	}

	/*
	 * List the rendezvouses that he or she’s RSVPd.
	 */

	@Test
	public void listRendezvousesRSVPTest() throws ParseException {

		System.out.println("-----List rendezvouses RSVP test. Positive 0, Negative 1.");

		final Object testingData[][] = {
			//Positive cases
			{
				"user2", "user2", null
			},

		//Negative cases
			//User 1 has not reserved any rendezvous
			{
				"user1", "user1", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListRendezvousesRSVPTest(
					i, 
					(Integer) this.getEntityId((String) testingData[i][0]), //userId
					(String) testingData[i][1], //username
					(Class<?>) testingData[i][2]);
	}
	protected void templateListRendezvousesRSVPTest(
			final Integer i, 
			final Integer userId, 
			final String username, 
			final Class<?> expected) {

		Class<?> caught = null;
		final User userBD = this.userService.findOne(userId);

		try {
			super.authenticate(username);
			Collection<Rendezvous> col = this.rendezvousService.findReservedByUser(userId);
			Assert.isTrue(col.size() != 0);
			super.unauthenticate();

			System.out.println(i + " Edit Rendezvous: " + userBD.getName() + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Edit Rendezvous: " + userBD.getName() + " " + oops.getClass().toString());
		}

		super.checkExceptions(expected, caught);
	}

}
