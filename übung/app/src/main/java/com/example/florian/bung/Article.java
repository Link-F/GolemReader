package com.example.florian.bung;

/**
 * Created by florian on 17.05.16.
 */
public class Article {
    private int article_id;
    private String headline;
    private String abstract_text;
    private String url;
    private String date;
    private String image_url[];
    private int image_width[];
    private int image_height[];

    public Article(int id,
                   String headline,
                   String abstract_text,
                   String url,
                   String date,
                   String image_url[],
                   int image_height[],
                   int image_width[]){
        article_id = id;
        this.headline = headline;
        this.abstract_text = abstract_text;
        this.url = url;
        this.date = date;
        this.image_url = image_url;
        this.image_height = image_height;
        this.image_width = image_width;

    }
}
