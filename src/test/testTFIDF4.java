/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.List;
import model.Document;
import model.InvertedIndex;
import model.Posting;
import model.Term;

/**
 *
 * @author Lenovo
 */
public class testTFIDF4 {

    public static void main(String[] args) {
        Document doc1 = new Document(1, "Shipment of gold damaged in a fire");
        Document doc2 = new Document(2, "delivery of silver arrived in a silver truck");
        Document doc3 = new Document(3, "shipment of gold arrived in a truck");

        InvertedIndex index = new InvertedIndex();
        index.addNewDocument(doc1);
        index.addNewDocument(doc2);
        index.addNewDocument(doc3);

        index.makeDictionaryWithTermNumber();
        ArrayList<Term> result = index.getDictionary();

        for (int i = 0; i < result.size(); i++) {
            System.out.println("====================================");
            System.out.println("Term = " + result.get(i).getTerm() + ", numberofDocument = "
                    + result.get(i).getNumberOfDocument());
            System.out.println("------------------------------------");
            ArrayList<Posting> tempPosting = result.get(i).getPostingList();
            for (int j = 0; j < tempPosting.size(); j++) {
                System.out.println("id_doc = " + tempPosting.get(j).getDocument().getId()
                        + ", numberofTerm = " + tempPosting.get(j).getNumberOfTerm());
            }
        }
        System.out.println("====================================");
        // number of document
        String tempString = "a";
        int result2 = index.getDocFreq(tempString);
        System.out.println("Number of Doc with " + tempString + " is " + result2);
    }
}
