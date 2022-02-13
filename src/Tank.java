import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * A type of {@link ActiveTower}, shoots {@link TankProjectile}s.
 */
public class Tank extends ActiveTower {
    private static final String DEFAULT_IMG_SRC = "res/images/tank.png";
    private static final int DEFAULT_RANGE = 100; // px
    private static final int DEFAULT_CD = 150; // ms
    private static final int DEFAULT_PRICE = 250;
    private static final int PROJECTILE_ID = 0; // 0 = tank projectile
    private static final int HIT_BOX_SIZE = 40;

    /**
     * Instantiates a new Tank.
     *
     * @param point the location of the Tank.
     */
    public Tank(Point point) {
        super(point, new Image(DEFAULT_IMG_SRC), DEFAULT_RANGE, DEFAULT_CD, PROJECTILE_ID);
        super.getOrientation().setAngle(Orientation.N);
    }

    public int getPrice() {
        return DEFAULT_PRICE;
    }

    @Override
    public void update(Input input, ArrayList<Slicer> slicers) {
        super.update(input, slicers);
    }

    @Override
    public Rectangle getHitBox() {
        return generateHitBox(super.getPosition(), HIT_BOX_SIZE);
    }
}
