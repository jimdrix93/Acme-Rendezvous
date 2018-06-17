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

import controllers.AbstractController;
import domain.Request;
import domain.Servicio;
import services.RequestService;
import services.ServicioService;

@Controller
@RequestMapping("/servicio/administrator")
public class ServicioAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ServicioService	servicioService;

	@Autowired
	private RequestService	requestService;


	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer categoryId) {
		ModelAndView result;
		
		Collection<Servicio> servicios;
		
		if(categoryId==null){
			servicios = this.servicioService.findAll();
		}else{
			servicios = this.servicioService.getServicesByCategoryId(categoryId);
		}
		
		result = new ModelAndView("servicio/administrator/list");
		result.addObject("requestURI", "servicio/administrator/list.do");
		result.addObject("servicios", servicios);

		return result;
	}
	
	//TODO falta un list por categoría
	
	// Cancel by GET (link)------------------------------------------------------

		@RequestMapping(value = "/cancel", method = RequestMethod.GET)
		public ModelAndView cancelByGET(@RequestParam(required = false) final Integer servicioId) {
			ModelAndView result;
			final Servicio selected = this.servicioService.findOne(servicioId);
			selected.setCancelled(!selected.isCancelled());
			Collection<Request> requests = requestService.findAllByServiceId(servicioId);
			for (Request request : requests) {
				request.setCancelled(selected.isCancelled());
				try {
					this.requestService.save(request);
				} catch (final Throwable oops) {
					oops.printStackTrace();
					result = this.createEditModelAndView(selected, "msg.commit.error");
				}
			}
			try {
				this.servicioService.save(selected);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.createEditModelAndView(selected, "msg.commit.error");
			}

			return result;
		}
	
//	// Delete by POST ------------------------------------------------------------
//
//		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
//		public ModelAndView delete(@Valid final Servicio servicio, final BindingResult binding) {
//			ModelAndView result;
//
//			if (binding.hasErrors())
//				result = this.createEditModelAndView(servicio);
//			else
//				try {
//					this.servicioService.delete(servicio);
//					result = new ModelAndView("redirect:../list.do");
//				} catch (final Throwable ooops) {
//					result = this.createEditModelAndView(servicio, "servicio.commit.error");
//				}
//			return result;
//		}
//
//		// Delete by GET (link)------------------------------------------------------
//
//		@RequestMapping(value = "/delete", method = RequestMethod.GET)
//		public ModelAndView deleteByGET(@RequestParam(required = false) final Integer servicioId) {
//			ModelAndView result;
//			final Servicio selected = this.servicioService.findOne(servicioId);
//			selected.setCancelled(true);
//			try {
//				this.servicioService.remove(selected);
//
//				result = new ModelAndView("redirect:list.do");
//			} catch (final Throwable oops) {
//				oops.printStackTrace();
//				result = this.createEditModelAndView(selected, "msg.commit.error");
//			}
//
//			return result;
//		}


	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Servicio servicio) {
		ModelAndView result;

		result = this.createEditModelAndView(servicio, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Servicio servicio, final String message) {
		ModelAndView result;

		result = new ModelAndView("servicio/user/create");
		result.addObject("servicio", servicio);
		result.addObject("message", message);

		return result;
	}

}
