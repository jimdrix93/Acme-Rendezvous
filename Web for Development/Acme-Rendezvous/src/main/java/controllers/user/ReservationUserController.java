
package controllers.user;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Question;
import domain.Rendezvous;
import domain.Reservation;
import domain.User;
import forms.FormularioPreguntas;
import services.AnswerService;
import services.QuestionService;
import services.RendezvousService;
import services.ReservationService;
import services.UserService;

@Controller
@RequestMapping("/reservation/user")
public class ReservationUserController {

	public ReservationUserController() {
		super();
	}

	// Services ---------------------------------------------------------------

	@Autowired
	private RendezvousService rendezvousService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private QuestionService questionService;
	@Autowired
	private AnswerService answerService;

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		final User user = this.userService.findByPrincipal();

		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findCreatedByUser(user.getId());

		result = new ModelAndView("rendezvous/user/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestUri", "rendezvous/user/list.do");
		result.addObject("user", user);
		result.addObject("showAddQuestion", true);

		return result;
	}

	// List Reserved ---------------------------------------------------------------
	@RequestMapping(value = "/listReserved", method = RequestMethod.GET)
	public ModelAndView listReserved() {

		ModelAndView result;
		final User user = this.userService.findByPrincipal();

		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findReservedByUser(user.getId());
		final Collection<Rendezvous> reserved = this.rendezvousService.findReservedAndNotCanceledByUserId(user.getId());
		final Collection<Rendezvous> canceled = this.rendezvousService.findCanceledByUserId(user.getId());

		result = new ModelAndView("rendezvous/user/listReserved");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestUri", "rendezvous/user/listReserved.do");
		result.addObject("user", user);
		result.addObject("reserved", reserved);
		result.addObject("canceled", canceled);
		result.addObject("reservedRendezvous", true);

		return result;
	}

	// List All ---------------------------------------------------------------
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll() {

		ModelAndView result;
		Collection<Rendezvous> rendezvouses;

		final User user;
		user = this.userService.findByPrincipal();

		// No mostrar rendezvouses de adultos a usuarios no adultos
		if (!user.getAdult())
			rendezvouses = this.rendezvousService.findAllNotAdult();
		else
			rendezvouses = this.rendezvousService.findAll();

		final Collection<Rendezvous> reserved = this.rendezvousService.findReservedAndNotCanceledByUserId(user.getId());
		result = new ModelAndView("rendezvous/list");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("requestUri", "rendezvous/user/listAll.do");
		result.addObject("reserved", reserved);
		result.addObject("user", user);

		return result;
	}

	// Create ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Rendezvous rendezvous;
		rendezvous = this.rendezvousService.create();

		result = this.createEditModelAndView(rendezvous);
		return result;
	}

	// Reserve -----------------------------------------------------------

	@RequestMapping(value = "/reserve", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {

		ModelAndView result;
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		Rendezvous rendezvous;
		rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);
		
		
		// Abortamos si la cita es pasado
		if (rendezvous.getMoment().before(new Date()))
			return new ModelAndView("redirect:/");

		// Abortamos si un usuario no adulto intenta reservar una cita para adultosinte
		if (!user.getAdult() && rendezvous.getAdult())
			return new ModelAndView("redirect:/");

		// Buscamos si ya existe una reserva y si estubiera cancelada.
		// En ese case se descancelaria la reserva pues ya habria respondido a las
		// preguntas si las tubiera
		Reservation reservation = this.reservationService.findReservationByUserAndRendezvous(user, rendezvous);
		if (reservation != null) {
			if (reservation.isCanceled()) {
				reservation.setCanceled(false);
				reservation = this.reservationService.save(reservation);
			}
			return new ModelAndView("redirect:/");
		} else {// reservation == null
				// Comprobamos si tiene preguntas asociadas
			Collection<Question> preguntas = questionService.findAllByRendezvousId(rendezvousId);
			if (preguntas.isEmpty()) {
				// Si no las tiene creamos la reserva y terminamos
				reservation = this.reservationService.create();
				Assert.notNull(reservation);

				Reservation saved;
				saved = this.rendezvousService.reserveRendezvous(reservation, rendezvous);
				Assert.notNull(saved);

				reservation = this.reservationService.save(saved);
				result = new ModelAndView("redirect:/");
			} else {// Si tiene preguntas creamos un formulario con las preguntas y la cita
				Map<String, String> preguntasMap = new HashMap<String, String>();
				for (Question question : preguntas) {
					preguntasMap.put(question.getText(), "");
				}
				FormularioPreguntas formularioPreguntas = new FormularioPreguntas();
				formularioPreguntas.setCuestionario(preguntasMap);
				formularioPreguntas.setRendezvous(rendezvous);
				formularioPreguntas.setUser(user);
				
				result = new ModelAndView("answer/user/edit");
				result.addObject("requestURI", "answer/user/save.do");
				result.addObject("formularioPreguntas", formularioPreguntas);

			}
		}
		return result;
	}

	// Edit ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;
		rendezvous = this.rendezvousService.findOneToEdit(rendezvousId);

		result = new ModelAndView("rendezvous/user/edit");
		result.addObject("rendezvous", rendezvous);

		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Rendezvous rendezvous, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(rendezvous);
		else
			try {
				// Usuario no adulto solo puede crear rendezvous no adulto
				final User user;
				user = this.userService.findByPrincipal();
				if (!user.getAdult())
					rendezvous.setAdult(false);

				this.rendezvousService.save(rendezvous);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable ooops) {
				result = this.createEditModelAndView(rendezvous, "rendezvous.commit.error");
			}
		return result;
	}

	// Delete ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final Rendezvous rendezvous, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(rendezvous);
		else
			try {
				this.rendezvousService.deleteByUser(rendezvous);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable ooops) {
				result = this.createEditModelAndView(rendezvous, "rendezvous.commit.error");
			}
		return result;
	}

	// Cancel -----------------------------------------------------------

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int rendezvousId) {
		ModelAndView result;

		Rendezvous rendezvous;
		rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);

		User user;
		user = this.userService.findByPrincipal();
		Assert.notNull(user);

		Reservation reservation;
		reservation = this.reservationService.findReservationByUserAndRendezvous(user, rendezvous);

		final Reservation canceled = this.rendezvousService.cancelRendezvous(reservation);
		this.reservationService.save(canceled);

		result = new ModelAndView("redirect:/");

		return result;
	}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous) {
		final ModelAndView result;
		result = this.createEditModelAndView(rendezvous, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Rendezvous rendezvous, final String message) {
		final ModelAndView result;
		result = new ModelAndView("rendezvous/user/create");
		result.addObject("rendezvous", rendezvous);
		result.addObject("message", message);

		return result;
	}

}
