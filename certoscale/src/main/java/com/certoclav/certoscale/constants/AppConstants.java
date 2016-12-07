package com.certoclav.certoscale.constants;


/**
 * The Constants class defines the constant fields used in all application files.  
 * 
 * @author Iulia Rasinar &lt;iulia.rasinar@nordlogic.com&gt;
 * 
 */
public class AppConstants {

	public static final Boolean IS_IO_SIMULATED = false;
	public static final Boolean APPLICATION_DEBUGGING_MODE = false;
	
	public static final String DOWNLOAD_LINK = "http://lvps46-163-113-210.dedicated.hosteurope.de:80/files/public-docs/certoscale/lilliput/update.zip";
	
	/**
	 * CertoCloud REST API
	 * Routes that can be accessed by everyone
	 */
	public final static String SERVER_URL = "http://api-certocloud.rhcloud.com";//www.ng-certocloud.rhcloud.com";
	public final static String REST_API_POST_LOGIN = "/login";// auth.login);
	public final static String REST_API_POST_SIGNUP = "/signup";// auth.signup);
	public final static String REST_API_POST_SIGNUP_EXIST = "/signup/exist";// auth.userExist);

	/** 
	 * CertoCloud REST API
	 * Routes that can be accessed only by autheticated users
	 */
	public final static String REST_API_POST_PROTOCOLS = "/api/protocols/";//, devices.getAll);
	public final static String REST_API_GET_DEVICES = "/api/devices/";//, devices.getAll);
	public final static String REST_API_POST_DEVICE = "/api/devices/";// devices.create);
	public final static String REST_API_PUT_DEVICE_RENAME = "/api/devices/:devicekey";// devices.rename);
	public final static String REST_API_DELETE_DEVICE = "/api/devices/:devicekey";// devices.delete);
	public final static String REST_API_GET_PROFILES = "/api/programs/";// programs.getAll);
	public final static String REST_API_POST_PROFILE = "/api/programs/";// programs.create);
	public final static String REST_API_DELETE_PROFILE = "/api/programs/:id";// programs.delete);

	public final static String REST_POST_SUPPORT = "/api/support";// support.send);

	/**
	 * Routes that can be accessed only by admin users
	 */
	public final static String REST_POST_CREATE_DEVICE = "/api/admin/devices/";// devices.createAdmin);
	
    public final static String PREFERENCE_KEY_STATISTICS = "preferences_weigh_statistic";



	


}