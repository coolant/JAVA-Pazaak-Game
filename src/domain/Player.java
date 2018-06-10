package domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.*;

/**
 * Geserialiseerde klasse die instaat voor het creëren van 2 Speler-objecten bij
 * het aanmaken van een nieuw spel
 * <p>
 * De spelerklasse is de klasse waar alle operaties en attributen van
 * spelerobjecten worden aangemaakt. Speler staat in voor de communicatie tussen
 * domeincontroller en verscheidene speler attributen, zoals de verschillende
 * spelerskaarten uit zijn/haar stapel.
 * </p>
 * <h2>Domeinregels</h2>
 * <h3>DR_SPELER_NAAM_LEEFTIJD</h3>
 * <p>
 * De naam van een speler is minstens 3 karakters lang en uniek. De naam mag
 * geen spaties of leestekens bevatten. Cijfers zijn wel toegelaten maar niet
 * als eerste karakter. Een speler moet dit jaar minstens 6 jaar en maximaal 99
 * zijn/worden.</p>
 * <h3>DR_SPELER_NIEUWE_STAPEL</h3>
 * <p>
 * De startstapel van elke speler bevat volgende kaarten.
 * https://i.imgur.com/UWJuw4B.png De speler heeft van elk van bovenstaande 10
 * kaarten exact 1 kaart in zijn/haar startstapel
 * </p>
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 * @since 2017-02-23
 * @version 1.0
 */
public class Player implements Cloneable, Comparator<Player>, Comparable<Player> {

    private final String name;
    private final int birthYear;
    private int credits;
    private List<Card> sideDeck;
    
    // TODO consider using a GameSet instead of a List:
    // domain rules state no identical cards can be added to the side deck
    // order doesn't matter since we're grabbing randoms from it anyway
    private List<Card> cardsInPossession;

    /**
     * Player creation method, see also: DR_SPELER_NAAM_LEEFTIJD
     *
     * @param name desired name of the player to be registered
     * @param birthYear the birth year, expressed as a positive integer: between
     * (Current year - 99) and (Current year - 6)
     *
     */
    public Player(String name, int birthYear) {
        this(name, birthYear, 5);
    }

    /**
     * Constructor to create a player, with a name, birthyear and credits
     * See also: DR_SPELER_NAAM_LEEFTIJD
     * 
     * @param name
     * @param birthYear
     * @param credits 
     */
    public Player(String name, int birthYear, int credits) {
        name = name.trim();
        validateName(name);
        validateBirthYear(birthYear);
        cardsInPossession = generateDefaultDeck();
        this.name = name;
        this.birthYear = birthYear;
        this.credits = credits;
        this.sideDeck = new ArrayList<>();
    }

    /**
     * Constructor to create a player, with an undefined name
     */
    public Player() {
        this("undefined", 1995);
    }

    /**
     * Method for creating 10 simplecard objects according to
     * DR_SPELER_NIEUWE_STAPEL
     *
     * De startstapel van elke speler bevat volgende kaarten.
     * https://i.imgur.com/UWJuw4B.png De speler heeft van elk van bovenstaande
     * 10 kaarten exact 1 kaart in zijn/haar startstapel
     *
     * @return a card arraylist with 10 values, according to
     * DR_SPELER_NIEUWE_STAPEL
     */
    private List<Card> generateDefaultDeck() {
        ArrayList<Card> cards = new ArrayList<>();

        // TODO - Call from CardRepository to grab default deck from the database
        // or else ... revert to these 10
        
        cards.add(new SimpleCard(CardType.POSITIVE, 2));
        cards.add(new SimpleCard(CardType.POSITIVE, 4));
        cards.add(new SimpleCard(CardType.POSITIVE, 5));
        cards.add(new SimpleCard(CardType.POSITIVE, 6));

        cards.add(new SimpleCard(CardType.SWAPPABLE, 1));
        cards.add(new SimpleCard(CardType.SWAPPABLE, 3));

        cards.add(new SimpleCard(CardType.NEGATIVE, 1));
        cards.add(new SimpleCard(CardType.NEGATIVE, 2));
        cards.add(new SimpleCard(CardType.NEGATIVE, 3));
        cards.add(new SimpleCard(CardType.NEGATIVE, 5));

        return cards;
    }

    /**
     * Grabs all cards in possession of a player
     * 
     * @return unmodifiableList of cards
     */
    public List<Card> getCardsInPossession(){
        return Collections.unmodifiableList(cardsInPossession);
    }
    
    /**
     * Checks if all the cards in the Player's side deck are not equal to null
     *
     * @return returns true if none of the player's cards are null
     */
    public boolean hasSideDeck() {
        return this.sideDeck.size() == 6 && this.sideDeck.stream().noneMatch(Objects::isNull);
    }

    
    public List<Card> getSideDeck(){
        return sideDeck;
    }

    public void addCardsToSideDeck(Card[] cards){
        for(Card card: cards)
        sideDeck.add(card);
    }
    
    /**
     * Grabs the players name
     * 
     * @return String with the name 
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Grabs the player name
     * 
     * @return StringProperty with the playername
     */
    public StringProperty nameProperty(){
        SimpleStringProperty nameProp = new SimpleStringProperty();
        nameProp.set(name);
        return nameProp;
    }

    /**
     * Grabs the credits of a player
     * 
     * @return int with the credits
     */
    public int getCredits() {
        return this.credits;
    }
    
    /**
     * Grabs the credits of a player
     * 
     * @return StringProperty with the credits
     */
    public StringProperty creditProp(){
        SimpleStringProperty creditProp = new SimpleStringProperty();
        creditProp.set("" + credits);
        return creditProp;
    }
    
    /**
     * Grabs the birthyear of a player
     * 
     * @return int with the birthyear
     */
    public int getBirthYear() {
        return this.birthYear;
    }
    
    /**
     * Grabs the birthyear of a player
     * 
     * @return StringProperty with the birthyear
     */
    public StringProperty birthYearProp(){
        SimpleStringProperty birthyearProp = new SimpleStringProperty();
        birthyearProp.set(""+ birthYear);
        return birthyearProp;
    }

    /**
     * Controle if the name comply with the conditions:
     * <ul>
     * <li>At least 3 characters long,</li>
     * <li>Unique,</li>
     * <li>No spaces or punctuation marks,</li>
     * <li>No number as first character</li>
     * </ul>
     *
     * Sidenote: Unique is controled in the DomainController
     *
     * @param name name to controle: DR_SPELER_NAAM_LEEFTIJD
     */
    private void validateName(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException("ERROR_NULLPOINTEREXCEPTION");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("ERROR_EMPTY_PLAYERNAME");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("ERROR_MINLENGTH_PLAYERNAME");
        }
        if (name.matches(".*[ ,;?!:.].*")) {
            throw new IllegalArgumentException("ERROR_PUNCTORSPACE_PLAYERNAME");
        }
        if (name.matches("^\\d.*")) {
            throw new IllegalArgumentException("ERROR_STARTWITHNUMBER_PLAYERNAME");
        }
    }

    /**
     * Controle if the birthyear of a player comply with the conditions:
     * 
     * <ul>
     * <li>At least 6 years old,</li>
     * <li>At most 99 years old</li>
     * </ul>
     *
     * sidenote: DR_SPELER_NAAM_LEEFTIJD
     * 
     * @param birthYear birthyear to controle
     * 
     * 
     */
    private void validateBirthYear(int birthYear) throws IllegalArgumentException {
        final int MIN_AGE = 6; // see also: DR_SPELER_NAAM_LEEFTIJD
        final int MAX_AGE = 99; // see also: DR_SPELER_NAAM_LEEFTIJD

        int currentYear = LocalDate.now().getYear();

        if (Integer.toString(birthYear).length() != 4) {
            throw new IllegalArgumentException("ERROR_MALFORMED_PLAYERBIRTHDATE");
        }
        if (birthYear > (currentYear - MIN_AGE)) {
            throw new IllegalArgumentException("ERROR_MINAGE_PLAYERBIRTHDATE");
        }
        if (birthYear < (currentYear - MAX_AGE)) {
            throw new IllegalArgumentException("ERROR_MAXAGE_PLAYERBIRTHDATE");
        }
    }


    /**
     * Grab an array of string with all data (name, birthyear, credits) of a player
     * 
     * @return Array of String
     */
    public String[] toStringArray() {
        return new String[]{
            this.getName(),
            Integer.toString(this.getBirthYear()),
            Integer.toString(this.getCredits())
        };
    }

    
    /**
     * Grab a string with the data (name, birthyear, credits) of a player
     * 
     * @return a string name - birthyear - credits
     */
    @Override
    public String toString() {
        return "[" + name + " - " + birthYear + " - " + credits + "]";
    }

    //TODO - getal moet positief zijn
    /**
     *  increment score of a player
     * 
     * @param creditsToBeAdded A positive integer with which to increment the current credit score
     */
    public void updateCredits(int creditsToBeAdded) {
        this.credits += creditsToBeAdded;
    }

    /**
     * Compares this Player with the specified player for order.
     * 
     * @param p player to compare
     * @return 
     */
    @Override
    public int compareTo(Player p) {
        return (this.name).compareTo(p.name);
    }

    /**
     * Indicates whether some other players is "equal to" this comparator.
     * 
     * @param p1 first player to compare
     * @param p2 first player to compare
     * @return 
     */
    @Override
    public int compare(Player p1, Player p2) {
        return p1.birthYear - p2.birthYear;
    }
    
    /**
     * Indicates whether some other object is "equal to" this one.
     * 
     * @param o
     * @return true if both are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return name.equals(player.name);
    }

    /**
     * Returns a hash code value for the Card.
     * 
     * @return int with a hashcode
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
