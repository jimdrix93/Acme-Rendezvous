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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ServicioService;
import controllers.AbstractController;
import domain.Servicio;

@Controller
@RequestMapping("/servicio/user")
public class ServicioUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ServicioService	servicioService;


	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Servicio> servicios;

		servicios = this.servicioService.findAll();
		result = new ModelAndView("servicio/user/list");
		result.addObject("requestURI", "servicio/user/list.do");
		result.addObject("servicios", servicios);

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Servicio servicio) {
		ModelAndView result;

		result = this.createEditModelAndView(servicio, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Servicio servicio, final String message) {
		ModelAndView result;

		result = new ModelAndView("servicio/user/create");
		result.addObject("servicio", servicio);
		result.addObject("message", message);

		return result;
	}

}
