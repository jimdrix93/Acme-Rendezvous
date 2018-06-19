
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import security.UserAccountService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UserAccountServiceTest extends AbstractTest {

	@Autowired
	private UserAccountService	userAccountService;


	@Test
	public void testCreateUserAccount() {
		UserAccount e = null;
		e = this.userAccountService.createAsAdmin();
		Assert.isTrue(e != null);
	}
	@Test
	public void testFindAllUserAccount() {
		final UserAccount c = this.userAccountService.createAsAdmin();

		final UserAccount saved = this.userAccountService.save(c);
		Assert.isTrue(this.userAccountService.findAll().contains(saved));
	}
	@Test
	public void testFindOneUserAccount() {
		final UserAccount c = this.userAccountService.createAsAdmin();
		final UserAccount saved = this.userAccountService.save(c);
		Assert.isTrue(this.userAccountService.findOne(saved.getId()).getId() == saved.getId());
	}

	@Test
	public void testSaveUserAccount() {
		final UserAccount c = this.userAccountService.createAsAdmin();
		final UserAccount saved = this.userAccountService.save(c);
		Assert.isTrue(this.userAccountService.findAll().contains(saved));
	}

	@Test
	public void testDeleteUserAccount() {
		final UserAccount c = this.userAccountService.createAsAdmin();
		final UserAccount saved = this.userAccountService.save(c);
		this.userAccountService.delete(saved);
		Assert.isTrue(!this.userAccountService.findAll().contains(saved));
	}

}
