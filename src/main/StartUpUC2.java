/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import domain.DomainController;
import ui.Application;
import ui.ChooseLangApp;

import java.sql.SQLException;

/**
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class StartUpUC2 {

    /**
     * @param args the command line arguments
     *             @throws SQLException Throws an exception if the driver library could not be located
     */
    public static void main(String[] args) throws SQLException {
        DomainController dc = new DomainController();
        
        dc.getTextController().setLanguage("english");
        
        Application chooseLang = new ChooseLangApp(dc);
        
        chooseLang.run();
    }
    
}
