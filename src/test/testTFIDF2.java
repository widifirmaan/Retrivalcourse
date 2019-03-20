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
public class testTFIDF2 {

    public static void main(String[] args) {
        Document doc2 = new Document(2, "delivery of silver arrived in a silver truck");
        
        ArrayList<Posting> result = doc2.getListofPosting();
        
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i).getTerm() + " = " + 
                    result.get(i).getNumberOfTerm());
            
        }
    }
}
