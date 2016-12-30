package kasirgalabs;

import java.util.logging.LogManager;

public class FoxServerLogManager extends LogManager {

    private volatile boolean resetEnabled;

    @Override
    public void reset() {
        if(resetEnabled) {
            super.reset();
        }
    }

    public void enableReset() {
        resetEnabled = true;
    }
}
