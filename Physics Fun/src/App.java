import java.awt.geom.Point2D;
import java.util.ArrayList;

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
        // Loop over and spawn 1000 random particles with diameter 5
        for(i = 0; i < 2000; i++) {
            Particle p1 = new Particle(5);
            StaticObjects.particles.add(p1); 

            // Loop through previous particles in list
            for (j = 0; j < i; j++) {
                Particle p2 = particles.get(j);
                double influence = calcParticleInfluence(p1, p2);
                p1.addToInfluencesList(influence);
                p2.addToInfluencesList(influence);
            }
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
            Thread.sleep((long)timeToSleep); // dividing by 1000000 converts ns to ms
            

        }

    }
    public static void update() {

        // Update Particle Pos
        
        int i, j;
        frameCount++;
        ArrayList<Particle> particles = StaticObjects.particles;
        for (i = 0; i < particles.size(); i++) {
            Particle p1 = particles.get(i);
            p1.processMovement();

            // Loop through later particles in list
            
            // for (j = i; j < particles.size(); j++) {
            //     Particle p2 = particles.get(j);
            //     double influence = calcParticleInfluence(p1, p2);
            //     p1.addToInfluencesList(influence);
            //     p2.addToInfluencesList(influence);
            // }
        }

        // Update Screen
        panel.repaint();
    }
    
    public static double calcParticleInfluence(Particle p1, Particle p2) {
        // See https://www.desmos.com/calculator/oducvmmkdw

        // Particles dont like to be close to each other
        double dist = Point2D.distance(p1.x, p1.y, p2.x, p2.y);
        if (dist > 20) {
            return 0;
        }
        dist = Math.max(.1, dist);  
        double spacingForce = -1 / dist;

        double gravity = 1 / Math.sqrt(dist + .1);
        // Particles are affected by gravity however
        double influence = spacingForce + gravity;
        return influence;
    }

}
