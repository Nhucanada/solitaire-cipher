/**
 * Your name here: Nathan Hu
 * Your McGill ID here: 261147733
 **/

public class SolitaireCipher {
    public Deck key;

    public SolitaireCipher (Deck key) {
        this.key = new Deck(key); // deep copy of the deck
    }

    /*
     * TODO: Generates a keystream of the given size
     */
    public int[] getKeystream(int size) {

        int[] keystream = new int[size];


        for (int i = 0; i < size; i++) {

            int value = key.generateNextKeystreamValue();
            keystream[i] = value;

        }

        return keystream;

    }

    /*
     * TODO: Encodes the input message using the algorithm described in the pdf.
     */
    public String encode(String msg) {

        String message = "";
        String encodedMessage = "";

        for(int i = 0; i < msg.length(); i++){

            char c = msg.charAt(i);

            if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')){

                message += (c + "").toUpperCase();

            }

        }

        int[] keyStream = getKeystream(message.length());

        for(int k = 0; k < message.length(); k++){

            int unicodeChar = message.charAt(k) + keyStream[k];

            while(unicodeChar > 'Z'){

                unicodeChar -= 26;

            }

            encodedMessage += (char) unicodeChar;

        }

        return encodedMessage;

    }

    /*
     * TODO: Decodes the input message using the algorithm described in the pdf.
     */
    public String decode(String msg) {

        String decodedMessage = "";
        int[] keyStream = getKeystream(msg.length());

        for(int i = 0; i < msg.length(); i++){

            int unicodeChar = msg.charAt(i) - keyStream[i];

            while(unicodeChar < 'A'){

                unicodeChar += 26;

            }

            decodedMessage += (char) unicodeChar;
        }

        return decodedMessage;

    }

}
