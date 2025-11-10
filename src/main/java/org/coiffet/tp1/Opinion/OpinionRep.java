package org.coiffet.tp1.Opinion;

import org.coiffet.tp1.Article.Article;
import org.springframework.data.repository.CrudRepository;

public interface OpinionRep extends CrudRepository<Opinion, Long> {

    Opinion getOpinionById(long id);

    void deleteByArticle(Article article);

}
