
package controllers.administrator;

import java.awt.List;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Manager;
import domain.Servicio;
import services.AdministratorService;
import services.ServicioService;
import utilities.Statistics;

@Controller
@RequestMapping("/dashboard")
public class DashboardAdministratorController extends AbstractController {

	@Autowired
	AdministratorService	administratorService;
	
	@Autowired
	ServicioService			servicioService;


	// Constructors -----------------------------------------------------------

	public DashboardAdministratorController() {
		super();
	}

		@RequestMapping(value = "/list", method = RequestMethod.GET)
		public ModelAndView display() {
			ModelAndView result;
			Statistics s;
			DecimalFormat df2 = new DecimalFormat("0.#####");
			
	
			result = new ModelAndView("dashboard/display");
	
			final Object[] stats1 = this.administratorService.dashboardRendezvousesByUser();
			s = new Statistics(stats1);
			result.addObject("dashboard1", new String[]{df2.format(s.getMean()), df2.format(s.getStdDev())} );
	
			final Object[] stats2 = this.administratorService.dashboardRendezvousesRatioCreation();
			s = new Statistics(stats2);
			result.addObject("dashboard2", new String[]{df2.format(s.getMean()), df2.format(s.getStdDev())} );
	
			final Object[] stats3 = this.administratorService.dashboardUsersPerRendezvous();
			s = new Statistics(stats3);
			result.addObject("dashboard3", new String[]{df2.format(s.getMean()), df2.format(s.getStdDev())} );
	
			final Object[] stats4 = this.administratorService.dashboardRendezvousesRsvp();
			s = new Statistics(stats4);
			result.addObject("dashboard4", new String[]{df2.format(s.getMean()), df2.format(s.getStdDev())} );
	
			final Collection<Object> stats5 = this.administratorService.dashboardRendezvousesTop10();
			result.addObject("dashboard5", stats5);
	
			final Object[] stats6 = this.administratorService.dashboardAnnouncementsRatio();
			s = new Statistics(stats6);
			result.addObject("dashboard6", new String[]{df2.format(s.getMean()), df2.format(s.getStdDev())} );
	
			final Collection<Object> statistics7 = this.administratorService.dashboardAnnouncementsAbove75();
			result.addObject("dashboard7", statistics7);
	
			final Collection<Object> statistics8 = this.administratorService.dashboardRendezvousesLinked();
			result.addObject("dashboard8", statistics8);
	
			final Object[] stats9 = this.administratorService.dashboardQuestionsPerRendezvous();
			s = new Statistics(stats9);
			result.addObject("dashboard9", new String[]{df2.format(s.getMean()), df2.format(s.getStdDev())} );
	
			final Object[] stats10 = this.administratorService.dashboardAnswersPerRendezvous();
			s = new Statistics(stats10);
			result.addObject("dashboard10", new String[]{df2.format(s.getMean()), df2.format(s.getStdDev())} );
	
			final Object[] stats11 = this.administratorService.dashboardRepliesPerComment();
			s = new Statistics(stats11);
			result.addObject("dashboard11", new String[]{df2.format(s.getMean()), df2.format(s.getStdDev())} );
			
		// Dashboard D09 6.2.1 The best-selling services.
		final Collection<Object> model = this.administratorService.findBestSellingServices();
		final Collection<Object> bestSellingServicios = new ArrayList<>();
		if (!model.isEmpty()) {
			int i = 0;
			for (Object servicio : model) {
				bestSellingServicios.add(servicio);
				i++;
				if (i > 9)
					break;
			}
		}
		result.addObject("bestSellinServices", bestSellingServicios);

		// Dashboard D09 2.0 6.2.2 The managers who provide more services than the average
		final Collection<Object> managersWhitCountServicesOverAverage = this.administratorService
				.findManagersWhitCountServicesOverAverage();
		result.addObject("managersWhitCountServicesOverAverage", managersWhitCountServicesOverAverage);

		//  Dashboard D09 6.2.3 The managers who have got more services cancelled.
		final Collection<Object> managersWhitMoreServicesCancelled = this.administratorService
				.findManagersOrederdByServicesCancelled();
		final Collection<Object> topManagersWhitMoreServicesCancelled = new ArrayList<>();
		if (!managersWhitMoreServicesCancelled.isEmpty()) {
			int i = 0;
			for (Object manager : managersWhitMoreServicesCancelled) {
				topManagersWhitMoreServicesCancelled.add(manager);
				i++;
				if (i > 0)
					break;
			}
		}
		result.addObject("managersWhitMoreServicesCancelled", topManagersWhitMoreServicesCancelled);
		
		// Dashboard D09 11.2.1 The average number of categories per rendezvous.
		final Collection<Object> distinctCartoriesOfRequestedServicesAndNumberOfRendezvouses = this.administratorService
				.findDistinctCartoriesOfRequestedServicesAndNumberOfRendezvouses();
		result.addObject("distinctCartoriesOfRequestedServicesAndNumberOfRendezvouses", distinctCartoriesOfRequestedServicesAndNumberOfRendezvouses);

		// Dashboard D09 11.2.2 The average ratio of services in each category.
		final Collection<Object> serviceNumberAndCategoryNumber = this.administratorService
				.findServiceNumberAndCategoryNumber();
		result.addObject("serviceNumberAndCategoryNumber",
				serviceNumberAndCategoryNumber);
		// Dashboard D09 11.2.3 The average, the minimum, the maximum, and the standard deviation of
		// services requested per rendezvous.
		final Collection<Object> avgMaxAndMinOfRequestPerRendezvous = this.administratorService
				.findAverageMaxAndMinServicesReaquestedPerRendezvous();
		result.addObject("avgMaxMinOfRequestPerRendezvous",
				avgMaxAndMinOfRequestPerRendezvous);
		// La desviacion estandar: stdev(x) = sqrt(sum(x*x)/count(x) - avg(x)*avg(x))
		Double stdev = 0.0;
		if (!avgMaxAndMinOfRequestPerRendezvous.isEmpty()) {
			Object[] datos = (Object[]) avgMaxAndMinOfRequestPerRendezvous.iterator().next();
			Double sum = ((Long) datos[3]).doubleValue();
			Double count = ((Long) datos[4]).doubleValue();
			Double avg = (Double) datos[0];
			stdev = Math.sqrt(sum / count - avg * avg);
		}
		System.out.println(stdev);
		result.addObject("stdevOfServicesPerRendezvous", String.format(Locale.US, "%.5f", stdev));
		
		// Dashboard D09 11.2.4 The top-selling services.
		final Collection<Object> topSellingServicios = new ArrayList<>();
		if (!model.isEmpty()) {
			int i = 0;
			for (Object servicio : model) {
				topSellingServicios.add(servicio);
				i++;
				if (i > 9)
					break;
			}
		}
		result.addObject("topSellingServicios", topSellingServicios);

		
		return result;

	}

}
