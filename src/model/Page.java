/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import java.util.Hashtable;

/**
 *
 * @author Devin
 */
public class Page {
   private String localFileName;
   private Date lastAccessedDate;
   private int totalTerms;
   private Hashtable<String,Integer> wordCountTable;

    public Date getLastAccessedDate() {
        return lastAccessedDate;
    }

    public void setLastAccessedDate(Date lastAccessedDate) {
        this.lastAccessedDate = lastAccessedDate;
    }

    public int getTotalTerms() {
        return totalTerms;
    }

    public void setTotalTerms(int totalTerms) {
        this.totalTerms = totalTerms;
    }

    public Hashtable<String, Integer> getWordCountTable() {
        return wordCountTable;
    }

    public void setWordCountTable(Hashtable<String, Integer> wordCountTable) {
        this.wordCountTable = wordCountTable;
    }
   

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    
   
}
