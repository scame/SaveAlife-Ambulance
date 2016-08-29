package com.example.scame.savealife.presentation.utility;

import android.os.Handler;
import com.dd.morphingbutton.IProgress;

import java.util.Random;

public class ProgressGenerator {

    public interface OnCompleteListener {

        void onComplete();
    }

    private OnCompleteListener mListener;
    private int mProgress;
    private final int PROGRESS_STEP = 5;
    private final int SIZE = 100;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(final IProgress button, int duration) {
        mProgress = 0;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress += PROGRESS_STEP;
                button.setProgress(mProgress);
                if (mProgress < SIZE) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }
            }
        }, duration);
    }

    public void start(final IProgress button) {
        start(button, SIZE*5);
    }

    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(SIZE);
    }
}
