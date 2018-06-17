/*
 * ProfileController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LinkService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Link;
import domain.Rendezvous;

@Controller
@RequestMapping("/link/user")
public class LinkUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private LinkService			linkService;
	@Autowired
	private RendezvousService	rendezvousService;


	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Link link;

		link = this.linkService.create(rendezvousId);
		result = this.createEditModelAndView(link);

		return result;
	}

	// Save --------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Link link, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(link);
		else
			try {
				this.linkService.save(link);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.createEditModelAndView(link, "link.commit.error");
			}

		return result;
	}

	// Delete by GET (link)------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam(required = false) final int rendezvousId, @RequestParam(required = false) final int linkedToRendezvousId) {
		ModelAndView result;
		final Link link = this.linkService.findLink(rendezvousId, linkedToRendezvousId);

		try {
			this.linkService.delete(link);

			result = new ModelAndView("redirect:/");
		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.createEditModelAndView(link, "link.commit.error");
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Link link) {
		ModelAndView result;

		result = this.createEditModelAndView(link, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Link link, final String message) {
		ModelAndView result;

		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findAll();
		rendezvouses.remove(link.getRendezvous());

		result = new ModelAndView("link/user/create");
		result.addObject("link", link);
		result.addObject("message", message);
		result.addObject("linkedToRendezvous", rendezvouses);

		return result;
	}

}
