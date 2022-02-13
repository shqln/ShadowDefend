import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * A type of {@link ActiveTower}, shoots {@link SuperTankProjectile}s.
 */
public class SuperTank extends ActiveTower {
    private static final String DEFAULT_IMG_SRC = "res/images/supertank.png";
    private static final int DEFAULT_RANGE = 150; // px
    private static final int DEFAULT_CD = 500; // ms
    private static final int DEFAULT_PRICE = 600;
    private static final int PROJECTILE_ID = 1; // 1 = supertank projectile
    private static final int HIT_BOX_SIZE = 50;


    /**
     * Instantiates a new Super tank.
     *
     * @param point the location of the Super Tank.
     */
    public SuperTank(Point point) {
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
