package org.coiffet.tp1;

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


}
