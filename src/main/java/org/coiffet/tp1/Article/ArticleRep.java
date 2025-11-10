package org.coiffet.tp1.Article;
import org.coiffet.tp1.User.User;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRep extends CrudRepository<Article, Integer> {


    Article getArticlesById(long id);

    Iterable<Article> getArticlesByAuthor(User author);
}
