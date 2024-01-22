import java.util.ArrayList;
import java.util.Random;

public class StaticObjects {
    // public static Particle particle1 = new Particle(10, 100, 100, 10, 10);
    public static ArrayList<Particle> particles = new ArrayList<>();
    
    public static double dt = 1.0 / 120;
    public static Random rand = new Random();

    public static ArrayList<Chunk> chunks = new ArrayList<>();
    public static ArrayList<Chunk> showChunks = new ArrayList<>();


}
