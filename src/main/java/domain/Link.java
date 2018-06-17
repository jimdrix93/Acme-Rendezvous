
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
	"rendezvous_id", "linkedToRendezvous_id"
}))
public class Link extends DomainEntity {

	private Rendezvous	rendezvous;

	private Rendezvous	linkedToRendezvous;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Rendezvous getRendezvous() {
		return this.rendezvous;
	}

	public void setRendezvous(final Rendezvous rendezvous) {
		this.rendezvous = rendezvous;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Rendezvous getLinkedToRendezvous() {
		return this.linkedToRendezvous;
	}

	public void setLinkedToRendezvous(final Rendezvous linkedRendezvous) {
		this.linkedToRendezvous = linkedRendezvous;
	}

}
