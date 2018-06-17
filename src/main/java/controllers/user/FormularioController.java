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
import forms.FormularioPreguntas;

@Controller
@RequestMapping("/formulario/user")
public class FormularioController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private LinkService			linkService;
	@Autowired
	private RendezvousService	rendezvousService;


	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		FormularioPreguntas ejemplo = new FormularioPreguntas();

		result = new ModelAndView("answer/user/createform", "formularioEjemplo", ejemplo);
		result.addObject("requestURI", "formulario/user/save.do");

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
