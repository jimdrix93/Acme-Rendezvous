package usecases;

import java.text.ParseException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Rendezvous;
import domain.Reservation;
import domain.User;
import services.RendezvousService;
import services.ReservationService;
import services.UserService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UseCaseReservationTest extends AbstractTest{

	@Autowired
	private RendezvousService		rendezvousService;
	@Autowired
	private ReservationService		reservationService;
	@Autowired
	private UserService				userService;
	
	/*
	 * Requerimientos:
	 * 5. An actor who is authenticated as a user must be able to:
	 * 		4. RSVP a rendezvous or cancel it. "RSVP" is a French term that means "Réservez, s'il
	 * 		   vous plaît"; it's commonly used in the anglo-saxon world to mean "I will attend this
	 *  	   rendezvous"; it's pronounced as "/ri:'serv/", "/ri:'serv'silvu'ple/", or "resvipi".
	 *  	   When a user RSVPs a rendezvous, he or she is assumed to attend it.
	 *  	5. List the rendezvouses that he or she's RSVPd
	 *  14. Some rendezvouses may be flagged as "adult only", in which case every attempt to RSVP
	 *  them by users who are under 18 must be prohibited. Such rendezvouses must not be dis-
	 *  played unless the user who is browsing them is at least 18 year old. Obviously, they must not
	 *  be shown to unauthenticated users.
	 */
	
	
	/*
	 * Caso de uso: 
	 * Usuario logado listando las citas existentes y reservando una
	 */
	@Test
	public void userListingRendezvousesAndReservingTest() throws ParseException {

		System.out.println("Test 6: Usuario logado listando las citas existentes y reservando una.");

		final Object testingData[][] = {
			//Positive cases
			{
				"user1", true, null
			},

			//Negative cases
			//Intentando reservar una creada por el propio usuario
			{
				"user1", false, DataIntegrityViolationException.class
			},
		};

		Class<?> caught = null;
		for (int i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.authenticate((String) testingData[i][0]);
				final User user = this.userService.findByPrincipal();

				Collection<Rendezvous> rs = null;
				if ((Boolean) testingData[i][1])
					rs = this.rendezvousService.findAllFinal();
				else
					rs = this.rendezvousService.findCreatedByUser(user.getId());

				final Reservation reserv = this.reservationService.create();
				reserv.setRendezvous(rs.toArray(new Rendezvous[rs.size()])[0]);
				reserv.setUser(this.userService.findByPrincipal());
				final Reservation saved = this.reservationService.save(reserv);

				Assert.isTrue(this.reservationService.findAll().contains(saved));

				System.out.println(i + " Test 6: ok");

			} catch (final Throwable oops) {
				caught = oops.getClass();
				System.out.println(i + " Test 6: " + oops.getClass().toString());
			}

			this.checkExceptions((Class<?>) testingData[i][2], caught);
		}
	}

	/*
	 * Caso de uso: 
	 * Usuario logado listando las citas a las que va a asistir y cancelando dicha asistencia
	 */
	@Test
	public void userListingRendezvousesAndCancelingTest() throws ParseException {

		System.out.println("Test 7: Usuario logado listando las citas a las que va a asistir y cancelando dicha asistencia.");

		final Object testingData[][] = {
			//Positive cases
			{
				"user2", true, null
			},

			//Negative cases
			//Intentando cancelar una ya cancelada
			{
				null, false, IllegalArgumentException.class
			},
		};

		Class<?> caught = null;
		for (int i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.authenticate((String) testingData[i][0]);
				final User user = this.userService.findByPrincipal();

				Collection<Rendezvous> rs = null;
				if ((Boolean) testingData[i][1])
					rs = this.rendezvousService.findReservedAndNotCanceledByUserId(user.getId());
				else
					rs = this.rendezvousService.findCanceledByUserId(user.getId());

				final Reservation res = this.reservationService.findReservationByUserAndRendezvous(user, rs.toArray(new Rendezvous[rs.size()])[0]);
				res.setCanceled(true);
				final Reservation saved = this.reservationService.save(res);
				Assert.isTrue(saved.isCanceled());

				System.out.println(i + " Test 7: ok");

			} catch (final Throwable oops) {
				caught = oops.getClass();
				System.out.println(i + " Test 7: " + oops.getClass().toString());
			}

			this.checkExceptions((Class<?>) testingData[i][2], caught);
		}
	}
	

	/*
	 * Caso de uso:
	 * Usuario menor de edad o anonimo no puede listar citas y no puede ver citas para adultos
	 */
	@Test
	public void listRendezvousesAndSeeRendezvousWithAdultOptionTest() {

		final Object testingData[][] = {
			{// user2 (adult) can list rendezvous with adult option (rendezvous8 is adult option selected)
				"user2", "rendezvous1", "rendezvous8", null
			}, {// user4 (not adult) can´t list rendezvous with adult option (rendezvous8 is adult option selected)
				"user4", "rendezvous1", "rendezvous8", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListAndSeeRendezvousWithAdultOption((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (Class<?>) testingData[i][3]);
	}

	protected void templateListAndSeeRendezvousWithAdultOption(final String username, final int rendezvousId, final int adultRendezvousId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			final Collection<Rendezvous> linkedRendezvouses;
			if (username != null) {
				final User user = this.userService.findByPrincipal();
				if (user.getAdult())
					linkedRendezvouses = this.rendezvousService.findAllFinal();
				else
					linkedRendezvouses = this.rendezvousService.findAllFinalAndNotAdult();
			} else
				linkedRendezvouses = this.rendezvousService.findAllFinalAndNotAdult();

			final Rendezvous adultRendezvous = this.rendezvousService.findOne(adultRendezvousId);

			Assert.isTrue(linkedRendezvouses.contains(adultRendezvous));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}	
}
