/**
 * Keeps the game-wide timescale multiplier. Timescale is a Singleton.
 */
public class Timescale {
    // change this to change the default game-wide timescale
    private static final int DEFAULT_TIMESCALE = 1;

    private static Timescale instance;
    private int timescale;

    private Timescale(int timescale) {
        this.timescale = timescale;
    }

    /**
     * Get the Timescale instance.
     *
     * @return the instance of Timescale
     */
    public static Timescale getTimescale(){
        if(Timescale.instance == null){
            Timescale.instance = new Timescale(DEFAULT_TIMESCALE);
        }
        return Timescale.instance;
    }


    /**
     * Gets the value of the timescale
     *
     * @return the value of the timescale
     */
    public int val() {
        return timescale;
    }

    /**
     * Increments the timescale by an amount specified.
     *
     * @param increment the amount
     */
    public void incTimescale(int increment) {
        Timescale.getTimescale().timescale = Timescale.getTimescale().timescale + increment;
    }

    /**
     * Decrements the timescale by an amount specified.
     *
     * @param difference the amount
     */
    public void decTimescale(int difference) {
        if(Timescale.getTimescale().timescale > 1){
            Timescale.getTimescale().timescale = Timescale.getTimescale().timescale - difference;
        }
    }

}
