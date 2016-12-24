package com.certoclav.certoscale.listener;


import com.certoclav.certoscale.model.ScaleState;

public interface ScaleStateListener {
 void onScaleStateChange(ScaleState state);
}
