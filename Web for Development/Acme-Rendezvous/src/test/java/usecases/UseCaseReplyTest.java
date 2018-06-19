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

import domain.Comment;
import domain.Reply;
import services.CommentService;
import services.RendezvousService;
import services.ReplyService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UseCaseReplyTest  extends AbstractTest {
	
	
	@Autowired
	ReplyService replyService;
	@Autowired
	CommentService commentService;
	@Autowired
	RendezvousService rendezvousService;
	

	
	/*
	 * Requerimientos:
	 * 19. In addition to writing a comment from scratch, a user may reply to a comment.
	 * 
	 */

	/*
	 * Caso de uso:
	 * Usuario logado listando comentarios y haciendo reply a uno
	 */
	
	@Test
	public void createTest() throws ParseException {
		
		System.out.println("-----Create reply test. Positive 0 to 4, Negative 5 to 6.");
		
		Object testingData[][]= {
				//Positive cases, users that have RSVP a rendezvous
				{"user2", "No estoy de acuerdo con este viaje!!!",  null},
				{"user3", "Pienso que no os va a gustar", null},
				{"user4", "Pienso lo mismo", null},
				{"user1", "¿No crees que no deberias de hacerlo?", null},
				{"user3", "Muy mal pensar eso!", null},
				
				//Negative cases
				//Without user
				{null, "Nueva respuesta al comentario 6",  IllegalArgumentException.class},
				//Without text
				{"user1", null, ConstraintViolationException.class},
				};
		
		
		for (int i = 0; i< testingData.length; i++) {
			templateCreateTest(
					i, 
					(String) testingData[i][0],
					(String) testingData[i][1],
					(Class<?>) testingData[i][2]
					);
		}
	}
	
	protected void templateCreateTest (
			Integer i,
			String username, 
			String text,
			Class<?> expected) {
		
		Class<?> caught = null;
		
		try {
			super.authenticate(username);
			Collection<Comment> comments = commentService.findAll();
			Comment c = comments.toArray(new Comment[comments.size()])[0];
			Reply reply = this.replyService.create(c.getId());
			reply.setText(text);
			Reply saved = this.replyService.save(reply);
			this.replyService.flush();
			System.out.println(i + " Create Reply: " + text + " ok");
		
		} catch (Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Create Reply: " + text + " " + oops.getClass().toString());
		}
		
		
		checkExceptions(expected, caught);		
	}
		

}
