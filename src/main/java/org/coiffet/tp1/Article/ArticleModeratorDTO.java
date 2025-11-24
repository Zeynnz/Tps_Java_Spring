package org.coiffet.tp1.Article;

import org.coiffet.tp1.Opinion.Opinion;
import org.coiffet.tp1.Opinion.OpinionType;
import org.coiffet.tp1.User.User;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ArticleModeratorDTO {

    private final String auteur;
    private final String contenu;
    private final String datePublication;
    private final List<User> usersLiked;
    private final List<User> usersDisliked;
    private final long totalLikes;
    private final long totalDislikes;

    public ArticleModeratorDTO(Article article) {
        this.auteur = article.getAuthor().getName();
        this.contenu = article.getContenu();

        // Date de publication
        if (article.getDatePublication() != null) {
            this.datePublication = article.getDatePublication()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            this.datePublication = "Date inconnue";
        }

        // Calcul des likes
        this.usersLiked = article.getOpinions().stream()
                .filter(o -> o.getType() == OpinionType.LIKE)
                .map(Opinion::getUser)
                .toList();

        // Calcul des dislikes
        this.usersDisliked = article.getOpinions().stream()
                .filter(o -> o.getType() == OpinionType.DISLIKE)
                .map(Opinion::getUser)
                .toList();

        this.totalLikes = usersLiked.size();
        this.totalDislikes = usersDisliked.size();
    }

    public String getAuteur() {
        return auteur;
    }

    public String getContenu() {
        return contenu;
    }

    public String getDatePublication() {
        return datePublication;
    }

    public List<User> getUsersLiked() {
        return usersLiked;
    }

    public long getTotalLikes() {
        return totalLikes;
    }

    public List<User> getUsersDisliked() {
        return usersDisliked;
    }

    public long getTotalDislikes() {
        return totalDislikes;
    }
}
