import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;


public class Particle {
    int diameter;
    double x;
    double y;
    double xvel;
    double yvel;
    double dt = 1.0 / 120.0;
    double r;
    ArrayList<ArrayList<Double>> influencesList = new ArrayList<>();
    double xAccel = 0; 
    double yAccel = 0;



    
    public Particle() {

    }

    public Particle(int diameter, double x, double y, double xvel, double yvel) {
        this.diameter = diameter;
        this.x = x;
        this.y = y;
        this.xvel = xvel;
        this.yvel = yvel;
        this.r = diameter * .5;
    }

    // random particle
    public Particle(int diameter) {

        this.diameter = diameter;
        r = diameter * 0.5;

        // Generate random x,y within panel bounds
        x = Math.random() * (App.panel.getWidth() - r);
        y = Math.random() * (App.panel.getHeight() - r);
        // System.out.println("x: " + x + "\ty: " + y);
      
        // Generate random velocities
        double speed = 100 * (Math.random() * 2 + 1);
        double angle = (Math.random() * 360);
        yvel = Math.sin(angle) * speed;
        xvel = Math.sqrt(speed * speed - yvel * yvel);
        if(StaticObjects.rand.nextBoolean()) {
            xvel *= -1;
          }

      }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public int getDiameter() {
        return diameter;
    }
    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }
    public double getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public double getXvel() {
        return xvel;
    }
    public void setXvel(int xvel) {
        this.xvel = xvel;
    }
    public double getYvel() {
        return yvel;
    }
    public void setYvel(int yvel) {
        this.yvel = yvel;
    }
    public Point2D getCenter() {
        Point2D center = new Point2D.Double(x, y);
        return center;
    }
    public Point2D getCorner() {
        Point2D corner = new Point2D.Double(x - r, y - r);
        return corner;
    }

    public ArrayList<ArrayList<Double>> getInfluencesList() {
        return influencesList;
    }

    public void addInfluence(double xInfluence, double yInfluence) {
        ArrayList<Double> influence = new ArrayList<>();
        influence.add(xInfluence); 
        influence.add(yInfluence);
        influencesList.add(influence);
        // System.out.println(influencesList);

        
    }

    public void processMovement() {  
 
        x += xvel * dt;
        y += yvel * dt;

        
        xvel += xAccel * dt;  
        yvel += yAccel * dt;

        // xvel += influencesList[0]
        applyFriction();
        // if (App.frameCount % 10 == 0) {
        checkBounds();
        applyInfluences();


        

    }

    @Override
    public String toString() {
        return "Particle [diameter=" + diameter + ", x=" + x + ", y=" + y + ", xvel=" + xvel + ", yvel=" + yvel
                + ", dt=" + dt + ", r=" + r + ", influencesList=" + influencesList + "]";
    }

    public void checkBounds() {
        if (x - r < 0) { // Too far left
            x = r;
            xvel *= -1;
        }
        else if (x + r > App.panel.getWidth()) { // Too far right
            x = App.panel.getWidth() - r;
            xvel *= -1;
        }
        // For upper wall
        else if (y - r < 0) {
            y = r;
            yvel *= -1;
        }
        // For lower wall
        else if (y + r > App.panel.getHeight()) {
            y = App.panel.getHeight() - r;
            yvel *= -1;
        }
    }
    public void applyFriction() {
        // Friction is smaller if dt is smaller
        double frictionFactor = (1 - dt*dt);
        // System.out.println(frictionFactor);
        xvel *= frictionFactor;
        yvel *= frictionFactor;
        // System.out.println("xvel: " + xvel);
    }


    public void applyInfluences() {
    // Influences smoothing factors
    double smoothFactor = 0.5;  

    // Running averages
    double xAccelAvg = 0;
    double yAccelAvg = 0;

    // Loop through each influence
    for (ArrayList<Double> influence : influencesList) {
        
        // Get x and y components
        double xInf = influence.get(0);
        double yInf = influence.get(1);

        // Update acceleration averages  
        xAccelAvg = xAccelAvg*smoothFactor + xInf*(1-smoothFactor);
        yAccelAvg = yAccelAvg*smoothFactor + yInf*(1-smoothFactor);

    }

    // Apply acceleration 
    xvel += xAccelAvg * dt;
    yvel += yAccelAvg * dt;

    // Clear accelerations for next frame
    xAccelAvg = 0;
    yAccelAvg = 0;

    }

    // public void applyInfluences() {
    //     double decayFactor = 0.5;  

    //     for (ArrayList<Double> influence : influencesList) {
    //         double xInf = influence.get(0);
    //         double yInf = influence.get(0);

    //         influence.set(0, xInf * decayFactor);
    //         influence.set(1, yInf * decayFactor);

    //         // Update accelerations from influences
    //         xAccel += -1 * xInf;
    //         yAccel += -1 * yInf;       
            
    //         // Smoothing factor (closer to 1 is more smooth)
    //         double smooth = 0.5;

    //         // Running averages
    //         double xAccelAvg = 0; 
    //         double yAccelAvg = 0;

    //         // Update averages
    //         xAccelAvg = xAccelAvg*smooth + xInf*(1-smooth);
    //         yAccelAvg = yAccelAvg*smooth + yInf*(1-smooth);

    //         // Apply averages to accelerate particle
    //         xvel += xAccelAvg * dt;
    //         yvel += yAccelAvg * dt;
    //     }
    // }

}    



