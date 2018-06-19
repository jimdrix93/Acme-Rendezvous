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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import services.RendezvousService;
import controllers.AbstractController;
import domain.Announcement;

@Controller
@RequestMapping("/announcement/administrator")
public class AnnouncementAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;
	@Autowired
	private RendezvousService	rendezvousService;


	//List ---------------------------------------------------------------		
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int rendezvousId) {

		ModelAndView result;

		final Collection<Announcement> announcements = this.announcementService.findByRendezvous(rendezvousId);

		result = new ModelAndView("announcement/administrator/list");
		result.addObject("announcements", announcements);
		result.addObject("requestUri", "announcement/administrator/list.do");

		return result;
	}

	// Delete by GET (link)------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteByAdministrator(@RequestParam(required = false) final int announcementId) {
		ModelAndView result;
		final Announcement announcement = this.announcementService.findOne(announcementId);

		try {
			this.announcementService.deleteByAdministrator(announcement);

			result = new ModelAndView("redirect:/");
		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.createEditModelAndView(announcement, "announcement.commit.error");
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Announcement announcement) {
		ModelAndView result;

		result = this.createEditModelAndView(announcement, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Announcement announcement, final String message) {
		ModelAndView result;

		result = new ModelAndView("announcement/administrator/edit");
		result.addObject("announcement", announcement);

		return result;
	}

}
