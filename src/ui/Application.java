/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public interface Application {

    void run();
    
    /**
     * add some println to the console to clear the screen
     */
    static void clearScreen() {
        for (int i = 0; i < 55; i++) {
            System.out.println();
        }
    }
}
