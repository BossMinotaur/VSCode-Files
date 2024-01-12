import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Panel extends JPanel{
    int i = 0;
    Ellipse2D defaultParticle = new Ellipse2D.Double();
    public double frameTimeDiff;
    int possibleFrames = 0;
    



    // Constructors 
    public Panel() {  
        super();
        initPanel();

    }

    // Getters / Setters
    public void setPossibleFrames(int possibleFrames) {
        this.possibleFrames = possibleFrames;
      }

    // Methods
    private void initPanel() {  
        
    }
    

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;       
        g2.setColor(Color.BLACK);

        // Draw Particles
        ArrayList<Particle> particles = StaticObjects.particles;
        // System.out.println(particles);
        for (i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            defaultParticle.setFrameFromCenter(p.getCenter(), p.getCorner());
            g2.fill(defaultParticle);
        }
        g2.drawString("Possible FPS: " + possibleFrames, 10, 20);
        g2.drawString("FPS Cap: " + 1 / StaticObjects.dt, 10, 50);


    }
}
