package model.lapCounter;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import es.sfernandez.raceyourtrack.RaceYourTrackApplication;
import es.sfernandez.raceyourtrack.app_error_handling.AppError;
import es.sfernandez.raceyourtrack.app_error_handling.AppErrorHandler;
import es.sfernandez.raceyourtrack.app_error_handling.AppUnCatchableException;

public class LapCounter {

    //---- Constants and Definitions ----
    private final char LAP_CHECKPOINT_KEY = 'C';
    private final char SPECIAL_CHECKPOINT_KEY = 'K';
    public interface LapCounterChangeStateListener {
        void onLapCounterChangeState();
    }

    //---- Attributes ----
    private boolean initialized = false;
    private List<LapCounterChangeStateListener> listListeners = new ArrayList<>();

    private TimestampRaceYourTrack[] lapTimes;
    private long lastTimestamp;
    private int numOfLaps, currentLap;
    private boolean isWorking;
    private boolean thereIsSpecialCheckpoint, specialCheckpointFounded;

    //---- Construction ----
    public LapCounter() {}

    //---- Methods ----
    public void initialize(final int numOfLaps, final boolean thereIsSpecialCheckpoint) {
        this.lastTimestamp = -1;
        this.isWorking = false;

        this.numOfLaps = numOfLaps;
        this.currentLap = 0;
        this.lapTimes = new TimestampRaceYourTrack[numOfLaps];

        this.thereIsSpecialCheckpoint = thereIsSpecialCheckpoint;
        this.specialCheckpointFounded = false;

//        this.listListeners.clear();

        this.initialized = true;
        executeListeners();
    }

    public void start() {
        if(!initialized) {
            throw new AppUnCatchableException(new AppError(AppErrorHandler.CodeErrors.MUST_NOT_HAPPEN_LAP_COUNTER_NOT_INITIALIZED, "", RaceYourTrackApplication.getContext()));
        }
        lastTimestamp = System.currentTimeMillis();
        isWorking = true;
    }

    /**
     * 0 if it was a lap check, 1 if it was a coin check, 2 if it was the end, -1 other case
     */
    public int checkPassed(final char checkedKey) {
        int resultCode = -1;

        switch(checkedKey) {
            case LAP_CHECKPOINT_KEY:
                long currentTimestamp = System.currentTimeMillis();
                lapTimes[currentLap] = TimestampRaceYourTrack.calculateTimestamp(lastTimestamp, currentTimestamp);
                lastTimestamp = currentTimestamp;
                ++currentLap;
                if(currentLap == numOfLaps) {
                    end();
                    resultCode = 2;
                } else {
                    resultCode = 0;
                }
                break;

            case SPECIAL_CHECKPOINT_KEY:
                specialCheckpointFounded = thereIsSpecialCheckpoint ? true : false;
                resultCode = specialCheckpointFounded ? 1 : -1;
                break;

            default:
                resultCode = -1;
                break;
        }

        executeListeners();

        return resultCode;
    }

    public void end() {
        isWorking = false;
        initialized = false;
        printResult();
    }

    private void executeListeners() {
        for(LapCounterChangeStateListener listener : listListeners) {
            listener.onLapCounterChangeState();
        }
    }

    public void addListener(final LapCounterChangeStateListener listener) {
        this.listListeners.add(listener);
    }

    public boolean isStarted() {
        return isWorking;
    }

    public boolean thereIsSpecialCheckpoint() {
        return thereIsSpecialCheckpoint;
    }

    public boolean isSpecialCheckpointFounded() {
        return specialCheckpointFounded;
    }

    public int getNumOfLaps() {
        return numOfLaps;
    }

    public int getCurrentLap() {
        return currentLap;
    }

    public Iterator<TimestampRaceYourTrack> getLapTimes() {
        return Arrays.stream(lapTimes).iterator();
    }

    private void printResult() {
        for(int i = 0; i < numOfLaps; ++i) {
            Log.i("LapCounter", "Lap " + i + ": " + lapTimes[i].toString());
        }
        Log.i("LapCounter", "SpecialCheckPointFounded: " + specialCheckpointFounded);
    }


}
