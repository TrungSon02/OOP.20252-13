public class Player {
    private String name;
    private double score;
    private boolean isPlaying;
    public Player(String name, double score) {
        this.name = name;
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
        if(points < 0){
            this.score += points;
        }
    }
    @Override
    public String toString() {
        return "Player{" + "name='" + name + '\'' + ", score=" + score + ", isPlaying=" + isPlaying + '}';
    }
}
