package usecases;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Link;
import domain.Rendezvous;
import services.LinkService;
import services.RendezvousService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UseCaseLinkTest  extends AbstractTest {


	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private LinkService			linkService;
	
	/* 
	 * Requerimientos:
	 * 13. A rendezvous may be explicitly linked to similar ones by its creator. Note that such links may
	 * be added or removed even if the rendezvous is saved in final mode. They must be listed
	 * whenever a rendezvous is shown.
	 * 16. An actor who is authenticated as a user must be able to:
	 * 		4. Link one of the rendezvouses that he or she's created to other similar rendezvouses.
	 */
	
	/*
	 * Caso de uso:
	 * Usuario logado listando las citas que ha creado, eligiendo una y asociandola a otra similar
	 */
	@Test
	public void userListRendezvousesAndChooseOneAndAssociateToOtherSimilarTest() {

		final Object testingData[][] = {
			{// user1 can associate rendezvous2 to rendezvous1
				"user1", "rendezvous1", "rendezvous2", null
			}, {// User2 can't create rendezvous1, for this reason can´t associate anyone
				"user2", "rendezvous1", "rendezvous2", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListRendezvousesAndAssociate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (Class<?>) testingData[i][3]);
	}

	protected void templateListRendezvousesAndAssociate(final String username, final int rendezvousId, final int similarRendezvousId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);

			final Link link = this.linkService.create(rendezvousId);
			final Rendezvous similarRendezvous = this.rendezvousService.findOne(similarRendezvousId);
			link.setLinkedToRendezvous(similarRendezvous);
			this.linkService.save(link);
			final Collection<Rendezvous> similarRendezvouses = this.rendezvousService.findSimilarByRendezvousAuthenticated(rendezvousId);

			Assert.isTrue(similarRendezvouses.contains(similarRendezvous));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Usuario logado listando citas, eligiendo una, y listando las citas asociadas a ella
	 */
	@Test
	public void userListRendezvousesAndChooseOneAndChooseSimilarOtherTest() {

		final Object testingData[][] = {
			{ // user2 can list linked similar rendezvouses of the rendezvous1
				"user2", "rendezvous1", "rendezvous6", null
			}, {
				//Rendezvous 2 is not linked to Rendezvous 1
				"user2", "rendezvous1", "rendezvous2", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListRendezvousesAndChooseOtherSimilar(
					(String) testingData[i][0], 
					super.getEntityId((String) testingData[i][1]), 
					super.getEntityId((String) testingData[i][2]), 
					(Class<?>) testingData[i][3]);
	}

	protected void templateListRendezvousesAndChooseOtherSimilar(final String username, final int rendezvousId, final int linkedRendezvousId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);
			final Collection<Link> links = this.linkService.findByRendezvousId(rendezvousId);

			final Rendezvous linkedRendezvous = this.rendezvousService.findOne(linkedRendezvousId);
			final Collection<Rendezvous> linkedRendezvouses = new LinkedList<>();
			for (final Link link : links)
				linkedRendezvouses.add(link.getLinkedToRendezvous());
			Assert.isTrue(linkedRendezvouses.contains(linkedRendezvous));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
