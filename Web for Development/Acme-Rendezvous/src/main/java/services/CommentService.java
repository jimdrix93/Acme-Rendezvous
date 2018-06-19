
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import domain.Actor;
import domain.Administrator;
import domain.Comment;
import domain.Rendezvous;
import domain.Reply;
import domain.User;

@Service
@Transactional
public class CommentService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private CommentRepository	commentRepository;

	// Supporting services
	@Autowired
	private ReplyService		replyService;
	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private ActorService		actorService;


	// Constructor ----------------------------------------------------------
	public CommentService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------
	public Comment create(final int rendezvousId) {
		final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
		Assert.notNull(rendezvous);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);

		final Comment result;

		result = new Comment();
		result.setUser((User) actor);
		result.setRendezvous(rendezvous);

		return result;
	}

	public Collection<Comment> findAll() {

		Collection<Comment> result;
		result = this.commentRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Comment> findAllByUser(final int userId) {

		// Comprobamos que el actor autenticado es un "User"

		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);

		// Comprobamos que el usuario autenticado coincide con el usario pasado
		Assert.isTrue(((User) actor).getId() == userId);

		Collection<Comment> result;

		result = this.commentRepository.findAllByUser(userId);
		Assert.notNull(result);

		return result;
	}

	public Comment findOne(final int commentId) {
		Comment result;

		result = this.commentRepository.findOne(commentId);
		Assert.notNull(result);

		return result;
	}

	public Comment save(final Comment comment) {
		Assert.notNull(comment);
		Comment saved;
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		Assert.isTrue(comment.getUser().equals(actor));

		if (comment.getId() == 0) {
			final Date moment = new Date(System.currentTimeMillis() - 1);
			comment.setMoment(moment);
		}

		saved = this.commentRepository.save(comment);

		return saved;
	}

	// TOASK ¿Solo administradores puden borrar comentarios?
	public void delete(final Comment comment) {
		Assert.notNull(comment);
		// Comprobamos que el actor autenticado es un "Administrator"

		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Administrator);

		// Buscamos y borramos todos las respuestas si las hubiera

		final Collection<Reply> replies = this.replyService.findAllByCommentId(comment.getId());
		if (!replies.isEmpty())
			this.replyService.deleteInBatch(replies);

		// Finalmente borramos el comentario

		this.commentRepository.delete(comment);
	}

	public Collection<Comment> findAllByRendezvousId(final int id) {

		return this.commentRepository.findAllByRendezvousId(id);
	}

	public void deleteByAdministrator(final Comment comment) {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Administrator);

		this.commentRepository.delete(comment);
	}

	public void flush() {
		this.commentRepository.flush();
	}
}
