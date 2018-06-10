package domain;

/**
 * Concrete klasse die eenvoudige kaarten (kaarten zonder invloed op andere
 * speelkaarten op het veld) definieert
 * <p>
 * SimpleCard is een realisatie van het Kaart interface en implementeert dus ook
 * alle abstracte methodes. Deze definieert een correcte werking van SimpleCard
 * en alle andere implementatieklassen. SimpleCard maakt gebruik van de CardType
 * enumeratie om bij te houden welk type kaart geinstantieerd is.
 * </p>
 *
  * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 * @since 2017-02-23
 * @version 1.0
 */
public class SimpleCard implements Card {

    private int value;
    private CardType type;

    /**
     * Contstructor create a new SimpleCard object
     * 
     * @param type type of the card
     * @param value numeric value of the cards
     */
    public SimpleCard(CardType type, int value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Grabs a description of the card
     * 
     * @return a string with the card type + value
     */
    @Override
    public String giveDescription() {
        String descr;
        
        switch(this.type) {
            case POSITIVE:
                descr = "+";
                break;
            case NEGATIVE:
                descr = "-";
                break;
            case SWAPPABLE:
                descr = "±";
                break;
            default:
                throw new AssertionError();
        }
        
        descr += Integer.toString(this.value);
        
        return descr;
    }

    /**
     * Grabs the type of the card
     * 
     * @return Cardtype, type of the enum see Card
     */
    @Override
    public CardType getType() {
        return this.type;
    }

    /**
     * set type of the card
     * 
     * @param type Cardtype, type of the enum see Card
     */
    public void setType(CardType type) {
        this.type = type;
    }

    /**
     * Grabs the numeric value of the card
     * 
     * @return int with numeric value of the card
     */
    @Override
    public int getValue() {
        return value;
    }
    /**
     * set value of the card
     * 
     * @param value numeric value
     */
    @Override
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Grabs information of the card
     * 
     * @return String with type;value
     */
    @Override
    public String toString() {
        return getType() + ";" + getValue();
    }

    /**
     * replaces ; in toSting to *
     * 
     * @return String type*value
     */
    @Override
    public String toBoardString() {
        return toString().replace(';', '*');
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

        SimpleCard that = (SimpleCard) o;

        return value == that.value && type == that.type;
    }

    /**
     * Returns a hash code value for the Card.
     * 
     * @return int with a hashcode
     */
    @Override
    public int hashCode() {
        int result = value;
        result = 31 * result + type.hashCode();
        return result;
    }
}
