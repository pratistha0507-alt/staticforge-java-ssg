package com.vityarthi.ssg;

import java.time.LocalDate;

public class Article implements Comparable<Article> {
    private final String title;
    private final LocalDate date;
    private final String content;
    private final String htmlFilename;

    public Article(String title, LocalDate date, String content, String htmlFilename) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.htmlFilename = htmlFilename;
    }

    public String getTitle() { return title; }
    public LocalDate getDate() { return date; }
    public String getContent() { return content; }
    public String getHtmlFilename() { return htmlFilename; }

    @Override
    public int compareTo(Article other) {
        return other.date.compareTo(this.date);
    }
}