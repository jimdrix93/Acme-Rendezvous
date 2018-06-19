/**
 * 
 */

package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;
import domain.Servicio;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	//select r from Request r where r.servicio.id = ?1
	@Query("select r from Request r where r.servicio = ?1")
	Collection<Request> getRequestsByService(Servicio servicio);

	@Query("select r from Request r where r.servicio.id = ?1")
	Collection<Request> findAllByServiceId(Integer servicioId);

	@Query("select r from Request r where r.rendezvous.user.id = ?1")
	Collection<Request> findAllByUserId(int id);

	@Query("select r from Request r where r.rendezvous.id = ?1")
	Collection<Request> findAllByRendezvousId(int rendezId);

}
