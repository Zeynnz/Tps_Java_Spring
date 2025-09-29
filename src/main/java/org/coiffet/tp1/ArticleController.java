package org.coiffet.tp1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/articles")
public class ArticleController {

    @Autowired
    private ArticleRep articleRep;

    @PostMapping(path="/add")
    public @ResponseBody String addArticle(@RequestParam String contenu, @RequestParam String author) {
        Article article = new Article();
        article.setContenu(contenu);
        article.setAuthor(author);
        articleRep.save(article);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Article> getAllArticles() {
        return articleRep.findAll();
    }


}
