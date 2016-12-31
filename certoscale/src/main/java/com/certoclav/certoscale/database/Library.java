package com.certoclav.certoscale.database;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * A simple demonstration object we are creating and persisting to the database.
 * implemented according to OHAUS user manual
 */
@DatabaseTable(tableName = "library")
public class Library {
	public static final String FIELD_LIBRARY_APPLICATION = "application";
	public static final String FIELD_LIBRARY_ID = "library_id";
	public static final String FIELD_LIBRARY_CLOUD_ID = "cloud_id";
	public static final String FIELD_LIBRARY_VERSION = "version";
	public static final String FIELD_LIBRARY_NAME = "name";
	public static final String FIELD_LIBRARY_TARA = "tara";
	public static final String FIELD_LIBRARY_TARGET = "target";
	public static final String FIELD_LIBRARY_SAMPLE_SIZE = "sample_size";
	public static final String FIELD_LIBRARY_AVERAGE_PIECE_WEIGHT = "apw";
	public static final String FIELD_LIBRARY_LIMIT_UNDER = "limit_under";
	public static final String FIELD_LIBRARY_LIMIT_UNDER_PIECES = "limit_under_pieces";
	public static final String FIELD_LIBRARY_LIMIT_OVER = "limit_over";
	public static final String FIELD_LIBRARY_LIMIT_OVER_PIECES = "limit_over_pieces";
	public static final String FIELD_LIBRARY_REFERENCE_WEIGHT= "reference_weight";
	public static final String FIELD_LIBRARY_REFERENCE_ADJUSTMENT= "reference_weight_adjustment";
	public static final String FIELD_LIBRARY_LEVEL = "level";
	public static final String FIELD_LIBRARY_MODE = "mode";
	public static final String FIELD_LIBRARY_DATE = "date";
	public static final String FIELD_LIBRARY_IS_LOCAL = "islocal";
	public static final String FIELD_LIBRARY_USER_EMAIL = "email";
	public static final String FIELD_LIBRARY_AVERAGING_TIME = "averagint_time";
	// id is generated by the database and set on the object automatically
	@DatabaseField(generatedId = true, columnName = FIELD_LIBRARY_ID)
	private int libraryId;

	@DatabaseField(columnName = FIELD_LIBRARY_APPLICATION)
	private int application;

	@DatabaseField(columnName = FIELD_LIBRARY_CLOUD_ID)
	private String cloudId;

	public double getAveragePieceWeight() {
		return averagePieceWeight;
	}

	public void setAveragePieceWeight(double averagePieceWeight) {
		this.averagePieceWeight = averagePieceWeight;
	}

	@DatabaseField(columnName = FIELD_LIBRARY_AVERAGE_PIECE_WEIGHT)
	private double averagePieceWeight;



	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getAveragingTime() {
		return averagingTime;
	}

	public void setAveragingTime(int averagingTime) {
		this.averagingTime = averagingTime;
	}

	@DatabaseField(columnName = FIELD_LIBRARY_AVERAGING_TIME)
	private int averagingTime;

	@DatabaseField(columnName = FIELD_LIBRARY_USER_EMAIL)
	private String userEmail;


	public Library(String userEmail, int application, String cloudId, int version, String name, double tara, double target, double sampleSize, double averagePieceWeight, double underLimit, double underLimitPieces, double overLimit, double overLimitPieces, double refweight, double refweightAdjustment,double level, int mode, Date date, Boolean isLocal) {
		this.userEmail = userEmail;
		this.application = application;
		this.cloudId = cloudId;
		this.version = version;
		this.name = name;
		this.tara = tara;
		this.target = target;
		this.sampleSize = sampleSize;
		this.averagePieceWeight = averagePieceWeight;
		this.underLimit = underLimit;
		this.underLimitPieces = underLimitPieces;
		this.overLimit = overLimit;
		this.overLimitPieces = overLimitPieces;
		this.referenceweight=refweight;
		this.referenceweightAdjustment=refweightAdjustment;
		this.level = level;
		this.mode = mode;
		this.date = date;
		this.isLocal = isLocal;
	}

	public int getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(int libraryId) {
		this.libraryId = libraryId;
	}

	public int getApplication() {
		return application;
	}

	public void setApplication(int application) {
		this.application = application;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTara() {
		return tara;
	}

	public void setTara(double tara) {
		this.tara = tara;
	}

	public double getTarget() {
		return target;
	}


	public double getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(double sampleSize) {
		this.sampleSize = sampleSize;
	}

	public double getUnderLimit() {
		return underLimit;
	}

	public double getUnderLimitPieces(){

		return underLimitPieces;
	}

	public double getReferenceWeight(){
		return referenceweight;
	}

	public double getReferenceweightAdjustment(){
		return referenceweightAdjustment;
	}

	public void setReferenceWeight(double referenceweight) {
		this.referenceweight = referenceweight;
	}

	public void setReferenceweightAdjustment(double referenceweightAdjustment) {
		this.referenceweightAdjustment = referenceweightAdjustment;
	}




	public void setUnderLimit(double underLimit) {
		this.underLimit = underLimit;
	}

	public void setTarget(double target){
		this.target = target;
	}
	public void setUnderLimitPieces(double underLimitPieces) {
		this.underLimitPieces = underLimitPieces;
	}

	public double getOverLimit() {
		return overLimit;
	}

	public double getOverLimitPieces() {
		return overLimitPieces;
	}
	public double getTargetPieces() {
		return target;
	}


	public void setOverLimit(double overLimit) {
		this.overLimit = overLimit;
	}

	public void setOverLimitPieces(double overLimitPieces) {
		this.overLimitPieces = overLimitPieces;
	}

	public double getLevel() {
		return level;
	}

	public void setLevel(double level) {
		this.level = level;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getLocal() {
		return isLocal;
	}

	public void setLocal(Boolean local) {
		isLocal = local;
	}

	@DatabaseField(columnName = FIELD_LIBRARY_VERSION)
	private int version;

	@DatabaseField(columnName = FIELD_LIBRARY_NAME)
	private String name;

	@DatabaseField(columnName = FIELD_LIBRARY_TARA)
	private double tara;


	@DatabaseField(columnName = FIELD_LIBRARY_SAMPLE_SIZE)
	private double sampleSize;

	@DatabaseField(columnName = FIELD_LIBRARY_LIMIT_UNDER)
	private double underLimit;

	@DatabaseField(columnName = FIELD_LIBRARY_TARGET)
	private double target;

	@DatabaseField(columnName = FIELD_LIBRARY_LIMIT_UNDER_PIECES)
	private double underLimitPieces;


	@DatabaseField(columnName = FIELD_LIBRARY_LIMIT_OVER)
	private double overLimit;

	@DatabaseField(columnName = FIELD_LIBRARY_LIMIT_OVER_PIECES)
	private double overLimitPieces;

	@DatabaseField(columnName = FIELD_LIBRARY_REFERENCE_WEIGHT)
	private double referenceweight;

	@DatabaseField(columnName = FIELD_LIBRARY_REFERENCE_ADJUSTMENT)
	private double referenceweightAdjustment;


	@DatabaseField(columnName = FIELD_LIBRARY_LEVEL)
	private double level;

	@DatabaseField(columnName = FIELD_LIBRARY_MODE)
	private int mode;


	@DatabaseField(columnName = FIELD_LIBRARY_DATE,dataType = DataType.DATE)
	private Date date;

	@DatabaseField(columnName = FIELD_LIBRARY_IS_LOCAL)
	private Boolean isLocal;


	Library() {
		// needed by ormlite
	}


}




