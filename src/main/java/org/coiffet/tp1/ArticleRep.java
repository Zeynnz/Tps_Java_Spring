package org.coiffet.tp1;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRep extends CrudRepository<Article, Integer> {


    Article getArticlesById(long id);
}
