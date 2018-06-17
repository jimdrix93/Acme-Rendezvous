
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AnswerRepository;
import domain.Answer;
import domain.Question;
import domain.Rendezvous;
import domain.Reservation;
import domain.User;

@Service
@Transactional
public class AnswerService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private AnswerRepository	answerRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;
	@Autowired
	private ReservationService	reservationService;
	@Autowired
	private QuestionService		questionService;


	// Constructor ----------------------------------------------------------
	public AnswerService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------

	public Answer create() {
		return new Answer();

	}

	public Answer create(final int questionId, final int rendezvousId) {
		final Question question = this.questionService.findOne(questionId);
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		final User user = this.userService.findByPrincipal();
		Assert.notNull(question);
		Assert.notNull(rendezvous);
		Assert.notNull(user);

		final Reservation reservation = this.reservationService.findReservationByUserAndRendezvous(user, rendezvous);
		Assert.notNull(reservation);
		Assert.isTrue(reservation.getUser().equals(user));

		final Answer result;

		result = new Answer();
		result.setQuestion(question);
		result.setReservation(reservation);

		return result;
	}

	public Collection<Answer> findAll() {

		Collection<Answer> result;

		result = this.answerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Answer> findByReservation(final Reservation reserva) {
		Assert.notNull(reserva);

		final Collection<Answer> result = this.answerRepository.findByReservation(reserva);

		return result;
	}

	public Collection<Answer> findByUserAndRendezvous(final int userId, final int rendezvousId) {
		final User user = this.userService.findOne(userId);
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(user);
		Assert.notNull(rendezvous);

		final Reservation reservation = this.reservationService.findReservationByUserAndRendezvous(user, rendezvous);

		final Collection<Answer> answers = this.answerRepository.findByReservation(reservation);

		return answers;
	}

	public Answer findOne(final int answerId) {
		Answer result;

		result = this.answerRepository.findOne(answerId);
		Assert.notNull(result);

		return result;
	}

	public Answer save(final Answer answer) {
		Assert.notNull(answer);
		final User user = this.userService.findByPrincipal();
		Assert.isTrue(answer.getReservation().getUser().equals(user));

		Answer saved;

		saved = this.answerRepository.save(answer);

		return saved;
	}

	public void delete(final Answer answer) {
		Assert.notNull(answer);

		this.answerRepository.delete(answer);
	}

	public void deleteInBatch(final Collection<Answer> answers) {
		// TOASK ¿Habria que comprobar usuario logado?
		this.answerRepository.deleteInBatch(answers);
	}

	public Collection<Answer> findByQuestionId(final int id) {
		return this.answerRepository.findByQuestionId(id);
	}

	public Answer findByReservationIdAndQuestionId(final int reservationId, final int questionId) {
		final Answer answer = this.answerRepository.findByReservationIdAndQuestionId(reservationId, questionId);
		return answer;
	}

	public void flush() {
		this.answerRepository.flush();

	}

}
