package domain;

import persistence.Mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository class for updating, grabbing and pushing cards to the mapper for persistence.
 */
public class CardRepository {

    private static List<Card> defaultCardStack;
    private final Mapper mapper;
    
    /**
     * Creates a card repository and grabs the default card stack from the database.
     *
     * @param mapper A mapper object must be provided with which to map the repository
     */
    public CardRepository(Mapper mapper) {
        this.mapper = mapper;
        defaultCardStack = pullAllCards();
    }

    /**
     * Search a card in the defaultCardStack
     * 
     * @param card string of card
     * @return Card you are looking for. If it doesn't exists it return null
     */
    public static Card findCard(String card) {
        if (defaultCardStack.stream().anyMatch(c -> c.toString().equals(card))) {
            return defaultCardStack.stream().filter(c -> c.toString().equals(card)).findAny().get();
        } else {
            return null;
        }
    }

    /**
     * Lists all purchased cards for a given Player p.
     *
     * @param p The player for which to list the cards they purchased
     * @return A list of cards, ready to be added to the player
     */
    public List<Card> grabPurchasedCardsForPlayer(Player p) {
        // TODO - HILDA - implement CardRepository.listPurchasedCardsForPlayer
        throw new UnsupportedOperationException();
    }

    /**
     * Gets all cards from the card mapper
     *
     * @return Returns a list of Card objects
     */
    private List<Card> pullAllCards() {
        List<String> cards = mapper.getCardMapper().getCards();
        return cards.stream()
                .map(c -> new SimpleCard(mapCardType(c.charAt(0)), Integer.parseInt(String.valueOf(c.charAt(1)))))
                .collect(Collectors.toList());
    }

    /**
     * Converts a character from the mapper to an enumeration literal
     *
     * @param cardTypeChar The character to be converted
     * @return Returns a CardType enumeration literal
     * @throws AssertionError if there is no enumeration literal corresponding to the given character
     */
    private CardType mapCardType(char cardTypeChar) {
        switch (cardTypeChar) {
            case 'N':
                return CardType.NEGATIVE;
            case 'P':
                return CardType.POSITIVE;
            case 'S':
                return CardType.SWAPPABLE;
            default:
                throw new AssertionError();
        }
    }

    /**
     * Method that grabs the starting set of cards given to a player on their creation
     * See DR_SPELER_NEIUWE_STAPEL
     *
     * @return Returns a list of Card objects containing the starter cards
     */
    public List<Card> getDefaultCardStack() {
        return defaultCardStack;
    }

    /**
     * Search the card in the database
     * 
     * @param card string of card 
     * @return card
     */
}
