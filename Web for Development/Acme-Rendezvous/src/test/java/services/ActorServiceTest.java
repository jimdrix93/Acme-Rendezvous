
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	@Autowired
	private ActorService			actorService;
	@Autowired
	private AdministratorService	adminService;


	@Test
	public void testFindAllActor() {

		super.authenticate("admin");

		final Actor c = this.adminService.findByPrincipal();

		c.setName("name");
		c.setSurname("surname");
		final Actor saved = this.actorService.save(c);
		Assert.isTrue(this.actorService.findAll().contains(saved));
	}
	@Test
	public void testFindOne() {
		super.authenticate("admin");

		final Actor c = this.adminService.findByPrincipal();

		c.setName("name");
		c.setSurname("surname");
		final Actor saved = this.actorService.save(c);
		Assert.isTrue(this.actorService.findOne(saved.getId()).getName() == c.getName());
	}

	@Test
	public void testSaveActor() {
		super.authenticate("admin");

		final Actor c = this.adminService.findByPrincipal();
		c.setName("name");
		c.setSurname("surname");
		final Actor saved = this.actorService.save(c);
		Assert.isTrue(this.actorService.findAll().contains(saved));
	}
	@Test
	public void testDeleteActor() {
		super.authenticate("admin");

		final Actor c = this.adminService.findByPrincipal();

		c.setName("name");
		c.setSurname("surname");
		final Actor saved = this.actorService.save(c);
		this.actorService.delete(saved);
		Assert.isTrue(!this.actorService.findAll().contains(saved));
	}

	@Test
	public void actorNoAutenticado() {

		final Actor c = this.adminService.findByPrincipal();
		c.setName("name");
		c.setSurname("surname");
		final Actor saved = this.actorService.save(c);
		Assert.isTrue(this.actorService.findAll().contains(saved));
	}

}
