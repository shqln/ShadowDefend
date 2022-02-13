import bagel.*;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The game Shadow defend.
 */
public class ShadowDefend extends AbstractGame {
    /**
     * Frames per second.
     * FPS = update() per second, MAY VARY FROM SYSTEM TO SYSTEM, change this to suit your system
     */
    public static final int FPS = 120;

    /**
     * The assumed FPS this game will be run in.
     * do not change this, this is used to scale things that are fps based
     */
    public static final double ASSUMED_FPS = 60.0;

    // Settings for keys
    /**
     * The title of the key binds information section on the buy panel .
     */
    public static final String KEY_TITLE = "Key Binds: ";

    /**
     * The description of key bind number 1 to be shown on the buy panel.
     * The default corresponding key is {@link #WAVE_START}
     */
    public static final String KEY_DESCRIPTION_1 = "Start Wave";

    /**
     * Key bind to start a wave.
     */
    public static final Keys WAVE_START = Keys.S;

    /**
     * The description of key bind number 2 to be shown on the buy panel.
     * The default corresponding key is {@link #INCREASE_TIMESCALE}
     */
    public static final String KEY_DESCRIPTION_2 = "Increase Timescale";

    /**
     * Key bind to increase the {@link Timescale}.
     */
    public static final Keys INCREASE_TIMESCALE = Keys.L;

    /**
     * The description of key bind number 3 to be shown on the buy panel.
     * The default corresponding key is {@link #DECREASE_TIMESCALE}
     */
    public static final String KEY_DESCRIPTION_3 = "Decrease Timescale";
    /**
     * Key bind to decrease the {@link Timescale}.
     */
    public static final Keys DECREASE_TIMESCALE = Keys.K;

    // for debugging purposes
    /**
     * Cheat code used for debugging purpose.
     * Key bind to increase {@link Player}'s money.
     */
    public static final Keys DEBUG_MONEY = Keys.M;
    /**
     * Cheat code used for debugging purpose.
     * Key bind to increase {@link Player}'s HP.
     */
    public static final Keys DEBUG_HP = Keys.N;

    /**
     * The number of Levels to be loaded from the resource folder (/res/levels).
     */
    private static final int MAX_LEVEL = 2;
    private Level activeLevel;
    private Iterator<Level> incomingLevelsIter;


    /**
     * Entry point for Bagel game
     * <p>
     * Explore the capabilities of Bagel: https://people.eng.unimelb.edu.au/mcmurtrye/bagel-doc/
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Create new instance of game and run it
        new ShadowDefend().run();
    }

    /**
     * Setup the game. Loads information about all {@link Level}s, {@link Wave}s, {@link WaveEvent}s into memory.
     */
    public ShadowDefend(){
        // Constructor
        // create levels
        parseLevels();
    }

    /**
     * Updates the game state approximately 60 times a second, potentially reading from input.
     * Note that on some machines the actual number of times the game updates per second may differ.
     * @param input The input instance which provides access to keyboard/mouse state information.
     */
    @Override
    protected void update(Input input) {
        // check for timescale controls and debugging controls
        checkInput(input);
        // if no level is active
        if(activeLevel == null){
            // if no level left, exit
            if(!incomingLevelsIter.hasNext()){
                Window.close();
            }
            else{
                System.out.println("=====activating a new level=====");
                // activate a new Level
                activeLevel = incomingLevelsIter.next();
                // reset player health and money
                Player.getPlayer().resetPlayer();
            }
        }
        // update current level and
        // if current level has finished, remove it
        else{
            if(!activeLevel.update(input)){
                if(incomingLevelsIter.hasNext()){
                    this.activeLevel = null;
                }
                else{
                    System.out.println("YOU WIN!");
                    StatusPanel.getPanel().setStatus(StatusPanel.STATUS_WIN);
                }
            }
        }
        StatusPanel.getPanel().update();

        if (Player.getPlayer().isDead()){
            System.out.println("YOU LOSE");
            Window.close();
        }
    }

    // creates all levels
    private void parseLevels(){
        ArrayList<Level> incomingLevels = new ArrayList<>();
        this.activeLevel = null;
        int i;
        for(i = 1; i <= MAX_LEVEL ; i ++){
            incomingLevels.add(new Level(i));
        }
        this.incomingLevelsIter = incomingLevels.iterator();
    }

    private void checkInput(@NotNull Input input){
        if(input.isDown(DEBUG_HP)){
            Player player = Player.getPlayer();
            player.setHp(player.getHP() + 100);
        }
        if(input.isDown(DEBUG_MONEY)){
            Player player = Player.getPlayer();
            player.transaction(5000);
        }

        // check timescale controls
        if(input.wasPressed(INCREASE_TIMESCALE)){
            Timescale.getTimescale().incTimescale(1);
        }
        if(input.wasPressed(DECREASE_TIMESCALE)){
            Timescale.getTimescale().decTimescale(1);
        }
    }
}

