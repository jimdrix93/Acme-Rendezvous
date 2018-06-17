
package controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Category;
import domain.Rendezvous;
import services.CategoryService;
import services.RendezvousService;
import utilities.TreeBuilder;

@Controller
@RequestMapping("/category")
public class CategoryController extends AbstractController {

	public CategoryController() {
		super();
	}

	// Services
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private RendezvousService rendezvousService;

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
		Collection<Category> categories;
		Collection<Rendezvous> categoryRendezvouses;
		categories = categoryService.findAll();
		Collections.sort((List<Category>) categories);
		Category selected = null;
		String tree = TreeBuilder.jspTree(categories, "");
		if (categoryId != null) {
			selected = categoryService.findOne(categoryId);
			categoryRendezvouses = rendezvousService.findAllFinalAndNotAdultByCategory(categoryId);
		} else {
			selected = new Category();
			selected.setName("-");
			categoryRendezvouses = rendezvousService.findAllFinalAndNotAdultUncategorized();
		}
		List<Category> ruta = TreeBuilder.getRuta(selected);
		result = new ModelAndView("category/browse");
		result.addObject("categories", categories);
		result.addObject("requestUri", "category/browse.do");
		result.addObject("selected", selected);
		result.addObject("rendezvouses", categoryRendezvouses);
		result.addObject("tree", tree);
		result.addObject("ruta", ruta);

		return result;
	}

}
