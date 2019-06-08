package View;

import Model.visitor.Visitor;

import java.awt.*;

public abstract class Map implements Visitor {
    private int timeout;

    public Map(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public abstract Image getBg();
}
