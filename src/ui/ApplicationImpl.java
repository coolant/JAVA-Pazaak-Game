package ui;

import domain.DomainController;

import java.util.Scanner;

public class ApplicationImpl implements Application {

    private static final Scanner SCANNER = new Scanner(System.in);
    private final DomainController dc;
    private Application currentApplication;
    
    public ApplicationImpl(DomainController dc) {
        this.dc = dc;
        run();
    }

    private void setCurrentApplication(Application currentApplication) {
        this.currentApplication = currentApplication;
    }

    private void setCurrentApplication(int input) {
        switch (input) {
            case 1:
                setCurrentApplication(new RegisterPlayerApp(dc));
                break;
            case 2:
                setCurrentApplication(new GameApp(2, dc));
                break;
            case 3:
                setCurrentApplication(new GameApp(3, dc));
                break;
            case 0:
                System.exit(0);
            case 9:
                throw new UnsupportedOperationException(); // TODO - add call to screen to edit game configuration file
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public final void run() {
        System.out.println(dc.getTextController().translateString("MESSAGE_WELCOME_GAME"));
        setCurrentApplication(new ChooseLangApp(dc));
        currentApplication.run();
        int input;
        do {
            printMenu();
            input = SCANNER.nextInt();
            setCurrentApplication(input);
            Application.clearScreen();
            currentApplication.run();
        } while (input != 9);
    }

    private void printMenu() {
        System.out.println("============================");
        System.out.printf("%s:      (%d players)%n", dc.getTextController().translateString("MESSAGE_MENU"), dc.countRegistrations());
        System.out.println("============================");
        System.out.println("1) " + dc.getTextController().translateString("OPTION_REGISTERNEWPLAYER"));
        System.out.println("2) " + dc.getTextController().translateString("OPTION_STARTNEWGAME"));
        System.out.println("3) " + dc.getTextController().translateString("OPTION_LOADEXISTINGGAME"));
        System.out.println();
        System.out.println("9) " + dc.getTextController().translateString("OPTION_EDIT_GAMECONFIG"));
        System.out.println("0) " + dc.getTextController().translateString("OPTION_EXITGAME"));
    }
}
