
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.Actor;
import domain.CreditCard;
import domain.Request;
import domain.Servicio;
import domain.User;

@Service
@Transactional
public class RequestService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private RequestRepository	requestRepository;


	// Constructor ----------------------------------------------------------
	public RequestService() {
		super();
	}


	// Supporting services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private CreditCardService	creditCardService;

	@Autowired
	private RendezvousService	rendezvousService;


	// Methods CRUD ---------------------------------------------------------
	public Request create() {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		final Request result;
		result = new Request();
		return result;
	}
	public Collection<Request> findAll() {

		Collection<Request> result;
		result = this.requestRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Request findOne(final int requestId) {
		Request result;

		result = this.requestRepository.findOne(requestId);
		Assert.notNull(result);

		return result;
	}

	public Request save(final Request request) {
		Assert.notNull(request);
		Request saved;
		CreditCard saveCredit;
		//	final Rendezvous rendez = this.rendezvousService.findOne(request.getRendezvous().getId());
		//	Rendezvous rendezSave;
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		//	rendez.getRequests().add(request);
		//	rendezSave = this.rendezvousService.save(rendez);
		saveCredit = this.creditCardService.save(request.getCreditCard());
		request.setCreditCard(saveCredit);
		//	request.setRendezvous(rendezSave);
		saved = this.requestRepository.save(request);

		return saved;
	}

	public void flush() {
		this.requestRepository.flush();
	}

	public void delete(final Request request) {
		Assert.notNull(request);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		this.requestRepository.delete(request);
	}

	public void deleteInBatch(final Collection<Request> requests) {
		Assert.notNull(requests);

		this.requestRepository.deleteInBatch(requests);
	}
	//Other
	public Collection<Request> getRequestsByService(final Servicio servicio) {
		Collection<Request> result;
		result = this.requestRepository.getRequestsByService(servicio);
		Assert.notNull(result);
		return result;
	}

	public Collection<Request> findAllByServiceId(final Integer servicioId) {
		return this.requestRepository.findAllByServiceId(servicioId);
	}

	public Collection<Request> findAllByUserId(final int id) {
		Collection<Request> result;
		result = this.requestRepository.findAllByUserId(id);
		Assert.notNull(result);
		return result;
	}

	public Collection<Request> findAllByRendezvousId(final int rendezId) {
		return this.requestRepository.findAllByRendezvousId(rendezId);
	}

}
