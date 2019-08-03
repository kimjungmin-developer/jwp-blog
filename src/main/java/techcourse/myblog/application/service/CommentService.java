package techcourse.myblog.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.exception.CommentNotFoundException;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.application.service.exception.NotMatchAuthorException;
import techcourse.myblog.domain.*;
import techcourse.myblog.domain.vo.CommentContents;

import java.util.List;

@Service
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private CommentRepository commentRepository;
    private UserService userService;
    private ArticleService articleService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    @Transactional
    public void save(CommentContents commentContents, Long articleId, Email email) {
        Article article = articleService.findById(articleId);
        User user = userService.findUserByEmail(email.getEmail());
        Comment comment = new Comment(commentContents, user, article);

        commentRepository.save(comment);
    }

    public List<CommentDto> findAllCommentsByArticleId(Long articleId, String sessionEmail) {
        Article article = articleService.findById(articleId);
        List<Comment> comments = commentRepository.findByArticle(article);
        List<CommentDto> commentDtos = Comment.toDto(comments);
        for (CommentDto commentDto : commentDtos) {
            commentDto.matchAuthor(sessionEmail);
        }
        return commentDtos;
    }

    public void delete(long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void modify(Long commentId, CommentContents commentContents) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("댓글이 존재하지 않습니다"));
        comment.changeContent(commentContents);
    }

    public void checkAuthor(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotExistArticleIdException("존재하지 않는 Article 입니다."));
        User author = comment.getAuthor();
        if (!author.compareEmail(email)) {
            throw new NotMatchAuthorException("너는 이 글에 작성자가 아니다. 꺼져라!");
        }
    }
}
