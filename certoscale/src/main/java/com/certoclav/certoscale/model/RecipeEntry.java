package com.certoclav.certoscale.model;


public class RecipeEntry {


private String name;

	public Double getMeasuredWeight() {
		return measuredWeight;
	}

	public void setMeasuredWeight(Double measuredWeight) {
		this.measuredWeight = measuredWeight;
	}

	private Double measuredWeight;

	public RecipeEntry(String name, Double weight) {
		this.name = name;
		this.weight = weight;


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

	private Double weight;



}




