package org.coiffet.tp1.Article;

import org.coiffet.tp1.User.User;
import org.coiffet.tp1.User.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/articles")
public class ArticleController {

    @Autowired
    private ArticleRep articleRep;

    @Autowired
    private UserRep userRep;

    @PostMapping(path="/add")
    public @ResponseBody String addArticle(@RequestParam String contenu, @RequestParam String author) {

        // Utilisateur correspondant
        User user = userRep.findByName(author);

        if (user == null) {
            return "Erreur : utilisateur " + author + " introuvable";
        }

        Article article = new Article();
        article.setContenu(contenu);
        article.setAuthor(user);
        articleRep.save(article);
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Article> getAllArticles() {
        return articleRep.findAll();
    }

    @GetMapping(path="/one")
    public @ResponseBody Article getArticleById(@RequestParam int id) {
        return articleRep.getArticlesById(id);
    }

    @PatchMapping
    public @ResponseBody String updateArticle(@RequestParam int id, @RequestParam String contenu, @RequestParam String author) {
        Article article = articleRep.getArticlesById(id);
        User user = userRep.findByName(author);

        if (user == null) {
            return "Erreur : utilisateur " + author + " introuvable";
        }
        if (contenu != null) {
            article.setContenu(contenu);
        }
        if(author != null && !author.equals(article.getAuthor().getName())) {
            article.setAuthor(user);
        }
        articleRep.save(article);
        return "Updated";
    }


}
