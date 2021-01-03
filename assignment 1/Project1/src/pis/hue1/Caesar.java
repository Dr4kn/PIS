package pis.hue1;

/**
 *     the key is the encryption key set with "setKey"
 *
 *      Klasseninvariante:
 *      a)  key != null
 *      b)  key no spaces
 *
 *      String key is set with setzeLosung or by using the constructor
 */
public class Caesar implements Codec
{
    private String key;


    /**
     * encodes the Text with the Caesar encryption method an encryption key has to be set first
     *
     * @param plainText the text that has to be encoded. The Content is irrelevant
     * @return A String that encoded the text with the key that was set
     */
    public String kodiere(String plainText)
    {
        return performEncodeAndDecode(plainText, false);
    }


    /**
     * decodes the Text with the Caesar encryption method an encryption key has to be set first
     *
     * @param cipherText the encoded Text that you wish to decode
     * @return the decoded text.
     */
    public String dekodiere(String cipherText)
    {
        return performEncodeAndDecode(cipherText, true);
    }


    /**
     * does encode and decode in one. You have to decide what it should do, because it does neither by default
     *
     * @param text to be encoded or decoded
     * @param decode true if want to decode/false to encode
     * @return text encoded or decoded depending on the choosen option
     */
    private String performEncodeAndDecode(String text, boolean decode)
    {
        int mainOffset = key.length();
        int offset;
        char beginning;
        int untilReset;
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray())
        {
            if (character != ' ')
            {
                /*
                checks which kind of offset to choose and which the start point should be for the algorithm below
                 */

                if (Character.isLowerCase(character))
                {
                    beginning = 'a';
                    untilReset = 26;
                }
                else if (Character.isUpperCase(character))
                {
                    beginning = 'A';
                    untilReset = 26;
                }
                else
                {
//                    System.out.println("Character not supported entering it without being shifted");
                    beginning = character;
                    untilReset = 1;
                }


                /*
                if it should decode it calculates the offset backwards from 'untilReset'. Alphabet would be 26
                Numbers would be 10. Only the offset changes for both decode and encode the same algorithm is used.
                 */

                if (decode)
                {
                    offset = untilReset - mainOffset;
                    if (offset < 0)
                    {
                        offset *= -1;
                    }
                }
                else
                {
                    offset = mainOffset;
                }

                /*
                shifts a char Array specified number of places to the right. If it would go over the next letter
                e.g. Z -> A it resets it to A with modulo that changes if it is letters (26) or numbers (10)
                */

                int originalAlphabetPosition = character - beginning;
                int newAlphabetPosition = (originalAlphabetPosition + offset) % untilReset;
                char newCharacter = (char) (beginning + newAlphabetPosition);
                result.append(newCharacter);

            }
            else
            {
                result.append(' ');
            }
        }

        return result.toString();
    }


    /**
     * @return the key that was set
     */
    public String gibLosung()
    {
        return key;
    }


    /**
     * @param key sets encryption key
     * @throws IllegalArgumentException If (key.length == 0 || contains spaces)
     */
    public void setzeLosung(String key) throws IllegalArgumentException
    {
        if (key.length() == 0)
        {
            throw new IllegalArgumentException("Key must at least contain one symbol. \nKey contains zero symbols");
        }
        else if ( key.contains(" "))
        {
            throw new IllegalArgumentException("Key contains at least one space. \nThe Key can't have any spaces");
        }
        this.key = key;
    }
}
