/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import cs_assignment.CS_Assignment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Page;
import model.WebPageCacheTable;

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
        String[] charAr = {",", ".", "?", "!", ";", ")", ".)", "\"", "(", "'", ":"};
        source = source.toLowerCase();
        String[] wordsAr = source.split("\\s+");
        Hashtable<String, Integer> hashtable = new Hashtable<>();
        Hashtable<String, Double> idfTable = WebPageCacheTable.getInstance().getIdfTable();

        for (int i = 0; i < wordsAr.length; i++) {

            for (int j = 0; j < charAr.length; j++) {
                wordsAr[i] = wordsAr[i].replace(charAr[j], "");
            }

            //removing prepostion
            if(isPreposition(wordsAr[i])){
                continue;
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

    public static String compareWebPages(String st) throws IOException{
        BufferedReader br=null;
        Page page=new Page();
        try {
            URL url = new URL(st);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            CS_Assignment.calculatePageData(sb,page);
            //compare page data
            comparePageDataWithLocalPages(page);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            if (br != null) {
                br.close();
            }
        }
        
        //returning selected web page
        return null;
    }
    
    
    //returns true if word is a preposition
    public static boolean isPreposition(String word){
        boolean state=false;
        String prep="and of with at from into during including until against among throughout despite "
                + "towards upon concerning to in for on by about like through over before between"
                + " after since without under within along following across behind beyond plus except"
                + " but up out around down off above near";
        String[] prepAr=prep.split(" ");
        for(String s:prepAr){
            if(word.equals(s)){
                state=true;
                break;
            }
        }
        return state;
    }

    public static void comparePageDataWithLocalPages(Page page) {
        Hashtable<String, Double> sPageTFTable = page.getTfTable();
        Hashtable<String,Page> webCacheTable  = WebPageCacheTable.getInstance();
        
        HashSet<String> contatinPageSet = new HashSet<String>();
        
        //iterating over webcache local pages db
        Enumeration<String> keys = webCacheTable.keys();
        while(keys.hasMoreElements()){
            String key=keys.nextElement();
            Page webpage = webCacheTable.get(key);
            
        }
        
    }
}
