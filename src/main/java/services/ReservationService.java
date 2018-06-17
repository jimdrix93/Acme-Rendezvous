
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ReservationRepository;
import domain.Answer;
import domain.Rendezvous;
import domain.Reservation;
import domain.User;

@Service
@Transactional
public class ReservationService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private ReservationRepository	reservationRepository;

	// Supporting services 
	@Autowired
	private AnswerService			answerService;
	@Autowired
	private UserService				userService;
	@Autowired
	private RendezvousService		rendezvousService;


	// Constructor ----------------------------------------------------------
	public ReservationService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------
	public Reservation create() {

		final Reservation result;

		result = new Reservation();

		return result;
	}

	public Collection<Reservation> findAll() {

		Collection<Reservation> result;

		result = this.reservationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Reservation findOne(final int reservationId) {
		Reservation result;

		result = this.reservationRepository.findOne(reservationId);
		Assert.notNull(result);

		return result;
	}

	public Reservation save(final Reservation reservation) {
		Assert.notNull(reservation);
		//		final User user = this.userService.findByPrincipal();
		//		Assert.notNull(user);
		//		Assert.isTrue(reservation.getRendezvous().getUser().equals(user));

		Reservation saved;

		saved = this.reservationRepository.save(reservation);

		return saved;
	}

	public void delete(final Reservation reservation) {
		Assert.notNull(reservation);

		this.reservationRepository.delete(reservation);
	}

	public Reservation findReservationByUserAndRendezvous(final User user, final Rendezvous rendezvous) {
		Assert.notNull(user);
		Assert.notNull(rendezvous);
		return this.reservationRepository.findReservationByUserAndRendezvous(user, rendezvous);
	}

	public Collection<Reservation> findAllByRendezvousId(final int rendezvousId) {

		return this.reservationRepository.findReservationsByRendezvous(rendezvousId);
	}

	public void deleteInBatch(final Collection<Reservation> reservations) {
		// TOASK ¿habria que comprobar aqui tambien que en usuario logado es admin?

		Assert.notEmpty(reservations);

		for (final Reservation reservation : reservations) {
			final Collection<Answer> answers = this.answerService.findByReservation(reservation);
			this.answerService.deleteInBatch(answers);
		}

		this.reservationRepository.deleteInBatch(reservations);
	}

	public boolean hasReservation(final int rendezvousId) {
		final User user = this.userService.findByPrincipal();
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);
		Assert.notNull(user);
		boolean res = false;
		final Reservation reservation = this.reservationRepository.findReservationByUserAndRendezvous(user, rendezvous);
		if (reservation != null)
			res = true;
		return res;
	}

	public Collection<Reservation> findAllByUser(final int userId) {
		final Collection<Reservation> reservations = this.reservationRepository.findAllByUserId(userId);

		return reservations;
	}

}
