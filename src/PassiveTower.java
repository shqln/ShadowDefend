import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.ArrayList;
import java.util.Random;

/**
 * Passive Towers are one time use, moving towers that support the player.
 */
public abstract class PassiveTower extends Tower{
    private double speed;
    private Vector2 direction;
    private int cooldown;
    private Timer dropTimer;
    private ArrayList<Bomb> bombs;
    private Random random;

    /**
     * Instantiates a new Passive tower.
     *
     * @param point     the starting location of the tower.
     * @param image     the image of the tower.
     * @param speed     the speed of the tower.
     * @param direction the direction in which the tower travels in.
     * @param cooldown  the cooldown of the tower's perk.
     */
    public PassiveTower(Point point, Image image, double speed, Vector2 direction, int cooldown) {
        super(point, image);
        this.speed = speed;
        this.direction = direction;
        this.cooldown = cooldown;
        this.bombs = new ArrayList<>();
        this.random = new Random();
        getRandomCooldown();
    }

    @Override
    public void update(Input input, ArrayList<Slicer> allSlicers) {
        System.out.println("plane active");
        super.update(input);
        // if passive tower has not been initialised yet
        if(direction == null){
            // calculate direction and location
            direction = Airplane.getNextDirection();
            Point currentLocation = super.getPosition();
            if(direction.x == 0){
                // vertical
                super.setPosition(new Point(currentLocation.x, 0));
                super.getOrientation().setAngle(Orientation.S);
            }
            else {
                // horizontal
                super.setPosition(new Point(0, currentLocation.y));
                super.getOrientation().setAngle(Orientation.E);
            }
        }
        else{
            // if it has been initialised, move it
            super.setPosition(super.getPosition().asVector().add(direction.mul(speed * Timescale.getTimescale().val()))
                    .asPoint());
        }
        // create and/or update bombs
        if(dropTimer.tick()){
            if(super.inWindow()){
                dropBomb();
                getRandomCooldown();
            }
        }
        updateBomb(allSlicers);
    }

    // create a new bomb artifact
    private void dropBomb(){
        bombs.add(new Bomb(super.getPosition()));
    }

    // updates all the bombs that belong to this tower
    private void updateBomb(ArrayList<Slicer> slicers){
        ArrayList<Bomb> temp = new ArrayList<>();
        for (Bomb bomb: bombs) {
            if(bomb.update(slicers)){
                temp.add(bomb);
            }
        }
        bombs.clear();
        bombs.addAll(temp);
        temp.clear();
    }

    private void getRandomCooldown(){
        int cd = random.nextInt(cooldown);
        dropTimer = new Timer(cd);
    }

    @Override
    public boolean inWindow() {
        return super.inWindow() || !bombs.isEmpty();
    }
}

