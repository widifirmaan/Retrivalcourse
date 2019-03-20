/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.util.ArrayList;
import model.Document;
import model.Posting;

/**
 *
 * @author Lenovo
 */
public class testDocument3 {

    public static void main(String[] args) {
        Document doc1= new Document(1, "computer information retrieval.");
        Document doc2= new Document(2, "computer organization and architecture.");
        
        String tokenDoc1[] = doc1.getListofTerm();
        String tokenDoc2[] = doc2.getListofTerm();
        
        ArrayList<Document> listOfDocument = new ArrayList<Document>();
        listOfDocument.add(doc1);
        listOfDocument.add(doc2);
        
        ArrayList<Posting> list = new ArrayList<Posting>();
        
        for (int i = 0; i < listOfDocument.size(); i++) {
            String[] termResult = listOfDocument.get(i).getListofTerm();
            for (int j = 0; j < termResult.length; j++) {
                Posting tempPosting = new Posting(termResult[j],listOfDocument.get(i));
                   list.add(tempPosting);
            }
        }
        System.out.println("Ukuran List = " + list.size() + "\n");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getTerm() + "," + list.get(i).getDocument().getId());
        }
    }
    
}
