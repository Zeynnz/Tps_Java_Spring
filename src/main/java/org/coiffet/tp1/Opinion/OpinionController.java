package org.coiffet.tp1.Opinion;

import org.coiffet.tp1.Article.Article;
import org.coiffet.tp1.Article.ArticleRep;
import org.coiffet.tp1.User.User;
import org.coiffet.tp1.User.UserRep;
import org.coiffet.tp1.User.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/opinions")
public class OpinionController {

    @Autowired
    private OpinionRep opinionRep;

    @Autowired
    private UserRep userRep;

    @Autowired
    private ArticleRep articleRep;

    @PostMapping(path = "/add")
    public String addOpinion(
            @RequestParam String type,          // "like" ou "dislike"
            @RequestParam String username,
            @RequestParam int articleId
    ) {
        User user = userRep.findByName(username);
        if(user == null) return "Utilisateur introuvable";

        if(user.getRole() != Role.PUBLISHER) return "Permission refusée : seul un publisher peut liker/disliker";

        Article article = articleRep.getArticlesById(articleId);
        if(article == null) return "Article introuvable";

        if(article.getAuthor().getId() == user.getId()) {
            return "Impossible de liker/disliker votre propre article";
        }

        Opinion opinion = new Opinion();
        opinion.setType(type.equalsIgnoreCase("like") ? OpinionType.LIKE : OpinionType.DISLIKE);
        opinion.setUser(user);
        opinion.setArticle(article);
        opinionRep.save(opinion);

        return "Opinion enregistrée : " + type;
    }

    // --- Consulter toutes les opinions ---
    @GetMapping(path = "/all")
    public Iterable<Opinion> getAllOpinions() {
        return opinionRep.findAll();
    }

    // --- Consulter une opinion par ID ---
    @GetMapping(path="/one")
    public Opinion getOpinionById(@RequestParam int id) {
        return opinionRep.getOpinionById(id);
    }

    // --- Supprimer une opinion (optionnel) ---
    @DeleteMapping(path="/delete")
    public String deleteOpinion(@RequestParam int id, @RequestParam String username) {
        Opinion opinion = opinionRep.getOpinionById(id);
        if(opinion == null) return "Opinion introuvable";

        User user = userRep.findByName(username);
        if(user == null) return "Utilisateur introuvable";

        // Seul l'auteur du like/dislike ou un moderator peut supprimer
        if(opinion.getUser().getId() == user.getId() || user.getRole() == Role.MODERATOR) {
            opinionRep.delete(opinion);
            return "Opinion supprimée";
        } else {
            return "Permission refusée";
        }
    }
}
