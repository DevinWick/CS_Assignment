/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Devin
 */
public class Page {

    private String localFileName;
    private Date lastAccessedDate;
    private int totalTerms;
    private Hashtable<String, Integer> wordCountTable;
    private Hashtable<String, Double> tfTable;
    private Hashtable<String, Double> tf_idfTable;

    public Hashtable<String, Double> getTf_idfTable() {
        if(tf_idfTable!=null){
            tf_idfTable=new Hashtable<>();
        }
        return tf_idfTable;
    }

    public void setTf_idfTable(Hashtable<String, Double> tf_idfTable) {
        this.tf_idfTable = tf_idfTable;
    }

    public Hashtable<String, Double> getTfTable() {
        if (tfTable == null) {
            tfTable = new Hashtable<>();
        }
        Enumeration<String> keys = wordCountTable.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            double count = wordCountTable.get(key);
            double tf = count / totalTerms;
            DecimalFormat df = new DecimalFormat("#.######");
            df.setRoundingMode(RoundingMode.CEILING);
            tfTable.put(key, Double.parseDouble(df.format(tf)));
        }
        return tfTable;
    }

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
