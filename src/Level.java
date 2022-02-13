import bagel.Input;
import bagel.MouseButtons;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Colour;
import bagel.util.Point;
import org.jetbrains.annotations.NotNull;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * A Level contains a unique map and a unique {@link Route}. It also contains a number of {@link Wave}s,
 * and the {@link Player}'s {@link Tower}s. When update is called on a Level, everything that belongs to the
 * Level will be updated.
 *
 */
public class Level {
    private int lv;
    private TiledMap map;
    private Route route;
    private ArrayList<Wave> incomingWaves;
    private Iterator<Wave> incomingWavesIter;
    private Wave activeWave;
    private ArrayList<Tower> towers;
    private Tower placingTower;

    /**
     * Instantiates a new Level.
     *
     * @param lv the Level number.
     */
    public Level(int lv) {
        System.out.println("creating level" + lv);
        this.lv = lv;
        createMap();
        this.route = new Route(new ArrayList<Point>( map.getAllPolylines().get(0)));
        this.incomingWaves = new ArrayList<>();
        parseWaves();
        this.incomingWavesIter = incomingWaves.iterator();
        this.activeWave = null;
        this.towers = new ArrayList<>();
        this.placingTower = null;
    }


    /**
     * Updates everything that belongs to a Level, including all {@link Tower}s and any active {@link Wave}.
     * The two panels, {@link BuyPanel} and {@link StatusPanel} are also updated with every call.
     *
     * @param input the input
     * @return true if the Level has at least 1 active or incoming {@link Wave} remaining, false if otherwise.
     */
// update everything that belongs to the current level
    // returns false to indicate level has finished
    public boolean update(Input input){
        // draw map
        map.draw(0,0,0,0, Window.getWidth(), Window.getHeight());

        // update panel, if a tower is purchased, prepare for placement
        Tower newTower = BuyPanel.getPanel().update(input);

        if(newTower != null){
            System.out.println("detected new tower");
            placingTower = newTower;
        }
        updateStatus();
        updatePlacingTower(input, newTower);

        // check input for wave start key
        checkInput(input);

        // if wave exists, update everything in the wave
        if (this.activeWave != null){
            // get all enemies for the towers
            ArrayList<Slicer> allSlicers = new ArrayList<>();
            for (WaveEvent waveEvent : activeWave.getActiveWaveEvents()) {
                if(waveEvent != null){
                    if(waveEvent.getActiveSlicers() != null){
                        allSlicers.addAll(waveEvent.getActiveSlicers());
                    }
                }
            }

            updateTowers(input, allSlicers);

            // if active wave has finished, remove it
            if(!activeWave.update(input)){
                // if no more waves, level is finished
                if(!incomingWavesIter.hasNext()){
                    return false;
                }
                StatusPanel.getPanel().increaseWaveNumber();
                activeWave = null;
            }
        }
        else{
            // no active wave but still need to render the towers
            updateTowers(input, new ArrayList<>());
        }
        return true;
    }


    private void createMap(){
        this.map = new TiledMap("res/levels/"+ this.lv + ".tmx");
    }


    // builds waves based on the specifications in waves.txt, the waves are stored in {incomingWaves}
    private void parseWaves() {
        try {
            FileInputStream stream = new FileInputStream("res/levels/waves.txt");
            Scanner scanner = new Scanner(stream);

            String[] specs;
            int waveNum;
            String waveType;
            ArrayList<WaveEvent> waveEvents = new ArrayList<>();
            int currentWaveNum = 1;

            // for each line in waves
            while(scanner.hasNextLine()){
                specs = scanner.nextLine().split(",");
                waveNum = Integer.parseInt(specs[0]);
                waveType = specs[1];
                // if wave is different, add current wave into list and create new wave
                if(currentWaveNum != waveNum){
                    this.incomingWaves.add(new Wave(waveEvents));
                    waveEvents = new ArrayList<>();
                }

                // add waveEvent into wave

                // if waveEvent a spawn event
                if(waveType.equals("spawn")){
                    waveEvents.add(new WaveEvent(this.route,
                            Integer.parseInt(specs[4]), specs[3], Integer.parseInt(specs[2])));
                }
                // if waveEvent is a delay event
                if(waveType.equals("delay")){
                    waveEvents.add(new WaveEvent(Integer.parseInt(specs[2])));
                }
                currentWaveNum = waveNum;
            }
            // add last wave into list
            this.incomingWaves.add(new Wave(waveEvents));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // update towers, removing airplanes that went outside the window
    private void updateTowers(Input input, ArrayList<Slicer> allSlicers){
        ArrayList<Tower> temp = new ArrayList<>();
        for (Tower tower: towers) {
            tower.update(input, allSlicers);
            if(placingTower != null){
                tower.drawRectangle();
            }
            if (tower.inWindow()){
                temp.add(tower);
            }
        }

        towers.clear();
        towers.addAll(temp);
        temp.clear();
    }

    // checks for awaiting start, wave in progress, placing
    private void updateStatus(){
        if(!StatusPanel.getPanel().getStatus().equals(StatusPanel.STATUS_WIN)){
            if (activeWave == null){
                StatusPanel.getPanel().setStatus(StatusPanel.STATUS_AWAITING_START);
            }
            else {
                StatusPanel.getPanel().setStatus(StatusPanel.STATUS_WAVE_IN_PROGRESS);
            }
            if (placingTower != null){
                StatusPanel.getPanel().setStatus(StatusPanel.STATUS_PLACING);
            }
        }
    }

    // update the tower the player is placing if it exists
    private void updatePlacingTower(Input input, Tower newTower){
        if(placingTower != null){
            Point cursor = new Point(input.getMouseX(), input.getMouseY());

            // change the orientation of the sprite if tower is an airplane
            if(placingTower instanceof Airplane){
                placingTower.getOrientation().facing(new Point(0,0),
                        Airplane.copyNextDirection().asPoint());
            }

            // tower follows cursor
            placingTower.setPosition(cursor);
            placingTower.render();

            // activetowers cannot be placed on path
            boolean placeable = true;
            if(placingTower instanceof ActiveTower){
                placeable = !map.hasProperty((int)cursor.x, (int)cursor.y, "blocked");
                if(placeable){
                    // checking if blocked by other towers
                    for (Tower tower: towers) {
                        if(tower instanceof ActiveTower){
                            if(tower.getHitBox().intersects(placingTower.getHitBox())){
                                placeable = false;
                                break;
                            }
                        }
                    }
                }
            }

            // cannot place on status panel
            if (StatusPanel.getPanel().getRect().intersects(placingTower.getHitBox())){
                placeable = false;
            }
            if (!placeable || placingTower.getHitBox().intersects(BuyPanel.getPanel().getRect())){
                placingTower.drawRectangle();
            }
            else{
                placingTower.drawRectangle(Colour.GREEN);
            }
            // if left click and left click not in the same frame as initial purchase
            // and tower is not blocked
            if (input.wasPressed(MouseButtons.LEFT) && newTower == null && placeable){
                // transaction is cancelled if the tower is placed on the panel
                if(BuyPanel.getPanel().getRect().intersects(placingTower.getHitBox())){
                    placingTower = null;
                }
                else{
                    System.out.println("setting new tower down at " + cursor.toString());
                    towers.add(placingTower);
                    Player.getPlayer().transaction(-placingTower.getPrice());
                    placingTower = null;
                }

            }
        }
    }

    // checks the input for wave start
    private void checkInput(@NotNull Input input){
        if(input.wasPressed(ShadowDefend.WAVE_START)){
            // check if wave is in progress
            if(this.activeWave == null){
                activeWave = incomingWavesIter.next();

            }
        }
    }
}