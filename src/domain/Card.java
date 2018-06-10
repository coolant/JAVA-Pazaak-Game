package domain;

/**
 * Interface containing base behaviour for all types of cards
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
interface Card {

    /**
     * Grabs the type and value of the card and outputs it as a String
     *
     * @return returns a type and value of the card
     */
    String giveDescription();

    /**
     * Grabs the type enum value of the card
     *
     * @return Returns and enum literal for the given type
     */
    CardType getType();
    
    /**
     * Grabs the numeric value of the card
     * 
     * @return int with a numeric value
     */
    int getValue();

    /**
     * Set the numeric value of the card
     * 
     * @param value int numeric value
     */
    void setValue(int value);

    /**
     * Grabs information of the card
     * 
     * @return String with type and value
     */
    @Override
    String toString();

    /**
     * replaces ; in toSting to *
     * 
     * @return String type*value
     */
    String toBoardString();
}
