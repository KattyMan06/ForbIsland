import java.awt.image.BufferedImage;

public class Card {

    private BufferedImage image;
    private String cardName;
    private String cardType;

    public Card(String name, String type){
        cardName = name;
        cardType = type;
    }
    public String getCardName(){
        return cardName;
    }

    public BufferedImage getImage(){
        return image;
    }

    public String getType(){
        return cardType;
    }

    public void setImage(BufferedImage i) { image = i; }
}
