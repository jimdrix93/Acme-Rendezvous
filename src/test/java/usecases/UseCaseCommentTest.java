
package usecases;

import java.text.ParseException;
import java.util.Collection;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Administrator;
import domain.Comment;
import domain.Rendezvous;
import domain.User;
import services.AdministratorService;
import services.CommentService;
import services.RendezvousService;
import services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/junit.xml"})
@Transactional
public class UseCaseCommentTest extends AbstractTest {

	// Service under test ------------------------
	@Autowired
	private CommentService	commentService;

	@Autowired
	private UserService userService;
	@Autowired
	private RendezvousService		rendezvousService;
	@Autowired
	private AdministratorService administratorService;

	
	/*
	 * Requerimientos:
	 * 3. The system must handle comments about the rendezvouses. For every comment the system
	 * must store the user who wrote it, the moment when it was written, the corresponding text,
	 * and an optional picture that is referenced by means of a URL.
	 * 5. An actor who is authenticated as a user must be able to:
	 * 		6. Comment on the rendezvouses that he or she has RSVPd.
	 * 6. An actor who is authenticated as an administrator must be able to:
	 * 		1. Remove a comment that he or she thinks is inappropriate.
	 */

	/*
	 * Caso de uso:
	 * Usuario anónimo listando comentarios por rendezvous
	 */
	@Test
	public void findByRendezvousTest() throws ParseException {
		
		System.out.println("-----Find Comment by rendezvous test. Positive 0 to 1, Negative 2.");
		
		Object testingData[][]= {
				//Positive cases
				{"rendezvous1", null},
				{"rendezvous2", null},
				
				//Negative cases
				//Without rendezvous
				{"9999", NullPointerException.class}
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
			Collection <Comment> comments = this.commentService.findAllByRendezvousId(rendezvousId);
			Assert.notNull(comments);
			System.out.println(i + " FindByRendezvous: " + rendezvousId + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " FindByRendezvous: " + rendezvousId + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}	
	
	
	/*
	 * Caso de uso:
	 * Usuario logado listando sus comentarios
	 */	
	@Test
	public void findAllByUserTest() throws ParseException {
		
		System.out.println("-----Find all Comment by user test. Positive 0 to 1, Negative 2.");
		
		Object testingData[][]= {
				//Positive cases
				{"user1", null},
				{"user2", null},
				
				//Negative cases
				//Without rendezvous
				{null, IllegalArgumentException.class}
				};
		
		
		for (int i = 0; i< testingData.length; i++) {
			templateFindAllByUserTest(
					i,
					(String) testingData[i][0],
					(Class<?>) testingData[i][1]
					);
		}
	}
	
	protected void templateFindAllByUserTest (
			Integer i,
			String user,
			Class<?> expected) {
		
		Class<?> caught = null;
		
		try {
			super.authenticate(user);
			int userId = this.userService.findByPrincipal().getId();
			Collection <Comment> comments = this.commentService.findAllByUser(userId);
			Assert.notNull(comments);
			System.out.println(i + " FindAllByUser: " + user + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " FindAllByUser: " + user + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}	
	
	
	/*
	 * Caso de uso:
	 * Usuario logado creando comentario y guardandolo
	 */		
	@Test
	public void createTest() throws ParseException {
		
		System.out.println("-----Create comment test. Positive 0 to 4, Negative 5 to 8.");
		
		Object testingData[][]= {
				//Positive cases, users that have RSVP a rendezvous
				{"rendezvous1", "user2", "Comentario 1", "http://www.google.es",  null},
				{"rendezvous1", "user3", "Comentario 2", "http://www.elpais.es",  null},
				{"rendezvous1", "user4", "Comentario 3", "http://www.elpais.es",  null},
				{"rendezvous2", "user1", "Comentario 4", "http://www.elpais.es",  null},
				{"rendezvous2", "user3", "Comentario 5", "http://www.elpais.es",  null},
				
				//Negative cases
				//Without rendezvous
				{null, "user2", "Comentario 6", "http://www.google.es", NullPointerException.class},
				//Without text 1
				{"rendezvous1", "user2", null, "http://www.google.es", ConstraintViolationException.class},
				//Without text 2
				{"rendezvous1", "user2", "", "http://www.google.es", ConstraintViolationException.class},
				//With a not RSVP rendezvous
				{"rendezvous1", "user1", "Comentario 10", "http://www.google.es", ConstraintViolationException.class}
				};
		
		
		for (int i = 0; i< testingData.length; i++) {
			templateCreateTest(
					i, 
					(Integer) this.getEntityIdNullable((String) testingData[i][0]),
					(String) testingData[i][1],
					(String) testingData[i][2],
					(String) testingData[i][3],
					(Class<?>) testingData[i][4]
					);
		}
	}
	
	protected void templateCreateTest (
			Integer i,
			Integer rendezvousId,
			String username, 
			String text,
			String picture,
			Class<?> expected) {
		
		Class<?> caught = null;
		
		try {
			super.authenticate(username);
			Comment c = commentService.create(rendezvousId);
			c.setText(text);
			c.setPicture(picture);
			this.commentService.save(c);
			this.commentService.flush();
			System.out.println(i + " Create Comment: " + text + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Create Comment: " + text + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}
	
	/*
	 * Caso de uso:
	 * Usuario logado listando comentarios y editando uno
	 */	
	@Test
	public void editTest() throws ParseException {
		
		System.out.println("-----Edit comment test. Positive 0 to 4, Negative 5 to 8.");
		
		
		Object testingData[][]= {
				//Positive cases, users that have RSVP a rendezvous
				{"comment1-1", "user2", "Comentario 1", "http://www.google.es",  null},
				{"comment2-1", "user3", "Comentario 2", "http://www.elpais.es",  null},
				{"comment3-1", "user4", "Comentario 3", "http://www.elpais.es",  null},
				{"comment1-2", "user1", "Comentario 4", "http://www.elpais.es",  null},
				{"comment2-2", "user3", "Comentario 5", "http://www.elpais.es",  null},
				
				//Negative cases
				//Without rendezvous
				{null, "user2", "Comentario 6", "http://www.google.es", NullPointerException.class},
				//Without text 1
				{"comment1-1", "user2", null, "http://www.google.es", ConstraintViolationException.class},
				//Without text 2
				{"comment1-1", "user2", "", "http://www.google.es", ConstraintViolationException.class},
				//With a different user
				{"comment1-1", "user1", "Comentario 10", "http://www.google.es", IllegalArgumentException.class}
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
			Integer commentId,
			String username, 
			String text,
			String picture,
			Class<?> expected) {
		
		Class<?> caught = null;
		
		try {
			super.authenticate(username);
			Comment c = commentService.findOne(commentId);
			c.setText(text);
			c.setPicture(picture);
			this.commentService.save(c);
			this.commentService.flush();
			System.out.println(i + " Edit Comment: " + text + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Edit Comment: " + text + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}
	
	/*
	 * Caso de uso:
	 * Usuario administrador eliminando comentarios
	 */	
	@Test
	public void deleteTest() throws ParseException {
		
		System.out.println("-----Delete comment test. Positive 0 to 1, Negative 2 to 4.");
		
		Object testingData[][]= {
				//Positive cases
				{"comment1-1", "admin", null},
				{"comment1-2", "admin", null},
				
				//Negative cases
				//Without comment
				{null, "admin", NullPointerException.class},
				//Without user
				{"comment3-1", null, IllegalArgumentException.class},
				//Without admin
				{"comment1-2", "user1", IllegalArgumentException.class}
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
			Integer commentId,
			String username, 
			Class<?> expected) {
		
		Class<?> caught = null;
		
		try {
			super.authenticate(username);
			Comment c = this.commentService.findOne(commentId);
			this.commentService.delete(c);
			this.commentService.flush();
			System.out.println(i + " Delete Comment: " + commentId + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Delete Comment: " + commentId + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}
	
	

	/*
	 * Caso de uso: 
	 * Usuario logado listando citas a las que va a asistir, y comentando una de ellas
	 */
	@Test
	public void userListingRendezvousesAndCommentingTest() throws ParseException {

		System.out.println("Test 8: Usuario logado listando citas a las que va a asistir, y comentando una de ellas.");

		final Object testingData[][] = {
			//Positive cases
			{
				"user2", "esta cita no me va a gustar", true, null
			},

			//Negative cases
			//Comentario nulo
			{
				"user2", null, false, ConstraintViolationException.class
			},
		};

		Class<?> caught = null;
		for (int i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.authenticate((String) testingData[i][0]);
				final User user = this.userService.findByPrincipal();

				Collection<Rendezvous> rs = null;
				rs = this.rendezvousService.findReservedAndNotCanceledByUserId(user.getId());

				final Rendezvous r = rs.toArray(new Rendezvous[rs.size()])[0];
				final Comment c = this.commentService.create(r.getId());
				c.setText((String) testingData[i][1]);
				final Comment saved = this.commentService.save(c);

				Assert.isTrue(this.commentService.findAll().contains(saved));

				System.out.println(i + " Test 8: ok");

			} catch (final Throwable oops) {
				caught = oops.getClass();
				System.out.println(i + " Test 8: " + oops.getClass().toString());
			}

			this.checkExceptions((Class<?>) testingData[i][3], caught);
		}
	}

	
	

	/*
	 * Caso de uso: 
	 * Usuario admin listando citas, eligiendo una, listando comentarios y eliminando uno inapropiado
	 */
	@Test
	public void adminListingRendezvousesAndDeletingCommentTest() throws ParseException {

		System.out.println("Test 9: Usuario admin listando citas, eligiendo una, listando comentarios y eliminando uno inapropiado.");

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
				final Collection<Comment> comments = this.commentService.findAll();

				final Comment c = comments.toArray(new Comment[comments.size()])[0];

				this.commentService.deleteByAdministrator(c);

				System.out.println(i + " Test 9: ok");

			} catch (final Throwable oops) {
				caught = oops.getClass();
				System.out.println(i + " Test 9: " + oops.getClass().toString());
			}

			this.checkExceptions((Class<?>) testingData[i][1], caught);
		}
	}	

}
