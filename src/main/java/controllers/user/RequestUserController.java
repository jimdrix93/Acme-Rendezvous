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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.RendezvousService;
import services.RequestService;
import services.ServicioService;
import services.UserService;
import controllers.AbstractController;
import domain.Rendezvous;
import domain.Request;
import domain.Servicio;
import domain.User;

@Controller
@RequestMapping("/request/user")
public class RequestUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private RequestService		requestService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ServicioService		servicioService;
	@Autowired
	private UserService			userService;


	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(final HttpSession session, @CookieValue(value = "holderName", defaultValue = "") final String holderName, @CookieValue(value = "brandName", defaultValue = "") final String brandName, @CookieValue(value = "number",
		defaultValue = "") final String number, @CookieValue(value = "expirationMonth", defaultValue = "") final String expirationMonth, @CookieValue(value = "expirationYear", defaultValue = "") final String expirationYear, @CookieValue(value = "cvv",
		defaultValue = "") final String cvv, @CookieValue(value = "id", defaultValue = "") final String id, @CookieValue(value = "requestId", defaultValue = "") final String requestId) {

		ModelAndView result;

		Request request;

		User principal;
		principal = this.userService.findByPrincipal();

		Request cookieRequest = null;
		if (!requestId.equals(""))
			cookieRequest = this.requestService.findOne(Integer.valueOf(requestId));

		request = this.requestService.create();
		result = this.createEditModelAndView(request);
		if (!(cookieRequest == null))
			if (cookieRequest.getRendezvous().getUser().equals(principal)) {
				result.addObject("holderName", holderName);
				result.addObject("brandName", brandName);
				result.addObject("number", number);
				result.addObject("expirationMonth", expirationMonth);
				result.addObject("expirationYear", expirationYear);
				result.addObject("cvv", cvv);
			}
		return result;
	}
	// Save --------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final HttpServletResponse response, final HttpSession session, @Valid final Request request, final BindingResult binding) {
		ModelAndView result;

		response.addCookie(new Cookie("holderName", request.getCreditCard().getHolderName()));
		response.addCookie(new Cookie("brandName", request.getCreditCard().getBrandName()));
		response.addCookie(new Cookie("number", request.getCreditCard().getNumber()));
		response.addCookie(new Cookie("expirationMonth", String.valueOf(request.getCreditCard().getExpirationMonth())));
		response.addCookie(new Cookie("expirationYear", String.valueOf(request.getCreditCard().getExpirationYear())));
		response.addCookie(new Cookie("cvv", String.valueOf(request.getCreditCard().getCvv())));
		response.addCookie(new Cookie("id", String.valueOf(session.getId())));

		if (binding.hasErrors())
			result = this.createEditModelAndView(request);
		else
			try {
				final Request saved = this.requestService.save(request);
				response.addCookie(new Cookie("requestId", String.valueOf(saved.getId())));
				result = new ModelAndView("redirect:/request/user/list.do");
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.createEditModelAndView(request, "comment.commit.error");
			}

		return result;

	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Request> requests;
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		requests = this.requestService.findAllByUserId(user.getId());
		result = new ModelAndView("request/user/list");
		result.addObject("requestURI", "request/user/list.do");
		result.addObject("requests", requests);

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Request request) {
		ModelAndView result;

		result = this.createEditModelAndView(request, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Request request, final String message) {
		ModelAndView result;
		final User user = this.userService.findByPrincipal();
		final Collection<Servicio> servicios = this.servicioService.AllServiciosNotCancelled();

		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findCreatedByUserAndNotDeleted(user.getId());

		result = new ModelAndView("request/user/create");
		result.addObject("request", request);
		result.addObject("message", message);
		result.addObject("servicios", servicios);
		result.addObject("rendezvouses", rendezvouses);

		return result;
	}

}
