package com.vityarthi.ssg;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // initialize the core site generator engine with folder paths
        SiteBuilder engine = new SiteBuilder("posts", "site");
        Scanner input = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("  StaticForge: Terminal Static Site Generator     ");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            System.out.println("\nDashboard Options:");
            System.out.println("1. Generate Static Site from Markdown");
            System.out.println("2. Search Articles by Keyword");
            System.out.println("3. Show Total Articles Count");
            System.out.println("4. Exit Program");
            System.out.print("Select an option (1-4): ");

            String option = input.nextLine().trim();
            try {
                switch (option) {
                    case "1":
                        engine.build();
                        System.out.println("[OK] Website compiled successfully in '/site' folder!");
                        break;
                    case "2":
                        System.out.print("Enter search keyword: ");
                        String query = input.nextLine();
                        List<Article> matches = engine.search(query);
                        System.out.println("Found " + matches.size() + " matching document(s):");
                        matches.forEach(item -> System.out.println(" -> " + item.getTitle() + " (" + item.getHtmlFilename() + ")"));
                        break;
                    case "3":
                        System.out.println("Current indexed article count: " + engine.getArticleCount());
                        break;
                    case "4":
                        System.out.println("Shutting down StaticForge. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Notice: Please enter a valid number (1 to 4).");
                }
            } catch (Exception err) {
                System.out.println("[Exception Caught] Error details: " + err.getMessage());
            }
        }
    }
}
