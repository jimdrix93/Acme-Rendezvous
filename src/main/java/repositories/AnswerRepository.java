/**
 * 
 */

package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Answer;
import domain.Reservation;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

	@Query("select a from Answer a where a.reservation = ?1")
	Collection<Answer> findByReservation(Reservation reserva);

	@Query("select a from Answer a where a.question.id = ?1")
	Collection<Answer> findByQuestionId(int questionId);

	@Query("select a from Answer a where a.reservation.id = ?1 and a.question.id = ?2")
	Answer findByReservationIdAndQuestionId(int reservationId, int questionId);

}
