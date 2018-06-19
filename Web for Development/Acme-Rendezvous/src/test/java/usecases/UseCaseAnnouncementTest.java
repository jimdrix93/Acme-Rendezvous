
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

import services.AnnouncementService;
import services.RendezvousService;
import utilities.AbstractTest;
import domain.Announcement;
import domain.Rendezvous;
import exceptions.HackingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UseCaseAnnouncementTest extends AbstractTest {

	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private AnnouncementService	announcementService;


	/* Requerimientos:
	 * 12. Rendezvouses may have announcements. The system must record the moment when an
	 * announcement is made, plus a title and a description.
	 * 15. An actor who is not authenticated must be able to: 
	 * 		1. List the announcements that are associated with each rendezvous.
	 * 16. An actor who is authenticated as a user must be able to:
	 * 		5. Display a stream of announcements that have been posted to the rendezvouses that
	 * 		   he or she's RSVPd. The announcements must be listed chronologically in descending
	 * 		   order.
	 * 17. An actor who is authenticated as an administrator must be able to:
	 * 		1. Remove an announcement that he or she thinks is inappropriate.
	 */
	
	/*
	 * Caso de uso:
	 * Usuario logado visionando un listado de anuncios de todas
	 * las citas en las que tiene reserva.
	 */
	@Test
	public void listAnnouncementsReserveRendezvousTest() {

		System.out.println("-----List announcement test. Positive 0 to 2, Negative 3 to 5.");

		final Object testingData[][] = {
			//Positivos
			{
				//Positivo 1: User1 access the announcements
				"P1", "user1", null
			}, {
				//Positivo 2: User access the announcements
				"P2", "user3", null
			}, {
				//Positivo 3: User1 don´t have reservation
				"P3", "user1", null
			},
			//Negativos
			{
				//Negativo 1: Anonymous tries to access the announcements
				"N1", "", IllegalArgumentException.class
			}, {
				//Negativo 12: User "blabla" tries to access the announcements
				"N2", "blabla", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListAnnouncementsReserveRendezvousTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(Class<?>) testingData[i][2]); //Exception class
	}


	protected void templateListAnnouncementsReserveRendezvousTest(final Integer i, final String nameTest, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Announcement> announcements = this.announcementService.findByReservedRendezvous();
			Assert.notNull(announcements);

			System.out.println(i + " List Announcement: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " List Announcement: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario admin listando citas, eligiendo una, listando anuncios y
	 * eliminando uno inapropiado.
	 */
	@Test
	public void deleteAnnouncementTest() {

		System.out.println("-----Delete announcement test. Positive 0 to 2, Negative 3 to 5.");

		final Object testingData[][] = {
			//Positives test cases
			{
				// Positivo 1: Admin deletes an inappropiate announcement
				"P1", "admin", "rendezvous1", "announcement1-1", null
			}, {
				//Positivo 2: Admin deletes announcement from other rendezvous
				"P2", "admin", "rendezvous2", "announcement2-1", null
			},
			//Negative test cases
			{
				// Negativo 1: Anonymous deletes user can create an user
				"N1", "", "rendezvous1", "announcement1-1", IllegalArgumentException.class
			}, {
				//Negativo 2: Admin deletes an inappropiate announcement that doesn't belong to rendezvous
				"N2", "admin", "rendezvous1", "announcement2-1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateUser(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //Rendezvous Id
				(String) testingData[i][3], //Announcement Id
				(Class<?>) testingData[i][4]); //Exception class
	}


	protected void templateCreateUser(final Integer i, final String nameTest, final String username, final String rendezvousId, final String announcementId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findAllFinal();
			final Rendezvous rendezvous = this.rendezvousService.findOne(super.getEntityId(rendezvousId));
			if (rendezvouses.contains(rendezvous)) {
				final Collection<Announcement> announcements = this.announcementService.findByRendezvous(super.getEntityId(rendezvousId));
				final Announcement announcement = this.announcementService.findOne(super.getEntityId(announcementId));
				if (announcements.contains(announcement))
					this.announcementService.deleteByAdministrator(announcement);
				else
					throw new IllegalArgumentException();
			} else
				throw new IllegalArgumentException();

			System.out.println(i + " Delete Announcement: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Delete Announcement: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}
	
	

	/*
	 * Caso de uso:
	 * Usuario anónimo listando anuncios por rendezvous
	 */
	@Test
	public void findByRendezvousTest() throws ParseException {
		
		System.out.println("-----Find by rendezvous test. Positive 0 to 1, Negative 2.");
		
		Object testingData[][]= {
				//Positive cases
				{"rendezvous1", null},
				{"rendezvous2", null},
				
				//Negative cases
				//Without rendezvous
				{"99999", NullPointerException.class}
				};
		
		
		for (int i = 0; i< testingData.length; i++) {
			templateFindByRendezvousTest(
					i,
					(Integer) this.getEntityIdNullable((String) testingData[i][0]),
					(Class<?>) testingData[i][1]
					);
		}
	}
	
	protected void templateFindByRendezvousTest (
			Integer i,
			Integer rendezvousId,
			Class<?> expected) {
		
		Class<?> caught = null;
		
		try {
			Collection <Announcement> announcements = this.announcementService.findByRendezvous(rendezvousId);
			
			System.out.println(i + " FindByRendezvous: " + rendezvousId + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " FindByRendezvous: " + rendezvousId + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}	
	
	/*
	 * Caso de uso:
	 * Usuario logado creando un anuncio en un rendezvous dado
	 */
	@Test
	public void createTest() throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		System.out.println("-----Create announcement test. Positive 0 to 4, Negative 5 to 10.");
		
		Object testingData[][]= {
				//Positive cases
				{"rendezvous1", "user1", "Vamos a gastarlo todo!", "Descripción con caracteres raros àéíóóàè`%%$$&##1323123!||ºººªª,.-;:_++{}", sdf.parse("1/12/2018"), null},
				{"rendezvous2", "user2", "Anuncio 2", "Esta es una nueva descripción", sdf.parse("21/12/2016"), null},
				{"rendezvous3", "user3", "Anuncio 3", "Descripción del anuncio 1", sdf.parse("21/2/2018"), null},
				{"rendezvous4", "user3", "Anuncio 4", "Y algo mas", sdf.parse("1/12/2017"), null},
				{"rendezvous4", "user3", "Anuncio 5", "Descripción del anuncio 1", sdf.parse("21/2/2018"), null},
				
				//Negative cases
				//Without rendezvous
				{null, "user2", "Anuncio 6", "Descripción del anuncio 1", sdf.parse("21/12/2018"), NullPointerException.class},
				//Without user
				{"rendezvous1", null, "Anuncio 7", "Descripción del anuncio 1", sdf.parse("21/12/2018"), IllegalArgumentException.class},
				//Without title
				{"rendezvous3", "user3", null, "Pues no se, puede ser", sdf.parse("21/12/2018"), ConstraintViolationException.class},
				//Without description
				{"rendezvous4", "user3", "Anuncio 9", null, sdf.parse("21/12/2018"), ConstraintViolationException.class},
				//Without moment
				{"rendezvous3", "user3", "Anuncio 10", "Tal vez tal vez", null, ConstraintViolationException.class},
				//With rendezvous not from the user
				{"rendezvous2", "user1", "Anuncio 11", "Descripción del anuncio 1", sdf.parse("21/12/2018"), HackingException.class},
				};
		
		
		for (int i = 0; i< testingData.length; i++) {
			templateCreateTest(
					i, 
					(Integer) super.getEntityIdNullable((String) testingData[i][0]),
					(String) testingData[i][1],
					(String) testingData[i][2],
					(String) testingData[i][3],
					(Date) testingData[i][4],
					(Class<?>) testingData[i][5]
					);
		}
	}
	
	protected void templateCreateTest (
			Integer i,
			Integer rendezvousId,
			String username, 
			String title,
			String description,
			Date moment,
			Class<?> expected) {
		
		Class<?> caught = null;
		
		try {
			super.authenticate(username);
			Announcement a = announcementService.create(rendezvousId);
			a.setTitle(title);
			a.setDescription(description);
			a.setMoment(moment);
			this.announcementService.save(a);
			this.announcementService.flush();
			
			System.out.println(i + " Create Announcement: " + title + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Create Announcement: " + title + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}
	
	
	/*
	 * Caso de uso:
	 * Usuario logado listando anuncios, y editando uno
	 */
	@Test
	public void editTest() throws ParseException {
	
		
		System.out.println("-----Edit announcement test. Positive 0 to 4, Negative 5 to 9.");
		
		Object testingData[][]= {
				//Positive cases
				{"announcement1-1", "user1", "Seguro de citas online", "Descripción del anuncio 1", null},
				{"announcement1-2", "user1", "Anuncio 2", "Descripción del anuncio 1", null},
				{"announcement2-1", "user2", "Ya a la venta", "Esta es una nueva descripción", null},
				{"announcement1-1", "user1", "Anuncio 4", "Descripción del anuncio 1", null},
				{"announcement1-2", "user1", "Llega a tu cita en perfecto estado", "Descripción del anuncio 1", null},
				
				//Negative cases
				//Without announcement
				{null, "user1", "Anuncio 6", "Descripción del anuncio 1",  NullPointerException.class},
				//Without user
				{"announcement1-1", null, "Anuncio 7", "Descripción del anuncio 1",  IllegalArgumentException.class},
				//Without title
				{"announcement1-1", "user1", null, "No se que hago aqui, como he llegado?",  ConstraintViolationException.class},
				//Without description
				{"announcement2-1", "user2", "Anuncio 9", null,  ConstraintViolationException.class},
				//With rendezvous not from the user
				{"announcement1-1", "user2", "Anuncio 11", "Descripción del anuncio 1",  IllegalArgumentException.class},
				};
		
		
		for (int i = 0; i< testingData.length; i++) {
			templateEditTest(
					i,
					(Integer) this.getEntityIdNullable((String) testingData[i][0]),
					(String) testingData[i][1],
					(String) testingData[i][2],
					(String) testingData[i][3],
					(Class<?>) testingData[i][4]
					);
		}
	}
	
	protected void templateEditTest (
			Integer i,
			Integer announcementId,
			String username, 
			String title,
			String description,
			Class<?> expected) {
		
		Class<?> caught = null;
		
		try {
			super.authenticate(username);
			Announcement a = announcementService.findOne(announcementId);
			a.setTitle(title);
			a.setDescription(description);
			this.announcementService.save(a);
			this.announcementService.flush();
			System.out.println(i + " Edit Announcement: " + title + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Edit Announcement: " + title + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}
	
	/*
	 * Caso de uso:
	 * Usuario logado listando anuncios, y eliminando uno
	 */
	@Test
	public void deleteTest() throws ParseException {
		
		System.out.println("-----Delete announcement by user test. Positive 0 to 1, Negative 2 to 4.");
		
		Object testingData[][]= {
				//Positive cases
				{"announcement1-1", "user1", null},
				{"announcement1-2", "user1", null},
				
				//Negative cases
				//Without announcement
				{null, "user1", NullPointerException.class},
				//Without user
				{"announcement2-1", null, IllegalArgumentException.class},
				//With rendezvous not from the user
				{"announcement2-1", "user1", IllegalArgumentException.class}
				};
		
		
		for (int i = 0; i< testingData.length; i++) {
			templateDeleteTest(
					i,
					(Integer) this.getEntityIdNullable((String) testingData[i][0]),
					(String) testingData[i][1],
					(Class<?>) testingData[i][2]
					);
		}
	}
	
	protected void templateDeleteTest (
			Integer i,
			Integer announcementId,
			String username, 
			Class<?> expected) {
		
		Class<?> caught = null;
		
		try {
			super.authenticate(username);
			Announcement a = announcementService.findOne(announcementId);
			this.announcementService.delete(a);
			this.announcementService.flush();
			System.out.println(i + " Delete Announcement by user: " + announcementId + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Delete Announcement by user: " + announcementId + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}
	
	/*
	 * Caso de uso:
	 * Usuario administrador eliminando un anuncio dado
	 */
	@Test
	public void deleteByAdministratorTest() throws ParseException {
		
		System.out.println("-----Delete announcement by administrator test. Positive 0 to 1, Negative 2 to 4.");
		
		Object testingData[][]= {
				//Positive cases
				{"announcement1-1", "admin", null},
				{"announcement1-2", "admin", null},
				
				//Negative cases
				//Without announcement
				{null, "admin", NullPointerException.class},
				//Without user
				{"announcement2-1", null, IllegalArgumentException.class},
				//With not admin
				{"announcement2-1", "user1", IllegalArgumentException.class}
				};
		
		
		for (int i = 0; i< testingData.length; i++) {
			templateDeleteByAdministratorTest(
					i,
					(Integer) this.getEntityIdNullable((String) testingData[i][0]),
					(String) testingData[i][1],
					(Class<?>) testingData[i][2]
					);
		}
	}
	
	protected void templateDeleteByAdministratorTest (
			Integer i,
			Integer announcementId,
			String username, 
			Class<?> expected) {
		
		Class<?> caught = null;
		
		try {
			super.authenticate(username);
			Announcement a = announcementService.findOne(announcementId);
			this.announcementService.deleteByAdministrator(a);
			this.announcementService.flush();
			System.out.println(i + " Delete Announcement by administrator: " + announcementId + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Delete Announcement by administrator: " + announcementId + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}	
	


	/*
	 * Caso de uso
	 * Usuario anonimo listando citas, eligiendo una y listando los anuncios
	 */
	@Test
	public void listRendezvousesChooseARendezvousAndListAnnouncementTest() {

		final Object testingData[][] = {
			{ // Anonymous can list rendezvouses and rendezvous announcements
				null, "rendezvous1", "announcement1-1", null
			}, {// Anonymous can list rendezvouses and rendezvous announcements, but the list of announcements not contains the announcement1-1
				null, "rendezvous2", "announcement1-1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListRendezvousesChooseARendezvousAndListAnnouncement((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (Class<?>) testingData[i][3]);
	}

	protected void templateListRendezvousesChooseARendezvousAndListAnnouncement(final String username, final int rendezvousId, final int announcementId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			final Collection<Rendezvous> listRendezvouses = this.rendezvousService.findAllFinal();
			final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
			Assert.isTrue(listRendezvouses.contains(rendezvous));

			final Collection<Announcement> listAnnouncements = this.announcementService.findByRendezvous(rendezvousId);
			final Announcement announcement = this.announcementService.findOne(announcementId);
			Assert.isTrue(listAnnouncements.contains(announcement));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario puede listar las citas que ha creado, elige una y crea un anuncio
	 */
	@Test
	public void listRendezvousesChooseARendezvousAndCreateAnnouncementTest() {

		final Object testingData[][] = {
			{ // User1 can list rendezvouses and create an announcement
				"user1", "rendezvous1", "titleAnnouncement1", "descriptionAnnouncement1", null
			}, {// Anonymous can list rendezvouses but can´t create an announcement
				null, "rendezvous1", "titleAnnouncement1", "descriptionAnnouncement1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListRendezvousesChooseARendezvousAndCreateAnnouncement((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	protected void templateListRendezvousesChooseARendezvousAndCreateAnnouncement(final String username, final int rendezvousId, final String titleAnnouncement, final String descriptionAnnouncement, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			final Collection<Rendezvous> listRendezvouses = this.rendezvousService.findAllFinal();
			final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
			Assert.isTrue(listRendezvouses.contains(rendezvous));

			final Announcement announcement = this.announcementService.create(rendezvousId);
			announcement.setTitle(titleAnnouncement);
			announcement.setDescription(descriptionAnnouncement);

			final Announcement announcementSaved = this.announcementService.save(announcement);
			this.announcementService.flush();

			Assert.isTrue(this.announcementService.findAll().contains(announcementSaved));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}


}
