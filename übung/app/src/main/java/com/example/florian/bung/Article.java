package com.example.florian.bung;

/**
 * Created by florian on 17.05.16.
 */
public class Article {
    public int article_id = 0;
    public String headline = new String();
    public String subheadline = new String();
    public String abstract_text = new String();
    public String url = new String();
    public String date = new String();
    //public String image_url[];
    //public int image_width[];
    //public int image_height[];
    public String image_url;
    public int image_width;
    public int image_height;
    public int pages;

    public Article(int id,
                   String headline,
                   String abstract_text,
                   String url,
                   String date,
                   String image_url,
                   int image_height,
                   int image_width){
        this.article_id = id;
        this.headline = headline;
        this.abstract_text = abstract_text;
        this.url = url;
        this.date = date;
        this.image_url = image_url;
        this.image_height = image_height;
        this.image_width = image_width;
    }

    public Article(int id,
                   String headline,
                   String subheadline,
                   String abstract_text,
                   String url,
                   String date,
                   String image_url,
                   int image_height,
                   int image_width,
                    int pages) {
        this.article_id = id;
        this.headline = headline;
        this.subheadline = subheadline;
        this.abstract_text = abstract_text;
        this.url = url;
        this.date = date;
        this.image_url = image_url;
        this.image_height = image_height;
        this.image_width = image_width;
        this.pages = pages;
    }
}
