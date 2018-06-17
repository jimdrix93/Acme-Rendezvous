
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LinkRepository;
import domain.Link;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class LinkService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private LinkRepository		linkRepository;

	// Supporting services 
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;


	// Constructor ----------------------------------------------------------
	public LinkService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------
	public Link create(final int rendezvousId) {
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		Assert.notNull(rendezvous);
		Assert.isTrue(rendezvous.getUser().equals(user));

		final Link result;

		result = new Link();
		result.setRendezvous(rendezvous);

		return result;
	}

	public Collection<Link> findAll() {

		Collection<Link> result;

		result = this.linkRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Link findOne(final int linkId) {
		Link result;

		result = this.linkRepository.findOne(linkId);
		Assert.notNull(result);

		return result;
	}

	public Link save(final Link link) {
		Assert.notNull(link);
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		Assert.isTrue(link.getRendezvous().getUser().equals(user));

		Link saved;

		saved = this.linkRepository.save(link);

		return saved;
	}

	public void delete(final Link link) {
		Assert.notNull(link);
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		Assert.isTrue(link.getRendezvous().getUser().equals(user));

		this.linkRepository.delete(link);
	}

	//Este busca tanto por link.rendezvousId como por link.linkedToRendezvousId
	public Collection<Link> findAllByRendezvousId(final int id) {

		return this.linkRepository.findAllByRendezvousId(id);

	}

	//Este busca solo por link.rendezvousId
	public Collection<Link> findByRendezvousId(final int rendezvousId) {

		return this.linkRepository.findByRendezvousId(rendezvousId);

	}

	public void deleteInBatch(final Collection<Link> links) {
		// TOASK ¿habria que comprobar aqui tambien que en usuario logado es admin?

		Assert.notEmpty(links);

		this.linkRepository.deleteInBatch(links);
	}

	public Link findLink(final int rendezvousId, final int linkedToRendezvousId) {
		final Link link = this.linkRepository.findLink(rendezvousId, linkedToRendezvousId);
		return link;
	}

}
