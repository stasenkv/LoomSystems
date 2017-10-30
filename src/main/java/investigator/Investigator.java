package investigator;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by stasenkv on 10/29/2017.
 */
public class Investigator {

    private Map<String, List<SingleWord>> mapOfPatterns = new HashMap<>();

    public void singleWordPatternCheck(String inputFileName, String outputFileName){
        /*General idea - build from each sentence a patterns by cutting every word (and replacing the word to $$$)
        For example. Sentence -  Moshe loves his mother. The patterns:
        $$$ loves his mother
        Moshe $$$ his mother
        Moshe loves $$$ mother
        Moshe loves his $$$
        Then creating the map with Key - Pattern and Value - list of single matched words.
        For example we have 2 similar sentences:
        Moshe loves his mother
        Aaron loves his mother
        The map will be:
        $$$ loves his mother -> Moshe, Aaron
        Moshe $$$ his mother -> Moshe
        Moshe loves $$$ mother -> Moshe
        Moshe loves his $$$ -> Moshe
        Aaron $$$ his mother -> Aaron
        Aaron loves $$$ mother -> Aaron
        Aaron loves his $$$ -> Aaron*/

        //read from input file line by line
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            String originalLine;
            while((originalLine = br.readLine()) != null) {
                //remove not single spaces, tabs: 01-01-2012    19:45:00  Naomi  is ... ->01-01-2012 19:45:00 Naomi is ...
                originalLine = originalLine.replaceAll("\\s+"," ");
                lineProcessing(originalLine);
            }
        } catch (IOException e){
            System.out.println("Exception while reading the file " + e);
        }
        saveToFile(outputFileName);
    }

    private void lineProcessing(String originalLine) {
        int indexOfNextSpace = 0;
        String line, keyPattern, singleWord;

        //remove date and time: 01-01-2012 19:45:00 Naomi is ... ->Naomi is ...
        int indexOfSecondSpace = originalLine.indexOf(" ", originalLine.indexOf(" ") + 1);
        line = originalLine.substring(indexOfSecondSpace + 1, originalLine.length());

        //build the map
        do {
            //at the beginning of sentence
            if (indexOfNextSpace == 0) {
                indexOfNextSpace = line.indexOf(" ");
                keyPattern = "$$$" + line.substring(indexOfNextSpace, line.length());
                singleWord = line.substring(0, indexOfNextSpace);
            } else {
                //at the end of sentence
                int indexOfCurrentSpace = indexOfNextSpace;
                indexOfNextSpace = line.indexOf(" ", indexOfNextSpace + 1);
                if (indexOfNextSpace == -1){
                    keyPattern = line.substring(0, indexOfCurrentSpace) + " $$$";
                    singleWord = line.substring(indexOfCurrentSpace + 1, line.length());
                } else {
                    //in the middle of sentence
                    keyPattern = line.substring(0,indexOfCurrentSpace) + " $$$" + line.substring(indexOfNextSpace, line.length());
                    singleWord = line.substring(indexOfCurrentSpace + 1, indexOfNextSpace);
                }
            }
            SingleWord word = new SingleWord(singleWord, convertStringToDate(originalLine.substring(0,indexOfSecondSpace)));
            //if map contains a pattern - add single word to the list, if not - add pattern and word to map
            if (mapOfPatterns.containsKey(keyPattern)){
                List<SingleWord> listSingleWords = mapOfPatterns.get(keyPattern);
                if (listSingleWords != null){
                    listSingleWords.add(word);
                }
            } else {
                List<SingleWord> words = new ArrayList<>();
                words.add(word);
                mapOfPatterns.put(keyPattern, words);
            }
        } while (indexOfNextSpace != -1);
    }

    private static Date convertStringToDate(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            System.out.println("Error in converting " + dateInString + " string to date: " + e + ". Using current date.");
            return new Date();
        }
        return date;
    }

    private static String convertDateToString(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateInString = formatter.format(date);
        return dateInString;
    }

    private void saveToFile(String outputFileName) {
        StringBuilder sb = new StringBuilder();
        //save to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName))) {
            for (Map.Entry<String, List<SingleWord>> entry: mapOfPatterns.entrySet()) {
                List<SingleWord> words = entry.getValue();
                if (words.size() > 1) {
                    sb.setLength(0);
                    for (SingleWord word : entry.getValue()) {
                        bw.write(convertDateToString(word.getDate()) + " " + entry.getKey().replace("$$$", word.getWord()));
                        bw.newLine();
                        if (sb.length() != 0){
                            sb.append(",");
                        }
                        sb.append(" " + word.getWord());
                    }
                    bw.write("The changing word was:" + sb);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Exception while writing to file " + e);
        }
    }
}
