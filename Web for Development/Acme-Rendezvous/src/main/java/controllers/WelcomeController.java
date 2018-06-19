/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	services.ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------

	@RequestMapping(value = "/index")
	public ModelAndView index() {
		ModelAndView result;

		final Locale locale = LocaleContextHolder.getLocale();

		SimpleDateFormat formatter;
		String moment;
		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		result = new ModelAndView("welcome/index");
		result.addObject("name", this.configurationService.findWelcomeMessage(locale.getLanguage()));
		result.addObject("moment", moment);

		return result;
	}
}
