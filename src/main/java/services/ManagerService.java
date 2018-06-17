package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Manager;
import domain.Servicio;
import domain.User;
import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;

@Service
@Transactional
public class ManagerService {
	//Repositories
	@Autowired
	private ManagerRepository managerRepository;
	//Services
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private ActorService actorService;
	
	//Constructor
	public ManagerService() {
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

		//Create
		public Manager create() {
			
			final Manager result = new Manager();
			
			UserAccount useraccount;
			useraccount = this.userAccountService.createAsManager();

		
			result.setUserAccount(useraccount);

			return result;
		}
		
		//Save
		public Manager save(final Manager manager) {
			Assert.notNull(manager);
			Manager result;

			if (manager.getId() == 0) {
				Md5PasswordEncoder encoder;

				encoder = new Md5PasswordEncoder();

				manager.getUserAccount().setPassword(encoder.encodePassword(manager.getUserAccount().getPassword(), null));
			}

			result = this.managerRepository.save(manager);

			return result;
		}
		
		//Other
		
		public Manager findManagerByService(Servicio servicio) {
			Manager result;
			result = this.managerRepository.findManagerByService(servicio);
			Assert.notNull(servicio);
			return result;
			
		}
		
		public Manager findByPrincipal() {
			Manager result;
			UserAccount userAccount;

			userAccount = LoginService.getPrincipal();
			Assert.notNull(userAccount);
			Actor actor = this.actorService.findByUserAccount(userAccount);
			Assert.isTrue(actor instanceof Manager);
			result = (Manager) actor;
			Assert.notNull(result);

			return result;
		}

		public Collection<Object> findManagersWhitCountServicesOverAverage() {
			Collection<Object>  result = managerRepository.findManagersWhitCountServicesOverAverage();
			return result;
		}

		public Collection<Object> findManagersOrederdByServicesCancelled() {
			Collection<Object>  result = managerRepository.findManagersOrederdByServicesCancelled();
			return result;
		}

		
		
	
	

}
