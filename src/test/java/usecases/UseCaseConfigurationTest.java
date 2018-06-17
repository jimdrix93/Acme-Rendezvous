
package usecases;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import services.ConfigurationService;
import utilities.AbstractTest;
import domain.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UseCaseConfigurationTest extends AbstractTest {

	// Service under test ------------------------
	@Autowired
	private ConfigurationService	configurationService;

	
	/*
	 * Requerimientos:
	 * 12. Acme Rendezvous, Inc. is franchising their business. They require the following data to cus-
	 * tomise their system for particular franchisees: the name of the business, a banner, and a
	 * welcome message (which must be available in the languages in which the system's availa-
	 * ble). This information must be displayed appropriately in the header of the pages and the
	 * welcome page. Administrators must be allowed to change the previous data at runtime.
	 * 13. The system must be ready to be deployed to a pilot franchisee with the following data:
	 * 		1. Name of business = "Adventure meetups"
	 * 		2. Banner = "https://tinyurl.com/adventure-meetup"
	 * 		3. English welcome message = "Your place to organise your adventure meetups!"
	 * 		4. Spanish welcome message = "Tu sitio para organizar quedadas de aventura".
	 */

	/*
	 * Caso de uso:
	 * Usuario administrador cambiando el nombre y el banner del sistema
	 */
	@Test
	public void editConfigurationTest() {

		System.out.println("-----Create Category test. Positive 0 to 1, Negative 2 to 3.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: Create category without categoryParent
				"P1", "admin", "configuration", "Name Business", "https://about.canva.com/wp-content/uploads/sites/3/2017/02/congratulations_-banner.png", null
			},

			//Negative test cases
			{// N1: Anonymous tries create category
				"N1", "admin", "configuration", "Name Business", "banner", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditConfigurationTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //ConfigurationId
				(String) testingData[i][3], //Name business
				(String) testingData[i][4], //Banner
				(Class<?>) testingData[i][5]); //Exception class
	}


	protected void templateEditConfigurationTest(final Integer i, final String nameTest, final String username, final String configurationId, final String nameBusiness, final String banner, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			final Configuration configuration = this.configurationService.findOne(super.getEntityId(configurationId));
			configuration.setName(nameBusiness);
			configuration.setBanner(banner);

			this.configurationService.save(configuration);
			this.configurationService.flush();

			System.out.println(i + " Edit Configuration: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			System.out.println(i + " Edit Configuration: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

}
