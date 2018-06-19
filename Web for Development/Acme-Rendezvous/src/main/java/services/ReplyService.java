
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ReplyRepository;
import domain.Actor;
import domain.Administrator;
import domain.Comment;
import domain.Reply;
import domain.User;

@Service
@Transactional
public class ReplyService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private ReplyRepository	replyRepository;

	// Supporting services ------------------------------------------------
	@Autowired
	private UserService		userService;
	@Autowired
	private CommentService	commentService;
	@Autowired
	private ActorService	actorService;


	// Constructor ----------------------------------------------------------
	public ReplyService() {
		super();
	}
	
	public void flush() {
		this.replyRepository.flush();
	}

	// Methods CRUD ---------------------------------------------------------
	public Reply create(final int commentId) {
		final Comment comment = this.commentService.findOne(commentId);
		Assert.notNull(comment);
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);

		final Reply result;

		result = new Reply();
		result.setComment(comment);
		result.setUser(user);

		return result;
	}

	public Collection<Reply> findAll() {

		Collection<Reply> result;

		result = this.replyRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Reply> findAllByCommentId(final int commentId) {

		Collection<Reply> result;

		result = this.replyRepository.findAllByCommentId(commentId);
		Assert.notNull(result);

		return result;
	}

	public Reply findOne(final int replyId) {
		Reply result;

		result = this.replyRepository.findOne(replyId);
		Assert.notNull(result);

		return result;
	}

	public Reply save(final Reply reply) {
		Assert.notNull(reply);
		Reply saved;
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		Assert.isTrue(reply.getUser().equals(actor));

		if (reply.getId() == 0) {
			final Date moment = new Date(System.currentTimeMillis() - 1);
			reply.setMoment(moment);
		}

		saved = this.replyRepository.save(reply);

		return saved;
	}

	public void delete(final Reply reply) {
		Assert.notNull(reply);

		this.replyRepository.delete(reply);
	}

	public void deleteInBatch(final Collection<Reply> replys) {
		Assert.notEmpty(replys);

		this.replyRepository.deleteInBatch(replys);
	}

	public void deleteByAdministrator(Reply selected) {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Administrator);

		this.replyRepository.delete(selected);
	}

}
