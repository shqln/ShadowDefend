import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

/**
 * A panel that renders at the bottom of the screen, showing the {@link Wave} number,
 * the current {@link Timescale}, and the {@link Player}'s HP.
 *
 * Is singleton
 */
// Singleton
public class StatusPanel extends Sprite {
    private static final String IMG_SRC = "res/images/statuspanel.png";
    private static final int HEIGHT = 13;
    private static StatusPanel statusPanel;
    /**
     * The Status that indicates victory.
     */
    public static final String STATUS_WIN = "Winner!";
    /**
     * The Status that indicates the player is placing a {@link Tower}.
     */
    public static final String STATUS_PLACING = "Placing";
    /**
     * The Status that indicates the game is waiting for the player to start the next {@link Wave}.
     * @see ShadowDefend#WAVE_START
     */
    public static final String STATUS_AWAITING_START = "Awaiting Start";
    /**
     * The Status that indicates a {@link Wave} is in progress
     */
    public static final String STATUS_WAVE_IN_PROGRESS = "Wave in progress";
    private static final Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", 20);
    private static final int HORIZONTAL_GAP = 232; //px
    private static final int TOP_OFFSET = 5; //px

    private int waveNumber;
    private String status;


    private StatusPanel() {
        super(new Point(new Image(IMG_SRC).getWidth() / 2, Window.getHeight() - HEIGHT),
                IMG_SRC);
        waveNumber = 1;
        status = STATUS_AWAITING_START;
    }

    /**
     * Gets the instance of StatusPanel.
     *
     * @return the instance of StatusPanel.
     */
    public static StatusPanel getPanel() {
        if (statusPanel == null){
            statusPanel = new StatusPanel();
        }
        return statusPanel;
    }

    /**
     * Renders the panel and all its information.
     */
    // updates the status panel
    public void update(){
        // render background
        super.render();
        // renders the texts
        double x = 0;
        double y = Window.getHeight() - HEIGHT + TOP_OFFSET;
        DrawOptions colour = new DrawOptions();

        font.drawString("Wave: " + waveNumber, x, y);
        x += HORIZONTAL_GAP;

        // change colour to green if timescale is greater than 1
        int timescale = Timescale.getTimescale().val();
        if(timescale > 1){
            colour.setBlendColour(Colour.GREEN);
        }

        font.drawString("Time Scale: " + timescale, x, y, colour);
        x += HORIZONTAL_GAP;
        font.drawString("Status: " + status, x, y);
        x += HORIZONTAL_GAP * 2;
        font.drawString("Lives: " + Player.getPlayer().getHP(), x, y);

    }

    // sets the status
    public void setStatus(String status) {
        this.status = status;
    }

    // get the status
    public String getStatus() {
        return status;
    }

    /**
     * Increase wave number by 1.
     */
    public void increaseWaveNumber(){
        waveNumber += 1;
    }
}

