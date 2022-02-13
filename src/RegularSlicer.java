import bagel.Image;
import bagel.util.Point;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A type of {@link Slicer}, does not spawn on death.
 */
// the most basic enemy
public class RegularSlicer extends Slicer {
    private final static String IMG_ADDRESS = "res/images/slicer.png";
    private final static Image SLICER_IMG = new Image(IMG_ADDRESS);
    private final static double DEFAULT_SPEED = 2; // px/f
    private final static int STARTING_HP = 1;
    private final static int reward = 2;
    private final static int penalty = 1;
    private int hp;

    /**
     * Instantiates a new Regular slicer.
     *
     * @param x     the x coordinate of the slicer.
     * @param y     the y coordinate of the slicer.
     * @param route the route the slicer will traverse.
     */
    public RegularSlicer(double x, double y, Route route) {
        super(SLICER_IMG, x, y, DEFAULT_SPEED, route);
        this.hp = STARTING_HP;
    }

    /**
     * Instantiates a new Regular slicer.
     *
     * @param coordinates the coordinates of the slicer.
     * @param route the route the slicer will traverse.
     */
    public RegularSlicer(@NotNull Point coordinates, Route route) {
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
        return new ArrayList<>();
    }

    @Override
    public int getDamage() {
        return penalty;
    }

    private void reward() {
        Player.getPlayer().transaction(reward);
    }
}

