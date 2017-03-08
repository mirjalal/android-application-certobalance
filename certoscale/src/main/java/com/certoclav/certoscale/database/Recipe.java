package com.certoclav.certoscale.database;


import android.util.Log;

import com.certoclav.certoscale.model.RecipeEntry;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple demonstration object we are creating and persisting to the database.
 */
@DatabaseTable(tableName = "recipe")
public class Recipe {

	// id is generated by the database and set on the object automatically


	Recipe(){

	}


	private final static String JSON_CLOUD_ID = "_id";
	private final static String JSON_NAME = "name";
	private final static String JSON_USER_ID = "userid";
	private final static String JSON_DEVICE_KEY = "devicekey";
	private final static String JSON_DATE = "date";
	private final static String JSON_VISIBILITY = "visibility";
	private final static String JSON_STEPS = "steps";
	private final static String JSON_USER_EMAIL = "email";

	private String userid="";
	private String userEmail = "";
	private String name = "";
	private String date = "";
	private String description = "";
	private String deviceKey = "";
	private String visibility = "private";

	private String steps = "";



	public String getRecipeJson() {
		generateJson();
		return recipeJson;
	}


	@DatabaseField(generatedId = true, columnName = "recipe_id")
	private int recipe_id;

	@DatabaseField(columnName = "recipe_json")
	private String recipeJson;

	@DatabaseField(columnName = "cloud_id")
	private String cloudId;

	public int getRecipe_id() {
		return recipe_id;
	}
	private List<RecipeEntry> recipeEntries = new ArrayList<RecipeEntry>();
	private String recipeName = "";




	public void parseJson(){

		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(recipeJson);
		}catch (Exception e){
			return;
		}

		try {
			cloudId = jsonObject.getString(JSON_CLOUD_ID);
		}catch (Exception e){
			cloudId = "";
		}

		try {
			name = jsonObject.getString(JSON_NAME);
		}catch (Exception e){
			name = "";
		}

		try {
			userEmail = jsonObject.getString(JSON_USER_EMAIL);
		}catch (Exception e){
			userEmail = "";
		}

		try {
			deviceKey = jsonObject.getString(JSON_DEVICE_KEY);
		}catch (Exception e){
			deviceKey = "";
		}

		try {
			date = jsonObject.getString(JSON_DATE);
		}catch (Exception e){
			date = "";
		}

		try {
			visibility = jsonObject.getString(JSON_VISIBILITY);
		}catch (Exception e){
			visibility = "private";
		}

		try{
			steps = jsonObject.getString(JSON_STEPS);
		}catch (Exception e){
			steps="";
		}


		recipeJson = jsonObject.toString();

	}


	public Recipe(String cloudId, String recipeName, List<RecipeEntry> entries) {
		this.cloudId = cloudId;
		this.recipeName = recipeName;
		this.recipeEntries = entries;
	}



	public Recipe(String recipeJson) {
		setRecipeJson(recipeJson);
		this.recipeJson = recipeJson;
	}

	public void setRecipeJson(String recipeJson) {
		this.recipeJson = recipeJson;
		parseJson();
	}



	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	public List<RecipeEntry> getRecipeEntries() {
		return recipeEntries;
	}

	public void setRecipeEntries(List<RecipeEntry> recipeEntries) {
		this.recipeEntries = recipeEntries;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}





	/**
	 * This function must be called before inserting the Recipe into the Database DatabaseService.insertRecipe()
	 */
	public void generateRecipeJson(){

		JSONObject jsonObjectRecipe = new JSONObject();
		try {
			JSONArray jsonArrayRecipeEntries = new JSONArray();
			for (RecipeEntry entry : recipeEntries) {
				jsonArrayRecipeEntries
						.put(new JSONObject()
						.put("name", entry.getName())
						.put("weight", entry.getWeight())
						.put("measuredWeight",entry.getMeasuredWeight()));
			}
			jsonObjectRecipe.put("recipeName", recipeName);
			jsonObjectRecipe.put("recipeEntries",jsonArrayRecipeEntries);
		}catch (Exception e){
			e.printStackTrace();
		}

		this.recipeJson = jsonObjectRecipe.toString();
	}




	public double getRecipeTotalWeight(){

		double totalTarget=0;
		for(int i=0;i<getRecipeEntries().size();i++){
			totalTarget = totalTarget + getRecipeEntries().get(i).getWeight();
		}

		return totalTarget;
	}



	public void parseRecipeJson(){
		try {
			JSONObject recipeJson = new JSONObject(this.recipeJson);
			recipeName = recipeJson.getString("recipeName");
		}catch (Exception e){
			e.printStackTrace();
		}

		recipeEntries = new ArrayList<RecipeEntry>();
		try {
			JSONObject jsonObjectRecipe = new JSONObject(recipeJson);
			JSONArray jsonArrayEntries = jsonObjectRecipe.getJSONArray("recipeEntries");
			for(int i = 0; i< jsonArrayEntries.length(); i++){
				recipeEntries.add(new RecipeEntry(jsonArrayEntries.getJSONObject(i).getString("name"),jsonArrayEntries.getJSONObject(i).getDouble("weight"),jsonArrayEntries.getJSONObject(i).getDouble("measuredWeight")));
			}
		}catch (Exception e){
			Log.e("Recipe", "error reading recipe entries" + e.toString());
			recipeEntries = new ArrayList<RecipeEntry>();
		}


	}


	public void generateJson(){
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put(JSON_CLOUD_ID,cloudId)
					  .put(JSON_NAME, name)
					  .put(JSON_USER_ID,userid)
					  .put(JSON_DEVICE_KEY,deviceKey)
					  .put(JSON_DATE,date)
					  .put(JSON_VISIBILITY,visibility)
					  .put(JSON_STEPS,steps);

		}catch (Exception e){
			e.printStackTrace();
		}
		recipeJson = jsonObject.toString();
	}




}




