/**
 * 
 */

package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Link;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {

	@Query("select l from Link l where l.rendezvous.id = ?1")
	Collection<Link> findAllByRendezvousId(int id);

	@Query("select l from Link l where l.rendezvous.id = ?1")
	Collection<Link> findByRendezvousId(int id);

	@Query("select l from Link l where l.rendezvous.id = ?1 and l.linkedToRendezvous.id = ?2")
	Link findLink(int rendezvousId, int linkedToRendezvousId);
}
