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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Comment;

@Controller
@RequestMapping("/comment/user")
public class CommentUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private CommentService		commentService;
	@Autowired
	private RendezvousService	rendezvousService;


	// Listing ----------------------------------------------------------------

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Collection<Comment> comments;

		comments = this.commentService.findAllByRendezvousId(rendezvousId);
		result = new ModelAndView("comment/user/list");
		result.addObject("requestUri", "comment/user/list.do");
		result.addObject("comments", comments);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Comment comment;

		comment = this.commentService.create(rendezvousId);
		result = this.createEditModelAndView(comment);

		return result;
	}

	// Save --------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Comment comment, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(comment);
		else
			try {
				this.commentService.save(comment);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.createEditModelAndView(comment, "comment.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Comment comment) {
		ModelAndView result;

		result = this.createEditModelAndView(comment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Comment comment, final String message) {
		ModelAndView result;

		result = new ModelAndView("comment/user/create");
		result.addObject("comment", comment);
		result.addObject("message", message);

		return result;
	}

}
