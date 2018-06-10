/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import domain.DomainController;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class SaveGameState extends GameAppState {
    private DomainController dc;
    private final Scanner IN = new Scanner(System.in);

    //Goede constructor
    public SaveGameState(GameApp gameApp, DomainController dc) {
        super(gameApp);
        this.dc = dc;
    }
    
    @Override
    public void run(){
        saveGame();
    }
    
    public void saveGame(){
        try{
            if(confirmSaveGame()){
                String gameName = giveGameName();
                if(dc.pushGame(gameName)){
                    System.out.println(dc.getTextController().translateString("NOTIFY_SUCCESSFUL_REGISTRATION_GAME"));
                }else{
                    System.out.println(dc.getTextController().translateString("NOTIFY_NOT_SUCCESSFUL_REGISTRATION_GAME"));
                }
                if(confirmPlaying()){
                    //TODO - speelt de volgende set
                    gameApp.toState(new PlaySetState(gameApp, this.dc));
                    gameApp.run();
                }else{
                    //TODO - terug naar menu
                    
                    
                    //System.exit(0);
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            IN.nextLine();
            
        }
    }
    
    private boolean confirmSaveGame(){
        System.out.println("----------------------------------------------------------------");
            System.out.println(dc.getTextController().translateString("MESSAGE_SAVE_GAME"));
            if (IN.next().substring(0, 1).toLowerCase().equals("y")) {
                return true;
            }
            System.out.println("----------------------------------------------------------------");
            return false;
    }
    
    private boolean confirmPlaying(){
        System.out.println("----------------------------------------------------------------");
            System.out.println(dc.getTextController().translateString("MESSAGE_KEEP_PLAYING"));
            if (IN.next().substring(0, 1).toLowerCase().equals("y")) {
                
                return true;
            }
            System.out.println("----------------------------------------------------------------");
            return false;
    }
    
    
    private String giveGameName(){
        String gameName = "";
        try{
            System.out.println(dc.getTextController().translateString("MESSAGE_VALID_GAMENAME"));
            gameName = IN.next();
            dc.validateGameName(gameName);
            return gameName;
        } catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
            IN.nextLine();
        }
        return giveGameName(); 
    }
}
