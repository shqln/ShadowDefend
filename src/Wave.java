import bagel.Input;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A Wave consists of a number of {@link WaveEvent}s. All waves belong to some {@link Level}.
 *
 * Updating a Wave will update all of its {@link WaveEvent}s.
 */
public class Wave {
    private ArrayList<WaveEvent> activeWaveEvents;
    private Iterator<WaveEvent> incomingWaveEventsIter;

    /**
     * Instantiates a new Wave.
     *
     * @param waveEvents the wave events.
     * @see WaveEvent
     */
    public Wave(@NotNull ArrayList<WaveEvent> waveEvents) {
        this.activeWaveEvents = new ArrayList<>();
        this.incomingWaveEventsIter = waveEvents.iterator();
    }

    /**
     * Updates everything that belongs to the Wave, including all of the active {@link WaveEvent}s.
     *
     * @param input the input
     * @return true if wave has at least 1 active waveEvent, false if otherwise.
     */
// updates wave events in the wave, if all wave events have been processed then return false
    public boolean update(Input input) {
        // if no event is active
        if(activeWaveEvents.isEmpty()){
            // if no event left, return false
            if(!incomingWaveEventsIter.hasNext()){
                System.out.println("Wave ended");
                return false;
            }
            // activate a new event
            System.out.println("starting a new event");
            activeWaveEvents.add(incomingWaveEventsIter.next());
        }
        updateWaveEvents(input);
        return true;
    }

    /**
     * Gets all active wave events that belong to the wave.
     *
     * @return the active wave events.
     * @see WaveEvent
     */
    public ArrayList<WaveEvent> getActiveWaveEvents() {
        return activeWaveEvents;
    }

    private void updateWaveEvents(Input input){
        // update current event and if current event has finished, remove it
        int len = activeWaveEvents.size();

        // temp stores wave events that are still active
        ArrayList<WaveEvent> temp = new ArrayList<>();

        // for each active event in the wave
        for(int i = 0; i < len; i ++){
            // update event and get status
            WaveEvent event = activeWaveEvents.get(i);
            int status = event.update(input);

            // event is still active, save it for next round
            if(status >= 0){
                temp.add(event);
            }

            // if the last activated event finished, try to activate a new event
            if(status <= 0 && i == len - 1 && incomingWaveEventsIter.hasNext()){
                temp.add(incomingWaveEventsIter.next());
                System.out.println("latest event ended, starting a new event");
            }
        }
        activeWaveEvents.clear();
        activeWaveEvents.addAll(temp);
        temp.clear();
    }
}
