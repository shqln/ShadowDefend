import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Point;
import java.util.ArrayList;


/**
 * Towers are entities that the player can place to defend the player.
 */
public abstract class Tower extends Sprite{
    /**
     * Instantiates a new Tower.
     *
     * @param point the location of the tower.
     * @param image the image of the tower.
     */
    public Tower(Point point, Image image) {
        super(point, image);
    }

    /**
     * Update the tower and all artifacts that belong to it, such as {@link Projectile}s and {@link Bomb}s.
     *
     * @param input   the input
     * @param slicers an array list of all the {@link Slicer}s active in the current {@link Level}.
     * @see Slicer
     * @see Level
     * @see Projectile
     * @see Bomb
     */
    public void update(Input input, ArrayList<Slicer> slicers){
        super.render();
    }

    /**
     * Renders the Tower.
     *
     * @param input the input
     */
    public void update(Input input){
        super.render();
    }

    public abstract int getPrice();

    /**
     * Checks if the tower is located in the window.
     *
     * @return true if the tower is in the window, false if otherwise.
     */
// checks if the tower is in the window
    public boolean inWindow(){
        Point location = super.getPosition();
        return !(location.x > Window.getWidth()) && !(location.x < 0) &&
                !(location.y > Window.getHeight()) && !(location.y < 0);

    }
}
