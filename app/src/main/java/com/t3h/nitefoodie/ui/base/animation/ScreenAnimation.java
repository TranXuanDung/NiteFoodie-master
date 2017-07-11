package com.t3h.nitefoodie.ui.base.animation;

import com.t3h.nitefoodie.R;

public enum  ScreenAnimation {
    OPEN_FULL(R.anim.enter_to_right, R.anim.exit_to_right,
                R.anim.enter_to_left, R.anim.exit_to_left ),
    NONE(0,0,0,0);

    private final int enterToRight;
    private final int exitToRight;
    private final int enterToLeft;
    private final int exitToLeft;

    ScreenAnimation(int enterToRight, int exitToRight, int enterToLeft, int exitToLeft) {
        this.enterToRight = enterToRight;
        this.exitToRight = exitToRight;
        this.enterToLeft = enterToLeft;
        this.exitToLeft = exitToLeft;
    }

    public int getEnterToRight() {
        return enterToRight;
    }

    public int getExitToRight() {
        return exitToRight;
    }

    public int getEnterToLeft() {
        return enterToLeft;
    }

    public int getExitToLeft() {
        return exitToLeft;
    }
}
