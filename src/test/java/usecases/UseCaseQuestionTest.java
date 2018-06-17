
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
import org.springframework.util.Assert;

import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
import services.UserService;
import utilities.AbstractTest;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UseCaseQuestionTest extends AbstractTest {

	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private QuestionService		questionService;
	@Autowired
	private AnswerService		answerService;
	@Autowired
	private UserService			userService;


	/*
	 * Requerimientos:
	 * 18. The creator of a rendezvous may associate a number of questions with it, each of which
	 * must be answered when a user RSVPs that rendezvous.
	 * 21. An actor who is authenticated as a user must be able to:
	 * 1. Manage the questions that are associated with a rendezvous that he or she's creat-
	 * ed previously.
	 */

	/*
	 * Caso de uso:
	 * Usuario logado listando citas creadas por el, eligiendo
	 * una y añadiendo/editando/eliminando preguntas.
	 */
	@Test
	public void createQuestionTest() {

		System.out.println("-----Create question test. Positive 0 to 1, Negative 2 to 3.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: User creator for rendezvous
				"P1", "user1", "rendezvous1", "¿Es posible llegar en coche a la cita?", null
			}, {// P2: User creator for rendezvous
				"P2", "user2", "rendezvous2", "¿Hay zona para minusvalidos en la cita?", null
			},

			//Negative test cases
			{// N1: Anonymous tries to create question
				"N1", "", "rendezvous1", "Pregunta Test 3", IllegalArgumentException.class
			}, {//N2: User4 hack user1
				"N2", "user4", "rendezvous1", "Pregunta Test 4", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateQuestionTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //Rendezvous 
				(String) testingData[i][3],	//Text Question
				(Class<?>) testingData[i][4]); //Exception class
	}

	protected void templateCreateQuestionTest(final Integer i, final String nameTest, final String username, final String rendezvous, final String text, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findCreatedByUser(super.getEntityId(username));
			final Rendezvous rendezvousBD = this.rendezvousService.findOne(super.getEntityId(rendezvous));

			final Question question = this.questionService.create(super.getEntityId(rendezvous));
			question.setText(text);
			this.questionService.save(question);
			this.questionService.flush();

			System.out.println(i + " Create Question: " + text + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Create Question: " + text + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario logado listando citas creadas por el, eligiendo
	 * una y añadiendo/editando/eliminando preguntas.
	 */
	@Test
	public void editQuestionTest() {

		System.out.println("-----Edit question test. Positive 0, Negative 1 to 2.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: User creator for rendezvous
				"P1", "user1", "rendezvous1", "question1-1", "¿Que hora es?", null
			}, {// P2: User creator for rendezvous
				"P2", "user1", "rendezvous1", "question2-1", "¿Pregunta @#~@#|231@#|09@	{}{},-..--,", null
			}, {// P3: User creator for rendezvous
				"P3", "user1", "rendezvous1", "question3-1", "¿Que pregunto?", null
			},

			//Negative test cases
			{// N1: Anonymous tries to edit question
				"N1", "", "rendezvous1", "question1-1", "Pregunta Test 4", IllegalArgumentException.class
			}, {//N2: User4 tries edit user1's question
				"N2", "user4", "rendezvous1", "question1-1", "Pregunta Test 5", IllegalArgumentException.class
			}, {//N3: Edit question without text
				"N3", "user1", "rendezvous1", "question1-1", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditQuestionTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //Rendezvous Id
				(String) testingData[i][3], //Question Id
				(String) testingData[i][4],	//Text Question
				(Class<?>) testingData[i][5]); //Exception class
	}

	protected void templateEditQuestionTest(final Integer i, final String nameTest, final String username, final String rendezvousId, final String questionId, final String text, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findCreatedByUser(super.getEntityId(username));
			final Rendezvous rendezvous = this.rendezvousService.findOne(super.getEntityId(rendezvousId));
			final Question question = this.questionService.findOne(super.getEntityId(questionId));
			if (rendezvous == question.getRendezvous()) {
				question.setText(text);
				this.questionService.save(question);
				this.questionService.flush();
			}

			System.out.println(i + " Edit Question: " + text + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Edit Question: " + text + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario logado listando citas creadas por el, eligiendo
	 * una y añadiendo/editando/eliminando preguntas.
	 */
	@Test
	public void deleteQuestionTest() {

		System.out.println("-----Delete question test. Positive 0, Negative 1 to 2.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: User creator for rendezvous
				"P1", "user1", "rendezvous1", "question1-1", null
			}, {// P2: User creator for rendezvous
				"P2", "user1", "rendezvous1", "question2-1", null
			}, {// P3: User creator for rendezvous
				"P3", "user1", "rendezvous1", "question3-1", null
			},

			//Negative test cases
			{// N1: Anonymous tries to delete question
				"N1", "", "rendezvous1", "question1-1", IllegalArgumentException.class
			}, {//N2: User4 tries delete user1's question
				"N2", "user4", "rendezvous1", "question1-1", IllegalArgumentException.class
			}, {//N3: Edit question that not exist
				"N3", "user1", "rendezvous1", "question3-5", NumberFormatException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteQuestionTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //Rendezvous Id
				(String) testingData[i][3], //Question Id
				(Class<?>) testingData[i][4]); //Exception class
	}

	protected void templateDeleteQuestionTest(final Integer i, final String nameTest, final String username, final String rendezvousId, final String questionId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findCreatedByUser(super.getEntityId(username));
			final Rendezvous rendezvous = this.rendezvousService.findOne(super.getEntityId(rendezvousId));
			final Question question = this.questionService.findOne(super.getEntityId(questionId));
			if (rendezvous == question.getRendezvous()) {
				this.questionService.delete(question);
				this.questionService.flush();
			}

			System.out.println(i + " Delete Question: " + questionId + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Delete Question: " + questionId + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario anonimo/logado listando citas, eligiendo una, listando asistentes,
	 * eligiendo uno, listando preguntas y respuestas del asistente
	 */
	@Test
	public void listQuestionsAndAnswersTest() {

		System.out.println("-----List Question and Answer test. Positive 0 to 1, Negative 2 to 4.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: User creator of rendezvous: user2's answers
				"P1", "user1", "rendezvous1", "user2", null
			}, {//P2: Anonymous: user2's answers
				"P2", "", "rendezvous1", "user2", null
			},
			//Negative test cases
			{// N1: User creator of rendezvous: user1's answers
				"N1", "user1", "rendezvous1", "user1", IllegalArgumentException.class
			}, {//N2: Anonymous: user3's answers
				"N2", "", "rendezvous2", "user3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListQuestionsAndAnswersTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //Rendezvous 
				(String) testingData[i][3], //Username attendants
				(Class<?>) testingData[i][4]); //Exception class
	}

	protected void templateListQuestionsAndAnswersTest(final Integer i, final String nameTest, final String username, final String rendezvous, final String usernameAttendant, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			if (username != "")
				super.authenticate(username);
			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findAllFinal();
			final Rendezvous rendezvousBD = this.rendezvousService.findOne(super.getEntityId(rendezvous));
			if (rendezvouses.contains(rendezvousBD)) {
				final Collection<User> attendants = this.userService.findAttendantsByRendezvous(super.getEntityId(rendezvous));
				final User attendant = this.userService.findOne(super.getEntityId(usernameAttendant));
				if (attendants.contains(attendant)) {
					final Collection<Question> questions = this.questionService.findAllByRendezvousId(super.getEntityId(rendezvous));
					if (questions != null) {
						final Collection<Answer> answers = this.answerService.findByUserAndRendezvous(super.getEntityId(usernameAttendant), super.getEntityId(rendezvous));
						Assert.notEmpty(answers);
					}

				} else
					throw new IllegalArgumentException();

			}

			System.out.println(i + " List Answer: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " List Answer: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

}
