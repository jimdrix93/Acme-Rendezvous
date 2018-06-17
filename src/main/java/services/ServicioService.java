
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ServicioRepository;
import domain.Actor;
import domain.Administrator;
import domain.Manager;
import domain.Request;
import domain.Servicio;
import domain.User;

@Service
@Transactional
public class ServicioService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private ServicioRepository	servicioRepository;


	// Constructor ----------------------------------------------------------
	public ServicioService() {
		super();
	}


	// Supporting services
	@Autowired
	private ActorService	actorService;
	@Autowired
	private ManagerService	managerService;
	@Autowired
	private RequestService	requestService;


	//CRUD Methods  ---------------------------------------------------------
	public Servicio create() {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Manager);

		final Servicio result;
		result = new Servicio();
		result.setCancelled(false);
		return result;
	}
	public Collection<Servicio> findAll() {

		Collection<Servicio> result;
		result = this.servicioRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Servicio findOne(final int servicioId) {
		Servicio result;

		result = this.servicioRepository.findOne(servicioId);
		Assert.notNull(result);

		return result;
	}

	//findOneToEdit
	public Servicio findOneToEdit(final int servicioId) {
		Servicio result;
		Assert.isTrue(servicioId != 0);
		result = this.servicioRepository.findOne(servicioId);
		this.checkPrincipal(result);
		return result;
	}

	public Servicio save(final Servicio servicio) {
		Assert.notNull(servicio);
		Servicio saved;
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Manager || actor instanceof Administrator);

		if (actor instanceof Manager && servicio.getId() != 0) {
			final Manager manager = this.managerService.findByPrincipal();
			Assert.isTrue(manager.getServicios().contains(servicio));
		}
		boolean first = false;
		if (servicio.getId() == 0)
			first = true;

		saved = this.servicioRepository.save(servicio);

		if (actor instanceof Manager && first) {
			final Manager m = (Manager) actor;
			final Collection<Servicio> servicios = m.getServicios();
			servicios.add(saved);
			m.setServicios(servicios);
		}

		return saved;
	}

	public void flush() {
		this.servicioRepository.flush();
	}

	public void delete(final Servicio servicio) {
		Assert.notNull(servicio);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Manager);
		this.checkPrincipal(servicio);
		this.checkNotRequiredByRendezvouses(servicio);
		Manager principal;
		principal = this.managerService.findByPrincipal();
		Assert.notNull(principal);
		Collection<Servicio> servicios;
		servicios = principal.getServicios();
		servicios.remove(servicio);
		principal.setServicios(servicios);
		this.servicioRepository.delete(servicio);
	}

	public Collection<Servicio> AllServiciosNotCancelled() {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		return this.servicioRepository.findAllNotCancelled();
	}

	public void checkPrincipal(final Servicio servicio) {
		Manager creator;
		Manager principal;

		creator = this.managerService.findManagerByService(servicio);
		principal = this.managerService.findByPrincipal();

		Assert.isTrue(creator.equals(principal));
	}

	public void checkNotRequiredByRendezvouses(final Servicio servicio) {

		Assert.notNull(servicio);
		Collection<Request> requests;
		requests = this.requestService.getRequestsByService(servicio);
		Assert.isTrue(requests.isEmpty());
	}

	public Collection<Object> findBestSelling() {
		final Collection<Object> result = this.servicioRepository.findServiciosOrdedBySelling();

		return result;
	}

	public Collection<Servicio> getServicesByCategoryId(final int categoryId) {
		return this.servicioRepository.getServicesByCategoryId(categoryId);
	}
}
