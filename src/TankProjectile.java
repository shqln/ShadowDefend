import bagel.Image;
import bagel.util.Point;

/**
 * A type of {@link Projectile} spawned by {@link Tank}s.
 */
public class TankProjectile extends Projectile{
    public static final int PROJECTILE_ID = 0;
    private static final String DEFAULT_IMG_SRC = "res/images/tank_projectile.png";
    private static final int DEFAULT_DAMAGE = 1;
    private static final double DEFAULT_SPEED = 10; // px/f

    /**
     * Instantiates a new Tank projectile.
     *
     * @param start  the starting location of the projectile
     * @param target the target of the projectile
     */
    public TankProjectile(Point start, Slicer target) {
        super(start, target, new Image(DEFAULT_IMG_SRC), DEFAULT_DAMAGE, DEFAULT_SPEED);
    }
}
