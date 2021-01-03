package pis.hue1;

/**
 *     the key is the encryption key set with "setKey"
 *
 *      Klasseninvariante:
 *      a)  key != null
 *      b)  key only has letters A-Z
 *
 *      String key is set with setzeLosung or by using the constructor
 */
public class Wuerfel implements Codec
{

    private String key;

    public Wuerfel(String key)
    {
        setzeLosung(key);
    }

    public Wuerfel()
    {
    }


    /**
     * encodes the Text with the Cube encryption method an encryption key has to be set first
     *
     * @param plainText the text that has to be encoded. The Content is irrelevant
     * @return A String that encoded the text with the key that was set
     */
    public String kodiere(String plainText)
    {
        int letter = 'A';
        char charKey[] = key.toUpperCase().toCharArray();
        char charPlainText[] = plainText.toCharArray();
        StringBuilder result = new StringBuilder();

        /*
        Goes through every letter in the alphabet and checks every position of the key if the letter is in there.
        If it is, the char at the letter position is added to the String Builder. To the letter position is then added
        the length of the key string until the Text is over.
         */

        while (letter <= 'Z')
        {
            for(int letterPosition = 0; letterPosition < key.length(); letterPosition++)
            {
                if (charKey[letterPosition] == letter)
                {
                    for (int i = letterPosition; i < plainText.length(); i += key.length())
                    {
                        result.append(charPlainText[i]);
                    }
                }
            }
            letter++;
        }

        return result.toString();
    }


    /**
     * decodes the Text with the Cube encryption method an encryption key has to be set first
     *
     * @param cipherText the encoded Text that you wish to decode
     * @return the decoded text.
     */
    public String dekodiere(String cipherText)
    {
        int letter = 'A';
        int numberOfLetters = 0;
        char charKey[] = key.toUpperCase().toCharArray();
        char charCipherText[] = cipherText.toCharArray();
        char decipheredText[] = new char[cipherText.length()];

        /*
        Goes through every letter in the alphabet and checks every position of the key if the letter is in there.
        If it is, the position is used to put it the correct position in the decipheredText and then adds the key length
        on it every time after that. This results in the char Array being filled at the correct encrypted position.
        The ciphered Text is gone through from left to right
         */

        while (letter <= 'Z')
        {
            for(int letterPosition = 0; letterPosition < key.length(); letterPosition++)
            {
                if (charKey[letterPosition] == letter && numberOfLetters < cipherText.length())
                {
                    for (int i = letterPosition; i < cipherText.length(); i += key.length())
                    {
                        decipheredText[i] = charCipherText[numberOfLetters];
                        numberOfLetters++;
                    }
                }
            }
            letter++;
        }

        return String.valueOf(decipheredText);
    }


    /**
     * @return the encryption key
     */
    public String gibLosung()
    {
        return key;
    }


    /**
     * The key can only contain letters A - Z and can't be empty
     *
     * @param key sets encryption key
     * @throws IllegalArgumentException if (key != A-Z || key.length == 0)
     */
    public void setzeLosung(String key) throws IllegalArgumentException
    {
        for (char character : key.toUpperCase().toCharArray())
        {
            if (character < 'A' || character > 'Z')
            {
                throw new IllegalArgumentException("The Key can only have letters from 'A' to 'Z' and nothing else");
            }
        }
        this.key = key;

        if (key.length() < 2)
        {
            throw new IllegalArgumentException("Must Contain at least 2 Character");
        }
    }
}
