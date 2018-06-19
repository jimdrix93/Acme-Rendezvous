package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Manager;
import domain.User;
import services.ManagerService;

@Controller
@RequestMapping("/manager")
public class ManagerController extends AbstractController {
	
	//Services
	@Autowired
	private ManagerService managerService;
	
	//Constructor
	
	public ManagerController() {
		super();
	}
	
	// Create manager ---------------------------------------------------------------
	@RequestMapping("/create")
	public ModelAndView create() {
		ModelAndView result;
		final Manager manager = this.managerService.create();

		result = this.createEditModelAndView(manager);

		return result;
	}
	
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Manager manager, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(manager);
		else
			try {
				this.managerService.save(manager);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(manager, "manager.commit.error");
			}
		return result;
	}
	
	// Auxiliary methods ---------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Manager manager) {
		final ModelAndView result;
		result = this.createEditModelAndView(manager, null);
		return result;
	}
	protected ModelAndView createEditModelAndView(final Manager manager, final String message) {
		final ModelAndView result;

		result = new ModelAndView("manager/create");
		result.addObject("manager", manager);
		result.addObject("message", message);

		return result;
	}
}
