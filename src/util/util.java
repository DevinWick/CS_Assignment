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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
            //compare page data and return result url
            return comparePageDataWithLocalPages(page);
            
            
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
        String prep="an be or were a as the was is are and of with at from into during including until against among throughout despite "
                + "towards upon concerning to in for on by about like through over before between"
                + " after since without under within along following across behind beyond plus except"
                + " but up out around down off above near";
        
        String other="^ /";
        prep.concat(other);
        String[] prepAr=prep.split(" ");
        for(String s:prepAr){
            if(word.equals(s)){
                state=true;
                break;
            }
        }
        return state;
    }

    public static String comparePageDataWithLocalPages(Page page) {
        Hashtable<String, Double> sPageTFTable = page.getTfTable();
        Hashtable<String,Page> webCacheTable  = WebPageCacheTable.getInstance();
        
        HashSet<String> contatinPageSet = new HashSet<String>();
        String[] keywords=getKeyWords(sPageTFTable);
        Map<String,Integer> keywordCount=new HashMap<String, Integer>();
        
        //iterating over webcache local pages db
        Enumeration<String> keys = webCacheTable.keys();
        while(keys.hasMoreElements()){
            String key=keys.nextElement();
            Hashtable<String, Double> ptf_idfTable = webCacheTable.get(key).getTf_idfTable();
            int noOfKeyWordsPresent=0;
            
            
            for(String s:keywords){
                if(ptf_idfTable.get(s)!=null){
                    noOfKeyWordsPresent++;
                }
            }
            keywordCount.put(key, noOfKeyWordsPresent);
        }
        
        ArrayList<Map.Entry<String,Integer>> keywordcountList=new ArrayList<>();
        keywordcountList.addAll(keywordCount.entrySet());
        Collections.sort(keywordcountList,new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        
        
        return keywordcountList.get(0).getKey();
        
    }

    //retreiving maximum 50 keywords with highest tf
    private static String[] getKeyWords(Hashtable<String, Double> sPageTFTable) {
        Set<Map.Entry<String, Double>> entrySet = sPageTFTable.entrySet();
        ArrayList<Map.Entry<String, Double>> wl=new ArrayList<>();
        ArrayList<String> keyList=new ArrayList<>();
        String[] keylistAr=new String[50];
        wl.addAll(entrySet);
        System.out.println("wl: "+wl);
        
        Collections.sort(wl,new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        
        for (int i = 0; i < wl.size(); i++) {
            keyList.add(wl.get(i).getKey());
            if(i==49){
                break;
            }
        }
        return keyList.toArray(keylistAr);
    }
}
