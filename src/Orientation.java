import bagel.util.Point;
import bagel.util.Vector2;
import org.jetbrains.annotations.NotNull;

/**
 * Keeps track of an entity's angle of orientation.
 */
// this class keeps track of the information of an enemy's orientation
public class Orientation {
    /**
     * North
     */
    public static final double N = -Math.PI / 2;
    /**
     * South
     */
    public static final double S = Math.PI/ 2;
    /**
     * East
     */
    public static final double E = 0;
    /**
     * West
     */
    public static final double W = Math.PI;
    private static final double DEFAULT_ANGLE = 0;
    private double angle; // angle of orientation, in radians

    /**
     * Instantiates a new Orientation.
     */
    public Orientation() {
        this.angle = DEFAULT_ANGLE;
    }

    /**
     * Instantiates a new Orientation.
     *
     * @param angle the angle
     */
    public Orientation(double angle) {
        this.angle = angle;
    }

    /**
     * Calculates the angle of an entity's orientation of facing a certain point.
     *
     * @param self  the coordinates of the entity / the origin of the vector
     * @param other the coordinates of the point the entity should face / the end of the vector
     * @return the angle.
     */
    // gets the angle of a line specified by two points
    public double facing(@NotNull Point self, @NotNull Point other){
        Vector2 direction = other.asVector().sub(self.asVector());
        double x = direction.x;
        double y = direction.y;
        final double PI = Math.PI;
        angle = Math.atan(y / x);

        // accounting for arctan in different quadrants
        if(x < 0){
            angle = angle + PI;
        }
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
    public double getAngle() {
        return angle;
    }
}
