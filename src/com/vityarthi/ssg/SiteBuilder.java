package com.vityarthi.ssg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SiteBuilder {
    private final Path inputDir;
    private final Path outputDir;
    private final List<Article> articles = new ArrayList<>();

    public SiteBuilder(String inPath, String outPath) {
        this.inputDir = Paths.get(inPath);
        this.outputDir = Paths.get(outPath);
    }

    public void build() throws IOException {
        articles.clear();
        Files.createDirectories(inputDir);
        Files.createDirectories(outputDir);

        try (Stream<Path> stream = Files.list(inputDir)) {
            List<Path> mdFiles = stream.filter(p -> p.toString().endsWith(".md")).collect(Collectors.toList());
            if (mdFiles.isEmpty()) {
                createSamplePost();
                mdFiles.add(inputDir.resolve("sample.md"));
            }

            for (Path path : mdFiles) {
                String content = Files.readString(path);
                String fileName = path.getFileName().toString().replace(".md", ".html");
                String title = path.getFileName().toString().replace(".md", "").replace("_", " ");
                
                String bodyHtml = MarkdownParser.parseToHtml(content);
                String fullHtml = wrapInTemplate(title, bodyHtml);
                Files.writeString(outputDir.resolve(fileName), fullHtml);

                articles.add(new Article(title, LocalDate.now(), content, fileName));
            }
        }
        generateIndexPage();
    }

    private void generateIndexPage() throws IOException {
        Collections.sort(articles);
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Course Blog & Articles</h1>\n<ul>\n");
        for (Article art : articles) {
            sb.append("  <li><a href=\"").append(art.getHtmlFilename()).append("\">")
              .append(art.getTitle()).append("</a> (").append(art.getDate()).append(")</li>\n");
        }
        sb.append("</ul>\n");
        Files.writeString(outputDir.resolve("index.html"), wrapInTemplate("Home - My Articles", sb.toString()));
    }

    private String wrapInTemplate(String title, String body) {
        return "<!DOCTYPE html>\n<html><head><meta charset=\"UTF-8\"><title>" + title + "</title>\n" +
               "<style>body{font-family:Arial,sans-serif;max-width:700px;margin:40px auto;line-height:1.6;padding:0 20px;}" +
               "code{background:#f4f4f4;padding:2px 5px;}blockquote{border-left:3px solid #333;margin:0;padding-left:10px;color:#555;}</style>" +
               "</head><body><nav><a href=\"index.html\">Home</a></nav><hr/>" + body + "</body></html>";
    }

    private void createSamplePost() throws IOException {
        String sampleMd = "# Welcome to My Blog\nThis page was automatically generated using **Java**.\n\n* Fast\n* Lightweight\n* Fully CLI-Driven\n";
        Files.writeString(inputDir.resolve("sample.md"), sampleMd);
    }

    public List<Article> search(String keyword) {
        return articles.stream()
                .filter(a -> a.getContent().toLowerCase().contains(keyword.toLowerCase()) || 
                             a.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public int getArticleCount() { return articles.size(); }
}