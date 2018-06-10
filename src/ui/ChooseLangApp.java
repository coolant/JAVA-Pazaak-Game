/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import domain.DomainController;
import java.util.Scanner;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class ChooseLangApp implements Application {

    private static final Scanner IN = new Scanner(System.in);
    private final DomainController dc;
    
    public ChooseLangApp(DomainController dc) {
        this.dc = dc;
    }

    @Override
    public void run() {
        chooseLanguage();
    }
    
    private void chooseLanguage() {

        try {
            System.out.println("Type 'EN' for English...");
            System.out.println("Appuyez 'FR' pour français...");
            System.out.println("Typ 'NL' voor Nederlands...");
            dc.getTextController().setLanguage(IN.next());

            Application.clearScreen();
        } catch (IllegalArgumentException e) {
            System.err.println("Incorrecte invoer, Invalid input, Entrée incorrecte");
            IN.nextLine();
            chooseLanguage();
        }
    }
    
   

}
