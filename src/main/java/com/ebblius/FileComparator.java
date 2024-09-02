package com.ebblius;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FileComparator {

    boolean compareFiles(File file1, File file2) throws FileNotFoundException, IOException{
        
        if(file1.length() != file2.length() || file1.lastModified() != file2.lastModified())
            return false;
        
        BufferedReader bReader1 = new BufferedReader(new FileReader(file1));
        BufferedReader bReader2 = new BufferedReader(new FileReader(file2));
        
        String line1 = "", line2 = "";
        while((line1 = bReader1.readLine()) != null && ((line2 = bReader2.readLine()) != null)){
            if(line1 == null ? line2 != null : !line1.equals(line2))
                return false;
        }
        return line1 == null && line2 == null;
    }

    List<String> getDifferences(File file1, File file2) throws FileNotFoundException, IOException{
        List<String> lines1 = new LinkedList();
        List<String> lines2 = new LinkedList();
        
        BufferedReader bReader1 = new BufferedReader(new FileReader(file1));
        BufferedReader bReader2 = new BufferedReader(new FileReader(file2));
        
        String line;
        while((line = bReader1.readLine()) != null)
        lines1.add(line);
        while((line = bReader2.readLine()) != null)
        lines1.add(line);
        
        List<String> differences = new LinkedList<>(lines1);
        differences.addAll(lines2);
        List<String> commonLines = new LinkedList<>(lines2);
        commonLines.retainAll(lines2);
        differences.removeAll(commonLines);
        
        return differences;
    }
 }