/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.WebPageCacheTable;

/**
 *
 * @author Devin
 */
public class util {

    public static int getWordCount(String s, String source) {
        int count = 0;
        s = s.toLowerCase();
        source = source.toLowerCase();
        Pattern pattern = Pattern.compile("\\b" + s + "\\b");
        Matcher m = pattern.matcher(source);

        while (m.find()) {
            count++;
        }

        return count;
    }

    public static int getTotalWords(String source) {
        return source.split("\\s+").length;
    }

    public static void main(String[] args) {
        String source = "cat cat, hi cater catfish my cat and your cat.";

        System.out.println(getWordCountsTable(source));
    }

    public static Hashtable<String, Integer> getWordCountsTable(String source) {
        String[] charAr = {",", ".", "?", "!", ";", ")", ".)", "\"","(","'",":"};
        source = source.toLowerCase();
        String[] wordsAr = source.split("\\s+");
        Hashtable<String, Integer> hashtable = new Hashtable<>();
        Hashtable<String, Double> idfTable = WebPageCacheTable.getInstance().getIdfTable();
        
        for (int i = 0; i < wordsAr.length; i++) {

            for (int j = 0; j < charAr.length; j++) {
                wordsAr[i] = wordsAr[i].replace(charAr[j], "");
            }

            //creating tf table
            if (hashtable.containsKey(wordsAr[i])) {
                hashtable.put(wordsAr[i], hashtable.get(wordsAr[i]) + 1);
            } else {
                hashtable.put(wordsAr[i], 1);
            }
            
            //adding words to idf table
            idfTable.put(wordsAr[i], -1.0);
            
            
        }
        return hashtable;
    }
}
