/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import domain.DomainController;
import gui.CreateSideDeckController;
import gui.MainController;
import gui.Navigator;
import gui.PlayGameController;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */


public class StartUpGUI extends Application {

    private MainController main;
    private Navigator navigator;
    
    @Override
    public void start(Stage stage) throws SQLException, IOException {
        navigator = new Navigator();
        main = new MainController();
        
        Scene scene = new Scene(main);
        Navigator.setMainController(main);
        
        stage.setScene(scene);
        
        stage.setOnShown((WindowEvent t) -> {
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        });
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(StartUpGUI.class, args);
    }
}
