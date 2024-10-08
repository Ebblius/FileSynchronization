package com.ebblius;

import java.util.ArrayList;
import java.util.List;

public class SyncReport {


    private final List<String> entries;

    public SyncReport() {
        this.entries = new ArrayList<>();
    }

    public synchronized void addEntry(String entry) {
        entries.add(entry);
    }

    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Sync Report Summary:\n");
        summary.append("Total actions: ").append(entries.size()).append("\n");

        int filesCopied = 0;
        int filesFailed = 0;

        for (String entry : entries) {
            if (entry.contains("File copied")) {
                filesCopied++;
            } else if (entry.contains("Failed")) {
                filesFailed++;
            }
        }

        summary.append("Files copied: ").append(filesCopied).append("\n");
        summary.append("Failures: ").append(filesFailed).append("\n");

        return summary.toString();
    }

    public List<String> getDetailedReport() {
        return entries;
    }

    public void printReport() {
        Logger.getInstance().info(getSummary());
        Logger.getInstance().info("Detailed Report:");
        for (String entry : entries) {
            Logger.getInstance().info(entry);
        }
    }


}
