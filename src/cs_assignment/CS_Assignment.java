/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs_assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Page;
import model.WebPageCacheTable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ui.MainFrame;
import util.Constants;
import util.util;

public class CS_Assignment {

    /**
     * @param args the command line arguments
     */
    static MainFrame mf;
    private static int webpageCount;
    private static int current;
    private static WebPageCacheTable webPageCacheTable = WebPageCacheTable.getInstance();

    public static void main(String[] args) {
        mf = new MainFrame();
        mf.setVisible(true);
        //mf.getjProgressBar1().setIndeterminate(true);
        mf.getjProgressBar1().setStringPainted(true);
        mf.getjProgressBar1().setMinimum(0);

        System.out.println(webPageCacheTable.equals(WebPageCacheTable.getInstance()));
        readControlFile();
    }

    private static void readControlFile() {
        mf.getjProgressBar1().setString("downloading files ...");
        try {
            File f = new File(CS_Assignment.class.getClass().getResource("/files/websites.txt").getFile());

            webpageCount = getPageCount(f);
            mf.getjProgressBar1().setMaximum(webpageCount);
            mf.getjProgressBar1().setValue(0);

            //reading page urls from control file;
            BufferedReader br = new BufferedReader(new FileReader(f));
            String st;
            while ((st = br.readLine()) != null) {
                //reading web pages one by one
                String str = st;
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("thread running-" + this.toString());
                            readWebPage(str);
                        } catch (IOException ex) {
                            Logger.getLogger(CS_Assignment.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                t.run();
            }

            mf.getjProgressBar1().setString("Loading Data ...");
            calculateIDFTable();
            calculateTF_IDFTable();
            loadDatatoGUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //reads web page using url string
    private static final void readWebPage(String st) throws IOException {
        Page page = new Page();
        BufferedReader br = null;
        try {
            URL url = new URL(st);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if (line.contains("This page was last edited on")) {
                    page.setLastAccessedDate(calculateDate(line));
                }
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            calculatePageData(sb, page);
            createFile(sb, url, page);

            //updating cache
            webPageCacheTable.put(st, page);
            System.out.println(webPageCacheTable);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

            if (br != null) {
                br.close();
            }
        }
    }

    //create local file after reading url
    private static void createFile(StringBuilder sb, URL url, Page page) throws Exception {
        File dir = new File("web_pages");
        if (!dir.exists()) {
            dir.mkdir();
        }
        String localfileName = generateFileName(url);
        page.setLocalFileName(localfileName);

        File file = new File(dir, localfileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(sb.toString());
            bw.flush();

            mf.getjProgressBar1().setValue(++current);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //generating a local file name string from url
    private static String generateFileName(URL url) {
        String[] filenameAr = url.toString().split("//")[1].split("/");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < filenameAr.length; i++) {
            builder.append(filenameAr[i]);
            if (i == (filenameAr.length - 1)) {
                builder.append(".html");
            } else {
                builder.append("_");
            }
        }
        //System.out.println(builder);
        return builder.toString();
    }

    //calculating last accessed date
    private static Date calculateDate(String line) {

        Document doc = Jsoup.parse(line);
        String[] strAr = doc.body().text().split(" ");
        String date = strAr[6].trim();
        String month = strAr[7].trim();
        String year = strAr[8].trim().split(",")[0];
        String time = strAr[10].trim();

        String datestr = date + " " + month + " " + year + " " + time;

        try {
            return new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN)
                    .parse(datestr);
        } catch (ParseException ex) {
            Logger.getLogger(CS_Assignment.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    private static void calculatePageData(StringBuilder sb, Page page) {
        //calculate tf-idf and other page data for each and every page
        //getting text neglecting html tags
        Document doc = Jsoup.parse(sb.toString());
        String text = doc.body().text();

        page.setTotalTerms(util.getTotalWords(text));
        page.setWordCountTable(util.getWordCountsTable(text));
        //calculating word count
    }

    //counting total no of web pages
    private static int getPageCount(File f) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(f));
        int count = 0;
        while (br.readLine() != null) {
            count++;
        }
        return count;
    }

    private static void loadDatatoGUI() {
        //load page data to gui
        mf.loadToGUI();
    }

    private static void calculateIDFTable() {
        Hashtable<String, Double> idfTable = webPageCacheTable.getIdfTable();
        Enumeration<String> idftablekeys = idfTable.keys();
        System.out.println("idf table....");
        int totalDocs = webPageCacheTable.getTotalDocuments();
        while (idftablekeys.hasMoreElements()) {
            String word = idftablekeys.nextElement();

            double idf = Math.log1p(totalDocs / webPageCacheTable.getTotalContainingDocuments(word));
            idfTable.put(word, idf);
        }
        System.out.println(idfTable);
        System.out.println("*****");
    }

    private static void calculateTF_IDFTable() {
        Enumeration<String> keys = webPageCacheTable.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Page page = webPageCacheTable.get(key);
            //creating clone of tf table
            Hashtable<String, Double> clone = (Hashtable< String, Double>) page.getTfTable().clone();
            //iterating tf table
            Enumeration<String> clonekeys = clone.keys();
            while (clonekeys.hasMoreElements()) {
                String nextElement = clonekeys.nextElement();
                clone.put(nextElement, clone.get(nextElement) / webPageCacheTable.getIdfTable().get(nextElement));
            }
            page.setTf_idfTable(clone);
        }
        
    }
}
