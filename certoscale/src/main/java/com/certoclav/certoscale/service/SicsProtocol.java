package com.certoclav.certoscale.service;

import android.os.AsyncTask;

import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;

import java.util.ArrayList;

import static android.R.attr.id;


/**
 * Created by Michael on 1/14/2017.
 */



public class SicsProtocol {

    private SicsProtocol(){
        sendWeightPeriodicallyThread.start();
    }
    private static SicsProtocol instance = new SicsProtocol();

    public static synchronized SicsProtocol getInstance(){
        return instance;

    }
    
    private boolean isSendingPeriodically = false;

    public final static String LEVEL_0 = "0";
    public final static String LEVEL_1 = "1";
    public final static String LEVEL_2 = "2";
    public final static String LEVEL_3 = "3";

    public final static String LEVEL_0_VERSION = "2.30";
    public final static String LEVEL_1_VERSION = "2.20";
    public final static String LEVEL_2_VERSION = "1.00";
    public final static String LEVEL_3_VERSION = "1.00";

    //Commands level 0, Version 2.3x
    public final static String COMMAND_RESET = "@";
    public final static String COMMAND_LIST = "I0";
    public final static String COMMAND_LEVEL = "I1";
    public final static String COMMAND_MODEL = "I2";
    public final static String COMMAND_VERSION_BALANCE = "I3";
    public final static String COMMAND_SERIAL= "I4";
    public final static String COMMAND_VERSION_SCREEN = "I5";
    public final static String COMMAND_WEIGHT_STABLE = "S";
    public final static String COMMAND_WEIGHT = "SI";
    public final static String COMMAND_WEIGHT_AUTOMATIC= "SIR";
    public final static String COMMAND_ZERO_WHEN_STABLE="Z";
    public final static String COMMAND_ZERO= "ZI";

    //Commands level 1, Version 2.2x

    public final static String COMMAND_TARE_WHEN_STABLE = "T";
    public final static String COMMAND_TARE= "TI";
    public final static String COMMAND_TEXT_WRITE = "D";
    public final static String COMMAND_TEXT_DELETE = "DW";
    public final static String COMMAND_BUTTON = "K";
    public final static String COMMAND_TARA_STORAGE_RW= "TA";
    public final static String COMMAND_TARA_STORAGE_DELETE="TAC";
    public final static String COMMAND_ON_WEIGHT_CHANGED= "SR";

    //Commands level 2
    public final static String COMMAND_GET_WEIGHT_WHEN_STABLE_WITH_CURRENT_UNIT = "SU";
    public final static String COMMAND_GET_DOOR_POSITION= "WS";
    public final static String COMMAND_SCALE_STANDBY = "PWR";
    public final static String COMMAND_GET_ID = "I10";
    public final static String COMMAND_GET_TYPE = "I11";
    public final static String COMMAND_GET_COMPONENTS= "I14";
    public final static String COMMAND_GET_SET_FILTER="M01";
    public final static String COMMAND_GET_SET_FILTER_ADJUSTMENT= "M02";
    public final static String COMMAND_GET_SET_ZERO_AUTOMATIC = "M03";
    public final static String COMMAND_GET_SET_IO_INPUTS= "M04";
    public final static String COMMAND_ENABLE_DISABLE_DOOR_AUTOMATIC = "M07";
    public final static String COMMAND_BEEP = "M12";
    public final static String COMMAND_ENABLE_DISABLE_SOFTKEYS = "M13";
    public final static String COMMAND_GET_SET_PRINT= "M24";
    public final static String COMMAND_ENABLE_DISABLE_BARGRAPH="M39";
    public final static String COMMAND_PRINT_TEXT= "P100";
    public final static String COMMAND_CALIBRATE= "C1";

    //REMOTE CONTROL
    public final static String COMMAND_SET_TEXT_IN_LINE = "P112";
    public final static String COMMAND_DELETE_TEXT_IN_LINE= "P113";
    public final static String COMMAND_SET_USER_DATA = "P114";
    public final static String COMMAND_DISABLE_BARGRAPH = "P120";
    public final static String COMMAND_ENABLE_BARGRAPH = "P121";
    public final static String COMMAND_ENABLE_DISABLE_USER_INPUT= "RM20";
    public final static String COMMAND_SET_SOFTKEY_NAME="RM30";
    public final static String COMMAND_SET_SOFTKEY_INDEX= "RM32";
    public final static String COMMAND_SET_DYNAMIC_PARAMETER = "RM34";
    public final static String COMMAND_CHANGE_SOFTKEY_NAMES_INSTANT= "RM35";
    public final static String COMMAND_GET_SET_SOFTKEY_ROWS = "RM36";
    public final static String COMMAND_PREPARE_SOFTKEY_NAMES = "RM37";
    public final static String COMMAND_APPLY_SOFTKEY_ROWS = "RM38";
    public final static String COMMAND_APPLY_SOFTKEY_NAMES= "RM39";
    public final static String COMMAND_GET_SET_BARCODESCANNER="RM44";
    public final static String COMMAND_SET_STANDARDKEY_INDEX= "RM48";
    public final static String COMMAND_ENABLE_DISABLE_NOTIFICATIONS= "RM49";
    public final static String COMMAND_ENABLE_DISABLE_POPUP = "RM51";
    public final static String COMMAND_SET_INFOKEY_PROPERTY= "RM52";
    public final static String COMMAND_ENABLE_DISABLE_INFOKEY="RM53";
    public final static String COMMAND_ENABLE_DISABLE_INFOWINDOW= "RM54";

    public final static String TERMINATOR = "\r\n";


    public final static String REPLY_OK_END = "A";
    public final static String REPLY_OK_NOT_ENDED = "B";
    public final static String REPLY_BUTTON_PRESSED_AND_NOT_PERFORMED = "A";
    public final static String REPLY_WEIGHT_UNSTABLE= "D";
    public final static String REPLY_FAIL_BLOCKED= "I";
    public final static String REPLY_FAIL_WRONG_SYNTAX= "L";
    public final static String REPLY_WEIHGT_STABLE= "S";
    public final static String REPLY_WEIGHT_OVER= "+";
    public final static String REPLY_WEIGHT_UNDER= "-";

    public final static String QM = "\"";


    public String processCommand(String command){



        String[] commandArray = command.split(" ");
        String commandId = commandArray[0];
        ArrayList<SicsResponseLine> responseLines = new ArrayList<SicsResponseLine>();

        switch (commandId){
            case COMMAND_LIST:
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_LIST));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_LEVEL));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_MODEL));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_VERSION_BALANCE));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_SERIAL));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_VERSION_SCREEN));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_WEIGHT_STABLE));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_WEIGHT_AUTOMATIC));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_RESET));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_ON_WEIGHT_CHANGED));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_NOT_ENDED,LEVEL_0,    COMMAND_ZERO_WHEN_STABLE));
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_END,      LEVEL_0,    COMMAND_ZERO));
                break;
            case COMMAND_LEVEL:
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_END,"0123",LEVEL_0_VERSION,LEVEL_1_VERSION,LEVEL_2_VERSION,LEVEL_3_VERSION));
                break;
            case COMMAND_MODEL:
                responseLines.add(new SicsResponseLine(commandId, REPLY_OK_END, AppConstants.MODEL_SCALE));
                break;
            case COMMAND_VERSION_BALANCE:
                try {
                    responseLines.add(new SicsResponseLine(commandId, REPLY_OK_END, ApplicationController.getContext().getPackageManager().getPackageInfo(ApplicationController.getContext().getPackageName(), 0).versionName));
                }catch (Exception e){
                    responseLines.add(new SicsResponseLine(commandId,REPLY_FAIL_BLOCKED));
                }
                    break;
            case COMMAND_SERIAL:
                responseLines.add(new SicsResponseLine(commandId,REPLY_OK_END, Scale.getInstance().getSerialnumber()));
                break;
            case COMMAND_VERSION_SCREEN:
                responseLines.add(new SicsResponseLine(commandId, REPLY_OK_END, AppConstants.MODEL_TABLET));
                break;
            case COMMAND_WEIGHT_STABLE:
                isSendingPeriodically = false;
                new AsyncTask<Boolean,Boolean,String>(){

                    @Override
                    protected String doInBackground(Boolean... params) {
                        while(Scale.getInstance().isStable() ==false){
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        return ApplicationManager.getInstance().getTaredValueAsStringWithUnit();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        Scale.getInstance().getSerialsServiceSics().sendMessage("S S " + s + TERMINATOR);
                    }

                }.execute();
                break;
            case COMMAND_WEIGHT:
                isSendingPeriodically = false;
                String status = "";
                if(Scale.getInstance().isStable()){
                    status = "S";
                }else{
                    status = "D";
                }
                responseLines.add(new SicsResponseLine("S", status, ApplicationManager.getInstance().getTaredValueAsStringWithUnit()));
                break;
            case COMMAND_WEIGHT_AUTOMATIC:
                isSendingPeriodically = true;
                break;
            case COMMAND_RESET:
                isSendingPeriodically = false;
                break;
            case COMMAND_ON_WEIGHT_CHANGED:
                isSendingPeriodically = false;
                break;
            case COMMAND_ZERO_WHEN_STABLE:
                new AsyncTask<Boolean,Boolean,Boolean>(){

                    @Override
                    protected Boolean doInBackground(Boolean... params) {
                        while(Scale.getInstance().isStable() ==false){
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                        ApplicationManager.getInstance().setTareInGram(0d);
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        super.onPostExecute(result);
                        Scale.getInstance().getSerialsServiceSics().sendMessage("Z" +" "+REPLY_OK_END + TERMINATOR);
                    }

                }.execute();
                break;
            case COMMAND_ZERO:
                ApplicationManager.getInstance().setTareInGram(0d);
                responseLines.add(new SicsResponseLine(commandId, REPLY_WEIGHT_UNSTABLE));
                break;
            default:
                //unknown command
                responseLines.add(new SicsResponseLine(commandId,REPLY_FAIL_WRONG_SYNTAX));
                break;
        }

        StringBuilder sb =new StringBuilder();

        for(SicsResponseLine line : responseLines){
            sb.append(line.getResponseCommand());
        }

        return sb.toString();

    }


    private void sendWeightToSerial(){

    }
    Thread sendWeightPeriodicallyThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                    if(isSendingPeriodically) {
                        String status = "";
                        if (Scale.getInstance().isStable()) {
                            status = "S";
                        } else {
                            status = "D";
                        }
                        Scale.getInstance().getSerialsServiceSics().sendMessage("S " + status + " " + ApplicationManager.getInstance().getTaredValueAsStringWithUnit() + TERMINATOR);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                }
            
        }
    });





















    public class SicsResponseLine {
        String iD = "";
        String status = "";

        public SicsResponseLine(String iD, String status, String... parameters) {
            this.iD = iD;
            this.status = status;
            if (parameters != null){
                for (String parameter : parameters) {
                    addParameter(parameter);
                }
            }
        }



        public void addParameter(String parameter){
            parammeterList.add(parameter);
        }
        public ArrayList<String> getParammeterList() {
            return parammeterList;
        }

        public void setParammeterList(ArrayList<String> parammeterList) {
            this.parammeterList = parammeterList;
        }

        public String getiD() {
            return iD;
        }

        public void setiD(String iD) {
            this.iD = iD;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        ArrayList<String> parammeterList = new ArrayList<String>();

        public String getResponseCommand(){
            StringBuilder sb = new StringBuilder();
            sb.append(id).append(" ").append(status);
            for(String parameter : parammeterList){
                sb.append(" ").append(parameter);
            }
            sb.append(TERMINATOR);
            return sb.toString();
        }

    }
}
