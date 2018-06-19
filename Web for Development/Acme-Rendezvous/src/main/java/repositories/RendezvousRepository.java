/*
 * AnnouncementRepository.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Rendezvous;

@Repository
public interface RendezvousRepository extends JpaRepository<Rendezvous, Integer> {

	@Query("select r from Rendezvous r where r.user.id = ?1")
	Collection<Rendezvous> findCreatedByUserId(int userId);

	@Query("select r from Rendezvous r where r.adult = false")
	List<Rendezvous> findAllNotAdult();

	@Query("select r from Rendezvous r where r.adult = false and r.draft = false")
	List<Rendezvous> findAllFinalAndNotAdult();

	@Query("select r from Rendezvous r where  r.draft = false")
	List<Rendezvous> findAllFinal();

	@Query("select rendezvous from Reservation r join r.rendezvous rendezvous where r.user.id = ?1")
	Collection<Rendezvous> findReservedByUserId(int userId);

	@Query("select rendezvous from Reservation r join r.rendezvous rendezvous where r.user.id = ?1 and r.canceled = true")
	Collection<Rendezvous> findCanceledByUserId(int userId);

	@Query("select rendezvous from Reservation r join r.rendezvous rendezvous where r.canceled=false and r.user.id = ?1")
	Collection<Rendezvous> findReservedAndNotCanceledByUserId(int userId);

	// Las distintas categorias de los servicios solicitados y el numero de citas. Para hayar The average number of categories per rendezvous.
	@Query("select count( DISTINCT re.servicio.category), (select count(ren) from Rendezvous ren) " + "from Rendezvous r left join r.requests re")
	Collection<Object> findDistinctCartoriesOfRequestedServicesAndNumberOfRendezvouses();

	// Numero de servicios y numero de categorias distitas. Para hayar The average ratio of services in each category
	@Query("select count(s), count(distinct c) from Servicio s right join s.category c")
	Collection<Object> findServiceNumberAndCategoryNumber();

	//The average services requested per rendezvous.
	@Query("select avg(r.requests.size) from Rendezvous r")
	Collection<Object> findAverageServicesReaquestedPerRendezvous();

	//The  average, the minimum, the maximum and the sum(X) and count(X) of services requested per rendezvous.
	@Query("select avg(r.requests.size), max(r.requests.size), min(r.requests.size), sum(r.requests.size*r.requests.size), count(r.requests.size) from Rendezvous r")
	Collection<Object> findAverageMaxAndMinServicesReaquestedPerRendezvous();

	@Query("select DISTINCT r from Rendezvous r join r.requests re where re.servicio.cancelled != true and re.servicio.category.id = ?1")
	Collection<Rendezvous> findByCategory(int categoryId);

	@Query("select distinct r from Rendezvous r join r.requests re where r.draft = false and r.adult = false and re.servicio.category.id = ?1")
	Collection<Rendezvous> findAllFinalAndNotAdultByCategory(Integer categoryId);

	@Query("select r from Rendezvous r where r.requests.size = 0 and r.adult = false and r.draft = false")
	Collection<Rendezvous> findAllFinalAndNotAdultUncategorized();

	@Query("select distinct r from Rendezvous r join r.requests re where r.draft = false and re.servicio.category.id = ?1")
	Collection<Rendezvous> findAllFinalByCategory(Integer categoryId);

	@Query("select r from Rendezvous r where r.requests.size = 0 and r.draft = false")
	Collection<Rendezvous> findAllFinalUncategorized();

	@Query("select r from Rendezvous r where r.user.id = ?1 and r.deleted = false")
	Collection<Rendezvous> findCreatedByUserAndNotDeleted(int userId);
}
