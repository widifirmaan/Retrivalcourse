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

/**
 *
 * @author Lenovo
 */
public class testUnsortedPosting {

    public static void main(String[] args) {
        Document doc1= new Document(1, "computer information retrieval.");
        Document doc2= new Document(2, "computer organization and architecture.");
        
        InvertedIndex index = new InvertedIndex();
        index.addNewDocument(doc1);
        index.addNewDocument(doc2);
        
        ArrayList<Posting> list = index.getUnsortedPostingList();
        
        System.out.println("Ukuran List = " + list.size() + "\n");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getTerm() + "," + list.get(i).getDocument().getId());
        }
    }
    
}
