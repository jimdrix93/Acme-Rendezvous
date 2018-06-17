package controllers.manager;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Category;
import domain.Manager;
import domain.Servicio;
import services.CategoryService;
import services.ManagerService;
import services.ServicioService;

@Controller
@RequestMapping("/servicio/manager")
public class ServicioManagerController extends AbstractController {

	// Services
	@Autowired
	private ServicioService servicioService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ManagerService managerService;

	// Create ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Servicio servicio;
		servicio = this.servicioService.create();

		result = this.createEditModelAndView(servicio);
		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Servicio servicio, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(servicio);
		else
			try {
				this.servicioService.save(servicio);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable ooops) {
				result = this.createEditModelAndView(servicio, "servicio.commit.error");
			}
		return result;
	}
	
	// Edit ---------------------------------------------------------------
		@RequestMapping(value = "/edit", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam final int servicioId) {
			ModelAndView result;
			Servicio servicio;
			servicio = this.servicioService.findOneToEdit(servicioId);
			Assert.notNull(servicio);
			Collection<Category> categories;
			categories = this.categoryService.findAll();
			Assert.notNull(categories);
			result = new ModelAndView("servicio/manager/edit");
			result.addObject("servicio", servicio);
			result.addObject("categories", categories);
			result.addObject("message", null);
			
			

			return result;
		}
	
	// Delete ---------------------------------------------------------------
		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
		public ModelAndView delete(final Servicio servicio, final BindingResult binding) {
			
			ModelAndView result;

				try {
					this.servicioService.delete(servicio);
					//TODO: Redirection to list.do
					result = new ModelAndView("redirect:/");
				} catch (final Throwable ooops) {
					result = this.createEditModelAndView(servicio, "servicio.commit.error");
				}
			return result;
		}
		
		// Listing all----------------------------------------------------------------

		@RequestMapping(value = "/list", method = RequestMethod.GET)
		public ModelAndView list() {
			ModelAndView result;
			Collection<Servicio> servicios;

			servicios = this.servicioService.findAll();
			result = new ModelAndView("servicio/manager/list");
			result.addObject("requestURI", "servicio/manager/list.do");
			result.addObject("servicios", servicios);

			return result;
		}
		
		// Listing my services----------------------------------------------------------------

		@RequestMapping(value = "/listMine", method = RequestMethod.GET)
		public ModelAndView listMine() {
			
			ModelAndView result;
			
			Collection<Servicio> servicios;
			
			Manager manager;
			manager = this.managerService.findByPrincipal();
			Assert.notNull(manager);

			servicios = manager.getServicios();
			
			result = new ModelAndView("servicio/manager/list");
			result.addObject("requestURI", "servicio/manager/listMine.do");
			result.addObject("servicios", servicios);
			result.addObject("mine", true);

			return result;
		}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final Servicio servicio) {
		final ModelAndView result;
		result = this.createEditModelAndView(servicio, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Servicio servicio, final String message) {
		final ModelAndView result;
		Collection<Category> categories;
		categories = this.categoryService.findAll();
		Assert.notNull(categories);
		result = new ModelAndView("servicio/manager/create");
		result.addObject("servicio", servicio);
		result.addObject("categories", categories);
		result.addObject("message", message);

		return result;
	}

}
