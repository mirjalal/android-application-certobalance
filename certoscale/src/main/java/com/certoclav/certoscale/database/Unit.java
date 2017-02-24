package com.certoclav.certoscale.database;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

/**
 * A simple demonstration object we are creating and persisting to the database.
 */
@DatabaseTable(tableName = "unit")
public class Unit {


	public static final String UNIT_GRAM="g";
	public static final String UNIT_OUNCE ="oz";
	public static final String UNIT_KILOGRAM="kg";
	public static final String UNIT_MILLIGRAM="mg";
	public static final String UNIT_STONE="st";
	public static final String UNIT_POUND="lb";
	public static final String UNIT_METRIC_CARAT="ct";
	public static final String UNIT_OUNCE_TROY="ozt";
	public static final String UNIT_PENNYWEIGHT="dwt";
	public static final String UNIT_GRAIN="Grain";
	public static final String UNIT_NEWTON = "N";
	public static final String UNIT_MOMME ="mom";
	public static final String UNIT_MESGHAL = "msg";
	public static final String UNIT_TAEL_HK = "HKt";
	public static final String UNIT_TAEL_SG = "SGt";
	public static final String UNIT_TICAL_ASIA = "ical";
	public static final String UNIT_TOLA = "tola";
	public static final String UNIT_BAHT = "bht";


	//required by ORM lite
	Unit(){
	}


	public int getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}

	@DatabaseField(generatedId = true, columnName = "unit_id")
	private int unit_id;

	@DatabaseField(columnName = "unit_json")
	private String unitJson;

	@DatabaseField(columnName = "cloud_id")
	private String cloudId;

	public Unit(Double exponent, Double factor, String description, String name, String cloudId,Boolean enabled,Boolean custom) {
		this.exponent = exponent;
		this.factor = factor;
		this.description = description;
		this.name = name;
		this.cloudId = cloudId;
		this.enabled = enabled;
		this.custom = custom;
	}

	private Boolean custom = false;
	private String name = "";
	private String description = "";
	private Double factor = 0d;
	private Double exponent = 0d;
	private Boolean enabled = true;

	public Boolean getCustom() {
		return custom;
	}

	public void setCustom(Boolean custom) {
		this.custom = custom;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getUnitJson() {
		return unitJson;
	}

	public void setUnitJson(String unitJson) {
		this.unitJson = unitJson;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getFactor() {
		return factor;
	}

	public void setFactor(Double factor) {
		this.factor = factor;
	}

	public Double getExponent() {
		return exponent;
	}

	public void setExponent(Double exponent) {
		this.exponent = exponent;
	}





	/**
	 * This function must be called before inserting the Recipe into the Database DatabaseService.insertRecipe()
	 */
	public void generateUnitJson(){

		JSONObject jsonObjectUnit = new JSONObject();
		try {

			jsonObjectUnit.put("name", name);
			jsonObjectUnit.put("description", description);
			jsonObjectUnit.put("factor",factor);
			jsonObjectUnit.put("exponent",exponent);
			jsonObjectUnit.put("custom",custom);
			jsonObjectUnit.put("enabled",enabled);

		}catch (Exception e){
			e.printStackTrace();
		}

		this.unitJson = jsonObjectUnit.toString();
	}








	public void parseUnitJson(){
		try {
			JSONObject unitJson = new JSONObject(this.unitJson);
			name = unitJson.getString("name");
			description = unitJson.getString("description");
			factor = unitJson.getDouble("factor");
			exponent = unitJson.getDouble("exponent");
			enabled = unitJson.getBoolean("enabled");
			custom = unitJson.getBoolean("custom");
		}catch (Exception e){
			e.printStackTrace();
		}

	}




	public String generateUnitJson(String name, Double factor,Double exponent, String description){
		JSONObject jsonObjectUnit = new JSONObject();

		try {
			jsonObjectUnit.put("name", name).put("factor", factor).put("exponent",exponent).put("description",description).put("enabled",enabled).put("custom",custom);
		}catch (Exception e){
			e.printStackTrace();
		}
		return jsonObjectUnit.toString();
	}
}




