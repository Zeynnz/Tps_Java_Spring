package org.coiffet.tp1.Opinion;

import org.coiffet.tp1.Article.Article;
import org.coiffet.tp1.Article.ArticleRep;
import org.coiffet.tp1.User.User;
import org.coiffet.tp1.User.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/opinions")
public class OpinionController {

    @Autowired
    private OpinionRep opinionRep;

    @Autowired
    private UserRep userRep;

    @Autowired
    private ArticleRep articleRep;

    @PostMapping(path = "/add")
    public @ResponseBody String addOpinion(@RequestParam String message,
                                           @RequestParam int userId,
                                           @RequestParam int articleId) {
        User user = userRep.findById(userId).orElse(null);
        Article article = articleRep.findById(articleId).orElse(null);

        if (user == null || article == null) {
            return "Erreur : utilisateur ou article introuvable.";
        }

        Opinion opinion = new Opinion();
        opinion.setMessage(message);
        opinion.setUser(user);
        opinion.setArticle(article);

        opinionRep.save(opinion);

        return "Opinion enregistrée avec succès.";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Opinion> getAllOpinions() {
        return opinionRep.findAll();
    }
}
