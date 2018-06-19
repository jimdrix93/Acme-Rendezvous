
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnswerService;
import domain.Answer;

@Controller
@RequestMapping("/answer")
public class AnswerController extends AbstractController {

	public AnswerController() {
		super();
	}


	//Services ---------------------------------------------------------------

	@Autowired
	private AnswerService	answerService;


	//List ---------------------------------------------------------------		
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int userId, @RequestParam final int rendezvousId) {

		ModelAndView result;

		final Collection<Answer> answers = this.answerService.findByUserAndRendezvous(userId, rendezvousId);

		result = new ModelAndView("answer/list");
		result.addObject("answers", answers);
		result.addObject("requestUri", "answer/list.do");

		return result;
	}

}
