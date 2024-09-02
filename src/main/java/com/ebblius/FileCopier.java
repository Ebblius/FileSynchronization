package com.ebblius;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileCopier {

    void copyFile(File source, File destination) throws FileNotFoundException, IOException{
        
        BufferedReader bReader = new BufferedReader( new FileReader(source));
        BufferedWriter bWriter = new BufferedWriter( new FileWriter(destination));
        
        if(!source.exists())
            throw new IOException("Kaynak dosya bulunamadı." + source.getAbsolutePath());
        if(!destination.exists())
            destination.createNewFile();
        
        String line;
        while((line = bReader.readLine()) != null){
            bWriter.write(line);
        }
    }

    void copyFiles(File[] sourceFiles, File destination) throws IOException{
        
            if (!destination.isDirectory()) 
            throw new IllegalArgumentException("Hedef dosya bir differenceszin olmalıdır: " + destination.getAbsolutePath());
            
            for(File sourceFile: sourceFiles){
                if (!sourceFile.exists()) {
                    System.err.println("Kaynak dosya bulunamadı: " + sourceFile.getAbsolutePath());
                    continue;
            }
                File destFile = new File(destination, sourceFile.getName());
                copyFile(sourceFile, destFile);
        }
    }


}
