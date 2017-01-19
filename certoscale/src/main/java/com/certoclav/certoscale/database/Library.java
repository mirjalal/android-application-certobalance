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
	public static final String FIELD_LIBRARY_LIMIT_UNDER_CHECK_WEIGHING= "limit_under_check";
	public static final String FIELD_LIBRARY_LIMIT_OVER_CHECK_WEIGHING = "limit_over_check";
	public static final String FIELD_LIBRARY_LIMIT_CHECK_TOLERANCE_UNDER= "under_tolerance_check";
	public static final String FIELD_LIBRARY_LIMIT_CHECK_NOMINAL="check_nominal";
	public static final String FIELD_LIBRARY_LIMIT_CHECK_TOLERANCE_Over="over_tolerance_check";
	public static final String FIELD_LIBRARY_WATER_TEMP="Water Temperature";
	public static final String FIELD_LIBRARY_LIQUID_DENSITY="Liquid Density";
	public static final String FIELD_LIBRARY_SINKER_VOLUME="Sinker Volume";
	public static final String FIELD_LIBRARY_OIL_DENSITY="Oil Density";
	public static final String FIELD_LIBRARY_OILED_WEIGHT="Oiled Weight";
	public static final String FIELD_LIBRARY_SQC_NOMINAL="Nominal Weight";
	public static final String FIELD_LIBRARY_SQC_PTOLERANCE1="+ Tolerance1";
	public static final String FIELD_LIBRARY_SQC_PTOLERANCE2="+ Tolerance2";
	public static final String FIELD_LIBRARY_SQC_NTOLERANCE1="- Tolerance1";
	public static final String FIELD_LIBRARY_SQC_NTOLERANCE2="- Tolerance2";
	public static final String FIELD_LIBRARY_PIPETTE_NOMINAL="Pipette Nominal";
	public static final String FIELD_LIBRARY_PIPETTE_WATERTEMP="Pipette Water Temperature";
	public static final String FIELD_LIBRARY_PIPETTE_INACCURACY="Pipette Inaccuracy";
	public static final String FIELD_LIBRARY_PIPETTE_IMPRECISION="Pipette Imprecision";
	public static final String FIELD_LIBRARY_PIPETTE_PRESSURE="Pipette Pressure";


	// id is generated by the database and set on the object automatically
	@DatabaseField(generatedId = true, columnName = FIELD_LIBRARY_ID)
	private int libraryId;

	@DatabaseField(columnName = FIELD_LIBRARY_APPLICATION)
	private int application;

	@DatabaseField(columnName = FIELD_LIBRARY_CLOUD_ID)
	private String cloudId;

	@DatabaseField(columnName = FIELD_LIBRARY_AVERAGE_PIECE_WEIGHT)
	private double averagePieceWeight;

	@DatabaseField(columnName = FIELD_LIBRARY_USER_EMAIL)
	private String userEmail;

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

	@DatabaseField(columnName = FIELD_LIBRARY_WATER_TEMP)
	private double waterTemp;

	@DatabaseField(columnName = FIELD_LIBRARY_LIQUID_DENSITY)
	private double liquidDensity;

	@DatabaseField(columnName = FIELD_LIBRARY_OILED_WEIGHT)
	private double oiledWeight;

	@DatabaseField(columnName = FIELD_LIBRARY_SINKER_VOLUME)
	private double sinkerVolume;

	@DatabaseField(columnName = FIELD_LIBRARY_OIL_DENSITY)
	private double oilDensity;


	@DatabaseField(columnName = FIELD_LIBRARY_TARGET)
	private double target;

	@DatabaseField(columnName = FIELD_LIBRARY_LIMIT_UNDER_PIECES)
	private double underLimitPieces;

	@DatabaseField(columnName = FIELD_LIBRARY_LIMIT_UNDER_CHECK_WEIGHING)
	private double underLimitCheckWeighing;

	@DatabaseField(columnName = FIELD_LIBRARY_LIMIT_OVER_CHECK_WEIGHING)
	private double overLimitCheckWeighing;

	@DatabaseField(columnName = FIELD_LIBRARY_LIMIT_CHECK_NOMINAL)
	private double CheckNominal;

	@DatabaseField(columnName = FIELD_LIBRARY_LIMIT_CHECK_TOLERANCE_UNDER)
	private double CheckToleranceUnder;

	@DatabaseField(columnName = FIELD_LIBRARY_LIMIT_CHECK_TOLERANCE_Over)
	private double CheckToleranceOver;

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

	@DatabaseField(columnName = FIELD_LIBRARY_AVERAGING_TIME)
	private double averagingTime;

	@DatabaseField(columnName = FIELD_LIBRARY_SQC_NOMINAL)
	private double SQCNominal;

	@DatabaseField(columnName = FIELD_LIBRARY_SQC_PTOLERANCE1)
	private double SQCpTolerance1;

	@DatabaseField(columnName = FIELD_LIBRARY_SQC_PTOLERANCE2)
	private double SQCpTolerance2;

	@DatabaseField(columnName = FIELD_LIBRARY_SQC_NTOLERANCE1)
	private double SQCnTolerance1;

	@DatabaseField(columnName = FIELD_LIBRARY_SQC_NTOLERANCE2)
	private double SQCnTolerance2;


	@DatabaseField(columnName = FIELD_LIBRARY_PIPETTE_NOMINAL)
	private double pipetteNominal;



	@DatabaseField(columnName = FIELD_LIBRARY_PIPETTE_WATERTEMP)
	private double pipetteWaterTemp;



	@DatabaseField(columnName = FIELD_LIBRARY_PIPETTE_INACCURACY)
	private double pipetteInaccuracy;



	@DatabaseField(columnName = FIELD_LIBRARY_PIPETTE_IMPRECISION)
	private double pipetteImprecision;



	@DatabaseField(columnName = FIELD_LIBRARY_PIPETTE_PRESSURE)
	private double pipettePressure;






	Library() {
		// needed by ormlite
	}

	public Library(String userEmail, int application, String cloudId, int version, String name, double tara, double target, double sampleSize, double averagePieceWeight, double underLimit,
				   double underLimitPieces, double overLimit, double overLimitPieces, double refweight, double refweightAdjustment,double level, int mode, Date date, Boolean isLocal, double avtime,
				   double underLimitcheck, double overLimitcheck, double checkNominal,double checkToleranceUnder,double checkToleranceOver, double waterTemp, double liquidDensity,double sinkerVolume ,
				   double oilDensity, double oiledWeight, double SQCNominal,double SQCpTolerance1,double SQCpTolerance2,double SQCnTolerance1, double SQCnTolerance2, double pipetteNominal, double pipetteWaterTemp,
				   double pipetteInaccuracy, double pipetteImprecision, double pipettePressure) {

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
		this.averagingTime = avtime;
		this.underLimitCheckWeighing=underLimitcheck;
		this.overLimitCheckWeighing=overLimitcheck;
		this.CheckNominal=checkNominal;
		this.CheckToleranceUnder=checkToleranceUnder;
		this.CheckToleranceOver=checkToleranceOver;
		this.waterTemp=waterTemp;
		this.liquidDensity=liquidDensity;
		this.sinkerVolume=sinkerVolume;
		this.oilDensity=oilDensity;
		this.oiledWeight=oiledWeight;
		this.SQCNominal=SQCNominal;
		this.SQCpTolerance1=SQCpTolerance1;
		this.SQCpTolerance2=SQCpTolerance2;
		this.SQCnTolerance1=SQCnTolerance1;
		this.SQCnTolerance2=SQCnTolerance2;
		this.pipetteNominal=pipetteNominal;
		this.pipetteWaterTemp=pipetteWaterTemp;
		this.pipetteInaccuracy=pipetteInaccuracy;
		this.pipetteImprecision=pipetteImprecision;
		this.pipettePressure=pipettePressure;


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

	public double getWaterTemp(){
		return waterTemp;
	}

	public double getSinkerVolume(){
		return sinkerVolume;
	}

	public double getLiquidDensity(){
		return liquidDensity;
	}

	public double getOilDensity(){
		return oilDensity;
	}

	public double getOiledWeight(){
		return oiledWeight;
	}
	public void setOiledWeight(double oiledWeight){
		this.oiledWeight=oiledWeight;
	}

	public double getUnderLimitPieces(){
		return underLimitPieces;
	}
	public double getUnderLimitCheckWeighing() {
		return underLimitCheckWeighing;
	}

	public double getCheckNominal() {
		return CheckNominal;
	}



	public double getCheckNominalToleranceOver() {
		return CheckToleranceOver;
	}


	public double getCheckNominalToleranceOverPercent() {
		if (CheckNominal==0) {return 0;}
		double percentover=(CheckToleranceOver+CheckNominal)/CheckNominal;
		percentover=(percentover-1)*100;
		return percentover;
	}

	public double getCheckNominalToleranceUnderPercent() {
		if (CheckNominal==0) {return 0;}
		double percentunder=(CheckNominal-CheckToleranceUnder)/CheckNominal;
		percentunder=(1-percentunder)*100;
		return percentunder;
	}

	public double getCheckNominalToleranceUnder() {
		return CheckToleranceUnder;
	}

	public double getOverLimitCheckWeighing() {
		return overLimitCheckWeighing;
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

	public void setDensityWaterTemp(double waterTemp){
		this.waterTemp=waterTemp;
	}

	public void setDensityLiquidDensity(double liquidDensity){
		this.liquidDensity=liquidDensity;
	}
	public void setDensityoiledWeight(double oiledWeight){
		this.oiledWeight=oiledWeight;
	}

	public void setSinkerVolume(double sinkerVolume){
		this.sinkerVolume=sinkerVolume;
	}

	public void setOilDensity(double oilDensity){
		this.oilDensity=oilDensity;
	}

	public void setUnderLimitCheckWeighing(double underLimit) {
		this.underLimitCheckWeighing = underLimit;
	}
	public void setCheckNominal(double nominal){
		this.CheckNominal=nominal;
	}

	public void setCheckNominalToleranceUnder(double nominalToleranceUnder){
		this.CheckToleranceUnder=nominalToleranceUnder;
	}

	public void setCheckNominalToleranceUnderPercent(double nominalToleranceUnderPercent){
		double underPercent=(nominalToleranceUnderPercent/100)*CheckNominal;
		this.CheckToleranceUnder=underPercent;
	}
	public void setCheckNominalToleranceOverPercent(double nominalToleranceOverPercentPercent){
		double overPercent=(nominalToleranceOverPercentPercent/100)*CheckNominal;
		this.CheckToleranceUnder=overPercent;
	}
	public void setCheckNominalToleranceOver(double nominalToleranceOver){
		this.CheckToleranceOver=nominalToleranceOver;
	}
	public void setOverLimitCheckWeighing(double overLimit) {
		this.overLimitCheckWeighing = overLimit;
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

	public double getAveragePieceWeight() {
		return averagePieceWeight;
	}

	public void setAveragePieceWeight(double averagePieceWeight) {
		this.averagePieceWeight = averagePieceWeight;
	}
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public double getAveragingTime() {
		return averagingTime;
	}

	public void setAveragingTime(double averagingTime) {
		this.averagingTime = averagingTime;
	}

	public double getSQCNominal() {
		return SQCNominal;
	}

	public void setSQCNominal(double SQCNominal) {
		this.SQCNominal = SQCNominal;
	}

	public double getSQCpTolerance1() {
		return SQCpTolerance1;
	}

	public void setSQCpTolerance1(double SQCpTolerance1) {
		this.SQCpTolerance1 = SQCpTolerance1;
	}


	public double getSQCpTolerance2() {
		return SQCpTolerance2;
	}

	public void setSQCpTolerance2(double SQCpTolerance2) {
		this.SQCpTolerance2 = SQCpTolerance2;
	}


	public double getSQCnTolerance1() {
		return SQCnTolerance1;
	}

	public void setSQCnTolerance1(double SQCnTolerance1) {
		this.SQCnTolerance1 = SQCnTolerance1;
	}

	public double getSQCnTolerance2() {
		return SQCnTolerance2;
	}

	public void setSQCnTolerance2(double SQCnTolerance2) {
		this.SQCnTolerance2 = SQCnTolerance2;
	}

	public double getPipetteNominal() {return pipetteNominal;}

	public void setPipetteNominal(double pipetteNominal) {this.pipetteNominal = pipetteNominal;}

	public double getPipetteWaterTemp() {return pipetteWaterTemp;}

	public void setPipetteWaterTemp(double pipetteWaterTemp) {this.pipetteWaterTemp = pipetteWaterTemp;}

	public double getPipetteInaccuracy() {return pipetteInaccuracy;	}

	public void setPipetteInaccuracy(double pipetteInaccuracy) {this.pipetteInaccuracy = pipetteInaccuracy;	}

	public double getPipetteImprecision() {return pipetteImprecision;	}

	public void setPipetteImprecision(double pipetteImprecision) {this.pipetteImprecision = pipetteImprecision;}

	public double getPipettePressure() {return pipettePressure;}

	public void setPipettePressure(double pipettePressure) {this.pipettePressure = pipettePressure;}
}




