import java.util.ArrayList;

public class Chunk {
    int xPos; // 0 - 152
    int yPos; // 0 - 94
    int size = 10;
    boolean shown = false;
    ArrayList<Particle> containedParticles = new ArrayList<>();
    int chunkArrayIndex;

    
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

    public int getChunkArrayIndex() {
        return chunkArrayIndex;
    }

    public void setChunkArrayIndex(int chunkArrayIndex) {
        this.chunkArrayIndex = chunkArrayIndex;
    }

    // public void getAdjChunk(int xOffset, int yOffset) {
    //     int adjChunkIndex = chunkArrayIndex + yOffset;
    //     adjChunkIndex += xOffset * 95; 
    //     // Chunk c = StaticObjects.chunks.get
    // }
    public ArrayList<Chunk> getThisAndAdjChunks() {
        ArrayList<Chunk> chunks = StaticObjects.chunks;
        ArrayList<Chunk> adjChunks = new ArrayList<>();
        // System.out.println(chunkArrayIndex);
        if (xPos != 0) {
            Chunk leftChunk = chunks.get(chunkArrayIndex - 95); 
            if (yPos != 0) {
                Chunk upperLeftChunk = chunks.get(chunkArrayIndex - 96);
                adjChunks.add(upperLeftChunk);
            }
            adjChunks.add(leftChunk);
            if (yPos != 94) {
                Chunk bottomLeftChunk = chunks.get(chunkArrayIndex - 94);
                adjChunks.add(bottomLeftChunk);
            }
        }
        if (yPos != 0) {
            Chunk upChunk = chunks.get(chunkArrayIndex - 1);
            adjChunks.add(upChunk);
        }
        adjChunks.add(this);
        if (yPos != 94) {
            Chunk downChunk = chunks.get(chunkArrayIndex + 1);
            adjChunks.add(downChunk);
        }
        if (xPos != 152) {
            Chunk rightChunk = chunks.get(chunkArrayIndex + 95);
            
            if (yPos != 0) {
                Chunk upperRightChunk = chunks.get(chunkArrayIndex + 94);
                adjChunks.add(upperRightChunk);
            }
            adjChunks.add(rightChunk);  
            if (yPos != 94) {
                
                Chunk bottomRightChunk = chunks.get(chunkArrayIndex + 96);
                adjChunks.add(bottomRightChunk);
            }
        }
        return adjChunks;
    
    }

    @Override
    public String toString() {
        return "Chunk [xPos=" + xPos + ", yPos=" + yPos + ", size=" + size + ", shown=" + shown
                + ", containedParticles=" + containedParticles + ", chunkArrayIndex=" + chunkArrayIndex + "]";
    }


    //     // int adjChunkIndex = chunkArrayIndex + yOffset;
    //     // adjChunkIndex += xOffset * 95; 
    //     // Chunk c = StaticObjects.chunks.get
    // }

}
