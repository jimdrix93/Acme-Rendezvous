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
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Answer;
import domain.Question;
import domain.Reservation;
import domain.User;
import forms.FormularioPreguntas;
import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
import services.ReservationService;
import services.UserService;

@Controller
@RequestMapping("/answer/user")
public class AnswerUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private AnswerService answerService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ReservationService reservationService;
	@Autowired
	private UserService userService;
	@Autowired
	private RendezvousService rendezvousService;

	// Fail page
	@RequestMapping(value = "/fail", method = RequestMethod.GET)
	public ModelAndView fail() {
		ModelAndView result;
		result = new ModelAndView("/answer/user/fail");

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int questionId, @RequestParam final int rendezvousId) {
		ModelAndView result;
		Answer answer;

		answer = this.answerService.create(questionId, rendezvousId);
		result = this.createEditModelAndView(answer);

		return result;
	}

	// Save --------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Answer answer, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(answer);
		else
			try {
				this.answerService.save(answer);
				result = new ModelAndView("redirect:/rendezvous/user/reserve.do?rendezvousId="
						+ answer.getReservation().getRendezvous().getId());

			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.createEditModelAndView(answer, "answer.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("formularioEjemplo") final FormularioPreguntas formulario,
			final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = new ModelAndView("answer/user/create");
			result.addObject("formulario", formulario);
			result.addObject("message", null);
		} else {

			final User user = this.userService.findByPrincipal();
			Assert.notNull(user);
			Assert.isTrue(user.equals(formulario.getUser()));
			Boolean todasRespondidas = true;
			// Comprobamos que las preguntas estan contestadas si no es cadena vacia o solo
			// espacios
			// Para ello:
			// 1º recuperamos las Questions
			Collection<Question> preguntas = questionService.findAllByRendezvousId(formulario.getRendezvous().getId());
			for (Question question : preguntas) {
				if (formulario.getCuestionario().get(question.getText()).trim().length() == 0) {
					todasRespondidas = false;
				}
			}

			// Creamos y guardamos la reserva
			if (todasRespondidas) {
				if (!user.getAdult() && formulario.getRendezvous().getAdult())
					return new ModelAndView("redirect:/");

				Reservation reservation = this.reservationService.findReservationByUserAndRendezvous(user,
						formulario.getRendezvous());

				if (reservation == null) {
					reservation = this.reservationService.create();
					Assert.notNull(reservation);

					Reservation done;
					done = this.rendezvousService.reserveRendezvous(reservation, formulario.getRendezvous());
					Assert.notNull(done);

					reservation = this.reservationService.save(done);

				} else if (reservation.isCanceled()) {
					reservation.setCanceled(false);
					reservation = this.reservationService.save(reservation);
				}

				for (Question question : preguntas) {
					Answer respuesta = answerService.create(question.getId(), formulario.getRendezvous().getId());
					respuesta.setText(formulario.getCuestionario().get(question.getText()).trim());
					answerService.save(respuesta);
				}

			} else {
				return new ModelAndView("redirect:/answer/user/fail.do");
			}
			// Guardamos las respuestas

			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Answer answer) {
		ModelAndView result;

		result = this.createEditModelAndView(answer, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Answer answer, final String message) {
		ModelAndView result;

		result = new ModelAndView("answer/user/create");
		result.addObject("answer", answer);
		result.addObject("message", message);

		return result;
	}

}
