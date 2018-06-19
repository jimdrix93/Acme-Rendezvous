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

import services.ReplyService;
import controllers.AbstractController;
import domain.Reply;

@Controller
@RequestMapping("/reply/user")
public class ReplyUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ReplyService	replyService;


	// Listing ----------------------------------------------------------------

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam final int commentId) {
		ModelAndView result;
		Collection<Reply> replys;

		replys = this.replyService.findAllByCommentId(commentId);
		result = new ModelAndView("reply/user/list");
		result.addObject("requestUri", "reply/user/list.do");
		result.addObject("replys", replys);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int commentId) {
		ModelAndView result;
		Reply reply;

		reply = this.replyService.create(commentId);
		result = this.createEditModelAndView(reply);

		return result;
	}

	// Save --------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Reply reply, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(reply);
		else
			try {
				this.replyService.save(reply);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.createEditModelAndView(reply, "reply.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Reply reply) {
		ModelAndView result;

		result = this.createEditModelAndView(reply, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Reply reply, final String message) {
		ModelAndView result;

		result = new ModelAndView("reply/user/create");
		result.addObject("reply", reply);
		result.addObject("message", message);

		return result;
	}

}
