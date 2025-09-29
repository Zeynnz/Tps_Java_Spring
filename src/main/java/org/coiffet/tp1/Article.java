package org.coiffet.tp1;

import jakarta.persistence.*;


@Entity
public class Article {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "contenu")
    private String contenu;

    private String author;

    @ManyToOne
    private User user;

    public Article() {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public long getId() {
        return id;
    }


}
