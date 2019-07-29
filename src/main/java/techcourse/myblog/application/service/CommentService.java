package techcourse.myblog.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.converter.ArticleConverter;
import techcourse.myblog.application.converter.CommentConverter;
import techcourse.myblog.application.converter.UserConverter;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private static ArticleConverter articleConverter = ArticleConverter.getInstance();
    private static CommentConverter commentConverter = CommentConverter.getInstance();
    private static UserConverter userConverter = UserConverter.getInstance();

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
    public void save(CommentDto commentDto, Long articleId, HttpSession session) {
        //Article article = articleConverter.convertFromDto(articleService.findById(articleId));
        Article article = articleService.findById(articleId);
        User user = userService.findUserByEmail((String) session.getAttribute("email"));
        commentDto.setArticle(article);
        commentDto.setAuthor(user);

        commentRepository.save(commentConverter.convertFromDto(commentDto));
    }

    public List<Comment> findAllCommentsByArticleId(Long articleId) {
        Article article = articleService.findById(articleId);
        List<Comment> comments = commentRepository.findByArticle(article);
        return comments;
    }
}
