package org.coiffet.tp1.Article;

import org.coiffet.tp1.Opinion.OpinionRep;
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

    @Autowired
    private OpinionRep opinionRep;

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

    //La publication, la consultation, la modification et la suppression des articles de blogs. Un article est caractérisé, a minima, par sa date de publication, son auteur et son contenu

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Article> getAllArticles() {
        return articleRep.findAll();
    }

    @GetMapping(path="/one")
    public @ResponseBody Article getArticleById(@RequestParam int id) {
        return articleRep.getArticlesById(id);
    }

    @GetMapping(path="/byAuthor")
    public @ResponseBody Iterable<Article> getArticlesByAuthor(@RequestParam User author) {
        return articleRep.getArticlesByAuthor(author);
    }

    @PatchMapping(path="/update")
    public @ResponseBody String updateArticle(@RequestParam int id, String contenu, String author) {
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

    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteArticle(@RequestParam int id) {
        Article article = articleRep.getArticlesById(id);
        User user = userRep.findByName(article.getAuthor().getName());

        if (user == null) {
            return "Erreur : utilisateur introuvable, aucun auteur trouvé avec le nom de l'auteur";
        }

        opinionRep.deleteByArticle(article);
        user.deleteArticleby(article);
        articleRep.delete(article);
        return "Deleted";
    }

}
