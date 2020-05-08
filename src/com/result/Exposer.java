package com.result;

/**
 * @author false
 * @date 20/4/14 14:00
 */
public class Exposer {
    private boolean expose;
    private String random;

    public Exposer(boolean expose, String random) {
        this.expose = expose;
        this.random = random;
    }

    public boolean isExpose() {
        return expose;
    }

    public void setExpose(boolean expose) {
        this.expose = expose;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }
}
