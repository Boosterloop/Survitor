package View;

import Model.visitor.Visitor;

import java.awt.*;

/**
 * Define a map / level
 */
public abstract class Map implements Visitor {
    private int timeout;

    /**
     * Constructor
     * @param timeout time to survive on the map
     */
    public Map(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Getter for timeout
     * @return timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Getter for the background image of the map
     * @return background image
     */
    public abstract Image getBg();
}
