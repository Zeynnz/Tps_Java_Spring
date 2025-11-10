package org.coiffet.tp1.Article;

import org.coiffet.tp1.Opinion.OpinionRep;
import org.coiffet.tp1.Opinion.OpinionType;
import org.coiffet.tp1.User.User;
import org.coiffet.tp1.User.UserRep;
import org.coiffet.tp1.User.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/articles")
public class ArticleController {

    @Autowired
    private ArticleRep articleRep;

    @Autowired
    private UserRep userRep;

    @Autowired
    private OpinionRep opinionRep;

    public record ArticlePublicDTO(String auteur, String contenu) {
        public ArticlePublicDTO(Article article) {
            this(article.getAuthor().getName(), article.getContenu());
        }
    }

    public record ArticleModeratorDTO(
            String auteur, String contenu, List<String> usersLiked,
            long totalLikes, List<String> usersDisliked, long totalDislikes
    ) {
        public ArticleModeratorDTO(Article article) {
            this(
                    article.getAuthor().getName(),
                    article.getContenu(),
                    article.getOpinions().stream()
                            .filter(o -> o.getType() == OpinionType.LIKE)
                            .map(o -> o.getUser().getName())
                            .toList(),
                    article.getOpinions().stream()
                            .filter(o -> o.getType() == OpinionType.LIKE)
                            .count(),
                    article.getOpinions().stream()
                            .filter(o -> o.getType() == OpinionType.DISLIKE)
                            .map(o -> o.getUser().getName())
                            .toList(),
                    article.getOpinions().stream()
                            .filter(o -> o.getType() == OpinionType.DISLIKE)
                            .count()
            );
        }

    }

    @PostMapping(path="/add")
    public String addArticle(@RequestParam String contenu, @RequestParam String username) {
        User user = userRep.findByName(username);
        if(user == null) return "Utilisateur introuvable";
        if(user.getRole() != Role.PUBLISHER) return "Permission refusée : seul un publisher peut ajouter un article";

        Article article = new Article();
        article.setContenu(contenu);
        article.setAuthor(user);
        articleRep.save(article);
        return "Article ajouté avec succès";
    }

    @GetMapping(path="/all")
    public List<?> getAllArticles(@RequestParam(required = false) String username) {
        User user = (username != null) ? userRep.findByName(username) : null;

        if(user == null) {
            return ((List<Article>) articleRep.findAll()).stream()
                    .map(ArticlePublicDTO::new)
                    .toList();
        }

        if(user.getRole() == Role.MODERATOR) {
            return ((List<Article>) articleRep.findAll()).stream()
                    .map(ArticleModeratorDTO::new)
                    .toList();
        }

        if(user.getRole() == Role.PUBLISHER) {
            return ((List<Article>) articleRep.findAll()).stream()
                    .map(article -> {
                        if(article.getAuthor().getId() == user.getId()) {
                            return new ArticleModeratorDTO(article); // ses propres articles
                        } else {
                            return new ArticlePublicDTO(article); // articles des autres
                        }
                    }).toList();
        }

        return ((List<Article>) articleRep.findAll()).stream()
                .map(ArticlePublicDTO::new)
                .toList();
    }

    @GetMapping(path="/one")
    public Object getArticleById(@RequestParam int id, @RequestParam(required = false) String username) {
        Article article = articleRep.getArticlesById(id);
        if(article == null) return "Article introuvable";

        User user = (username != null) ? userRep.findByName(username) : null;

        if(user == null) return new ArticlePublicDTO(article);

        if(user.getRole() == Role.MODERATOR) return new ArticleModeratorDTO(article);

        if(user.getRole() == Role.PUBLISHER) {
            if(article.getAuthor().getId() == user.getId()) return new ArticleModeratorDTO(article);
            else return new ArticlePublicDTO(article);
        }

        return new ArticlePublicDTO(article);
    }

    @PatchMapping(path="/update")
    public String updateArticle(@RequestParam int id, @RequestParam String contenu, @RequestParam String username) {
        Article article = articleRep.getArticlesById(id);
        if(article == null) return "Article introuvable";

        User user = userRep.findByName(username);
        if(user == null) return "Utilisateur introuvable";

        if(user.getRole() != Role.PUBLISHER || article.getAuthor().getId() != user.getId()) {
            return "Permission refusée : seul l'auteur publisher peut modifier cet article";
        }

        article.setContenu(contenu);
        articleRep.save(article);
        return "Article mis à jour";
    }

    @DeleteMapping(path="/delete")
    public String deleteArticle(@RequestParam int id, @RequestParam String username) {
        Article article = articleRep.getArticlesById(id);
        if(article == null) return "Article introuvable";

        User user = userRep.findByName(username);
        if(user == null) return "Utilisateur introuvable";

        if(user.getRole() == Role.MODERATOR || (user.getRole() == Role.PUBLISHER && article.getAuthor().getId() == user.getId())) {
            opinionRep.deleteByArticle(article);
            article.getAuthor().deleteArticleby(article);
            articleRep.delete(article);
            return "Article supprimé";
        } else {
            return "Permission refusée : suppression non autorisée";
        }
    }
}
