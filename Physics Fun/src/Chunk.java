import java.util.ArrayList;

public class Chunk {
    int xPos; // 0 - 152
    int yPos; // 0 - 94
    int size = 10;
    boolean shown = false;
    ArrayList<Particle> containedParticles = new ArrayList<>();

    
    public Chunk(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void showChunk() {
        this.shown = true;
        StaticObjects.showChunks.add(this);
    }

    public void stopShowingChunk() {
        this.shown = false;
        StaticObjects.showChunks.remove(this);
    }

    public int getxPos() {
        return xPos;
    }


    public void setxPos(int xPos) {
        this.xPos = xPos;
    }


    public int getyPos() {
        return yPos;
    }


    public void setyPos(int yPos) {
        this.yPos = yPos;
    }


    public int getSize() {
        return size;
    }


    public void setSize(int size) {
        this.size = size;
    }


    public ArrayList<Particle> getContainedParticles() {
        return containedParticles;
    }


    public void addContainedParticle(Particle particle) {
        this.containedParticles.add(particle);
    }

    public void clearContainedParticles() {
        this.containedParticles.clear();
    }


}
