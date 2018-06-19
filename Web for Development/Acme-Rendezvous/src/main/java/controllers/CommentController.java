/*
 * ProfileController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.RendezvousService;
import domain.Comment;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private CommentService		commetService;
	@Autowired
	private RendezvousService	rendezvousService;


	// Listing ----------------------------------------------------------------

	@RequestMapping("/list")
	public ModelAndView action1() {
		ModelAndView result;
		Collection<Comment> comments;

		comments = this.commetService.findAll();
		result = new ModelAndView("comment/list");
		result.addObject("requestURI", "comment/list.do");
		result.addObject("comments", comments);

		return result;
	}

	// Ancillary methods ------------------------------------------------------

}
