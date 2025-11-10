package org.coiffet.tp1.Article;

import java.time.format.DateTimeFormatter;

public class ArticlePublicDTO {
    private final String auteur;
    private final String contenu;
    private String datePublication;

    public ArticlePublicDTO(Article article) {
        if (article != null) {
            this.auteur = article.getAuthor() != null ? article.getAuthor().getName() : "Auteur inconnu";
            this.contenu = article.getContenu() != null ? article.getContenu() : "";

            if (articleR5A05WebSecurityConfigurer .getDatePublication() != null) {
                // Format date as string, e.g., "10/11/2025"
                this.datePublication = article.getDatePublication()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
                this.datePublication = "Date inconnue";
            }
        } else {
            this.auteur = "Auteur inconnu";
            this.contenu = "";
        }
    }

    // Getters
    public String getAuteur() {
        return auteur;
    }

    public String getContenu() {
        return contenu;
    }

    public String getDatePublication() {
        return datePublication;
    }
}
