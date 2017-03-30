package uk.ac.bris.cs.scotlandyard.ui.ai;

/**
 * Created by Adam on 30/03/2017.
 */
public class MrX {
    private int location;
    private int lastKnownLocation;

    public int getLocation() {
        return this.location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void setLastKnownLocation(int location) {
        this.lastKnownLocation = location;
    }

    public int getLastKnownLocation() {
        return this.lastKnownLocation;
    }
}
