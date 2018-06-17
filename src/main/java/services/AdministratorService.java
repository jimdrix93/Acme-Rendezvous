/*
 * AdministratorService.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Administrator;
import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class AdministratorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService			actorService;
	@Autowired
	private ServicioService			servicioService;
	@Autowired
	private ManagerService			managerService;
	@Autowired
	private RendezvousService		rendezvousService;


	// Constructors -----------------------------------------------------------

	public AdministratorService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Administrator create() {
		Administrator result;

		result = new Administrator();
		Assert.notNull(result);

		return result;
	}

	public Collection<Administrator> findAll() {
		Collection<Administrator> result;

		result = this.administratorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Administrator findOne(final int administratorId) {
		Administrator result;

		result = this.administratorRepository.findOne(administratorId);
		Assert.notNull(result);

		return result;
	}

	public Administrator save(final Administrator administrator) {
		Assert.notNull(administrator);

		Administrator result;

		result = this.administratorRepository.save(administrator);

		return result;
	}

	public void delete(final Administrator administrator) {
		Assert.notNull(administrator);
		Assert.isTrue(administrator.getId() != 0);

		this.administratorRepository.delete(administrator);
	}

	// Other business methods -------------------------------------------------

	public Administrator findByPrincipal() {
		Administrator result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = (Administrator) this.actorService.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	// Dashboard

		public Object[] dashboardRendezvousesByUser() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Double> statistics = this.administratorRepository.dashboardRendezvousesByUser();
	
			return statistics.toArray();
		}
	
		public Object[] dashboardRendezvousesRatioCreation() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Double> statistics = this.administratorRepository.dashboardRendezvousesRatioCreation();
	
			return statistics.toArray();
		}
	
		public Object[] dashboardUsersPerRendezvous() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Double> statistics = this.administratorRepository.dashboardUsersPerRendezvous();
	
			return statistics.toArray();
		}
	
		public Object[] dashboardRendezvousesRsvp() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Double> statistics = this.administratorRepository.dashboardRendezvousesRsvp();
	
			return statistics.toArray();
		}
	
		public Collection<Object> dashboardRendezvousesTop10() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Object> statistics = this.administratorRepository.findTop10dashboardRendezvousesTop10();
	
			return statistics;
		}
	
		public Object[] dashboardAnnouncementsRatio() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Double> statistics = this.administratorRepository.dashboardAnnouncementsRatio();
	
			return statistics.toArray();
		}
	
		public Collection<Object> dashboardAnnouncementsAbove75() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Object> statistics = this.administratorRepository.dashboardAnnouncementsAbove75();
	
			return statistics;
		}
	
		public Collection<Object> dashboardRendezvousesLinked() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Object> statistics = this.administratorRepository.dashboardRendezvousesLinked();
	
			return statistics;
		}
	
		public Object[] dashboardQuestionsPerRendezvous() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Double> statistics = this.administratorRepository.dashboardQuestionsPerRendezvous();
	
			return statistics.toArray();
		}
	
		public Object[] dashboardAnswersPerRendezvous() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Double> statistics = this.administratorRepository.dashboardAnswersPerRendezvous();
	
			return statistics.toArray();
		}
	
		public Object[] dashboardRepliesPerComment() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
	
			final Collection<Double> statistics = this.administratorRepository.dashboardRepliesPerComment();
	
			return statistics.toArray();
		}

		public Collection<Object> findBestSellingServices() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
			Collection<Object> result = servicioService.findBestSelling();
			return result;
		}

		public Collection<Object> findManagersWhitCountServicesOverAverage() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
			Collection<Object> result = managerService.findManagersWhitCountServicesOverAverage();
			return result;
		}

		public Collection<Object> findManagersOrederdByServicesCancelled() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
			Collection<Object> result = managerService.findManagersOrederdByServicesCancelled();
			
			return result;
		}

		public Collection<Object> findDistinctCartoriesOfRequestedServicesAndNumberOfRendezvouses() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
			Collection<Object> result;
			result = rendezvousService.findDistinctCartoriesOfRequestedServicesAndNumberOfRendezvouses();
			return result;
		}

		public Collection<Object> findServiceNumberAndCategoryNumber() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
			Collection<Object> result;
			result = rendezvousService.findServiceNumberAndCategoryNumber();
			return result;
		}

		public Collection<Object> findAverageMaxAndMinServicesReaquestedPerRendezvous() {
			final Administrator admin = this.findByPrincipal();
			Assert.notNull(admin);
			Collection<Object> result;
			result = rendezvousService.findAverageMaxAndMinServicesReaquestedPerRendezvous();
			return result;
		}

}
