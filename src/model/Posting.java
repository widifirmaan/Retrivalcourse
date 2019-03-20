/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Lenovo
 */
public class Posting implements Comparable<Posting>{
    private String term;
    private Document document;
    private int numberOfTerm = 1;
    private double weight=0.0;
    

    public Posting(String term, Document document) {
        this.term = term;
        this.document = document;
    }
    
    public Posting(Document document) {
        this.document = document;
    }
    public Posting() {
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
    
    @Override
    public int compareTo(Posting o) {
       return this.term.compareTo(o.term);
    }
    
    public int getNumberOfTerm() {
        return numberOfTerm;
    }

    public void setNumberOfTerm(int numberOfTerm) {
        this.numberOfTerm = numberOfTerm;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
}
