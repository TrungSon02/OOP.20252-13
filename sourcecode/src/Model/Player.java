package Model;
import java.util.Random;

public class Player {
    private String name;    
    private double score;
    private String avatar;

    static final String[] PLAYER_ADJECTIVES = {"Hungry", "Brave", "Cunning", "Swift", "Almighty", "Wise", "Fierce", "Smart", "Quick"};
    static final String[] PLAYER_NOUN = {"Hippo", "Giraffe", "Elephant", "Lion","Tiger", "Bear", "Wolf", "Fox", "Eagle", "Shark","Sloth","Turtle","Pattrick","Banana","Chongus",};
    
    private String generatePlayerNames(){
        Random random = new Random();
        String tmp = PLAYER_ADJECTIVES[random.nextInt(PLAYER_ADJECTIVES.length)] + PLAYER_NOUN[random.nextInt(PLAYER_NOUN.length)];
        return tmp;
    }
 
    public Player() {
        this.name = generatePlayerNames();
        this.score = 0;
        this.avatar = "/image/avatar/Pattrick_" + (new Random().nextInt(9)+1) + ".jpg";
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public double getScore() {
        return score;
    }

    public void resetScore(){
        this.score = 0;
    }


    public void updateScore(int points){
        this.score += points;
    }

}

