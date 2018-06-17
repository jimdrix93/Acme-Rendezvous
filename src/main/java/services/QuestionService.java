
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.QuestionRepository;
import domain.Actor;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.Reservation;
import domain.User;
import forms.QuestionEditForm;

@Service
@Transactional
public class QuestionService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private QuestionRepository	questionRepository;

	// Supporting services 
	@Autowired
	private AnswerService		answerService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private UserService			userService;
	@Autowired
	private ReservationService	reservationService;


	// Constructor ----------------------------------------------------------
	public QuestionService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------
	public Question create(final int rendezvousId) {

		final Question result;
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		Assert.isTrue(rendezvous.getUser().equals(actor));
		Assert.notNull(rendezvous);

		result = new Question();
		result.setRendezvous(rendezvous);

		return result;
	}

	public Collection<Question> findAll() {

		Collection<Question> result;

		result = this.questionRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Question findOne(final int questionId) {
		Question result;

		result = this.questionRepository.findOne(questionId);
		Assert.notNull(result);

		return result;
	}

	public Question save(final Question question) {
		Assert.notNull(question);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		Assert.isTrue(question.getRendezvous().getUser().equals(actor));

		Question saved;

		saved = this.questionRepository.save(question);

		return saved;
	}

	public void flush() {
		this.questionRepository.flush();
	}

	public void delete(final Question question) {
		Assert.notNull(question);
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		final Question questionFind = this.findOne(question.getId());
		Assert.isTrue(questionFind.getRendezvous().getUser().equals(user));

		final Collection<Question> questions = new LinkedList<Question>();
		questions.add(questionFind);
		this.deleteInBatch(questions);
	}

	public Collection<Question> findAllByRendezvousId(final int id) {

		return this.questionRepository.findAllByRendezvousId(id);
	}

	public void deleteInBatch(final Collection<Question> questions) {
		// TOASK ¿habria que comprobar aqui tambien que en usuario logado es admin?

		Assert.notEmpty(questions);

		for (final Question question : questions) {
			final Collection<Answer> answers = this.answerService.findByQuestionId(question.getId());
			this.answerService.deleteInBatch(answers);
		}

		this.questionRepository.deleteInBatch(questions);

	}

	public Collection<Question> findAllUnansweredByRendezvousId(final int rendezvousId) {
		final Collection<Question> questionsUnanswered = new ArrayList<Question>();
		final Collection<Question> questions = this.findAllByRendezvousId(rendezvousId);
		final User user = this.userService.findByPrincipal();
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);

		Assert.notNull(user);
		Assert.notNull(rendezvous);

		//Dependiendo de si es el creador del rendezvous o no...
		if (user.getId() != rendezvous.getUser().getId()) {

			final Reservation reservation = this.reservationService.findReservationByUserAndRendezvous(user, rendezvous);

			for (final Question q : questions) {
				final Answer answer = this.answerService.findByReservationIdAndQuestionId(reservation.getId(), q.getId());
				if (answer == null)
					questionsUnanswered.add(q);
			}
			return questionsUnanswered;

		} else
			return questions;
	}

	public Question findOneToEdit(final int questionId) {
		final User user = this.userService.findByPrincipal();
		final Question question = this.findOne(questionId);
		Assert.notNull(user);
		Assert.notNull(question);
		Assert.isTrue(question.getRendezvous().getUser().equals(user));

		return question;
	}

	public Question reconstruct(final QuestionEditForm questionForm) {
		Assert.notNull(questionForm);
		final Question question = this.findOne(questionForm.getId());
		Assert.notNull(question);
		question.setText(questionForm.getText());

		return question;
	}

	public QuestionEditForm construct(final Question question) {
		Assert.notNull(question);
		final QuestionEditForm questionForm = new QuestionEditForm();
		questionForm.setId(question.getId());
		questionForm.setText(question.getText());

		return questionForm;
	}
}
