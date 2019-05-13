/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.id.IndonesianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

/**
 *
 * @author AxYxA
 */
public class Document implements Comparable<Document> {
    private int id;
    private String content;
    private String realContent;
    private String namaDokumen;

    public String getNamaDokumen() {
        return namaDokumen;
    }

    public void setNamaDokumen(String namaDokumen) {
        this.namaDokumen = namaDokumen;
    }

    public String getRealContent() {
        return realContent;
    }

    public void setRealContent(String realContent) {
        this.realContent = realContent;
    }

    public Document() {
    }

    public Document(int id) {
        this.id = id;
    }

    public Document(String content) {
        this.content = content;
        this.realContent = content;
    }

    public Document(int id, String content) {
        this.id = id;
        this.content = content;
        this.realContent = content;
    }

    @Override
    public String toString() {
        return "Document{" + "id=" + id + ", content=" + content + ", realContent=" + realContent + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getListofTerm() {
        String value = this.getContent();
        value = value.replaceAll("[.,?!]", "");
        return value.split(" ");
    }

    public ArrayList<Posting> getListofPosting() {
        //panggil fungsi getlistofterm
        String[] tempString = getListofTerm();
        //buat object arraylistPosting result untuk menampung hasil
        ArrayList<Posting> result = new ArrayList<Posting>();
        //buat looping sebanyak list of term
        for (int i = 0; i < tempString.length; i++) {
            //di dalam looping
            //cek jika term pertama maka
            if (i == 0) {
                //buat object tempPosting
                Posting tempPosting = new Posting(tempString[i], this);
                //set atribut documentnya, gunakan this
                //tambahkan ke Arraylist Result
                result.add(tempPosting);
            } else {
                //lainnya
                //sorting ArrayList result
                Collections.sort(result);
                //cek apakah tersm sudah ada, maka gunakan fungsi search dengan keluaran indeks objek yang memenuhi
                Posting temPosting = new Posting(tempString[i], this);
                int indexCari = Collections.binarySearch(result, temPosting);
                //jika hasil cari kurang dari 0 (objek tidak ada)
                if (indexCari < 0) {
                    //buat object tempPosting
                    Posting tempPosting = new Posting(tempString[i], this);
                    //set atribut documentnya, gunakan this
                    //tambahkan ke Arraylist Result
                    result.add(tempPosting);
                } else {
                    //lainnya (objek ada)
                    //ambil postingnya, tambahkan 1 ke numberoftermnya 
                    //dengan fungsi 
                    int TempNumber = result.get(indexCari).getNumberOfTerm() + 1;
                    result.get(indexCari).setNumberOfTerm(TempNumber);
                }
            }
        }
        return result;
    }

    public int compareTo(Document t) {
        return id - t.getId();
    }

    public void readFile(int idDoc, File file) {
        this.setId(idDoc);
        String line = null;

        try {
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                this.setContent(line);
            }   

            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("File not Found.");
        }
        catch(IOException ex) {
            System.out.println(ex.toString());
        }
    }
    
    public void Stemming(){
        String text = content;
//        System.out.println("Text = "+text);
        Version matchVersion = Version.LUCENE_7_7_0; // Substitute desired Lucene version for XY
        Analyzer analyzer = new IndonesianAnalyzer();
        analyzer.setVersion(matchVersion);
        // buat token
        TokenStream tokenStream = analyzer.tokenStream("myField", new StringReader(text.trim()));
        // stemming
        tokenStream = new PorterStemFilter(tokenStream);
        // buat string baru tanpa stopword
        StringBuilder sb = new StringBuilder();
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                sb.append(term + " ");
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
        content = sb.toString();
    }
    
}
