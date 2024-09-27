/**
 * Your name here: Nathan Hu
 * Your McGill ID here: 261147733
 **/

import java.util.Random;

public class Deck {
    public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
    public static Random gen = new Random();

    public int numOfCards; // contains the total number of cards in the deck
    public Card head; // contains a pointer to the card on the top of the deck

    /*
     * TODO: Initializes a Deck object using the inputs provided
     */
    public Deck(int numOfCardsPerSuit, int numOfSuits) {
        if(numOfCardsPerSuit > 13 || numOfSuits < 1 || numOfCardsPerSuit < 1 || numOfSuits > suitsInOrder.length){

            throw new IllegalArgumentException("Input Error");

        }

        for (int i = 0; i < numOfSuits; i++) {

            String currentSuit = suitsInOrder[i];

            for (int k = 1; k <= numOfCardsPerSuit; k++) {

                PlayingCard card = new PlayingCard(currentSuit, k);
                addCard(card);

            }

        }

        addCard(new Joker("red"));
        addCard(new Joker("black"));

    }

    /*
     * TODO: Implements a copy constructor for Deck using Card.getCopy().
     * This method runs in O(n), where n is the number of cards in d.
     */
    public Deck(Deck d) {

        Card currentCard = d.head;

        for(int i = 0; i < d.numOfCards; i++){

            Card cardToAdd = currentCard.getCopy();
            addCard(cardToAdd);

            currentCard = currentCard.next;

        }

    }

    /*
     * For testing purposes we need a default constructor.
     */
    public Deck() {}

    /*
     * TODO: Adds the specified card at the bottom of the deck. This
     * method runs in $O(1)$.
     */
    public void addCard(Card c) {

        if(head == null){

            head = c;
            head.next = c;
            head.prev = c;

        }

        else{

            c.prev = head.prev;
            head.prev.next = c;
            head.prev = c;
            c.next = head;

        }

        numOfCards += 1;

    }

    /*
     * TODO: Shuffles the deck using the algorithm described in the pdf.
     * This method runs in O(n) and uses O(n) space, where n is the total
     * number of cards in the deck.
     */
    public void shuffle() {

        Card[] cards = new Card[numOfCards];
        Card currentCard = head;
        Deck shuffledDeck = new Deck();

        for(int i = 0; i < numOfCards; i++){

            cards[i] = currentCard;
            currentCard = currentCard.next;

        }

        for(int k = numOfCards-1; k > 0; k--){

            int j = gen.nextInt(k+1);
            Card temp = cards[k];
            cards[k] = cards[j];
            cards[j] = temp;

        }


        for(int m = 0; m < numOfCards; m++){

            shuffledDeck.addCard(cards[m]);

        }

        head = shuffledDeck.head;

    }

    /*
     * TODO: Returns a reference to the joker with the specified color in
     * the deck. This method runs in O(n), where n is the total number of
     * cards in the deck.
     */
    public Joker locateJoker(String color) {

        Card currentCard = head.next;

        while (currentCard != head) {

            if (currentCard instanceof Joker && ((Joker) currentCard).getColor().equals(color)) {

                return (Joker)currentCard;

            }

            currentCard = currentCard.next;

        }

        return null;
    }

    /*
     * TODO: Moved the specified Card, p positions down the deck. You can
     * assume that the input Card does belong to the deck (hence the deck is
     * not empty). This method runs in O(p).
     */
    public void moveCard(Card c, int p) {

        Card top;
        Card next;
        Card nextNext;
        Card bottom;

        if (c == head) {

            for (int i = 0; i < p; i++) {

                top = c;
                next = c.next;
                nextNext = next.next;
                bottom = c.prev;
                next.next = top;
                next.prev = nextNext;
                bottom.next = next;
                top.next = nextNext;
                top.prev = next;
                nextNext.prev = top;

            }

        }

        else {

            for (int k = 0; k < p; k++) {

                next = c;
                nextNext = c.next;
                top = c.prev;
                bottom = c.next.next;
                next.next = bottom;
                next.prev = nextNext;
                nextNext.next = next;
                nextNext.prev = top;
                top.next = nextNext;
                bottom.prev = next;

            }

        }

    }

    /*
     * TODO: Performs a triple cut on the deck using the two input cards. You
     * can assume that the input cards belong to the deck and the first one is
     * nearest to the top of the deck. This method runs in O(1)
     */
    public void tripleCut(Card firstCard, Card secondCard) {

        Card zeroCard = firstCard.prev;
        Card thirdCard = secondCard.next;

        if (head.prev == secondCard) {

            secondCard.next = head;
            head.prev = secondCard;
            head = firstCard;
            Card tmp = zeroCard;
            zeroCard.next = head;
            head.prev = tmp;

        }

        else if (zeroCard == head.prev) {

            zeroCard.next = head;

            head = thirdCard;
            Card tmp = thirdCard.next;
            thirdCard.prev = head;
            head.next = tmp;
            head.prev = secondCard;

        }

        else {

            zeroCard.next = thirdCard;
            thirdCard.prev = zeroCard;
            Card tail = head.prev;
            Card temp2 = head;
            head = thirdCard;
            tail.next = firstCard;
            firstCard.prev = tail;
            temp2.prev = secondCard;
            secondCard.next = temp2;

        }

    }

    /*
     * TODO: Performs a count cut on the deck. Note that if the value of the
     * bottom card is equal to a multiple of the number of cards in the deck,
     * then the method should not do anything. This method runs in O(n).
     */
    public void countCut() {

        Card last = head.prev;
        int num = last.getValue() % numOfCards;

        if (num != 0) {

            Card cut = head;

            for (int i = 0; i < num; i++) {

                cut = cut.next;

            }

            if (cut != head.prev) {

                Card prevTail = head.prev;
                Card newSecondLast = cut.prev;
                Card prevSecondLast = head.prev.prev;
                head.prev = prevSecondLast;
                prevSecondLast.next = head;
                prevTail.prev = newSecondLast;
                newSecondLast.next = prevTail;
                head = cut;
                head.prev = prevTail;
                prevTail.next = head;

            }
        }

    }

    /*
     * TODO: Returns the card that can be found by looking at the value of the
     * card on the top of the deck, and counting down that many cards. If the
     * card found is a Joker, then the method returns null, otherwise it returns
     * the Card found. This method runs in O(n).
     */
    public Card lookUpCard() {

        Card top = head;

        if(top instanceof Joker){

            return null;

        }

        else{

            int value = head.getValue();
            Card currentCard = head;

            for(int i = 0; i < value ; i++){

                currentCard = currentCard.next;

            }

            if (currentCard instanceof Joker) {

                return null;

            }
            else {

                return currentCard;

            }
        }

    }

    /*
     * TODO: Uses the Solitaire algorithm to generate one value for the keystream
     * using this deck. This method runs in O(n).
     */
    public int generateNextKeystreamValue() {

        Card card = null;

        while (card == null) {

            Joker red = locateJoker("red");
            moveCard(red, 1);
            Joker black = locateJoker("black");
            moveCard(black, 2);

            if (getPos(red) > getPos(black)) {

                tripleCut(black, red);

            }

            else {

                tripleCut(red, black);

            }

            countCut();
            card = lookUpCard();

        }

        int cardValue = card.getValue();

        return cardValue;

    }

    public int getPos(Card card) {

        Card currentCard = head;

        for(int i = 0; i < numOfCards; i++){

            if(currentCard.equalTo(card)){

                return i;

            }

            currentCard = currentCard.next;

        }

        return -1;

    }


    public abstract class Card {
        public Card next;
        public Card prev;

        public abstract Card getCopy();
        public abstract int getValue();

        public abstract boolean equalTo(Card c);

    }

    public class PlayingCard extends Card {
        public String suit;
        public int rank;

        public PlayingCard(String s, int r) {
            this.suit = s.toLowerCase();
            this.rank = r;
        }

        public String toString() {
            String info = "";
            if (this.rank == 1) {
                //info += "Ace";
                info += "A";
            } else if (this.rank > 10) {
                String[] cards = {"Jack", "Queen", "King"};
                //info += cards[this.rank - 11];
                info += cards[this.rank - 11].charAt(0);
            } else {
                info += this.rank;
            }
            //info += " of " + this.suit;
            info = (info + this.suit.charAt(0)).toUpperCase();
            return info;
        }

        public PlayingCard getCopy() {
            return new PlayingCard(this.suit, this.rank);
        }

        public int getValue() {
            int i;
            for (i = 0; i < suitsInOrder.length; i++) {
                if (this.suit.equals(suitsInOrder[i]))
                    break;
            }

            return this.rank + 13*i;
        }

        public boolean equalTo(Card card) {

            if(card instanceof PlayingCard){

                if(((PlayingCard)card).suit == this.suit && ((PlayingCard)card).rank == this.rank){

                    return true;

                }

            }

            return false;

        }

    }

    public class Joker extends Card{
        public String redOrBlack;

        public Joker(String c) {
            if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black"))
                throw new IllegalArgumentException("Jokers can only be red or black");

            this.redOrBlack = c.toLowerCase();
        }

        public String toString() {
            //return this.redOrBlack + " Joker";
            return (this.redOrBlack.charAt(0) + "J").toUpperCase();
        }

        public Joker getCopy() {
            return new Joker(this.redOrBlack);
        }

        public int getValue() {
            return numOfCards - 1;
        }

        public String getColor() {
            return this.redOrBlack;
        }

        public boolean equalTo(Card card){

            if(card instanceof Joker){

                if(((Joker)card).getColor() == this.redOrBlack){

                    return true;

                }

            }

            return false;

        }

    }

}
