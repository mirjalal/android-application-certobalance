package com.certoclav.certoscale.database;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;


@DatabaseTable(tableName = "protocol")
public class Protocol {


	private static final String JSON_CONTENT = "content";
	private final static String JSON_DEVICE_KEY = "devicekey";
	private final static String JSON_DATE = "date";
	private final static String JSON_VISIBILITY = "visibility";
	private final static String JSON_NAME = "name";
	private final static String JSON_CLOUD_ID = "_id"; //user db id from MongoDb
	private final static String JSON_USER_EMAIL = "email";


	private String name = "";
	private String userEmail = "";
	private String deviceKey = "";
	private String visibility = "global";
	private String content = "";
	private String date = "";
	private String cloudId = "";



	public String getProtocolJson() {
		generateJson();
		return protocolJson;
	}


	@DatabaseField(generatedId = true, columnName = "protocol_id")
	private int item_id;

	@DatabaseField(columnName = "protocol_json")
	private String protocolJson;



	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}


	// id is generated by the database and set on the object automatically


	Protocol() {

	}

	public Protocol( String protocolJson) {
		setProtocolJson(protocolJson);
		this.protocolJson = protocolJson;
	}


	public void setProtocolJson(String itemJson) {
		this.protocolJson = itemJson;
		parseJson();
	}

	public void parseJson(){

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(protocolJson);
		}catch (Exception e){
			return;
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
			visibility = jsonObject.getString(JSON_VISIBILITY);
		}catch (Exception e){
			visibility = "private";
		}



		try {
			date = jsonObject.getString(JSON_DATE);
		}catch (Exception e){
			date = "";
		}


		try {
			deviceKey = jsonObject.getString(JSON_DEVICE_KEY);
		}catch (Exception e){
			deviceKey = "";
		}


		try {
			cloudId = jsonObject.getString(JSON_CLOUD_ID);
		}catch (Exception e){
			cloudId = "";
		}

		try {
			content = jsonObject.getString(JSON_CONTENT);
		}catch (Exception e){
			content = "";
		}
		protocolJson = jsonObject.toString();

	}







	public Protocol(String cloudId, String protocolJson) {
		this.cloudId = cloudId;
		this.protocolJson = protocolJson;
	}

	public Protocol(String cloudId, String name, String userEmail, String deviceKey, String date, String visibility, String content) {
		this.cloudId = cloudId;
		this.name = name;
		this.userEmail = userEmail;
		this.deviceKey = deviceKey;
		this.date = date;
		this.visibility = visibility;
		this.content = content;
	}




	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getDeviceKey() {
		return deviceKey;
	}

	public void setDeviceKey(String deviceKey) {
		this.deviceKey = deviceKey;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}




	public void generateJson(){
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put(JSON_CLOUD_ID,cloudId)
					.put(JSON_NAME, name)
					.put(JSON_DEVICE_KEY,deviceKey)
					.put(JSON_DATE,date)
					.put(JSON_USER_EMAIL, userEmail)
					.put(JSON_VISIBILITY,visibility)
					.put(JSON_CONTENT,content);
		}catch (Exception e){
			e.printStackTrace();
		}
		protocolJson = jsonObject.toString();
	}


}






