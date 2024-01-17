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
        for (i = 0; i < 14534; i++) {
            int x = i / 95;
            int y = i % 95;

            Chunk chunk = new Chunk(x, y);
            StaticObjects.chunks.add(chunk);
        }
        
        // Loop over and spawn 1000 random particles with diameter 5
        for(i = 0; i < 300; i++) {
            Particle p = new Particle(5);
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

        // Loop through chunks, calc forces, and clear them
        for (i = 0; i < chunks.size(); i++) {
            Chunk c = chunks.get(i);
            int numContainedParticles = c.getContainedParticles().size();
           
            
            // Loop through particles in chunk
            for (int p1Index = 0; p1Index < numContainedParticles; p1Index++) {
                Particle p1 = particles.get(p1Index);
                
                // Loop through later particles in chunk
                for (j = p1Index + 1; j < numContainedParticles; j++) {
                    Particle p2 = particles.get(j);

                    double[] p1InfluenceArray = calcParticleInfluence(p1, p2);
                    double[] p2InfluenceArray = Arrays.stream(p1InfluenceArray).map(x -> x * -1).toArray();

                    p1.addToInfluencesList(p1InfluenceArray);
                    


                }


                
            }



            
            c.clearContainedParticles();
        }
        
        // Loop through particles
        for (i = 0; i < particles.size(); i++) {
            Particle p1 = particles.get(i);
            p1.processMovement();

        // Assign particles to chunk
        int chunkX = (int) (p1.x / 10);
        int chunkY = (int) (p1.y / 10);

        int chunkIndex = chunkX * 95 + chunkY;
        // System.out.println("Chunk X: " + chunkX);
        // System.out.println("Chunk Y: " + chunkY);

        // System.out.println("Chunk index: " + chunkIndex);
        StaticObjects.chunks.get(chunkIndex).addContainedParticle(p1);
        
        }

        // Highlight given Chunk
        StaticObjects.chunks.get(7000).showChunk();
        System.out.println(StaticObjects.chunks.get(7000).getContainedParticles());

        // Update Screen
        panel.repaint();
    }
    
    public static double[] calcParticleInfluence(Particle p1, Particle p2) {
        // See https://www.desmos.com/calculator/oducvmmkdw

        // Particles dont like to be close to each other
        double dist = Point2D.distance(p1.x, p1.y, p2.x, p2.y);
        if (dist > 20) {
            return (new double[] {0 , 0});
        }
        dist = Math.max(.1, dist);  
        double spacingForce = -1 / dist - .05983 / Math.log(dist);

        // Particles are affected by gravity however
        double gravity = 1 / Math.sqrt(dist + .1);
        
        // Influence is a range from -7.626 (less than .1 from each other, pushed apart) to .169 (pulled together by gravity)
        double influence = spacingForce + gravity;
        
        double xShare;
        if (dist == .1) {
            xShare = StaticObjects.rand.nextDouble();
        }
        else {
            xShare = (p1.x - p2.x) * (p1.x - p2.x) / dist * dist;
        }
        double yShare = 1 - xShare;
        double[] shareArray = new double[] {xShare * influence, yShare * influence};

        // p1 - p2
        // If p2 is greater, p1 should get nex
        int xSgn = Double.compare(p1.x, p2.x);
        int ySgn = Double.compare(p1.y, p2.y);

        double[] p1Array = {xShare * influence * xSgn, yShare * influence * ySgn};
        // double[] p2Array = {-1 * xShare * influence * xSgn, -1 * yShare * influence * ySgn};
        return p1Array;
    }

}
