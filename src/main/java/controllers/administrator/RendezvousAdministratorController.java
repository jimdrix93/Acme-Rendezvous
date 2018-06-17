
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezvousService;
import controllers.AbstractController;
import domain.Rendezvous;

@Controller
@RequestMapping("/rendezvous/administrator")
public class RendezvousAdministratorController extends AbstractController {

	public RendezvousAdministratorController() {
		super();
	}


	//Services

	@Autowired
	private RendezvousService	rendezvousService;


	// List ---------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;

		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findAll();
		result = new ModelAndView("rendezvous/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestUri", "rendezvous/administrator/list.do");

		return result;
	}

	// Delete by POST ------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final Rendezvous rendezvous, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(rendezvous);
		else
			try {
				this.rendezvousService.delete(rendezvous);
				result = new ModelAndView("redirect:../list.do");
			} catch (final Throwable ooops) {
				result = this.createEditModelAndView(rendezvous, "rendezvous.commit.error");
			}
		return result;
	}

	// Delete by GET (link)------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteByGET(@RequestParam(required = false) final Integer rendezvousId) {
		ModelAndView result;
		final Rendezvous selected = this.rendezvousService.findOne(rendezvousId);

		try {
			this.rendezvousService.remove(selected);

			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.createEditModelAndView(selected, "msg.commit.error");
		}

		return result;
	}

	// Auxiliary methods ---------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous) {
		final ModelAndView result;
		result = this.createEditModelAndView(rendezvous, null);
		return result;
	}
	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous, final String message) {
		final ModelAndView result;
		result = new ModelAndView("rendezvous/administrator/create");
		result.addObject("rendezvous", rendezvous);
		result.addObject("message", message);

		return result;
	}
}
