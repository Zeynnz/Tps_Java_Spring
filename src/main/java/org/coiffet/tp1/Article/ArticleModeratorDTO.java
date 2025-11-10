package org.coiffet.tp1.Article;

import org.coiffet.tp1.Opinion.OpinionType;

import java.util.List;

public class ArticleModeratorDTO {

    private final String auteur;
    private final String contenu;
    private final List<String> usersLiked;
    private long totalLikes;
    private final List<String> usersDisliked;
    private long totalDislikes;

    public ArticleModeratorDTO(Article article) {
        this.auteur = article.getAuthor().getName();
        this.contenu = article.getContenu();

        // Calcul des likes
        this.usersLiked = article.getOpinions().stream()
                .filter(o -> o.getType() == OpinionType.LIKE)
                .map(o -> o.getUser().getName())
                .toList();

        // Calcul des dislikes
        this.usersDisliked = article.getOpinions().stream()
                .filter(o -> o.getType() == OpinionType.DISLIKE)
                .map(o -> o.getUser().getName())
                .toList();

    }

    public String getAuteur() {
        return auteur;
    }

    public String getContenu() {
        return contenu;
    }

    public List<String> getUsersLiked() {
        return usersLiked;
    }

    public long getTotalLikes() {
        return totalLikes;
    }

    public List<String> getUsersDisliked() {
        return usersDisliked;
    }

    public long getTotalDislikes() {
        return totalDislikes;
    }
}
