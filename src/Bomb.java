import bagel.Image;
import bagel.util.Point;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A type of artifacts spawned by {@link Airplane}s, explodes after 2 seconds and
 * dealing damage to nearby {@link Slicer}s.
 */
public class Bomb extends Sprite{
    private static final String IMG_SRC = "res/images/explosive.png";
    private static final int DEFAULT_DAMAGE = 500;
    private static final int DEFAULT_RANGE = 200; // px
    private static final int DEFAULT_COOLDOWN = 2000; //ms
    private Timer timer;

    /**
     * Instantiates a new Bomb.
     *
     * @param location the location of the bomb.
     */
    public Bomb(Point location) {
        super(location, new Image(IMG_SRC));
        this.timer = new Timer(DEFAULT_COOLDOWN);

    }

    /**
     * Renders the bomb and goes through its countdown logic.
     *
     * @param allSlicers an array list of every active {@link Slicer} in the {@link Level}.
     * @return true if the bomb is still on the map, false if it has exploded.
     */
    // returns true if still on the map, false if exploded
    public boolean update(ArrayList<Slicer> allSlicers){
        super.render();
//        drawRectangle();
        System.out.println("updating bomb");
        if(timer.tick()){
            System.out.println("boom");
            explode(allSlicers);
            return false;
        }
        return true;
    }

    private void explode(@NotNull ArrayList<Slicer> allSlicers){
        for (Slicer slicer: allSlicers) {
            // 10 is hitbox size of slicers
            if(slicer.getPosition().distanceTo(super.getPosition()) <= DEFAULT_RANGE + 10){
                slicer.damageSelf(DEFAULT_DAMAGE);
            }
        }

    }
}

