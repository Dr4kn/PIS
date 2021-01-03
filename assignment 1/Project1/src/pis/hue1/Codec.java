package pis.hue1;

interface Codec
{
    String kodiere(String plainText);
    String dekodiere(String cipherText);
    String gibLosung();
    void setzeLosung(String key) throws IllegalArgumentException; // if the key is useless
}
