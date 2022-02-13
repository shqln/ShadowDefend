import bagel.Drawing;
import bagel.Image;
import bagel.Input;
import bagel.util.Colour;
import bagel.util.Point;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

/**
 * Active Towers are stationary towers that shoot bullets at enemies in range.
 */
public abstract class ActiveTower extends Tower {
    private int range; // px
    private Timer cooldownTimer;
    private boolean isReady;
    private ArrayList<Projectile> projectiles;
    private int projectileID;

    /**
     * Instantiates a new Active tower.
     *
     * @param location     the location of the tower.
     * @param image        the image of the tower.
     * @param range        the range of the tower.
     * @param cooldown     the fire cooldown of the tower.
     * @param projectileID the {@link Projectile} the tower uses, in the form of its ID.
     */
    public ActiveTower(Point location, Image image, int range, int cooldown, int projectileID) {
        super(location, image);
        this.projectiles = new ArrayList<>();
        this.range = range;
        this.cooldownTimer = new Timer(cooldown);
        this.isReady = true;
        this.projectileID = projectileID;
    }

    @Override
    public void update(Input input, ArrayList<Slicer> slicers) {
        if(!isReady){
            if(cooldownTimer.tick()){
                isReady = true;
            }
        }
        boolean adjusted = false; // if orientation has been adjusted this update
        if(!slicers.isEmpty()){
            for (Slicer slicer: slicers) {
                // showRange(slicer);
                if(inRange(slicer)){
                    // adjust orientation to the first enemy in range
                    if(!adjusted){
                        super.getOrientation().facing(super.getPosition(), slicer.getPosition());
                        adjusted = true;
                    }

                    // check cd and shoot
                    if (isReady){
                        shoot(slicer);
                        isReady = false;
                        break;
                    }
                }
            }
        }
        updateProjectiles(input);
    }

    /**
     * Checks whether a slicer is in the tower's firing range.
     *
     * @param slicer the slicer to be checked
     * @return true if it is in range, false if otherwise.
     */
    public boolean inRange(@NotNull Slicer slicer){
        return super.getPosition().distanceTo(slicer.getPosition()) <= this.range;
    }

    private void shoot(Slicer slicer){
        if(projectileID == TankProjectile.PROJECTILE_ID){
            projectiles.add(new TankProjectile(super.getPosition(),slicer));
        }
        if(projectileID == SuperTankProjectile.PROJECTILE_ID){
            projectiles.add(new SuperTankProjectile(super.getPosition(),slicer));
        }
    }

    // for debugging purposes
    private void showRange(Slicer slicer){
        super.drawRectangle();
        if(inRange(slicer)){
            Drawing.drawLine(super.getPosition(), slicer.getPosition(), 3, Colour.RED);
        }
    }

    // updates all the projectiles that belong to this tower
    private void updateProjectiles(Input input){
        ArrayList<Projectile> temp = new ArrayList<>();
        for (Projectile projectile: projectiles) {
            if(!projectile.update(input)){
                temp.add(projectile);
            }
        }
        projectiles.clear();
        projectiles.addAll(temp);
        temp.clear();

        super.update(input);
    }
}
