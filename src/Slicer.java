import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

/**
 * Slicers are one type of enemy that are spawned. Some of them spawn other slicers on death.
 */
public abstract class Slicer extends Sprite{
    private double speed;
    private Route route;
    private Orientation orientation;
    private boolean isDead;

    /**
     * Instantiates a new Slicer.
     *
     * @param img         the image of the slicer.
     * @param coordinates the coordinates of the slicer.
     * @param spd         the speed of the slicer.
     * @param route       the route the slicer will traverse.
     */
    public Slicer(Image img, Point coordinates, double spd, Route route) {
        super(coordinates, img);
        this.speed = spd;
        this.route = route;
        this.orientation = new Orientation();
        isDead = false;
    }

    /**
     * Instantiates a new Slicer.
     *
     * @param img   the image of the slicer.
     * @param x     the x coordinate of the slicer.
     * @param y     the y coordinate of the slicer.
     * @param spd   the speed of the slicer.
     * @param route the route the slicer will traverse.
     */
    public Slicer(Image img, double x, double y, double spd, Route route) {
        super(new Point(x, y), img);
        this.speed = spd;
        if(route != null){
            this.route = route.copy();
        }
        this.orientation = new Orientation();
        isDead = false;
    }

    /**
     * Checks if the slicer is dead.
     *
     * @return true if the slicer is dead, false if otherwise.
     */
    public boolean isDead() {
        return isDead;
    }

    // moves enemy and returns true if still on route
    // returns false if enemy has finished route alive
    private boolean move() {
        Point next = this.route.next(super.getPosition(), this.speed * Timescale.getTimescale().val());
        if (next != null){
            // update the location and rotation of the enemy
            orientation.facing(super.getPosition(),route.getCurrentTarget());
            super.setPosition(next);
            return true;
        }
        else {
            // no more points are available in the route, finished traversing
            return false;
        }
    }

    /**
     * Updates the slicer by moving it along its route and rendering its {@link Sprite}.
     *
     * @param input the input
     * @return true if the slicer is still active, false if the slicer has finished traversing its route or is dead.
     */
    // calculates and draws the enemy on screen at its coordinates, returns false if enemy has finsihed route
    public boolean update(Input input) {
        boolean onRoute = move();
        if (!onRoute || isDead){
            return false;
        }
        super.render(this.orientation);
        return true;
    }

    /**
     * Deducts an amount of HP specified. If the slicer dies, reward is given to the player.
     *
     * @param damage the amount of HP to be deducted.
     * @return true if the slicer is still alive afterwards, false if otherwise.
     */
    public abstract boolean damageSelf(int damage);

    /**
     * Calculates if a slicer with a certain HP specified is dead and sets its status accordingly.
     *
     * @param hp the HP of the slicer
     */
    // checks if a slicer with HP specified is alive and sets {isDead} accordingly
    public void checkHP(int hp){
        if (hp <= 0){
            this.isDead = true;
        }
    }

    /**
     * Creates an ArrayList of Slicers that are spawned on the slicer's death.
     *
     * @return the array list of Slicers to be spawned.
     */
    // returns an arraylist of slicers spawned on slicer's death
    public abstract ArrayList<Slicer> spawn();

    public Route getRoute() {
        return route;
    }

    /**
     * Gets the amount of damage the slicer deals to the player.
     *
     * @return the damage.
     */
    public abstract int getDamage();
}

