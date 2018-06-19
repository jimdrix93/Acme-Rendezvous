/**
 * 
 */

package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

	@Query("select s from Servicio s where s.cancelled = false")
	List<Servicio> findAllNotCancelled();

	@Query("select r.servicio, count(r) from Request r group by r.servicio order by count(r) desc")
	Collection<Object> findServiciosOrdedBySelling();
	
	@Query("select max(r.servicio) from Request r where r.servicio.cancelled = false group by r.servicio order by count(r)")
	Collection<Servicio> findBestSelling();
	
	@Query("select s from Servicio s where s.category.id = ?1")
	Collection<Servicio> getServicesByCategoryId(int categoryId);
}
