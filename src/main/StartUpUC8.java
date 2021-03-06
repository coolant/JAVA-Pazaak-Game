/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import domain.DomainController;
import ui.Application;
import ui.SaveGameState;

import java.sql.SQLException;
import ui.GameApp;

/**
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class StartUpUC8 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        DomainController dc = new DomainController();

        dc.getTextController().setLanguage("english");
        
        Application game = new GameApp(5, dc);
        
        game.run();

    }

}
