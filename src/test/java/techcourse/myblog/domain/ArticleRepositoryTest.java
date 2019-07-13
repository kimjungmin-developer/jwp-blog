package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleRepositoryTest {
    private ArticleRepository articleRepository;
    private Article testArticle;

    @BeforeEach
    void setUp() {
        this.articleRepository = new ArticleRepository();
        this.testArticle = new Article("2","3","4");
        this.testArticle = testArticle.insertId(1,testArticle);
        this.articleRepository.add(this.testArticle);
    }

    @Test
    void findAll_호출후_값_조작시_에러_테스트() {
        assertThrows(UnsupportedOperationException.class, () -> this.articleRepository.findAll().add(new Article("1","2","3")));
    }

    @Test
    void 아이디값으로_조회_테스트() {
        assertThat(this.testArticle).isEqualTo(this.articleRepository.findById(1));
    }

    @Test
    void 다음_아이디값_생성_테스트() {
        assertThat(this.articleRepository.newArticleId()).isEqualTo(2);
    }

    @Test
    void article_업데이트_테스트() {
        Article articleToCompare = new Article("this is a test","yes this is","hello test");
        articleToCompare = articleToCompare.insertId(1, articleToCompare);
        this.articleRepository.update(1, articleToCompare);
        Article testArticle = articleRepository.findById(1);
        assertThat(testArticle).isEqualTo(articleToCompare);
    }

    @Test
    void article_삭제_테스트() {
        this.articleRepository.remove(1);
        assertThrows(IllegalArgumentException.class, () -> this.articleRepository.findById(1));
    }
}
