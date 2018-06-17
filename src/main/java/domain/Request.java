
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "servicio_id, rendezvous_id")
})
public class Request extends DomainEntity {

	//Attributes 
	private String		comments;
	//Relationships
	private Servicio	servicio;
	private CreditCard	creditCard;
	private Rendezvous	rendezvous;
	private boolean     cancelled;

	public Request() {
		super();
	}

	public String getComments() {
		return this.comments;
	}
	public void setComments(final String comments) {
		this.comments = comments;
	}
	@ManyToOne(optional = false)
	@NotNull
	@Valid
	public Servicio getServicio() {
		return this.servicio;
	}
	public void setServicio(final Servicio servicio) {
		this.servicio = servicio;
	}

	@ManyToOne(optional = false)
	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public Rendezvous getRendezvous() {
		return this.rendezvous;
	}

	public void setRendezvous(final Rendezvous rendezvous) {
		this.rendezvous = rendezvous;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	
}
