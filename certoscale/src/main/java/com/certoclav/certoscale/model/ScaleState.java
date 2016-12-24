package com.certoclav.certoscale.model;

/**
 * This is the current state of the scale.
 * The state bust be ON_AND_MODE_GRAM. If not, the supervisor.SateMachine tries to reach this mode
 */
public enum ScaleState {
	OFF,
	CABLE_NOT_CONNECTED,
	ON_AND_MODE_GRAM, //Scale is connected and user is logged in
	ON_AND_MODE_NOT_GRAM,
	ON_AND_CALIBRATING,
	DISCONNECTED //if screen is not connected with scale, but user wants to ignore it
}
