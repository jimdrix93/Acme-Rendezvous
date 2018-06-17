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
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.RendezvousService;
import services.ReplyService;
import controllers.AbstractController;
import domain.Comment;
import domain.Rendezvous;
import domain.Reply;

@Controller
@RequestMapping("/comment/administrator")
public class CommentAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private CommentService		commentService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private ReplyService		replyService;


	// Listing ----------------------------------------------------------------

	@RequestMapping("/list")
	public ModelAndView action1() {
		ModelAndView result;
		Collection<Comment> comments;

		comments = this.commentService.findAll();
		result = new ModelAndView("comment/administrator/list");
		result.addObject("requestUri", "comment/administrator/list.do");
		result.addObject("comments", comments);

		return result;
	}

	// Delete by GET (link)------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteByGET(@RequestParam(required = false) final Integer Id) {
		ModelAndView result;
		final Comment selected = this.commentService.findOne(Id);

		try {
			
			Collection<Reply> replies = this.replyService.findAllByCommentId(Id);
			for (Reply reply : replies) {
				replyService.deleteByAdministrator(reply);
			}
			this.commentService.deleteByAdministrator(selected);

			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.createEditModelAndView(selected, "msg.commit.error");
		}

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezvousId) {
		ModelAndView result;
		Comment comment;

		comment = this.commentService.create(rendezvousId);
		result = this.createEditModelAndView(comment);

		result.addObject("requestURI", "administrator/comment/edit.do");

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int commentId) {
		ModelAndView result;
		Comment comment;

		comment = this.commentService.findOne(commentId);
		Assert.notNull(comment);
		result = this.createEditModelAndView(comment);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Comment comment, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(comment);
		else
			try {
				this.commentService.save(comment);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.createEditModelAndView(comment, "msg.commit.error");
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
		final Rendezvous rendezvous = comment.getRendezvous();

		if (comment.getId() == 0) {
			result = new ModelAndView("administrator/comment/create");
			result.addObject("note", comment);
			result.addObject("rendezvouses", this.rendezvousService.findAll());

		} else {
			result = new ModelAndView("administrator/comment/edit");
			result.addObject("comment", comment);
			result.addObject("rendezvous", rendezvous);
			result.addObject("moment", comment.getMoment());
			result.addObject("rendezvousId", rendezvous.getId());
			result.addObject("message", message);

		}

		return result;
	}

}
