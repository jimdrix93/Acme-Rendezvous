package usecases;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Request;
import domain.Servicio;
import services.CreditCardService;
import services.RendezvousService;
import services.RequestService;
import services.ServicioService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UseCaseRequestTest  extends AbstractTest {


	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private ServicioService		servicioService;
	@Autowired
	private RequestService		requestService;
	@Autowired
	private CreditCardService	creditCardService;

	/*
	 * Requerimientos:
	 * 4. An actor who is authenticated as a user must be able to:
	 * 		3. Request a service for one of the rendezvouses that he or she's created. He or she
	 * 		   must specify a valid credit card in every request for a service. Optionally, he or she
	 * 		   can provide some comments in the request
	 * 8. The system should fill credit card forms automatically when a user requests a service. It
	 * must use the data last entered by a user. Storing that information in a cookie is recom-
	 * mended. (Please, recall that the Spanish Transpositions Law requires every information sys-
	 * tem to inform its users about the cookies that it uses.)
	 */
	
	/*
	 * Caso de uso:
	 * Usuario puede listar servicios existentes, eligiendo uno, realizando peticion para un rendezvous que haya creado
	 */
	@Test
	public void listServiceChooseAServiceAndCreateRequestForARendezvousTest() {
		final Object testingData[][] = {
			{ // User1 can list services and create a request for a rendezvous
				"user1", "servicio1", "commentsServicio1", "creditCard1", "rendezvous1", null
			}, {// User1 can't use creditCard2 
				null, "servicio1", "commentsServicio3", "creditCard2", "rendezvous1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListServiceChooseAServiceAndCreateRequestForARendezvous((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], super.getEntityId((String) testingData[i][3]),
				super.getEntityId((String) testingData[i][4]), (Class<?>) testingData[i][5]);
	}

	protected void templateListServiceChooseAServiceAndCreateRequestForARendezvous(final String username, final int servicioId, final String comment, final int creditCardId, final int rendezvousId, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			final Collection<Servicio> listServicios = this.servicioService.findAll();
			final Servicio servicio = this.servicioService.findOne(servicioId);
			Assert.isTrue(listServicios.contains(servicio));

			final Request request = this.requestService.create();

			request.setComments(comment);
			request.setServicio(servicio);
			request.setCreditCard(this.creditCardService.findOne(creditCardId));
			request.setRendezvous(this.rendezvousService.findOne(rendezvousId));

			final Request savedRequest = this.requestService.save(request);
			this.requestService.flush();
			Assert.isTrue(this.requestService.findAll().contains(savedRequest));

			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
	
}
