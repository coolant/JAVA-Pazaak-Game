package main;

import domain.DomainController;
import ui.Application;
import ui.ApplicationImpl;

/**
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class StartUp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("==== PAZAAK ====");
        System.out.println("");

        DomainController dc = new DomainController();
        Application app = new ApplicationImpl(dc);

    }

}
