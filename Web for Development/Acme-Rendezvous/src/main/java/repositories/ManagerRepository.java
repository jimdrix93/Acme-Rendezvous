package repositories;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;
import domain.Servicio;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	// select m from Manager m where (select s from Servicio s where s.id = 80)
	// member of m.servicios;
	@Query("select m from Manager m where ?1 member of m.servicios")
	Manager findManagerByService(Servicio servicio);

	// The average services per manager
	@Query("select avg(m.servicios.size) from Manager m ")
	Collection<Manager> findAverageServicesPerManager();

	// The managers who provide more services than the average
	@Query("select m, m.servicios.size, (select avg(ma.servicios.size) from Manager ma) from Manager m where m.servicios.size > (select avg(ma.servicios.size) from Manager ma) order by  m.servicios.size desc")
	Collection<Object> findManagersWhitCountServicesOverAverage();

	// The managers who have got more services cancelled.
	@Query("select m, count(s) from Manager m join m.servicios s where s.cancelled = true group by m order by count(s) desc")
	Collection<Object> findManagersOrederdByServicesCancelled();

}
