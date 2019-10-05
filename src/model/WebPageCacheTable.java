/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Enumeration;
import java.util.Hashtable;


//used to store cache details of web pages
public class WebPageCacheTable extends Hashtable<String, Page>{
    
    private static WebPageCacheTable instance;
    
    private Hashtable<String,Double> idfTable;

    public Hashtable<String, Double> getIdfTable() {
        if(idfTable==null){
            idfTable=new Hashtable<>();
        }
        return idfTable;
    }
    
    private WebPageCacheTable(){
        
    }

    public static WebPageCacheTable getInstance() {
        if(instance==null){
            instance=new WebPageCacheTable();
        }
        return instance;
    }

    public int getTotalDocuments(){
        return size();
    }
    
    public int getTotalContainingDocuments(String word){
        int count=0;
        Enumeration<String> keys = instance.keys();
        while(keys.hasMoreElements()){
            String key=keys.nextElement();
            if(instance.get(key).getWordCountTable().containsKey(word)){
                count++;
            }
        }
        
        return count;
    }
}
