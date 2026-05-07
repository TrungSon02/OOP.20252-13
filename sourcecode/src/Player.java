import java.util.Random;

public class Player {
    private String name;
    private double score;
    private boolean isPlaying;

    static String[] playerAdjectives = {"Hungry", "Brave", "Cunning", "Swift", "Almighty", "Wise", "Fierce", "Smart", "Quick", " "};
    static String[] playerNoun = {"Hippo", "Giraffe", "Elephant", "Lion", "Tiger", "Bear", "Wolf", "Fox", "Eagle", "Shark","Sloth","Turtle",
                            " ","Amazon","Pattrick","Banana","Walter","Chongus","man"};

    private String[] playerNames = new String[playerAdjectives.length * playerNoun.length];
    
    {
        int index = 0;
        for (String adjective : playerAdjectives) {
            for (String noun : playerNoun) {
                playerNames[index++] = adjective + " " + noun;
            }
        }
    }

    public Player(String name, double score) {
        Random random = new Random();
        this.name = playerNames[random.nextInt(playerNames.length)];
        this.score = 0;
        this.isPlaying = false;
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

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void addScore(int points){
        if(points > 0){
            this.score += points;
        }
    }
    @Override
    public String toString() {
        return "Player{" + "name='" + name + '\'' + ", score=" + score + ", isPlaying=" + isPlaying + '}';
    }
}
