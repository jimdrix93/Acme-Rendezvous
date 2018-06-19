/**
 * 
 */

package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Rendezvous;
import domain.Reservation;
import domain.User;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	@Query("select r from Reservation r where r.user = ?1 and r.rendezvous = ?2")
	Reservation findReservationByUserAndRendezvous(User user, Rendezvous rendezvous);

	@Query("select r from Reservation r where r.rendezvous.id = ?1")
	Collection<Reservation> findReservationsByRendezvous(int rendezvousId);

	@Query("select r from Reservation r where r.user.id = ?1")
	Collection<Reservation> findAllByUserId(int userId);

}
