import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

/**
 * A type of {@link PassiveTower} that travel in a straight line and drops bombs at a random interval.
 *
 * The direction will alternate between horizontal and vertical.
 */
public class Airplane extends PassiveTower {
    private static final String IMG_SRC = "res/images/airsupport.png";
    private static final double DEFAULT_SPEED = 5 * (ShadowDefend.ASSUMED_FPS / ShadowDefend.FPS); // px / frame
    private static final int DEFAULT_COOLDOWN = 300; // ms
    private static final Vector2 DEFAULT_DIRECTION = Vector2.right;
    private static final int DEFAULT_PRICE = 500;
    private static Vector2 nextDirection;

    /**
     * Instantiates a new Airplane.
     *
     * @param location the starting location of the plane.
     */
    public Airplane(Point location) {
        super(location, new Image(IMG_SRC), DEFAULT_SPEED, null, DEFAULT_COOLDOWN);
    }

    @Override
    public int getPrice() {
        return DEFAULT_PRICE;
    }

    /**
     * Returns the direction the next airplane will travel in as a {@link Vector2}, and calculates the next direction.
     *
     * @return the direction vector.
     * @see Vector2
     */
    // gets and changes the next direction
    @NotNull
    public static Vector2 getNextDirection(){
        if(nextDirection == null){
            nextDirection = DEFAULT_DIRECTION;
        }
        Vector2 thisDirection = nextDirection;
        // swap x and y : vertical <=> horizontal
        nextDirection = new Vector2(thisDirection.y, thisDirection.x);
        return thisDirection;
    }

    /**
     * Returns the direction the next airplane will travel in without calculating the direction following that.
     *
     * i.e. using this returns the same thing as {@link #getNextDirection()} without changing the next one.
     *
     * @return the direction vector.
     */
    // gets the next direction without changing it
    public static Vector2 copyNextDirection(){
        if (nextDirection == null){
            return DEFAULT_DIRECTION;
        }
        return nextDirection;
    }

    @Override
    public void update(Input input, ArrayList<Slicer> allSlicers){
        super.update(input, allSlicers);
    }

    // also checks if all bombs have exploded
    @Override
    public boolean inWindow() {
        return super.inWindow();
    }
}