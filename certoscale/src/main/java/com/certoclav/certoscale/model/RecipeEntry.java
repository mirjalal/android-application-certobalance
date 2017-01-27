package com.certoclav.certoscale.model;


public class RecipeEntry {


	private String name = "";
	private Double measuredWeight = -1d;
	private Double weight = -1d;


	public Double getMeasuredWeight() {
		return measuredWeight;
	}

	public void setMeasuredWeight(Double measuredWeight) {
		this.measuredWeight = measuredWeight;
	}



	public RecipeEntry(String name, Double weight, Double measuredWeight) {
		this.name = name;
		this.weight = weight;
		this.measuredWeight=measuredWeight;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}





}




