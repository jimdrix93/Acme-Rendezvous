
package security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAccountService {

	@Autowired
	private UserAccountRepository	userAccountRepository;


	public UserAccountService() {
		super();
	}
	
	public UserAccount createAsAdmin() {
		UserAccount res;
		res = new UserAccount();
		final Authority authority = new Authority();
		final List<Authority> authorities = new ArrayList<Authority>();
		authority.setAuthority(Authority.ADMINISTRATOR);
		authorities.add(authority);
		res.setAuthorities(authorities);

		return res;
	}
	

	public UserAccount createAsUser() {
		UserAccount res;
		res = new UserAccount();
		final Authority authority = new Authority();
		final List<Authority> authorities = new ArrayList<Authority>();
		authority.setAuthority(Authority.USER);
		authorities.add(authority);
		res.setAuthorities(authorities);

		return res;
	}
	
	public UserAccount createAsManager() {
		UserAccount res;
		res = new UserAccount();
		final Authority authority = new Authority();
		final List<Authority> authorities = new ArrayList<Authority>();
		authority.setAuthority(Authority.MANAGER);
		authorities.add(authority);
		res.setAuthorities(authorities);

		return res;
	}


	public Collection<UserAccount> findAll() {
		return this.userAccountRepository.findAll();
	}
	public UserAccount findOne(final int userAccountId) {
		return this.userAccountRepository.findOne(userAccountId);
	}

	public UserAccount save(final UserAccount userAccount) {

		return this.userAccountRepository.save(userAccount);

	}
	public void delete(final UserAccount userAccount) {
		this.userAccountRepository.delete(userAccount);
	}
}
