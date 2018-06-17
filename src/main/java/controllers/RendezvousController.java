
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezvousService;
import domain.Rendezvous;

@Controller
@RequestMapping("/rendezvous")
public class RendezvousController extends AbstractController {

	public RendezvousController() {
		super();
	}


	//Services ---------------------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;


	//List ---------------------------------------------------------------		
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;

		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findAllFinalAndNotAdult();
		result = new ModelAndView("rendezvous/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestUri", "rendezvous/list.do");

		return result;
	}

	//List Similar---------------------------------------------------------------		
	@RequestMapping(value = "/listSimilar", method = RequestMethod.GET)
	public ModelAndView listSimilar(@RequestParam final int rendezvousId) {

		ModelAndView result;

		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findSimilarRendezvousAnonymous(rendezvousId);

		result = new ModelAndView("rendezvous/listSimilar");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("requestUri", "rendezvous/listSimilar.do");

		return result;
	}

	@RequestMapping(value = "/listCategory", method = RequestMethod.GET)
	public ModelAndView listCategory(@RequestParam final int categoryId) {

		ModelAndView result;

		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findByCategory(categoryId);

		result = new ModelAndView("rendezvous/listCategory");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestUri", "rendezvous/listCategory.do");

		return result;
	}
}
