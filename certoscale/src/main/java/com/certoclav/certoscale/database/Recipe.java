package com.certoclav.certoscale.database;


import com.certoclav.certoscale.model.RecipeEntry;
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


	public int getRecipe_id() {
		return recipe_id;
	}

	@DatabaseField(generatedId = true, columnName = "recipe_id")
	private int recipe_id;


	@DatabaseField(columnName = "recipe_json")
	private String recipeJson;

	public String generateRecipeJson(String name, List<RecipeEntry> recipeEntrys){
		JSONObject jsonObjectRecipe = new JSONObject();
		try {
			JSONArray jsonArrayRecipeEntries = new JSONArray();
			for (RecipeEntry entry : recipeEntrys) {
				jsonArrayRecipeEntries.put(new JSONObject().put("name", entry.getName()).put("weight", entry.getWeight()));
			}
			jsonObjectRecipe.put("recipeName", name);
			jsonObjectRecipe.put("recipeEntries",jsonArrayRecipeEntries);
		}catch (Exception e){
			e.printStackTrace();
		}
		return jsonObjectRecipe.toString();
	}

	public Recipe(String cloudId, String recipeJson) {
		this.cloudId = cloudId;
		this.recipeJson = recipeJson;
	}
	public Recipe(String cloudId, String recipeName, List<RecipeEntry> entries) {
		this.cloudId = cloudId;
		this.recipeJson = generateRecipeJson(recipeName,entries);
	}
	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	public String getRecipeJson() {
		return recipeJson;
	}

	public void setRecipeJson(String recipeJson) {
		this.recipeJson = recipeJson;
	}

	@DatabaseField(columnName = "cloud_id")
	private String cloudId;


	public String getRecipeName() {
		String retVal = "";

		try {
			JSONObject recipeJson = new JSONObject(this.recipeJson);
			retVal = recipeJson.getString("recipeName");
		}catch (Exception e){
			e.printStackTrace();
		}
		return retVal;

	}

	public List<RecipeEntry> getRecipeEntries() {
		ArrayList<RecipeEntry> entries = new ArrayList<RecipeEntry>();
		try {
			JSONObject jsonObjectRecipe = new JSONObject(recipeJson);
			JSONArray jsonArrayEntries = jsonObjectRecipe.getJSONArray("recipeEntries");
			for(int i = 0; i< jsonArrayEntries.length(); i++){
				entries.add(new RecipeEntry(jsonArrayEntries.getJSONObject(i).getString("name"),jsonArrayEntries.getJSONObject(i).getDouble("weight")));
			}

		}catch (Exception e){
			entries = new ArrayList<RecipeEntry>();
		}
		return entries;

	}
}




