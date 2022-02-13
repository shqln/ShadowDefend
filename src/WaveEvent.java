import bagel.Input;
import bagel.util.Point;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;

/**
 *  WaveEvents are events in waves, there are two types of WaveEvents: {@link #delayEvent()} and {@link #spawnEvent()}.
 *  <p>
 *  All enemies must belong to a WaveEvent, and is updated when their respective WaveEvent {@link #update(Input)}s.
 */
public class WaveEvent {
    private static final String REGULAR = "slicer";
    private static final String SUPER = "superslicer";
    private static final String MEGA = "megaslicer";
    private static final String APEX = "apexslicer";


    private Timer timer;
    private String spawnType;
    private int enemyCount;
    private Route route;
    private ArrayList<Slicer> incomingSlicers;
    private ArrayList<Slicer> activeSlicers;

    /**
     * Instantiates a new Spawn Event
     *
     * @param route         specifies the route that will be assigned to the enemies {@link #spawnEvent()}'s spawn.
     * @param spawnInterval specifies the delay time, in ms, between spawns in a {@link #spawnEvent()}.
     * @param spawnType     specifies the spawn of enemies to be spawned in a {@link #spawnEvent()}
     * @param enemyCount    specifies the number of enemies to be spawned in a {@link #spawnEvent()}
     * @see Route
     */
    // spawn event constructor
    public WaveEvent(@NotNull Route route, int spawnInterval, String spawnType, int enemyCount) {
        this.route = route.copy();
        this.spawnType = spawnType;
        this.enemyCount = enemyCount;
        this.incomingSlicers = new ArrayList<>();
        this.activeSlicers = new ArrayList<>();
        // create the pool of slicers to be spawned
        for(int i = 0; i < enemyCount; i ++){
            incomingSlicers.add(this.newSlicer());
        }
        this.timer = new Timer(spawnInterval);
    }

    /**
     * Instantiates a new Delay Event.
     *
     * @param delayTime specifies the delay time, in ms, for a {@link #delayEvent()}
     */
    // a delay event is a waveEvent with enemy type {null}, spawn interval {delayTime} and spawn count {0}
    // delay event constructor
    public WaveEvent(int delayTime) {
        this.spawnType = null;
        this.enemyCount = 0;
        this.incomingSlicers = null;
        this.activeSlicers = null;
        this.timer = new Timer(delayTime);
    }


    @Nullable
    private Slicer newSlicer(){
        Point firstPoint = route.getCurrentTarget();
        if(this.spawnType.equals(REGULAR)){
            return new RegularSlicer(firstPoint, this.route);
        }
        if(this.spawnType.equals(SUPER)){
            return new SuperSlicer(firstPoint, this.route);
        }
        if(this.spawnType.equals(MEGA)){
            return new MegaSlicer(firstPoint, this.route);
        }
        if(this.spawnType.equals(APEX)){
            return new ApexSlicer(firstPoint, this.route);
        }
        return null;
    }

    /**
     * Calculates and executes the next action in the wave
     *
     * @param input the player's input
     * @return integer 1 if event is still active, 0 if event is over but contains active enemies, -1 if event is fully inactive
     */
    // at least 1 enemy is alive on the map, -1 if event is fully complete
    public int update(Input input){
        // if it is a delay event
        if(spawnType == null){
            return delayEvent();
        }
        // it is a spawn event
        spawnEvent();
        // update all slicers
        updateSlicers(input);

        // checking and returning the status of the wave event
        // if there are unspawned slicers
        if(!incomingSlicers.isEmpty()){
            return 1;
        }
        // all slicers have been spawned
        else {
            // if no slicers from this event remain on the map
            if (activeSlicers.isEmpty()){
                return -1;
            }
            // if at least one is still on the map
            else{
                return 0;
            }
        }
    }

    /**
     * Gets the active slicers in the wave event.
     *
     * @return the active slicers
     */
    public ArrayList<Slicer> getActiveSlicers() {
        return activeSlicers;
    }

    /**
     * Runs in {@link #update(Input)}.
     *
     * Delays a certain amount of time before the next WaveEvent
     *
     * @return 1 if the delay is incomplete, -1 if the delay is over
     */
    private int delayEvent(){
        // if delay is over, return -1, otherwise return 1
        if (timer.tick()){
            return -1;
        }
        else{
            return 1;
        }
    }

    /**
     * Runs in {@link #update(Input)}.
     *
     * Spawns enemies at a fixed rate
     */
    private void spawnEvent(){
        //if there are still unspawned enemies
        if(!incomingSlicers.isEmpty()){
            int poolSize = incomingSlicers.size();
            if(poolSize == enemyCount){ // at the beginning
                // spawn one
                activeSlicers.add(incomingSlicers.remove(0));

            }
            // spawn delay timer
            if(timer.tick()){
                // spawn one
                activeSlicers.add(incomingSlicers.remove(0));
            }
        }
    }

    private void updateSlicers(Input input){
        // do calculations and render for every enemy remaining in wave
        if(!activeSlicers.isEmpty()){
            ArrayList<Slicer> temp = new ArrayList<>();
            // update each slicer
            for (Slicer i: activeSlicers) {
                if (i.update(input)){
                    temp.add(i); // temp stores the enemies that are alive
                }
                else {
                    if (!i.isDead()){
                        // slicer finished traversing alive
                        Player.getPlayer().damageSelf(i.getDamage());
                    }
                }
                // do any spawning
                if (i.isDead()){
                    temp.addAll(i.spawn());
                }
            }
            // remove the dead and the finished
            activeSlicers.clear();
            activeSlicers.addAll(temp);
            temp.clear();
        }
    }
}

