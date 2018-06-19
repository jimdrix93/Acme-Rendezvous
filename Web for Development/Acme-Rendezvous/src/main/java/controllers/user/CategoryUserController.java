
package controllers.user;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Category;
import domain.Rendezvous;
import domain.User;
import services.CategoryService;
import services.RendezvousService;
import services.UserService;
import utilities.TreeBuilder;

@Controller
@RequestMapping("/category/user")
public class CategoryUserController extends AbstractController {

	public CategoryUserController() {
		super();
	}

	// Services
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private RendezvousService rendezvousService;
	@Autowired
	private UserService userService;

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Category> categories;
		final boolean admin = false;

		categories = this.categoryService.findAll();

		result = new ModelAndView("category/list");
		result.addObject("requestURI", "category/list.do");
		result.addObject("categories", categories);
		result.addObject("admin", admin);

		return result;
	}
	// Browse Rendezvouses by category --------------------------------------------

	@RequestMapping(value = "/browse", method = RequestMethod.GET)
	public ModelAndView browse(@RequestParam(required = false) Integer categoryId) {
		ModelAndView result;
		final User user;
		user = this.userService.findByPrincipal();

		Collection<Category> categories;
		Collection<Rendezvous> categoryRendezvouses;
		categories = categoryService.findAll();
		Collections.sort((List<Category>) categories);
		Category selected = null;
		String tree = TreeBuilder.jspTree(categories, "user/");
		if (categoryId != null) {
			selected = categoryService.findOne(categoryId);
			if (!user.getAdult())// No mostrar rendezvouses de adultos a usuarios no adultos
				categoryRendezvouses = rendezvousService.findAllFinalAndNotAdultByCategory(categoryId);
			else
				categoryRendezvouses = this.rendezvousService.findAllFinalByCategory(categoryId);
		} else {
			selected = new Category();
			selected.setName("-");
			if (!user.getAdult())// No mostrar rendezvouses de adultos a usuarios no adultos
				categoryRendezvouses = rendezvousService.findAllFinalAndNotAdultUncategorized();
			else
				categoryRendezvouses = this.rendezvousService.findAllFinalUncategorized();
		}
		List<Category> ruta = TreeBuilder.getRuta(selected);
		result = new ModelAndView("category/user/browse");
		result.addObject("categories", categories);
		result.addObject("requestUri", "category/user/browse.do");
		result.addObject("selected", selected);
		result.addObject("rendezvouses", categoryRendezvouses);
		result.addObject("tree", tree);
		result.addObject("ruta", ruta);

		return result;
	}

}
