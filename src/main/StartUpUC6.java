package main;

import domain.DomainController;
import ui.Application;
import ui.GameApp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class StartUpUC6
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException
    {
        DomainController dc = new DomainController();
        dc.getTextController().setLanguage("english");
     
        Application ga = new GameApp(4, dc);
        
        ga.run();
    }
    
}
