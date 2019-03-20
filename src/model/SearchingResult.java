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
public class SearchingResult implements Comparable<SearchingResult> {

    public static final int FAKTOR = 1000;
    private double similarity;
    private Document document;

    public SearchingResult(double similarity, Document document) {
        this.similarity = similarity;
        this.document = document;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public int compareTo(SearchingResult result) {
        return Double.compare(similarity, result.getSimilarity());
    }
}
