import java.util.Random;

public class Player {
    private String name;    
    private double score;

    static String[] playerAdjectives = {"Hungry", "Brave", "Cunning", "Swift", "Almighty", "Wise", "Fierce", "Smart", "Quick"};
    static String[] playerNoun = {"Hippo", "Giraffe", "Elephant", "Lion","Tiger", "Bear", "Wolf", "Fox", "Eagle", "Shark","Sloth","Turtle","Pattrick","Banana","Chongus",};
    
    private String generatePlayerNames(){
        //FU AI
        Random random = new Random();
        String tmp = playerAdjectives[random.nextInt(playerAdjectives.length)] + " " + playerNoun[random.nextInt(playerNoun.length)];
        return tmp;
    }
 
    public Player() {
        this.name = generatePlayerNames();
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public void resetScore(){
        this.score = 0;
    }


    public void addScore(int points){
        if(points > 0){
            this.score += points;
        }
    }

}
