package com.fcutility;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FCUtility {
    public static final String START_MESSAGE = "File Copy Utility Started ";
    public static final String END_MESSAGE = "File Copy Utility Ended ";
    static int numberOfLinesInFile=0;
    static int numberOfFoldersCopied=0;
    static int numberOfFilesErroredWhileCopying =0;

    String sourceFilePath;
    String destinationPath;
    File inputFIle;
    List<String> foldersInFile = new ArrayList<>();
    Map<String,String> erroredFolders = new HashMap<>();
    public static void main (String[] args){
        printPrettyMessage(10, START_MESSAGE);
        FCUtility fcUtility = new FCUtility();
        fcUtility.populateUserInputs();
        if(fcUtility.validateInputs())
            fcUtility.copyFiles();
        fcUtility.displayResults();
        printPrettyMessage(10, END_MESSAGE);
    }

    static void printPrettyMessage(int n,String message) {
        String pattern = String.join("",Collections.nCopies(n,"* "));
        System.out.println("\n"+pattern+message+pattern);
    }
    void populateUserInputs(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter absolute input file (txt format) path: ");
        this.sourceFilePath = scanner.next();
        System.out.println("Enter destination path: ");
        this.destinationPath = scanner.next();
    }
    boolean validateInputs(){
        inputFIle = new File(sourceFilePath);
        if(inputFIle.exists() && !inputFIle.isDirectory()) {
            return true;
        }
        System.out.println("ERROR :: INPUT TEXT FILE DOES NOT EXIST IN THE GIVEN PATH");
        return false;
    }
    void copyFiles(){
        try (Stream<String> stream = Files.lines(Paths.get(sourceFilePath))) {
            foldersInFile = stream
                    .collect(Collectors.toList());
            numberOfLinesInFile = foldersInFile.size();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR :: ERROR WHILE READING INPUT TEXT FILE. EXITING SYSTEM");
            System.exit(1);
        }
        if(foldersInFile.size()!=0){
           for(String folder : foldersInFile){
               try{
                   File src = new File(folder);
                   File dest = new File (destinationPath+"/"+folder.substring(folder.lastIndexOf("/")));
                   FileUtils.copyDirectory(src, dest);
                   numberOfFoldersCopied++;
               }
               catch (Exception e){
                   erroredFolders.put(folder,e.getMessage());
                   numberOfFilesErroredWhileCopying++;
                   continue;
               }
           }
        }
        else{
            System.out.println("ERROR :: INPUT FILE IS EMPTY. EXITING SYSTEM");
            System.exit(1);
        }
    }
    void displayResults(){
        System.out.println("********************************************************* Execution Details ************************************************");
        System.out.println("\t Total number of lines read from input file:\t"+numberOfLinesInFile);
        System.out.println("\t Total number of folders copied succesfully :\t"+numberOfFoldersCopied);
        System.out.println("\t Total number of folders errored while copying :\t"+numberOfFilesErroredWhileCopying);
        if(numberOfFilesErroredWhileCopying>0)
        {
            System.out.println("\t Details of folders which errored out : ");
            erroredFolders.forEach((key,value)-> System.out.println("Folder Name "+key +", Error "+value));
        }
    }
}
