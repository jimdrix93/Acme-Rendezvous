
package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Rendezvous;
import domain.Reservation;
import domain.User;
import forms.RendezvousEditForm;
import services.RendezvousService;
import services.ReservationService;
import services.UserService;

@Controller
@RequestMapping("/rendezvous/user")
public class RendezvousUserController extends AbstractController {

	public RendezvousUserController() {
		super();
	}


	// Services ---------------------------------------------------------------

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;

	@Autowired
	private ReservationService	reservationService;


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
		result.addObject("isCreater", true);

		return result;
	}

	//List Similar---------------------------------------------------------------		
	@RequestMapping(value = "/listSimilar", method = RequestMethod.GET)
	public ModelAndView listSimilar(@RequestParam final int rendezvousId) {

		ModelAndView result;

		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findSimilarByRendezvousAuthenticated(rendezvousId);

		result = new ModelAndView("rendezvous/user/listSimilar");
		result.addObject("rendezvouses", rendezvouses);
		result.addObject("rendezvousId", rendezvousId);
		result.addObject("requestUri", "rendezvous/user/listSimilar.do");
		result.addObject("delete", true);

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

		//No mostrar rendezvouses de adultos a usuarios no adultos
		if (!user.getAdult())
			rendezvouses = this.rendezvousService.findAllFinalAndNotAdult();
		else
			rendezvouses = this.rendezvousService.findAllFinal();

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
	public ModelAndView create(@RequestHeader(value = "referer", required = false) final String backUrl) {
		ModelAndView result;
		Rendezvous rendezvous;
		rendezvous = this.rendezvousService.create();

		result = this.createEditModelAndView(rendezvous);
		result.addObject("backUrl", backUrl);
		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Rendezvous rendezvous, final BindingResult binding, final String backUrl) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("rendezvous/user/edit");
			result.addObject("rendezvous", rendezvous);
			result.addObject("backUrl", backUrl);
		} else
			try {
				//Usuario no adulto solo puede crear rendezvous no adulto
				final User user;
				user = this.userService.findByPrincipal();
				if (!user.getAdult())
					rendezvous.setAdult(false);

				this.rendezvousService.save(rendezvous);
				result = new ModelAndView("redirect:/rendezvous/user/list.do");
			} catch (final Throwable ooops) {
				if (ooops.getMessage().equals("Moment must be in future."))
					result = this.createEditModelAndView(rendezvous, "rendezvous.commit.error.date");
				else
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
				result = new ModelAndView("redirect:/rendezvous/user/list.do");
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

		result = new ModelAndView("redirect:/rendezvous/user/listAll.do");

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

	//FORMS

	//edit rendezvous form
	// Edit ---------------------------------------------------------------
	@RequestMapping(value = "/editForm", method = RequestMethod.GET)
	public ModelAndView editForm(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Rendezvous rendezvous;
		rendezvous = this.rendezvousService.findOneToEdit(rendezvousId);
		final RendezvousEditForm rendezvousEditForm = this.rendezvousService.constructEditForm(rendezvous);

		result = new ModelAndView("rendezvous/user/editForm");
		result.addObject("rendezvousEditForm", rendezvousEditForm);

		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/editForm", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RendezvousEditForm rendezvousEditForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("rendezvous/user/editForm");
			result.addObject("rendezvousEditForm", rendezvousEditForm);
		} else
			try {

				final Rendezvous rendezvous = this.rendezvousService.reconstruct(rendezvousEditForm);
				this.rendezvousService.save(rendezvous);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable ooops) {
				if (ooops.getMessage().equals("Moment must be in future."))
					result = this.createEditModelAndView(rendezvousEditForm, "rendezvous.commit.error.date");
				else
					result = this.createEditModelAndView(rendezvousEditForm, "rendezvous.commit.error");

			}
		return result;
	}

	// Delete ---------------------------------------------------------------
	@RequestMapping(value = "/editForm", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final RendezvousEditForm rendezvousEditForm, final BindingResult binding) {
		ModelAndView result;
		final Rendezvous rendezvous = this.rendezvousService.reconstruct(rendezvousEditForm);

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

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final RendezvousEditForm rendezvousEditForm) {
		final ModelAndView result;
		result = this.createEditModelAndView(rendezvousEditForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final RendezvousEditForm rendezvousEditForm, final String message) {
		final ModelAndView result;
		result = new ModelAndView("rendezvous/user/editForm");
		result.addObject("rendezvousEditForm", rendezvousEditForm);
		result.addObject("message", message);

		return result;
	}

}
