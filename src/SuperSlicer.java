import bagel.Image;
import bagel.util.Point;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

/**
 * A type of {@link Slicer}, spawns 2 {@link RegularSlicer}s on death.
 */
public class SuperSlicer extends Slicer {
    private final static String IMG_ADDRESS = "res/images/superslicer.png";
    private final static Image SLICER_IMG = new Image(IMG_ADDRESS);
    private final static double DEFAULT_SPEED = 1.5; // px/f
    private final static int STARTING_HP = 1;
    private final static int reward = 15;
    private final static int spawnCount = 2;
    private int hp;

    /**
     * Instantiates a new Super slicer.
     *
     * @param x     the x coordinate of the slicer.
     * @param y     the y coordinate of the slicer.
     * @param route the route the slicer will traverse.
     */
    public SuperSlicer(double x, double y, Route route) {
        super(SLICER_IMG, x, y, DEFAULT_SPEED, route);
        this.hp = STARTING_HP;
    }

    /**
     * Instantiates a new Super slicer.
     *
     * @param coordinates the coordinates of the slicer.
     * @param route the route the slicer will traverse.
     */
    public SuperSlicer(@NotNull Point coordinates, Route route) {
        super(SLICER_IMG, coordinates.x, coordinates.y, DEFAULT_SPEED, route);
        this.hp = STARTING_HP;
    }

    @Override
    // returns true if still alive, false if dead
    public boolean damageSelf(int damage) {
        if(!isDead()){
            hp -= damage;
            super.checkHP(hp);
            if(hp > 0){
                return true;
            }
            reward();
        }
        return false;
    }
    @Override
    public ArrayList<Slicer> spawn() {
        ArrayList<Slicer> spawns = new ArrayList<>();
        for (int i = 0; i < spawnCount; i++) {
            spawns.add(new RegularSlicer(super.getPosition(),super.getRoute().copy()));
        }
        return spawns;
    }
    @Override
    public int getDamage() {
        return spawnCount * new RegularSlicer(0,0, null).getDamage();
    }
    private void reward() {
        Player.getPlayer().transaction(reward);
    }
}

