
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import domain.Actor;
import domain.CreditCard;
import domain.User;

@Service
@Transactional
public class CreditCardService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private CreditCardRepository	creditCardRepository;


	// Constructor ----------------------------------------------------------
	public CreditCardService() {
		super();
	}


	// Supporting services
	@Autowired
	private ActorService	actorService;


	// Methods CRUD ---------------------------------------------------------
	public CreditCard create() {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		final CreditCard result;
		result = new CreditCard();
		return result;
	}
	public Collection<CreditCard> findAll() {

		Collection<CreditCard> result;
		result = this.creditCardRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public CreditCard findOne(final int creditCardId) {
		CreditCard result;

		result = this.creditCardRepository.findOne(creditCardId);
		Assert.notNull(result);

		return result;
	}

	public CreditCard save(final CreditCard creditCard) {
		Assert.notNull(creditCard);
		CreditCard saved;
		if (this.creditCardRepository.findSameCreditcard(creditCard.getNumber(), creditCard.getCvv()) != null)
			saved = this.creditCardRepository.findSameCreditcard(creditCard.getNumber(), creditCard.getCvv());
		else
			saved = this.creditCardRepository.save(creditCard);
		return saved;
	}
	public void delete(final CreditCard creditCard) {
		Assert.notNull(creditCard);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		this.creditCardRepository.delete(creditCard);
	}

}
