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
public class RegisterPlayerApp implements Application {

    private final DomainController dc;
    private static final Scanner IN = new Scanner(System.in);
    
    public RegisterPlayerApp(DomainController dc) {
        this.dc = dc;
    }

    @Override
    public void run() {
        registerNewPlayer();
    }
    
    private void registerNewPlayer() {
        try {
//            Application.clearScreen();
            System.out.println(dc.getTextController().translateString("QUESTION_GIVENAME_PLAYER"));
            String playerName = IN.next();
            System.out.println(dc.getTextController().translateString("QUESTION_GIVEBIRTH_PLAYER"));
            int playerBirthYear = IN.nextInt();
//            Application.clearScreen();
            dc.validatePlayerName(playerName);
            if (dc.registerPlayer(playerName, playerBirthYear)) {
                System.out.println(dc.getTextController().translateString("NOTIFY_SUCCESSFUL_REGISTRATION_PLAYER"));
                // TODO find out how parameterised string translation works
                // so the output is more like: Player <NAME>, with birthdate <DATE>
                // was successfully created.
                // or something like that...
            }else{
                System.out.println(dc.getTextController().translateString("NOTIFY_NOT_SUCCESSFUL_REGISTRATION_PLAYER"));
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            IN.nextLine();
            registerNewPlayer();
        } catch (Exception e) {
            System.err.println(dc.getTextController().translateString("ERROR_INVALIDINPUT_GENERIC"));
            IN.nextLine();
            registerNewPlayer();
        }
        
    }
    

}
