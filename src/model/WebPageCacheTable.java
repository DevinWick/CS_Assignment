/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Hashtable;


//used to store cache details of web pages
public class WebPageCacheTable extends Hashtable<String, Page>{
    
    private static WebPageCacheTable instance;
    
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
    
    public int getTotalContainingDocuments(String key){
        int count=0;
        for(int i=0;i<size();i++){
            if(get(i).getWordCountTable().contains(key)){
                count++;
            }
        }
        return count;
    }
}
