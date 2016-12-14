package com.certoclav.certoscale.model;
class SpinnerModeItem{
	ScaleApplication type = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	String name = null;

	public SpinnerModeItem(ScaleApplication type) {

	}

	public ScaleApplication getType() {
		return type;
	}

	public void setType(ScaleApplication type) {
		this.type = type;
	}
}