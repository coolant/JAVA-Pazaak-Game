/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import ui.LoadGameState;
import domain.DomainController;
import java.sql.SQLException;
import ui.Application;
import ui.GameApp;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class StartUpUC9 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        DomainController dc = new DomainController();
        
        dc.getTextController().setLanguage("english");
        
        Application game = new GameApp(3, dc);
        
        game.run();
    }
    
}
