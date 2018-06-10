/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
@RunWith(value = Parameterized.class)
public class ParameterizedPlayerTest {
    
    private String name;
    private int by;

    public ParameterizedPlayerTest(String name, int by) {
        this.name = name;
        this.by = by;
    }
    
    @Parameters
    public static Collection<Object[]> getTestParams() {
        return Arrays.asList(new Object[][] {
            {".John   ;", 2017},
            {"jo", 2099},
            {"1jo", 1000}, 
            {" .dkeo", 1917},
            {"..jooo", 3000},
        });
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongNameTest() {
       new Player(name, 1992);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongBirthYearTest() {
       new Player("Stijn", by);
    }
    
     
}
