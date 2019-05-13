/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author W
 */
public class InvertedIndex {

    private ArrayList<Document> listOfDocument = new ArrayList<Document>();
    private ArrayList<Term> dictionary = new ArrayList<Term>();

    public InvertedIndex() {
    }

    public void addNewDocument(Document document) {
        this.listOfDocument.add(document);
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

    public ArrayList<Posting> search(String query) {
//        makeDictionary();
        String[] tempQuery = query.split(" ");
        ArrayList<Posting> tempPosting = new ArrayList<>();
        for (int i = 0; i < tempQuery.length; i++) {
            String string = tempQuery[i];
            if (i == 0) {
                tempPosting = searchOneWord(tempQuery[i]);
            } else {
                ArrayList<Posting> tempPosting1 = searchOneWord(tempQuery[i]);
                tempPosting = intersection(tempPosting, tempPosting1);
            }
        }
        return tempPosting;
    }

    public ArrayList<Posting> searchOneWord(String query) {
        Term tempTerm = new Term(query);
        if (getDictionary().isEmpty()) {
            // dictionary kosong
            return null;
        } else {
            int positionTerm = Collections.binarySearch(dictionary, tempTerm);
            if (positionTerm < 0) {
                // tidak ditemukan
                return null;
            } else {
                return dictionary.get(positionTerm).getPostingList();
            }
        }
    }

    public ArrayList<Posting> intersection(ArrayList<Posting> p1,
            ArrayList<Posting> p2) {
        // mengecek p1 atau p2 sama dengan null?
        if (p1 == null || p2 == null) {
            // mengembalikan posting p1 atau p2
            return new ArrayList<>();
        }
        // menyiapkan posting tempPosting
        ArrayList<Posting> tempPostings = new ArrayList<>();
        // menyiapkan variable p1Index dan p2Index
        int p1Index = 0;
        int p2Index = 0;

        // menyiapkan variable post1 dan post2 bertipe Posting 
        Posting post1 = p1.get(p1Index);
        Posting post2 = p2.get(p2Index);

        while (true) {
            // mengecek id document post1 = id document post2?
            if (post1.getDocument().getId() == post2.getDocument().getId()) {
                try {
                    // menambahkan post1 ke tempPosting
                    tempPostings.add(post1);
                    // p1Index dan p2Index bertambah 1
                    p1Index++;
                    p2Index++;

                    post1 = p1.get(p1Index);
                    post2 = p2.get(p2Index);
                } catch (Exception ex) {
                    // menghentikan program
                    break;
                }

            } // mengecek id document post1 < id document post2?
            else if (post1.getDocument().getId() < post2.getDocument().getId()) {
                try {
                    // p1Index bertambah 1
                    p1Index++;
                    post1 = p1.get(p1Index);
                } catch (Exception ex) {
                    // menghentikan program
                    break;
                }

            } else {
                try {
                    // p2Index bertambah 1
                    p2Index++;
                    post2 = p2.get(p2Index);
                } catch (Exception ex) {
                    // menghentikan program
                    break;
                }
            }
        }
        // mengembalikan tempPosting
        return tempPostings;
    }

    public ArrayList<Posting> getUnsortedPostingList() {
        //siapkan posting listnya
        ArrayList<Posting> list = new ArrayList<Posting>();

        //buat node posting untuk doc1
        for (int i = 0; i < listOfDocument.size(); i++) {
            //buat list of term dari document ke i
            String[] termResult = listOfDocument.get(i).getListofTerm();
            //loop sebanyak term dari document ke i
            for (int j = 0; j < termResult.length; j++) {
                //buat object tempPosting
                Posting tempPosting = new Posting(termResult[j], listOfDocument.get(i));
                //cek kemunculan term

                list.add(tempPosting);
            }
        }
        return list;
    }

    public ArrayList<Posting> getUnsortedPostingListWithTermNumber() {
        // cek untuk term yang muncul lebih dari 1 kali
        // siapkan posting List
        ArrayList<Posting> list = new ArrayList<Posting>();
        // buat node Posting utk listofdocument
        for (int i = 0; i < getListOfDocument().size(); i++) {
            // buat listOfTerm dari document ke -i
            //String[] termResult = getListOfDocument().get(i).getListofTerm();
            ArrayList<Posting> postingDocument = getListOfDocument().get(i).getListofPosting();
            // loop sebanyak term dari document ke i
            for (int j = 0; j < postingDocument.size(); j++) {
                // ambil objek posting
                Posting tempPosting = postingDocument.get(j);
                // cek kemunculan term
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

    public ArrayList<Posting> getSortedPostingListWithTermNumber() {
        // siapkan posting List
        ArrayList<Posting> list = new ArrayList<Posting>();
        // panggil list yang belum terurut
        list = this.getUnsortedPostingListWithTermNumber();
        // urutkan
        Collections.sort(list);
        return list;
    }

    public void makeDictionary() {
        //Cek deteksi ada term yang frekuensinya lebih dari 1 di dalam dokumen

        //buat posting term terurut
        ArrayList<Posting> list = getSortedPostingList();
        //looping buat list of term (dictionary)
        for (int i = 0; i < list.size(); i++) {
            //cek dictionary kosong atau tidak
            if (dictionary.isEmpty()) {
                //buat term
                Term term = new Term(list.get(i).getTerm());
                //tambah posting list untuk term ini
                term.getPostingList().add(list.get(i));
                //tambah ke dictionary
                getDictionary().add(term);
            } else {
                //dictionary sudah ada isinya
                //buat term baru
                Term tempTerm = new Term(list.get(i).getTerm());
                //pembandingan apakah term sudah ada atau belum
                int position = Collections.binarySearch(dictionary, tempTerm);//keluarannya berupa posisi indeksnya
                if (position < 0) {
                    //term baru
                    //tambah posting list ke term 
                    tempTerm.getPostingList().add(list.get(i));
                    //tambahkan term ke dictionary
                    dictionary.add(tempTerm);
                } else {
                    //term ada
                    //tambahkan posting list saja dari existing term
                    dictionary.get(position).getPostingList().add(list.get(i));
                    //urutkan posting list
                    Collections.sort(dictionary.get(position).getPostingList());
                }
                //urutkan term dictionary
                Collections.sort(dictionary);
            }
        }
    }

    public void makeDictionaryWithTermNumber() {
        // cek deteksi ada term yang frekuensinya lebih dari 
        // 1 pada sebuah dokumen
        // buat posting list term terurut
        ArrayList<Posting> list = getSortedPostingListWithTermNumber();
        // looping buat list of term (dictionary)
        for (int i = 0; i < list.size(); i++) {
            // cek dictionary kosong?
            if (getDictionary().isEmpty()) {
                // buat term
                Term term = new Term(list.get(i).getTerm());
                // tambah posting ke posting list utk term ini
                term.getPostingList().add(list.get(i));
                // tambah ke dictionary
                getDictionary().add(term);
            } else {
                // dictionary sudah ada isinya
                Term tempTerm = new Term(list.get(i).getTerm());
                // pembandingan apakah term sudah ada atau belum
                // luaran dari binarysearch adalah posisi
                int position = Collections.binarySearch(getDictionary(), tempTerm);
                if (position < 0) {
                    // term baru
                    // tambah postinglist ke term
                    tempTerm.getPostingList().add(list.get(i));
                    // tambahkan term ke dictionary
                    getDictionary().add(tempTerm);
                } else {
                    // term ada
                    // tambahkan postinglist saja dari existing term
                    getDictionary().get(position).
                            getPostingList().add(list.get(i));
                    // urutkan posting list
                    Collections.sort(getDictionary().get(position)
                            .getPostingList());
                }
                // urutkan term dictionary
                Collections.sort(getDictionary());
            }

        }

    }

    //fungsi mencari frekuensi sebuah term dalam sebuah index
    public int getDocumentFrequency(String term) {
        Term tempTerm = new Term(term);
        // cek apakah term ada di dictionary
        int index = Collections.binarySearch(dictionary, tempTerm);
        if (index > 0) {
            // term ada
            // ambil ArrayList<Posting> dari object term
            ArrayList<Posting> tempPosting = dictionary.get(index)
                    .getPostingList();
            // return ukuran posting list
            return tempPosting.size();
        } else {
            // term tidak ada
            return -1;
        }
    }

    //fungsi untuk mencari invers term dari sebuah index
    public double getInverseDocumentFrequency(String term) {
        Term tempTerm = new Term(term);
        // cek apakah term ada di dictionary
        int index = Collections.binarySearch(dictionary, tempTerm);
        if (index > 0) {
            // term ada
            // jumlah total dokumen
            int N = listOfDocument.size();
            // jumlah dokumen dengan term i
            int ni = getDocumentFrequency(term);
            // idf = log10(N/ni)
            double Nni = (double) N / ni;
            return Math.log10(Nni);
        } else {
            // term tidak ada
            // nilai idf = 0
            return 0.0;
        }
    }

    public int getTermFrequency(String term, int idDocument) {
        Document document = new Document();
        document.setId(idDocument);
        int pos = Collections.binarySearch(listOfDocument, document);
        if (pos >= 0) {
            ArrayList<Posting> tempPosting = listOfDocument.get(pos).getListofPosting();
            Posting posting = new Posting();
            posting.setTerm(term);
            int postingIndex = Collections.binarySearch(tempPosting, posting);
            if (postingIndex >= 0) {
                return tempPosting.get(postingIndex).getNumberOfTerm();
            }
            return 0;
        }

        return 0;
    }

    public ArrayList<Posting> makeTFIDF(int idDocument) {
        // buat posting list hasil
        ArrayList<Posting> result = new ArrayList<Posting>();
        // buat document temporary, sesuai passing parameter
        Document temp = new Document(idDocument);
        // cek document temp, ada di dalam list document?
        int cari = Collections.binarySearch(listOfDocument, temp);
        // jika ada, variable cari akan berisi indeks. nilai lebih dari 0
        if (cari >= 0) {
            // dokumen ada
            // baca dokumen sesuai indek di list dokumen
            temp = listOfDocument.get(cari);
            // buat posting list dengan bobot masih 0
            result = temp.getListofPosting();
            // isi bobot dari posting list
            for (int i = 0; i < result.size(); i++) {
                // ambil term
                String tempTerm = result.get(i).getTerm();
                // cari idf
                double idf = getInverseDocumentFrequency(tempTerm);
                // cari tf
                int tf = result.get(i).getNumberOfTerm();
                // hitung bobot
                double bobot = tf * idf;
                // set bobot pada posting
                result.get(i).setWeight(bobot);
            }
            Collections.sort(result);
        } else {
            // dokumen tidak ada
        }
        return result;
    }

    public double getInnerProduct(ArrayList<Posting> p1, ArrayList<Posting> p2) {
        // urutkan posting list
        Collections.sort(p2);
        Collections.sort(p1);
        // buat temp hasil
        double result = 0.0;
        // looping dari posting list p1
        for (int i = 0; i < p1.size(); i++) {
            // ambil temp
            Posting temp = p1.get(i);
            // cari posting di p2
            boolean found = false;
            for (int j = 0; j < p2.size() && found == false; j++) {
                Posting temp1 = p2.get(j);
                if (temp1.getTerm().equalsIgnoreCase(temp.getTerm())) {
                    // term sama
                    found = true;
                    // kalikan bobot untuk term yang sama
                    result = result + temp1.getWeight() * temp.getWeight();
                }
            }
        }
        return result;
    }

    public ArrayList<Posting> getQueryPosting(String query) {
        // buat dokumen
        Document temp = new Document(-1, query);
        // buat posting list
        ArrayList<Posting> result = temp.getListofPosting();
        // hitung bobot
        // isi bobot dari posting list
        for (int i = 0; i < result.size(); i++) {
            // ambil term
            String tempTerm = result.get(i).getTerm();
            // cari idf
            double idf = getInverseDocumentFrequency(tempTerm);
            // cari tf
            int tf = result.get(i).getNumberOfTerm();
            // hitung bobot
            double bobot = tf * idf;
            // set bobot pada posting
            result.get(i).setWeight(bobot);
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Fungsi untuk menghitung panjang dari sebuah posting
     *
     * @param posting
     * @return
     */
    public double getLengthOfPosting(ArrayList<Posting> posting) {
        double result = 0.0;
        for (int i = 0; i < posting.size(); i++) {
            result = result + Math.pow(posting.get(i).getWeight(), 2);
        }
        double hasil = Math.sqrt(result);
        return hasil;
    }

    /**
     * Fungsi untuk menghitung cosine similarity
     *
     * @param posting
     * @param posting1
     * @return
     */
    public double getCosineSimilarity(ArrayList<Posting> posting, ArrayList<Posting> posting1) {
        double atas = getInnerProduct(posting, posting1);
        double panjangPosting = getLengthOfPosting(posting);
        double panjangPosting1 = getLengthOfPosting(posting1);
        double hasil = atas / (Math.sqrt(panjangPosting * panjangPosting1));
        return hasil;
    }

    /**
     * Fungsi untuk mencari berdasar nilai TFIDF
     *
     * @param query
     * @return
     */
    public ArrayList<SearchingResult> searchTFIDF(String query) {
        ArrayList<SearchingResult> hasil = new ArrayList<>();
        ArrayList<Posting> pQuery = getQueryPosting(query);
        for (int i = 0; i < listOfDocument.size(); i++) {
            ArrayList<Posting> tempDocWeight = makeTFIDF(listOfDocument.get(i).getId());
            double hasilDotProduct = getInnerProduct(tempDocWeight, pQuery);
            if (hasilDotProduct > 0) {
                SearchingResult hasilCari = new SearchingResult(hasilDotProduct, listOfDocument.get(i));
                hasil.add(hasilCari);
            }
        }
        Collections.sort(hasil);
        return hasil;
    }

    /**
     * Fungsi untuk mencari dokumen berdasarkan cosine similarity
     *
     * @param query
     * @return
     */
    public ArrayList<SearchingResult> searchCosineSimilarity(String query) {
        ArrayList<SearchingResult> hasil = new ArrayList<>();
        ArrayList<Posting> pQuery = getQueryPosting(query);
        for (int i = 0; i < listOfDocument.size(); i++) {
            ArrayList<Posting> tempDocWeight = makeTFIDF(listOfDocument.get(i).getId());
            double Cosine = getCosineSimilarity(tempDocWeight, pQuery);
            if (Cosine > 0) {
                SearchingResult hasilCari = new SearchingResult(Cosine, listOfDocument.get(i));
                hasil.add(hasilCari);
            }
        }
        Collections.sort(hasil);
        return hasil;
    }

    public void readDirectory(File directory) {
        // baca isi directory
        File files[] = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            // buat document baru
            Document doc = new Document();
            doc.setId(i); // set idDoc sama dengan i
            // baca isi file
            // Isi file disimpan di atribut content dari objeck document
            // variabel i merupakan idDocument;
            File file = files[i];
            doc.readFile(i + 1, file);
            doc.Stemming();
            doc.setNamaDokumen(file.getName());
            // masukkan file isi directory ke list of document pada obye index
            this.addNewDocument(doc);
        }
        // lakukan indexing atau buat dictionary
        this.makeDictionaryWithTermNumber();
    }
}
