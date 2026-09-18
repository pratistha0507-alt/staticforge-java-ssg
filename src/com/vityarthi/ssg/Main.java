package com.vityarthi.ssg;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SiteBuilder builder = new SiteBuilder("posts", "site");
        Scanner sc = new Scanner(System.in);
        System.out.println("=================================================");
        System.out.println("   StaticForge: CLI Static Site Generator        ");
        System.out.println("=================================================");

        while (true) {
            System.out.println("\nSelect an action:");
            System.out.println("1. Build Site (Parse Markdown to HTML)");
            System.out.println("2. Search Articles by Keyword");
            System.out.println("3. Show Site Statistics");
            System.out.println("4. Exit");
            System.out.print("Enter choice (1-4): ");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        builder.build();
                        System.out.println("[SUCCESS] Generated static website in the 'site/' directory!");
                        break;
                    case "2":
                        System.out.print("Enter search term: ");
                        String query = sc.nextLine();
                        List<Article> results = builder.search(query);
                        System.out.println("Found " + results.size() + " matches:");
                        results.forEach(r -> System.out.println(" - " + r.getTitle() + " (" + r.getHtmlFilename() + ")"));
                        break;
                    case "3":
                        System.out.println("Total articles indexed: " + builder.getArticleCount());
                        break;
                    case "4":
                        System.out.println("Exiting StaticForge. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Please choose between 1 and 4.");
                }
            } catch (Exception e) {
                System.out.println("[ERROR] An issue occurred: " + e.getMessage());
            }
        }
    }
}