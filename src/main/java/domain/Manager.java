package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Manager extends Actor{
	
	//Attributes
	private String VATnumber;
	//Relationships
	private Collection<Servicio> servicios;
	
	public Manager() {
		super();
	}

	@OneToMany
	@Valid
	public Collection<Servicio> getServicios() {
		return servicios;
	}


	public void setServicios(Collection<Servicio> servicios) {
		this.servicios = servicios;
	}


	
	@NotBlank
	@Pattern(regexp="[\\w\\d-]*")
	public String getVATnumber() {
		return VATnumber;
	}


	public void setVATnumber(String vATnumber) {
		VATnumber = vATnumber;
	}
	
	
}
