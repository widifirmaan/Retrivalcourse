/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import model.Document;
import model.InvertedIndex;
import model.Posting;
import model.Term;

/**
 *
 * @author Lenovo
 */
public class testIntersection {

    public static void main(String[] args) {
        Document doc1 = new Document(1, "computer information retrieval.");
        Document doc2 = new Document(2, "computer organization and architecture");
        Document doc3 = new Document(3, "machine learning inteligence architecture");
        Document doc4 = new Document(4, "machine learning artificial architecture");

        InvertedIndex index = new InvertedIndex();

        index.addNewDocument(doc1);
        index.addNewDocument(doc2);
        index.addNewDocument(doc3);
        index.addNewDocument(doc4);

        index.makeDictionary();

        String query = "machine architecture";

        ArrayList<Posting> postings = index.search(query);
//        ArrayList<Posting> r1 = index.searchOneWord("machine");
//        ArrayList<Posting> r2 = index.searchOneWord("learning");
//        ArrayList<Posting> postings = index.intersection(r1, r2);

        for (int i = 0; i < postings.size(); i++) {
            System.out.println("id_doc = " + postings.get(i).getDocument().getId());
            System.out.println((i + 1) + ". " + postings.get(i).getDocument().getContent());
        }
//        for (int i = 0; i < postings.size(); i++) {
//            if (i == 0) {
//                System.out.println("id_doc = " + postings.get(i).getDocument().getId());
//                System.out.println((i + 1) + ". " + postings.get(i).getDocument().getContent());
//            } else if (i >= 1) {
//                if (!postings.get(i - 1).getDocument().getContent().equals(postings.get(i).getDocument().getContent())) {
//                System.out.println("id_doc = " + postings.get(i).getDocument().getId());
//                System.out.println((i + 1) + ". " + postings.get(i).getDocument().getContent());
//                } else {
//                    break;
//                }
//            }
//        }
    }
}
