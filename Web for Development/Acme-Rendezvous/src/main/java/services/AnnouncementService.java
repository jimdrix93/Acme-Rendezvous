
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AnnouncementRepository;
import domain.Actor;
import domain.Administrator;
import domain.Announcement;
import domain.Rendezvous;
import domain.Reservation;
import domain.User;
import exceptions.HackingException;

@Service
@Transactional
public class AnnouncementService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private AnnouncementRepository	announcementRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private RendezvousService		rendezvousService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private UserService				userService;
	@Autowired
	private ReservationService		reservationService;


	// Constructor ----------------------------------------------------------
	public AnnouncementService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------
	public Announcement create(final int rendezvousId) throws HackingException {
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		Assert.notNull(rendezvous);
		
		try {
			Assert.isTrue(rendezvous.getUser().equals(user));
		} catch (Throwable oops) {
			throw new HackingException(oops);
		}

		final Announcement result;

		result = new Announcement();
		result.setRendezvous(rendezvous);

		return result;
	}

	public Collection<Announcement> findAll() {

		Collection<Announcement> result;

		result = this.announcementRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Announcement findOne(final int announcementId) {
		Announcement result;

		result = this.announcementRepository.findOne(announcementId);
		Assert.notNull(result);

		return result;
	}

	public Announcement save(final Announcement announcement) {
		Assert.notNull(announcement);
		Announcement saved;
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		Assert.isTrue(announcement.getRendezvous().getUser().equals(user));

		if (announcement.getId() == 0) {
			final Date moment = new Date(System.currentTimeMillis() - 1);
			announcement.setMoment(moment);
		}

		saved = this.announcementRepository.save(announcement);

		return saved;
	}

	public void delete(final Announcement announcement) {
		Assert.notNull(announcement);
		final Actor user = this.userService.findByPrincipal();
		Assert.notNull(user instanceof User);
		Assert.isTrue(announcement.getRendezvous().getUser().equals(user));

		this.announcementRepository.delete(announcement);
	}

	// Other business methods -------------------------------------------------

	public Collection<Announcement> findByRendezvous(final int rendezvousId) {
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);
		final Collection<Announcement> announcements = this.announcementRepository.findByRendezvous(rendezvousId);
		Assert.notNull(announcements);
		return announcements;
	}

	public Collection<Announcement> findByReservedRendezvous() {
		final List<Announcement> announcements = new ArrayList<Announcement>();
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);

		final Collection<Reservation> reservations = this.reservationService.findAllByUser(user.getId());
		for (final Reservation r : reservations) {
			Assert.isTrue(r.getUser().equals(user));
			final Collection<Announcement> announcementsByRendezvous = this.announcementRepository.findByRendezvous(r.getRendezvous().getId());
			announcements.addAll(announcementsByRendezvous);
		}

		final Comparator<Announcement> comparator = new Comparator<Announcement>() {

			@Override
			public int compare(final Announcement a1, final Announcement a2) {
				return a2.getMoment().compareTo(a1.getMoment()); // use your logic
			}
		};

		Collections.sort(announcements, comparator);

		return announcements;
	}

	public Collection<Announcement> findByHasReservation(final int rendezvousId) {
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		Assert.isTrue(this.reservationService.hasReservation(rendezvousId));
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);
		final Collection<Announcement> announcements = this.announcementRepository.findByRendezvous(rendezvousId);
		Assert.notNull(announcements);
		return announcements;
	}

	public void deleteInBatch(final Collection<Announcement> announcements) {
		// TOASK ¿habria que comprobar aqui tambien que en usuario logado es admin?

		Assert.notEmpty(announcements);

		this.announcementRepository.deleteInBatch(announcements);

	}

	public void deleteByAdministrator(final Announcement announcement) {
		// TODO Auto-generated method stub
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Administrator);

		this.announcementRepository.delete(announcement);
	}

	public void flush() {
		this.announcementRepository.flush();
	}

}
