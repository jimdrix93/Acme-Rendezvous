
package controllers.administrator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CategoryService;
import services.RendezvousService;
import services.UserService;
import utilities.TreeBuilder;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Category;
import domain.Rendezvous;
import domain.User;


@Controller
@RequestMapping("/category/administrator")
public class CategoryAdministratorController extends AbstractController {

	public CategoryAdministratorController() {
		super();
	}


	//Services
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private RendezvousService rendezvousService;
	@Autowired
	private ActorService actorService;

	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		
		ModelAndView result;
		Category category;
		
		
		category = this.categoryService.create();
		result = this.createEditModelAndView(category);
		
		return result;
	}
	
	// Edition ----------------------------------------------------------------

		@RequestMapping(value = "/edit", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam final int categoryId) {
			ModelAndView result;
			Category category;

			category = this.categoryService.findOne(categoryId);
			
			result = this.createEditModelAndView(category);

			return result;
		}

	

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Category category, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(category);
		else
			try {
				this.categoryService.save(category);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(category, "category.commit.error");
			}

		return result;
	}
	
	//Delete
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Category category, final BindingResult binding) {
		ModelAndView result;

		try {
			this.categoryService.delete(category);
			result = new ModelAndView("redirect:/");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(category, "category.commit.error");
		}

		return result;
	}
	

	
	
	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer parentCategoryId) {
		ModelAndView result;
		Collection<Category> categories;
		final boolean admin = true;

		if (parentCategoryId == null)
			categories = this.categoryService.getFirstLevelCategories();
		else
			categories = this.categoryService.getChildCategories(parentCategoryId);

		result = new ModelAndView("category/administrator/list");
		result.addObject("requestURI", "category/administrator/list.do");
		result.addObject("categories", categories);
		result.addObject("admin", admin);
		return result;
	}
	
	// Browse Rendezvouses by category --------------------------------------------

		@RequestMapping(value = "/browse", method = RequestMethod.GET)
		public ModelAndView browse(@RequestParam(required = false) Integer categoryId) {
			ModelAndView result;
			final Actor actor;
			actor = this.actorService.findByPrincipal();
			Assert.isTrue(actor instanceof Administrator);

			Collection<Category> categories;
			Collection<Rendezvous> categoryRendezvouses;
			categories = categoryService.findAll();
			Collections.sort((List<Category>) categories);
			Category selected = null;
			String tree = TreeBuilder.jspTree(categories, "administrator/");
			if (categoryId != null) {
				selected = categoryService.findOne(categoryId);
				categoryRendezvouses = this.rendezvousService.findAllFinalByCategory(categoryId);
			} else {
				selected = new Category();
				selected.setName("-");
				categoryRendezvouses = this.rendezvousService.findAllFinalUncategorized();
			}
			List<Category> ruta = TreeBuilder.getRuta(selected);
			result = new ModelAndView("category/administrator/browse");
			result.addObject("categories", categories);
			result.addObject("requestUri", "category/administrator/browse.do");
			result.addObject("selected", selected);
			result.addObject("rendezvouses", categoryRendezvouses);
			result.addObject("tree", tree);
			result.addObject("ruta", ruta);

			return result;
		}

	
	// Ancillary methods ------------------------------------------------------

		protected ModelAndView createEditModelAndView(final Category category) {
			ModelAndView result;

			result = this.createEditModelAndView(category, null);

			return result;
		}

		protected ModelAndView createEditModelAndView(final Category category, final String message) {
			ModelAndView result;
			Collection<Category> categories;


			result = new ModelAndView("category/administrator/edit");
			categories = categoryService.findAll();
			categories.remove(category);
			
			result.addObject("category", category);
			result.addObject("message", message);
			result.addObject("categories", categories);

			return result;
		}
	
	

}
