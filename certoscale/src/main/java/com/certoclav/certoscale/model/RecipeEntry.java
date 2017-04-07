package com.certoclav.certoscale.model;


import com.certoclav.certoscale.supervisor.ApplicationManager;

public class RecipeEntry {


	private String description = "";
	private Double weight = -1d;

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getArticleNumber() {
		return articleNumber;
	}

	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	private int step = 0;
	private String articleNumber = " ";
	private String unit = " ";
	private String instruction = " ";
	private Double measuredWeight = 0d;



	public Double getMeasuredWeight() {
		return measuredWeight;
	}

	public void setMeasuredWeight(Double measuredWeight) {
		this.measuredWeight = measuredWeight;
	}


	public RecipeEntry(String description, Double weight, int step, String articleNumber, String unit, String instruction, Double measuredWeight) {
		this.description = description;
		this.weight = weight;
		this.step = step;
		this.articleNumber = articleNumber;
		this.unit = unit;
		this.instruction = instruction;
		this.measuredWeight = measuredWeight;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getWeight() {
		return weight;
	}
	public Double getScaledWeight(){
		Double scaledWeight = 0d;

		try {
			scaledWeight = weight * ApplicationManager.getInstance().getScalingFactor();
		}catch (Exception e){
			scaledWeight = weight;
		}

		return scaledWeight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}





}




