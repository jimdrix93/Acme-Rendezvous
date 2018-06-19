/*
 * ProfileController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

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
import domain.Comment;
import domain.Reply;

@Controller
@RequestMapping("/reply/administrator")
public class ReplyAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ReplyService replyService;

	// Listing ----------------------------------------------------------------

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam final int commentId) {
		ModelAndView result;
		Collection<Reply> replys;

		replys = this.replyService.findAllByCommentId(commentId);
		result = new ModelAndView("reply/administrator/list");
		result.addObject("requestUri", "reply/administrator/list.do");
		result.addObject("replys", replys);
		result.addObject("commentId", commentId);

		return result;
	}

	// Delete by GET (link)------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteByGET(@RequestParam(required = false) final Integer replyId,
			@RequestParam(required = false) final Integer commentId) {
		ModelAndView result;
		final Reply selected = this.replyService.findOne(replyId);

		try {
			this.replyService.deleteByAdministrator(selected);
			result = new ModelAndView("redirect:list.do");
			result.addObject("commentId", commentId);
		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.createEditModelAndView(selected, "msg.commit.error");
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
