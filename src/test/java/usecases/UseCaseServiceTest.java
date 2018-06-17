
package usecases;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.CategoryService;
import services.ManagerService;
import services.ServicioService;
import utilities.AbstractTest;
import domain.Category;
import domain.Manager;
import domain.Servicio;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UseCaseServiceTest extends AbstractTest {

	@Autowired
	private ServicioService			servicioService;
	@Autowired
	private ManagerService	managerService;
	@Autowired
	private ActorService	actorService;
	@Autowired
	private CategoryService	categoryService;	

	/*
	 * Requerimientos:
	 * 2. Managers manage services, for which the system must store a name, a description, and an
	 * optional picture.
	 * 4. An actor who is authenticated as a user must be able to:
	 * 		2. List the services that are available in the system.
	 * 5. An actor who is registered as a manager must be able to:
	 * 		1. List the services that are available in the system.
	 * 6. An actor who is authenticated as an administrator must be able to:
	 * 		1. Cancel a service that he or she finds inappropriate. Such services cannot be re-
	 * 		   quested for any rendezvous. They must be flagged appropriately when listed.
	 * 7. Pictures are not requested to be stored by the system, but their URLs to storage services like
	 * Flickr.com or Pinterest.com.
	 * 9. Services belong to categories, which may be organised into arbitrary hierarchies. A category
	 * is characterised by a name and a description.
	 * 11. An actor who is authenticated as an administrator must be able to:
	 * 		1. Manage the categories of services, which includes listing, creating, updating, delet-
	 * 		   ing, and re-organising them in the category hierarchies.
	 */

	/*
	 * Caso de uso:	 
	 * Administrador seleccionando un servicio y cancelandolo por inapropiado
	 */
	@Test
	public void listServiceAndDeleteOneForInappropiateTest() {
		final Object testingData[][] = {
			{
				// Positivo
				"admin", "servicio4", null
			}, {
				// Negativo: Usuario no logado como administrador
				null, "servicio4", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templatelistServiceAndDeleteOneForInappropiate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void templatelistServiceAndDeleteOneForInappropiate(final String username, final Integer servicioId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);
			final Servicio servicio = this.servicioService.findOne(servicioId);

			Assert.isTrue(this.servicioService.findAll().contains(servicio));

			servicio.setCancelled(true);
			final Servicio savedServicio = this.servicioService.save(servicio);
			this.servicioService.flush();

			Assert.isTrue(savedServicio.isCancelled(), "Tiene que estar cancelado el servicio");
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	/*
	 * Caso de uso:
	 * Manager creando un servicio nuevo
	 */
	@Test
	public void createServiceByManagerTest() {
		final Object testingData[][] = {
			{
				// Positivo
				"manager1", "nombreServicio", "Servicio de taxi para tu cita", "http://www.fotoServicio.com/foto1", null, null
			}, {
				// Negativo: Foto no es URL
				"manager1", "nombreServicio", "Servicio de maquillaje para tu cita", "foto", null, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			if (testingData[i][4] != null)
				this.templateCreateServiceByManager((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], super.getEntityId((String) testingData[i][4]), (Class<?>) testingData[i][5]);
			else
				this.templateCreateServiceByManager((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], 0, (Class<?>) testingData[i][5]);
	}

	protected void templateCreateServiceByManager(final String username, final String name, final String description, final String picture, final Integer categoryId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);
			final Servicio servicio = this.servicioService.create();

			servicio.setName(name);
			servicio.setDescription(description);
			servicio.setPicture(picture);
			if (categoryId != 0) {
				final Category category = this.categoryService.findOne(categoryId);
				servicio.setCategory(category);
			}
			final Servicio savedServicio = this.servicioService.save(servicio);
			this.servicioService.flush();

			Assert.isTrue(this.servicioService.findAll().contains(savedServicio));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Manager listando sus servicios y editando uno
	 */
	@Test
	public void listServiceAndEditTest() {
		final Object testingData[][] = {
			{
				// Positivo
				"manager1", "servicio1", null
			}, {
				// Negativo, servicio no le pertenece
				"manager1", "servicio3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListServiceAndEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	protected void templateListServiceAndEdit(final String username, final Integer servicioId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);
			final Manager manager = this.managerService.findByPrincipal();
			final Servicio servicio = this.servicioService.findOne(servicioId);

			// Comprueba que el manager contiene el servicio1
			Assert.isTrue(manager.getServicios().contains(servicio));

			// Cambio de category1.1 a category2
			servicio.setCategory(this.categoryService.findOne(super.getEntityId("category2")));
			final Servicio savedServicio = this.servicioService.save(servicio);
			this.servicioService.flush();

			Assert.isTrue(this.servicioService.findAll().contains(savedServicio));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	/*
	 * Caso de uso:
	 * Manager creando un servicio y eliminandolo posteriormente
	 */
	@Test
	public void createServiceAndDeleteTest() {
		final Object testingData[][] = {
			{ // Usuario logeado, manager que crea el servicio, parámetros del servicio a crear, categoryId, excepción. 
				"manager1", "manager1", "Bar El Gimnasio", "Servicio de hosteleria", "http://www.fotoServicio.com/foto1", null, null
			}, {
				null, "manager1", "Peluquería", "Ponte guapa para tu cita!", "http://www.fotoServicio.com/foto1", null, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			if (testingData[i][5] != null)
				this.templateCreateServiceAndDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4],
					super.getEntityId((String) testingData[i][5]), (Class<?>) testingData[i][6]);
			else
				this.templateCreateServiceAndDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], 0, (Class<?>) testingData[i][6]);
	}

	protected void templateCreateServiceAndDelete(final String username, final Integer managerId, final String name, final String description, final String picture, final Integer categoryId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			// Nos logeamos como manager1 y creamos el servicio, para después borrarlo
			final Manager manager = (Manager) this.actorService.findOne(managerId);
			this.authenticate(manager.getUserAccount().getUsername());
			final Servicio servicio = this.servicioService.create();

			servicio.setName(name);
			servicio.setDescription(description);
			servicio.setPicture(picture);
			if (categoryId != 0) {
				final Category category = this.categoryService.findOne(categoryId);
				servicio.setCategory(category);
			}
			final Servicio savedServicio = this.servicioService.save(servicio);
			this.servicioService.flush();

			Assert.isTrue(this.servicioService.findAll().contains(savedServicio));

			this.unauthenticate();
			// Manager borra un  servicio
			this.authenticate(username);
			this.servicioService.delete(savedServicio);
			this.servicioService.flush();
			Assert.isTrue(!this.servicioService.findAll().contains(savedServicio));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
	
	
}
