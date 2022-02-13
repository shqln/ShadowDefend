import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

/**
 * Projectiles are artifacts fired by Active Towers to damage enemies.
 */
// projectile has a target, will fly towards the target at a given speed until hit
public abstract class Projectile extends Sprite {
    private Slicer target;
    private int damage;
    private double speed; // px/f

    /**
     * Instantiates a new Projectile.
     *
     * @param start  the starting position of the projectile.
     * @param target the target of the projectile.
     * @param image  the image of the projectile.
     * @param damage the damage of the projectile.
     * @param speed  the speed of the projectile.
     */
    public Projectile(Point start, Slicer target, Image image, int damage, double speed) {
        super(start, image);
        this.target = target;
        this.damage = damage;
        this.speed = speed * ShadowDefend.ASSUMED_FPS / ShadowDefend.FPS;
    }

    /**
     * Calculates the projectile's location based on its speed and target.
     *
     * @param input the input
     * @return true when the projectile hits the target, false otherwise.
     */
// updates the projectile, fly towards its target, returns true when it hits the target
    public boolean update(Input input){
        super.render();
        Point currentPosition = super.getPosition();
        Point targetPosition = this.target.getPosition();
        int timescale = Timescale.getTimescale().val();
        if(currentPosition.distanceTo(targetPosition) <= speed * timescale
                + 5){ // hitbox of all slicers is a 5 px circle
            target.damageSelf(damage);
            return true;
        }
        // update the location of the projectile
        else{
            Vector2 targetVector = targetPosition.asVector();
            Vector2 currentVector = currentPosition.asVector();
            super.setPosition(currentVector.add(targetVector.sub(currentVector).normalised().mul(speed * timescale))
                    .asPoint());
            return false;
        }
    }
}
