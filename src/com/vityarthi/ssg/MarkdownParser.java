package com.vityarthi.ssg;

public class MarkdownParser {

    public static String parseToHtml(String markdownText) {
        StringBuilder html = new StringBuilder();
        String[] lines = markdownText.split("\\r?\\n");
        boolean inList = false;

        for (String rawLine : lines) {
            String line = rawLine.trim();

            if (line.startsWith("* ") || line.startsWith("- ")) {
                if (!inList) {
                    html.append("<ul>\n");
                    inList = true;
                }
                String content = line.substring(2);
                html.append("  <li>").append(inlineFormat(content)).append("</li>\n");
                continue;
            } else if (inList) {
                html.append("</ul>\n");
                inList = false;
            }

            if (line.startsWith("### ")) {
                html.append("<h3>").append(inlineFormat(line.substring(4))).append("</h3>\n");
            } else if (line.startsWith("## ")) {
                html.append("<h2>").append(inlineFormat(line.substring(3))).append("</h2>\n");
            } else if (line.startsWith("# ")) {
                html.append("<h1>").append(inlineFormat(line.substring(2))).append("</h1>\n");
            } else if (line.startsWith("> ")) {
                html.append("<blockquote>").append(inlineFormat(line.substring(2))).append("</blockquote>\n");
            } else if (!line.isEmpty()) {
                html.append("<p>").append(inlineFormat(line)).append("</p>\n");
            }
        }

        if (inList) html.append("</ul>\n");
        return html.toString();
    }

    private static String inlineFormat(String text) {
        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "<strong>$1</strong>");
        text = text.replaceAll("\\*(.*?)\\*", "<em>$1</em>");
        text = text.replaceAll("`(.*?)`", "<code>$1</code>");
        return text;
    }
}