
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.RendezvousService;
import services.UserService;
import domain.Actor;
import domain.Administrator;
import domain.User;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	// Supporting services -----------------------------------------------------
	@Autowired
	ActorService			actorService;
	@Autowired
	UserService				userService;
	@Autowired
	RendezvousService		rendezvousService;
	@Autowired
	AdministratorService	administratorService;


	// Constructors -----------------------------------------------------------

	public ActorController() {
		super();
	}

	// Edition -----------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		final Actor actor = this.actorService.findByPrincipal();
		ModelAndView result;

		result = this.createEditModelAndView(actor);
		return result;
	}

	// SaveAdmin -----------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveAdmin")
	public ModelAndView saveAdmin(@Valid final Administrator admin, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(admin);
		else
			try {
				this.administratorService.save(admin);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable ooops) {
				result = this.createEditModelAndView(admin, "actor.commit.error");
			}
		return result;
	}

	// SaveUser -----------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveUser")
	public ModelAndView saveUser(@Valid final User user, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(user);
		else
			try {
				this.userService.save(user);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable ooops) {
				result = this.createEditModelAndView(user, "actor.commit.error");
			}
		return result;
	}

	// Ancillary Methods -----------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Actor actor) {
		assert actor != null;
		ModelAndView result;

		result = this.createEditModelAndView(actor, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor, final String message) {
		ModelAndView result;
		assert actor != null;
		String actorType = "";

		result = new ModelAndView("actor/edit");

		if (actor.getClass().equals(Administrator.class)) {
			final Administrator administrator = (Administrator) actor;
			actorType = "administrator";
			result.addObject(actorType, administrator);
			result.addObject("actorType", actorType);
		} else if (actor.getClass().equals(User.class)) {
			final User user = (User) actor;
			actorType = "user";
			result.addObject(actorType, user);
			result.addObject("actorType", actorType);
		}

		result.addObject("message", message);

		return result;
	}

}
