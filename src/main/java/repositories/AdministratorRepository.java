/*
 * AdministratorRepository.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {


	@Query("select a from Administrator a where a.userAccount.id = ?1")
	Administrator findByUserAccountId(int userAccountId);

	
	//The average and the standard deviation of rendezvouses created per user.
	@Query("select count(a) from Rendezvous a right join a.user r group by r.id")
	Collection<Double> dashboardRendezvousesByUser();
	
	//The ratio of users who have ever created a rendezvous versus the users who have never created any rendezvouses
	@Query("select ((select count(distinct r.user) from Rendezvous r) * 100)/ count(u) from User u")
	Collection<Double> dashboardRendezvousesRatioCreation();

	//The average and the standard deviation of users per rendezvous.
	@Query("select count(a) from Reservation a right join a.rendezvous r group by r.id")
	Collection<Double> dashboardUsersPerRendezvous();
	
	//The average and the standard deviation of rendezvouses that are RSVPd per user.
	@Query("select count(r) from Reservation r group by r.user")
	Collection<Double> dashboardRendezvousesRsvp();
	
	//The top-10 rendezvouses in terms of users who have RSVPd them.
	@Query("select r.rendezvous, count(r.user) as cnt from Reservation r group by r.rendezvous order by cnt desc")
	Collection<Object> findTop10dashboardRendezvousesTop10();
	
	
	
	
	
	//The average and the standard deviation of announcements per rendezvous.
	@Query("select count(a) from Announcement a right join a.rendezvous r group by r.id")
	Collection<Double> dashboardAnnouncementsRatio();
	
	//The rendezvouses that whose number of announcements is above 75% the average number of announcements per rendezvous.
	@Query("select r, count(a) from Announcement a right join a.rendezvous r group by r having count(a) > (count(a)/count(r))*1.75")
	Collection<Object> dashboardAnnouncementsAbove75();
	
	//The rendezvouses that are linked to a number of rendezvouses that is greater than the average plus 10%.
	@Query("select r, count(l) from Link l right join l.rendezvous r group by r having count(l) > (count(l)/count(r))*1.1")
	Collection<Object> dashboardRendezvousesLinked();

	
	//The average and the standard deviation of the number of questions per rendezvous.
	@Query("select count(a) from Question a right join a.rendezvous r group by r.id")
	Collection<Double> dashboardQuestionsPerRendezvous();
	
	//The average and the standard deviation of the number of answers to the questions per rendezvous.
	@Query("select count(a) from Answer a right join a.question q right join q.rendezvous r group by r.id")
	Collection<Double> dashboardAnswersPerRendezvous();
	
	//The average and the standard deviation of replies per comment.
	@Query("select count(r) from Reply r group by r.comment")
	Collection<Double> dashboardRepliesPerComment();
}
