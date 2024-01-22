import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;

public class App {
    public static Panel panel = new Panel();
    public static int frameCount = 0;

    
    public static void main(String[] args) throws Exception {
        boolean running = true;
        double frameTimeStart, frameTimeEnd, frameTime;
        double dt = StaticObjects.dt;
        int i, j, h;
        ArrayList<Particle> particles = StaticObjects.particles;

        

        System.out.println("Hello, World!");

        JFrame frame = new JFrame("Particle Physics Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1530, 950); 
        frame.add(panel); 
        

        
        frame.setVisible(true);
        panel.setVisible(false);

        
        // Create 14535 chunks covering screen
        for (i = 0; i < 14535; i++) {
            int x = i / 95;
            int y = i % 95;

            Chunk chunk = new Chunk(x, y);
            chunk.setChunkArrayIndex(i);
            StaticObjects.chunks.add(chunk);
        }
        
        // Loop over and spawn 1000 random particles with diameter 5
        for(i = 0; i < 20; i++) {
            Particle p = new Particle(10);
            StaticObjects.particles.add(p); 

            // Assign particles to chunk
            int chunkX = (int) (p.x / 10);
            int chunkY = (int) (p.y / 10);

            int chunkIndex = chunkX * 95 + chunkY;
            StaticObjects.chunks.get(chunkIndex).addContainedParticle(p);;
        }
        
        // System.out.println()
        panel.setVisible(true);
        int possibleFrames = 0;

        while (running == true) {

            

            frameTimeStart = System.nanoTime();


            if (frameCount % 10 == 0) {
                panel.setPossibleFrames(possibleFrames);  
            }
            
            update();
            
            frameTimeEnd = System.nanoTime();
            

            double timeToSleep = dt * 1000 - Math.max(0,(frameTimeEnd - frameTimeStart) / 1000000);
            possibleFrames = (int)(1 / ((StaticObjects.dt * 1000 - timeToSleep) / 1000));
            // System.out.println(1 / ((StaticObjects.dt * 1000 - timeToSleep) / 1000));
            Thread.sleep(Math.max(0, (long)timeToSleep)); // dividing by 1000000 converts ns to ms
            

        }

    }
    public static void update() {

        // Update Particle Pos
        
        int i, j;
        frameCount++;
        ArrayList<Particle> particles = StaticObjects.particles;
        ArrayList<Chunk> chunks = StaticObjects.chunks;
        

        // Loop through chunks
        for (i = 0; i < chunks.size(); i++) {
            Chunk currentChunk = chunks.get(i);
            ArrayList<Chunk> thisAndAdjChunks = currentChunk.getThisAndAdjChunks();

            // Create empty list to hold all particles  
            ArrayList<Particle> allNearbyParticles = new ArrayList<>();

            // Loop through each chunk
            for (Chunk chunk : thisAndAdjChunks) {
                ArrayList<Particle> chunkParticles = chunk.getContainedParticles();
                allNearbyParticles.addAll(chunkParticles); 
            }
            if (allNearbyParticles.size() > 1) {
                // Loop through particles in chunk
                for (int p1Index = 0; p1Index < allNearbyParticles.size(); p1Index++) {
                    Particle p1 = allNearbyParticles.get(p1Index);
                    // Loop through later particles in chunk and calc influences
                    for (j = p1Index + 1; j < allNearbyParticles.size(); j++) {
                        Particle p2 = particles.get(j);

                        double[] p1InfluenceArray = calcParticleInfluence(p1, p2);
                        boolean cont = p1InfluenceArray[0] != 0 || p1InfluenceArray[1] != 0;
                        if (cont)
                            // System.out.println("p1InfluenceArray[0]: " + p1InfluenceArray[1] + "equal to zero? ");
                            // System.out.println(p1InfluenceArray[0]);

                            // System.out.println(p1InfluenceArray[0] != 0.0);
                            p1.addInfluence(p1InfluenceArray[0], p1InfluenceArray[1]);
                            // System.out.println(p1InfluenceArray[0] + " " + (p1InfluenceArray[1]));
                            p2.addInfluence(-1 * p1InfluenceArray[0], -1 * p1InfluenceArray[1]);
                            if (i == 7000) {
                                System.out.println("p1 influences list: " + p1.getInfluencesList());
                        }
                    }
                }
            }
            allNearbyParticles.clear();
            
            currentChunk.clearContainedParticles();
        }
        
        // Loop through particles
        for (i = 0; i < particles.size(); i++) {
            Particle p1 = particles.get(i);
            p1.processMovement();

        // Assign particles to chunk
        int chunkX = (int) (p1.x / 10);
        int chunkY = (int) (p1.y / 10);

        int chunkIndex = chunkX * 95 + chunkY;
        if (chunkIndex > 14534) {
            chunkIndex = 14534;
        }
        StaticObjects.chunks.get(chunkIndex).addContainedParticle(p1);
        
        }

        // Highlight given Chunk
        StaticObjects.chunks.get(7000).showChunk();
        // System.out.println(StaticObjects.chunks.get(7000).getContainedParticles());


        // Update Screen
        panel.repaint();
    }
    
    public static double[] calcParticleInfluence(Particle p1, Particle p2) {
        System.
        // See https://www.desmos.com/calculator/oducvmmkdw

        // Calculate unit vector pointing from p2 to p1
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        double dist = Math.sqrt(dx*dx + dy*dy);

        double avgSize = (p1.getR() * p1.getR() + p2.getR() * p2.getR()) / 2;
        
        // Using Lennard-Jones formula
        double A = 10; 
        double B = 5;
        double r = dist / avgSize; // normalize distance
        double rPower12 = Math.pow(r, 12); 
        double rPower6 = Math.pow(r, 6);
        double influence = -A / rPower12 + B / rPower6;

        double nx = dx / dist; 
        double ny = dy / dist;
      
        // Scale influence by unit vector to get x and y components
        double xInfluence = influence * nx;
        double yInfluence = influence * ny;
      
        return new double[]{xInfluence, yInfluence};
      
      }

}
// TODO: make speed influence influences so faster balls effect a bigger influence on their opponents
// TODO: add proper accelleration

// the quick browb fox ran over the brown dofg