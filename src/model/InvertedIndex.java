/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.sun.org.apache.xerces.internal.util.DOMUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Lenovo
 */
public class InvertedIndex {

    private ArrayList<Document> listOfDocument = new ArrayList<Document>();
    private ArrayList<Term> dictionary = new ArrayList<Term>();

    public InvertedIndex() {

    }

    public void addNewDocument(Document document) {
        listOfDocument.add(document);
    }

    public ArrayList<Posting> getUnsortedPostingList() {
        ArrayList<Posting> list = new ArrayList<Posting>();

        for (int i = 0; i < listOfDocument.size(); i++) {
            String[] termResult = listOfDocument.get(i).getListofTerm();
            for (int j = 0; j < termResult.length; j++) {
                Posting tempPosting = new Posting(termResult[j], listOfDocument.get(i));
                list.add(tempPosting);
            }
        }
        return list;
    }

    public ArrayList<Posting> getSortedPostingList() {
        ArrayList<Posting> list = new ArrayList<Posting>();
        list = this.getUnsortedPostingList();
        Collections.sort(list);
        return list;
    }

    public ArrayList<Posting> getUnsortedPostingListWithTermNumber() {
        ArrayList<Posting> list = new ArrayList<Posting>();
        for (int i = 0; i < listOfDocument.size(); i++) {
            ArrayList<Posting> postingDoc = listOfDocument.get(i).getListofPosting();
            for (int j = 0; j < postingDoc.size(); j++) {
                Posting tempPosting = postingDoc.get(j);
                list.add(tempPosting);
            }
        }
        return list;
    }

    public ArrayList<Posting> getSortedPostingListWithTermNumber() {
        ArrayList<Posting> list = new ArrayList<Posting>();
        list = this.getUnsortedPostingListWithTermNumber();
        Collections.sort(list);
        return list;
    }

    public void makeDictionaryWithTermNumber() {
        ArrayList<Posting> list = this.getSortedPostingListWithTermNumber();

        for (int i = 0; i < list.size(); i++) {
            if (getDictionary().isEmpty()) {
                Term term = new Term(list.get(i).getTerm());
                term.getPostingList().add(list.get(i));
                getDictionary().add(term);
            } else {
                Term tempTerm = new Term(list.get(i).getTerm());
                int position = Collections.binarySearch(getDictionary(), tempTerm);
                if (position < 0) {
                    tempTerm.getPostingList().add(list.get(i));
                    getDictionary().add(tempTerm);
                } else {
                    getDictionary().get(position).getPostingList().add(list.get(i));
                    Collections.sort(getDictionary().get(position).getPostingList());
                }
                Collections.sort(getDictionary());
            }
        }
    }

    public ArrayList<Posting> search(String kunci) {
        String[] q = kunci.split(" ");
        ArrayList<Posting> result = new ArrayList<>();
        for (int i = 0; i < q.length; i++) {
            String string = q[i];
            if (i == 0) {
                result = searchOneWord(q[i]);
            } else {
                ArrayList<Posting> result2 = searchOneWord(q[i]);
                result = intersection(result, result2);
            }
        }
        return result;
    }

    public ArrayList<Posting> intersection(ArrayList<Posting> p1, ArrayList<Posting> p2) {
        if (p1 == null || p2 == null) {
            return new ArrayList<>();
        }

        ArrayList<Posting> posting = new ArrayList<>();
        int index_p1 = 0;
        int index_p2 = 0;

        Posting post1 = p1.get(index_p1);
        Posting post2 = p2.get(index_p2);

        while (true) {
            if (post1.getDocument().getId() == post2.getDocument().getId()) {
                try {
                    posting.add(post1);
                    index_p1++;
                    index_p2++;
                    post1 = p1.get(index_p1);
                    post2 = p2.get(index_p2);
                } catch (Exception e) {
                    break;
                }
            } else if (post1.getDocument().getId() < post2.getDocument().getId()) {
                try {
                    index_p1++;
                    post1 = p1.get(index_p1);
                } catch (Exception e) {
                    break;
                }
            } else {
                try {
                    index_p2++;
                    post2 = p2.get(index_p2);
                } catch (Exception e) {
                    break;
                }
            }
        }
        return posting;
    }

    public ArrayList<Posting> searchOneWord(String kunci) {
        Term tempTerm = new Term(kunci);
        if (getDictionary().isEmpty()) {
            return null;
        } else {
            int positionTerm = Collections.binarySearch(dictionary, tempTerm);
            if (positionTerm < 0) {
                return null;
            } else {
                return dictionary.get(positionTerm).getPostingList();
            }
        }
    }

    public void makeDictionary() {
        ArrayList<Posting> list = this.getSortedPostingList();
        for (int i = 0; i < list.size(); i++) {
            if (getDictionary().isEmpty()) {
                Term term = new Term(list.get(i).getTerm());
                term.getPostingList().add(list.get(i));
                getDictionary().add(term);
            } else {
                Term tempTerm = new Term(list.get(i).getTerm());
                int position = Collections.binarySearch(getDictionary(), tempTerm);
                if (position < 0) {
                    tempTerm.getPostingList().add(list.get(i));
                    getDictionary().add(tempTerm);
                } else {
                    getDictionary().get(position).getPostingList().add(list.get(i));
                    Collections.sort(getDictionary().get(position).getPostingList());
                }
                Collections.sort(getDictionary());
            }
        }
    }

    public int getDocFreq(String term) {
        ArrayList<Term> result = getDictionary();
        int count = 0;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getTerm().equals(term)) {
                count = result.get(i).getNumberOfDocument();
            }
        }
        return count;

//        Term tempTerm = new Term(term);
//        int index = Collections.binarySearch(dictionary, tempTerm);
//        if (index > 0) {
//            ArrayList<Posting> tempPost = dictionary.get(index).getPostingList();
//            return tempPost.size();
//        }else{
//            return -1;
//        }
    }

    public double getInverseDocFreq(String term) {
        double n = getDocFreq(term);
        double N = listOfDocument.size();
        return Math.log10(N / n);
//        Term tempTerm = new Term(term);
//        int index = Collections.binarySearch(dictionary, tempTerm);
//        if (index > 0) {
//            int N = listOfDocument.size();
//            int ni = getDocFreq(term);
//            return Math.log10(N/ni);
//        }else{
//            return 0.0;
//        }
    }

    public int getTermFreq(String term, int idDoc) {
        Document doc = new Document();
        doc.setId(idDoc);
        int index = Collections.binarySearch(listOfDocument, doc);
        if (index >= 0) {
            ArrayList<Posting> tempPost = getListOfDocument().get(index).getListofPosting();
            Posting post = new Posting();
            post.setTerm(term);
            int postIndex = Collections.binarySearch(tempPost, post);
            if (postIndex > 0) {
                return tempPost.get(postIndex).getNumberOfTerm();
            }
            return 0;
        }
        return 0;
    }

    public ArrayList<Posting> makeTFIDF(int idDocument) {
        Document doc = new Document();
        doc.setId(idDocument);
        int cek = Collections.binarySearch(listOfDocument, doc);
        if (cek < 0) {
            return null;
        } else {
            doc = listOfDocument.get(cek);
            ArrayList<Posting> result = doc.getListofPosting();
            Collections.sort(result);
            for (int i = 0; i < result.size(); i++) {
                double w = getTermFreq(result.get(i).getTerm(), idDocument) * getInverseDocFreq(result.get(i).getTerm());
                result.get(i).setTerm(result.get(i).getTerm());
                result.get(i).setNumberOfTerm(getTermFreq(result.get(i).getTerm(), idDocument));
                result.get(i).setWeight(w);
            }
            return result;
        }
    }

    public double getInnerProduct(ArrayList<Posting> p1, ArrayList<Posting> p2) {
        double hasil = 0;
        for (int i = 0; i < p1.size(); i++) {
            int pos = Collections.binarySearch(p2, p1.get(i));
            if (pos >= 0) {
                hasil = hasil + (p1.get(i).getWeight()
                        * p2.get(pos).getWeight());
            }
        }
        return hasil;
    }

    public ArrayList<Posting> getQueryPosting(String query) {
        Document doc = new Document();
        doc.setContent(query);
        ArrayList<Posting> queryPost = doc.getListofPosting();
        for (int i = 0; i < queryPost.size(); i++) {
            double w = queryPost.get(i).getNumberOfTerm()
                    * getInverseDocFreq(queryPost.get(i).getTerm());
            queryPost.get(i).setWeight(w);
        }

        return queryPost;
    }

    public double getLengthOfPosting(ArrayList<Posting> posting) {
        // membuat variabel untuk mengambil panjang dokumen
        double tempPost = 0;
        // perulangan sebanyak posting
        for (int i = 0; i < posting.size(); i++) {
            // melakukan perhitungan dengan rumus setiap weight setiap posting di pangkat 2
            tempPost += Math.pow(posting.get(i).getWeight(), 2);
        }
        return Math.sqrt(tempPost);
    }

    public double getCosineSimilarity(ArrayList<Posting> posting, ArrayList<Posting> posting1) {
        // mengambil nilai InnerProduct dan dimasukan ke variabel baru
        double ip = getInnerProduct(posting, posting1);
        // membuat variabel baru untuk melakukan perhitungan cosine similarity
        double hasil = 0;
        // melakukan perhitungan cosine similarity
        hasil = ip / (getLengthOfPosting(posting) * getLengthOfPosting(posting1));
        return hasil;
    }

    public ArrayList<SearchingResult> searchTFIDF(String query) {
        // membuat arraylist searching result untuk memasukan hasil dari pencarian
        ArrayList<SearchingResult> result = new ArrayList<SearchingResult>();
        // menghitung tfidf untuk kata yg dicari
        ArrayList<Posting> queryPost = this.getQueryPosting(query);
        
        // perulangan sebanyak dokumen
        for (int i = 1; i <= listOfDocument.size(); i++) {
            // arraylist untuk menghitung tfidf untuk dokumen
            ArrayList<Posting> tempDoc = this.makeTFIDF(i);
            // menghitung innerproduct
            double innerProduct = this.getInnerProduct(queryPost, tempDoc);
            // memasukan innerproduct dan dokumen ke searching result
            SearchingResult doc = new SearchingResult(innerProduct, tempDoc.get(i-1).getDocument());
            // memasukan data variabel doc ke arraylist result
            result.add(doc);
        }
        // melakukan pengurutan
        Collections.sort(result);
        // melakukan pembalikan urutan
        Collections.reverse(result);
        return result;
    }

    public ArrayList<SearchingResult> searchCosineSimilarity(String query) {
        // membuat arraylist searching result untuk memasukan hasil dari pencarian
        ArrayList<SearchingResult> result = new ArrayList<SearchingResult>();
        // menghitung tfidf untuk kata yg dicari
        ArrayList<Posting> queryPost = this.getQueryPosting(query);
        
        // perulangan sebanyak dokumen
        for (int i = 1; i <= listOfDocument.size(); i++) {
            // arraylist untuk menghitung tfidf untuk dokumen
            ArrayList<Posting> tempDoc = this.makeTFIDF(i);
            // menghitung similarity
            double similarity = this.getCosineSimilarity(queryPost, tempDoc);
            // memasukan similarity dan dokumen ke searching result
            SearchingResult doc = new SearchingResult(similarity,tempDoc.get(i-1).getDocument());
            // memasukan data variabel doc ke arraylist result
            result.add(doc);
        }
       // melakukan pengurutan
        Collections.sort(result);
        // melakukan pembalikan urutan
        Collections.reverse(result);
        return result;
    }

    public ArrayList<Document> getListOfDocument() {
        return listOfDocument;
    }

    public void setListOfDocument(ArrayList<Document> listOfDocument) {
        this.listOfDocument = listOfDocument;
    }

    public ArrayList<Term> getDictionary() {
        return dictionary;
    }

    public void setDictionary(ArrayList<Term> dictionary) {
        this.dictionary = dictionary;
    }

}
