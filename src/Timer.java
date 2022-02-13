import java.util.HashMap;

/**
 * A countdown timer used to keeps track of time. Does this by counting frames.
 *
 * Timer adjusts itself based on the {@link Timescale}.
 *
 * @see Timescale
 */
public class Timer {
    private static final int INITIAL_TIME = 0;

    private double maxTime; // frames
    private double currentTime; // frames
    private int currentScale;
    // stores max times for different timescales, prevents information loss
    // when increasing to very high timescale then decrease to lower timescales
    private HashMap<Integer, Double> maxTimes;

    /**
     * Instantiates a new Timer.
     *
     * @param maxTime the time in ms to countdown from
     */
    public Timer(int maxTime) {
        this.currentScale = Timescale.getTimescale().val();
        // maxTime is in ms
        // turn ms into frames
        this.maxTime = maxTime * ShadowDefend.FPS / 1000.0 / currentScale;
        this.currentTime = INITIAL_TIME;
        this.maxTimes = new HashMap<>();
        this.maxTimes.put(this.currentScale, this.maxTime);
    }

    /**
     * Every time tick() is called, timer updates its time by 1 frame. When the timer finishes counting down it resets.
     *
     * Calibrates itself every time the {@link Timescale} changes.
     *
     * @return true if the designated time has elapsed, false if it is still counting down.
     * @see Timescale
     */
    // increments currerntTime every time tick() is called until it reaches maxTime
    // in which case current time is reset and returns true
    // when timescale increases, clock ticks faster
    public boolean tick(){
        int newScale = Timescale.getTimescale().val();
        // if the time scale has changed, update it
        if (newScale != this.currentScale){
            double ratio = (double)currentScale / newScale;
            // if max time of this scale has already been calculated, use it, otherwise calculate max time
            if(maxTimes.containsKey(newScale)){
                maxTime = maxTimes.get(newScale);
                System.out.println("max time of scale: " + newScale + "= " + maxTime);
            }
            else{
                maxTime = maxTime * ratio;
                maxTimes.put(newScale,maxTime);
            }
            currentTime = currentTime * ratio;
            currentScale = newScale;
        }

        currentTime += 1;
        // >= because currentTime can go over maxTime
        if (currentTime >= maxTime){
            currentTime = INITIAL_TIME;
            return true;
        }
        else {
            return false;
        }
    }
}