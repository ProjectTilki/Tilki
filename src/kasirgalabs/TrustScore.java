/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kasirgalabs;

/**
 *
 * @author daghan
 */
public class TrustScore {

    private int skor;

    public TrustScore() {
        this.skor = 100;
    }

    public void skorAzalt(int ceza) {
        System.out.println("Skor " + ceza + " puan azaltildi !");
        this.skor -= ceza;
    }

    public int getSkor() {
        return this.skor;
    }
}
