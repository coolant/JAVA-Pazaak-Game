/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import org.junit.Test;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class PlayerTest {
        
    public PlayerTest() {
    }
    
    @Test
    public void goodPlayer(){
        Player player = new Player("Senne", 1995);
    }
    
    @Test
    public void spaceBeforeName(){
        Player player = new Player("   Senne", 1995);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongName(){
        Player player = new Player("Se", 1995);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void emptyName(){
        Player player = new Player("", 1995);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void spaceName(){
        Player player = new Player("     ", 1995);
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void spaceBetweenTwoCharachterName(){
        Player player = new Player("S enne", 1995);
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void periodInName(){
        Player player = new Player("Senne.", 1995);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void startWithNumberInName(){
        Player player = new Player("12Test", 1995);
    }

    //geen . , ; : ! ?

    @Test(expected = IllegalArgumentException.class)
    public void commaInName(){
        Player player = new Player("Test,", 1995);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void semicolonInName(){
        Player player = new Player("Tes;t", 1995);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void columnInName(){
        Player player = new Player("T:est", 1995);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void questionMarkInName(){
        Player player = new Player("Te?st", 1995);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void explanationMarkInName(){
        Player player = new Player("T!est", 1995);
    }
    
     @Test(expected = NullPointerException.class)
    public void playerNameisNull(){
        Player player = new Player(null, 1995);
    }
    
    //Birthday
    
    @Test
    public void playerWithGoodBirthYear(){
        Player player = new Player("Senne", 1995);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void playerIsTooOld(){
        Player player = new Player("Senne", 1910);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void notABirthyear(){
        Player player = new Player("Senne", 110);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void playerIsTooYoung(){
        Player player = new Player("Senne", 2015);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void playerIsAFuturePlayer(){
        Player player = new Player("Senne", 2020);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void playerGiveANegativeBirthYear(){
        Player player = new Player("Senne", -2020);
    }
    
//    @Test(expected = IllegalArgumentException.class)
//    public void birthYearNotANumber(){
//        Player player = new Player("Senne", Integer.parseInt("test"));
//    }
}
