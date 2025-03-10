package com.certoclav.certoscale.database;


import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


@DatabaseTable(tableName = "protocol")
public class Protocol {

    private static final String JSON_CONTENT_STRING = "content";
    private final static String JSON_CONTENT_JSON = "content_json";
    private final static String JSON_DEVICE_KEY = "devicekey";
    private final static String JSON_DATE = "date";
    private final static String JSON_VISIBILITY = "visibility";
    private final static String JSON_NAME = "name";
    private final static String JSON_CLOUD_ID = "_id"; //user db id from MongoDb
    private final static String JSON_USER_EMAIL = "email";
    private final static String JSON_SIGNATURE = "signature";

    //additional for ash_determinatin_ticket_system
    private final static String JSON_ASH_SAMPLE_NAME = "ash_name_sample";
    private final static String JSON_ASH_BEAKER_NAME = "ash_name_beaker";
    private final static String JSON_ASH_ARRAY_GLOW_WEIGHTS = "ash_array_glow_weights";
    private final static String JSON_ASH_ARRAY_GLOW_WEIGHTS_IGNORED_INDEX = "ash_array_glow_weights_ignored_index";
    private final static String JSON_ASH_ARRAY_GLOW_WEIGHTS_USERS = "ash_array_glow_weights_users";
    private final static String JSON_ASH_WEIGHT_BEAKER = "ash_weight_beaker";
    private final static String JSON_ASH_WEIGHT_BEAKER_WITH_SAMPLE = "ash_weight_beaker_with_sample";
    private final static String JSON_OVEN_TEMPERATURE = "ash_oven_temperature";
    private final static String JSON_IS_PENDING = "ispending";


    private String name = "";
    private String userEmail = "";
    private String deviceKey = "";
    private String visibility = "global";
    private String content = "";

    @DatabaseField(columnName = "protocol_date")
    private String date = "";

    @DatabaseField(columnName = "is_uploaded")
    private int isUploaded;

    private Date dateFortmated;
    private String cloudId = "-1";
    private String contentJson = "";
    private double ovenTemperature = 0;
    private double recentWeight = 0;

    private boolean isParsedCompletely;

    public String getAshSampleName() {
        return ashSampleName;
    }

    public void setAshSampleName(String ashSampleName) {
        this.ashSampleName = ashSampleName;
    }

    public String getAshBeakerName() {
        return ashBeakerName;
    }

    public void setAshBeakerName(String ashBeakerName) {

        this.ashBeakerName = ashBeakerName;
    }


    public void saveIntoDb() {
        DatabaseService db = new DatabaseService(ApplicationController.getContext());
        while (db.deleteProtocol(this) == -1) ;
        db.insertProtocol(this);
    }

    public List<Double> getAshArrayGlowWeights(boolean isValidOnly) {
        if (isValidOnly) {
            List<Double> validWeights = new ArrayList<>();
            for (int i = 0; i < ashArrayGlowWeights.size(); i++) {
                if (ashArrayGlowWeightsIgnoredIndex.contains(i)) continue;
                validWeights.add(ashArrayGlowWeights.get(i));
            }
            return validWeights;
        }
        return ashArrayGlowWeights;
    }

    public List<Integer> getAshArrayGlowWeightsIgnoredIndex() {
        return ashArrayGlowWeightsIgnoredIndex;
    }
//
//    public Double getAshArrayGlowWeightsDifference() {
//        if (getAshArrayGlowWeights(true).size() >= 2) {
//            int lastIndex = getAshArrayGlowWeights(true).size() - 1;
//            return getAshArrayGlowWeights(true).get(lastIndex) - getAshArrayGlowWeights(true).get(lastIndex - 1);
//        }
//        return -1d;
//    }
//
//
//    public void setAshArrayGlowWeights(List<Double> ashArrayGlowWeights) {
//        this.ashArrayGlowWeights = ashArrayGlowWeights;
//    }


    public Double getBeakerWeight() {
        return ashWeightBeaker;
    }

    public void saveBeakerWeight(Double ashWeightBeaker) {
        this.ashWeightBeaker = ashWeightBeaker;
    }

    public Double getAshWeightBeakerWithSample() {
        return ashWeightBeakerWithSample;
    }

    public Double getSampleWeight() {
        return ashWeightBeakerWithSample - ashWeightBeaker;
    }

    public void saveBeakerAndSampleWeight(Double ashWeightBeakerWithSample) {
        this.ashWeightBeakerWithSample = ashWeightBeakerWithSample;
    }

    @DatabaseField(columnName = "protocol_sample_name")
    private String ashSampleName = "";
    @DatabaseField(columnName = "protocol_beaker_name")
    private String ashBeakerName = "";
    private List<Double> ashArrayGlowWeights = new ArrayList<>();
    private List<Integer> ashArrayGlowWeightsIgnoredIndex = new ArrayList<>();
    private List<String> ashArrayGlowWeightsUser = new ArrayList<>();
    private Double ashWeightBeaker = 0d;
    private Double ashWeightBeakerWithSample = 0d;

    public Boolean getIsPending() {
        return isPending;
    }

    public void setPending(Boolean isPending) {
        this.isPending = isPending;
    }

    @DatabaseField(columnName = "protocol_is_pending")
    private Boolean isPending = true;


    @DatabaseField(columnName = JSON_SIGNATURE, dataType = DataType.STRING_BYTES)
    private String signature;


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

    public Date getDateFormart() {
        if (dateFortmated == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                dateFortmated = dateFormat.parse(getDate());
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        }
        return dateFortmated;
    }


    public void setDate(String date) {
        this.date = date;
    }


    // id is generated by the database and set on the object automatically


    public Protocol() {

    }

    public Protocol(String protocolJson) {
        setProtocolJson(protocolJson);
        this.protocolJson = protocolJson;
    }


    public void setProtocolJson(String itemJson) {
        this.protocolJson = itemJson;
        parseJson();
    }

    public void parseJsonLight() {
        isParsedCompletely = false;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(protocolJson);
        } catch (Exception e) {
            return;
        }

        try {
            ashSampleName = jsonObject.getString(JSON_ASH_SAMPLE_NAME);
        } catch (Exception e) {
            ashSampleName = "";
        }

        try {
            ashBeakerName = jsonObject.getString(JSON_ASH_BEAKER_NAME);
        } catch (Exception e) {
            ashBeakerName = "";
        }

        try {
            isPending = jsonObject.getBoolean(JSON_IS_PENDING);
        } catch (Exception e) {
            isPending = false;
        }
    }

    public void parseJson() {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(protocolJson);
        } catch (Exception e) {
            return;
        }

        try {
            name = jsonObject.getString(JSON_NAME);
        } catch (Exception e) {
            name = "";
        }
        try {
            userEmail = jsonObject.getString(JSON_USER_EMAIL);
        } catch (Exception e) {
            userEmail = "";
        }

        try {
            visibility = jsonObject.getString(JSON_VISIBILITY);
        } catch (Exception e) {
            visibility = "private";
        }


        try {
            date = jsonObject.getString(JSON_DATE);
        } catch (Exception e) {
            date = "";
        }


        try {
            deviceKey = jsonObject.getString(JSON_DEVICE_KEY);
        } catch (Exception e) {
            deviceKey = "";
        }


        try {
            cloudId = jsonObject.getString(JSON_CLOUD_ID);
        } catch (Exception e) {
            cloudId = "";
        }

        try {
            content = jsonObject.getString(JSON_CONTENT_STRING);
        } catch (Exception e) {
            content = "";
        }
        try {
            signature = jsonObject.getString(JSON_SIGNATURE);

        } catch (Exception e) {
            signature = "";
        }

        try {
            content = jsonObject.getString(JSON_CONTENT_STRING);
        } catch (Exception e) {
            content = "";
        }


        try {
            ashSampleName = jsonObject.getString(JSON_ASH_SAMPLE_NAME);
        } catch (Exception e) {
            ashSampleName = "";
        }

        try {
            ashBeakerName = jsonObject.getString(JSON_ASH_BEAKER_NAME);
        } catch (Exception e) {
            ashBeakerName = "";
        }

        ashArrayGlowWeights.clear();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray = jsonObject.getJSONArray(JSON_ASH_ARRAY_GLOW_WEIGHTS);
        } catch (Exception e) {
            jsonArray = new JSONArray();
        }


        for (int i = 0; i < jsonArray.length(); i++) {
            Double glowWeight;
            try {
                glowWeight = Double.valueOf(jsonArray.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
                glowWeight = 0d;
            }
            ashArrayGlowWeights.add(glowWeight);
        }

        //Get Ingored weight index
        ashArrayGlowWeightsIgnoredIndex.clear();
        JSONArray jsonArrayIngored = new JSONArray();

        try {
            jsonArrayIngored = jsonObject.getJSONArray(JSON_ASH_ARRAY_GLOW_WEIGHTS_IGNORED_INDEX);
        } catch (Exception e) {
            jsonArrayIngored = new JSONArray();
        }

        for (int i = 0; i < jsonArrayIngored.length(); i++) {
            try {
                int index = Integer.valueOf(jsonArrayIngored.get(i).toString());
                ashArrayGlowWeightsIgnoredIndex.add(index);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //********************************


        try {
            ashWeightBeaker = jsonObject.getDouble(JSON_ASH_WEIGHT_BEAKER);
        } catch (Exception e) {
            ashWeightBeaker = 0d;
        }

        try {
            ashWeightBeakerWithSample = jsonObject.getDouble(JSON_ASH_WEIGHT_BEAKER_WITH_SAMPLE);
        } catch (Exception e) {
            ashWeightBeakerWithSample = 0d;
        }

        try {
            isPending = jsonObject.getBoolean(JSON_IS_PENDING);
        } catch (Exception e) {
            isPending = false;
        }

        try {
            ovenTemperature = jsonObject.getDouble(JSON_OVEN_TEMPERATURE);
        } catch (Exception e) {
            ovenTemperature = 0d;
        }


        ashArrayGlowWeightsUser.clear();
        jsonArray = new JSONArray();

        try {
            jsonArray = jsonObject.getJSONArray(JSON_ASH_ARRAY_GLOW_WEIGHTS_USERS);
        } catch (Exception e) {
            jsonArray = new JSONArray();
        }


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject glowWeightsEntry = new JSONObject();
            String username = "";
            try {
                username = (String) jsonArray.get(i);
            } catch (Exception e) {
                username = "";
            }
            ashArrayGlowWeightsUser.add(username);
        }

        protocolJson = jsonObject.toString();
        isParsedCompletely = true;
    }


    public Protocol(String cloudId, String protocolJson) {
        this.cloudId = cloudId;
        this.protocolJson = protocolJson;
    }

    public Protocol(String cloudId, String name, String userEmail, String deviceKey, String date, String visibility, String content, String signature) {
        this.cloudId = cloudId;
        this.name = name;
        this.userEmail = userEmail;
        this.deviceKey = deviceKey;
        this.date = date;
        this.visibility = visibility;
        this.content = content;
        this.signature = signature;
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

    public int getId() {
        return item_id;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


    public void generateJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(JSON_CLOUD_ID, cloudId)
                    .put(JSON_NAME, name)
                    .put(JSON_DEVICE_KEY, deviceKey)
                    .put(JSON_DATE, date)
                    .put(JSON_USER_EMAIL, userEmail)
                    .put(JSON_VISIBILITY, visibility)
                    .put(JSON_CONTENT_STRING, content)
                    .put(JSON_SIGNATURE, signature)
                    .put(JSON_ASH_SAMPLE_NAME, ashSampleName)
                    .put(JSON_ASH_BEAKER_NAME, ashBeakerName)
                    .put(JSON_ASH_WEIGHT_BEAKER, ashWeightBeaker)
                    .put(JSON_ASH_WEIGHT_BEAKER_WITH_SAMPLE, ashWeightBeakerWithSample)
                    .put(JSON_IS_PENDING, isPending)
                    .put(JSON_OVEN_TEMPERATURE, ovenTemperature);

            JSONArray jsonArrayGlowWeights = new JSONArray();
            for (Double value : ashArrayGlowWeights) {
                jsonArrayGlowWeights.put(value);
            }
            jsonObject.put(JSON_ASH_ARRAY_GLOW_WEIGHTS, jsonArrayGlowWeights);


            JSONArray jsonArrayGlowWeightsIgnoredIndex = new JSONArray();
            for (Integer value : ashArrayGlowWeightsIgnoredIndex) {
                jsonArrayGlowWeightsIgnoredIndex.put(value);
            }
            jsonObject.put(JSON_ASH_ARRAY_GLOW_WEIGHTS_IGNORED_INDEX, jsonArrayGlowWeightsIgnoredIndex);


            JSONArray jsonArrayGlowWeightsUsers = new JSONArray();
            for (String value : ashArrayGlowWeightsUser) {
                jsonArrayGlowWeightsUsers.put(value);
            }
            jsonObject.put(JSON_ASH_ARRAY_GLOW_WEIGHTS_USERS, jsonArrayGlowWeightsUsers);


        } catch (Exception e) {
            e.printStackTrace();
        }
        protocolJson = jsonObject.toString();
    }

    public String generateCSV() {
        parseJson();
        return content.replaceAll(": ", ";").replaceAll(":", ";");
    }

    public Double getLastGlowWeight() {
        List<Double> weights = getAshArrayGlowWeights(true);
        if (weights.size() > 0)
            return weights.get(weights.size() - 1);
        else
            return getSampleWeight() + getBeakerWeight();
    }

    public Double getAshResultInGram() {
        try {
            Double weightWetSample = ashWeightBeakerWithSample - ashWeightBeaker;
            Double weightDrySample = getLastGlowWeight() - ashWeightBeaker;

            return weightDrySample;
        } catch (Exception e) {
            return 0d;
        }

    }

//    public float getAshResultInPercent() {
//        try {
//            float weightWetSample = ((roundDouble(roundDouble(ashWeightBeakerWithSample) - roundDouble(ashWeightBeaker))) * 10000) / 10000f;
//            float weightDrySample = ((roundDouble(roundDouble(getLastGlowWeight()) - roundDouble(ashWeightBeaker))) * 10000) / 10000f;
//            Log.d("result_1", roundDouble(getLastGlowWeight()) + " " + roundDouble(ashWeightBeaker));
//            Log.d("result_2", ((roundDouble(getLastGlowWeight()) - roundDouble(ashWeightBeaker)) * 10000) + "");
//            Log.d("result_2", (((roundDouble(getLastGlowWeight()) - roundDouble(ashWeightBeaker)) * 10000) / 10000) + "");
//            Log.d("result", weightDrySample + " " + weightWetSample + " " +
//                    weightDrySample / weightWetSample + " " + ashWeightBeakerWithSample + " " + ashWeightBeaker + " " + getLastGlowWeight());
//            float result = (weightDrySample / weightWetSample) * 100f;
//            return roundDouble(result);
//        } catch (Exception e) {
//            return 0f;
//        }
//
//    }

    public String getAshResultPercentageAsString() {
        try {
            Double weightWetSample = ashWeightBeakerWithSample - ashWeightBeaker;
            weightWetSample = Double.valueOf(ApplicationManager.getInstance()
                    .getTransformedWeightAsString(weightWetSample).replace(",", "."));
            Double weightDrySample = getLastGlowWeight() - ashWeightBeaker;
            Double result = (weightDrySample * 100.0 / weightWetSample);
            return ApplicationManager.getInstance().getTransformedWeightAsString(result.doubleValue());
        } catch (Exception e) {
            return "0.0000";
        }
    }

    public double getLastAshWeight() {
        return getLastGlowWeight() - ashWeightBeaker;
    }

    public double getOvenTemperature() {
        return ovenTemperature;
    }

    public void setOvenTemperature(double ovenTemperature) {
        this.ovenTemperature = ovenTemperature;
    }

    public double getRecentWeight(boolean current) {
        List<Double> weights = ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeights(true);

        for (Double w : weights)
            android.util.Log.e("weightash", w + "");

        if (!current)
            if (weights.size() == 1)
                return ApplicationManager.getInstance().getCurrentProtocol().getAshWeightBeakerWithSample();
            else
                return weights.get(weights.size() - 2);


        return weights.get(weights.size() - 1);
    }

    public void abortLastWeight(boolean isInRawData) {

        if (isInRawData)
            ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeightsIgnoredIndex()
                    .add(ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeights(false).size() - 1);
        else {

            if (ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeights(false).size() > 0)
                ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeights(false)
                        .remove(ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeights(false).size() - 1);

            if (ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeightsUser(false).size() > 0)
                ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeightsUser(false)
                        .remove(ApplicationManager.getInstance().getCurrentProtocol().getAshArrayGlowWeightsUser(false).size() - 1);
        }
    }

    public boolean isParsedCompletely() {
        return isParsedCompletely;
    }

    public void setRecentWeight(double recentWeight) {
        this.recentWeight = recentWeight;
    }

    public List<String> getAshArrayGlowWeightsUser(boolean isValidOnly) {
        if (isValidOnly) {
            List<String> validWeightsUser = new ArrayList<>();
            for (int i = 0; i < ashArrayGlowWeightsUser.size(); i++) {
                if (ashArrayGlowWeightsIgnoredIndex.contains(i)) continue;
                validWeightsUser.add(ashArrayGlowWeightsUser.get(i));
            }
            return validWeightsUser;
        }
        return ashArrayGlowWeightsUser;
    }

    public boolean isUploaded() {
        return isUploaded == 1;
    }

    public void setUploaded(boolean isUploaded) {
        this.isUploaded = isUploaded ? 1 : 0;
    }
}



