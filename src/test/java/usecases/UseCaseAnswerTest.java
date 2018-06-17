
package usecases;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
import utilities.AbstractTest;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UseCaseAnswerTest extends AbstractTest {

	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private QuestionService		questionService;
	@Autowired
	private AnswerService		answerService;


	/*
	 * Requerimientos:
	 * 18. The creator of a rendezvous may associate a number of questions with it, each of which
	 * must be answered when a user RSVPs that rendezvous.
	 * 20. An actor who is not authenticated must be able to:
	 * 1. Display information about the users who have RSVPd a rendezvous, which, in turn,
	 * must show their answers to the questions that the creator has registered.
	 * 21. An actor who is authenticated as a user must be able to:
	 * 2. Answer the questions that are associated with a rendezvous that he or she's RSVPing now.
	 */

	/*
	 * Caso de uso:
	 * Usuario logado listando citas a las que va a asistir, eligiendo una y respondiendo preguntas
	 */
	@Test
	public void answerToQuestionTest() {

		System.out.println("-----Create answer test. Positive 0, Negative 1 to 2.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: User attendants for rendezvous
				"P1", "user3", "rendezvous1", "question1-1", "Creo que la pregunta no procede.", null
			}, {// P2: User creator for rendezvous
				"P2", "user3", "rendezvous1", "question2-1", "Claro, hay que llevar ropa de abrigo", null
			}, {// P3: User creator for rendezvous
				"P3", "user3", "rendezvous1", "question3-1", "No creo que sea necesario!!!", null
			},

			//Negative test cases
			{// N1: Anonymous tries to answer a question
				"N1", "", "rendezvous1", "question1-1", "Casi seguro que no es posible", IllegalArgumentException.class
			}, {//N2: User4 tries to answer a question without text
				"N2", "user4", "rendezvous1", "question1-1", "", ConstraintViolationException.class
			}, {//N3: User1 tries to answer a question that not exist
				"N3", "user1", "rendezvous1", "question3-5", "Por supuesto que llegaremos a tiempo para verlo!", NumberFormatException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAnswerQuestionTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //Rendezvous Id
				(String) testingData[i][3], //Question Id
				(String) testingData[i][4],	// Answer text
				(Class<?>) testingData[i][5]); //Exception class
	}

	protected void templateAnswerQuestionTest(final Integer i, final String nameTest, final String username, final String rendezvousId, final String questionId, final String answerText, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findAllFinal();
			final Rendezvous rendezvous = this.rendezvousService.findOne(super.getEntityId(rendezvousId));
			final Question question = this.questionService.findOne(super.getEntityId(questionId));
			final Answer answer = this.answerService.create(super.getEntityId(questionId), super.getEntityId(rendezvousId));
			answer.setText(answerText);
			this.answerService.save(answer);
			this.answerService.flush();

			System.out.println(i + " Create Answer: " + answerText + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Create Answer: " + answerText + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}
}
