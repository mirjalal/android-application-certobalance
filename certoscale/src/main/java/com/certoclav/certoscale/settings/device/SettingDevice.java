package com.certoclav.certoscale.settings.device;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.VolumeProvider;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.constants.AppConstants;
import com.certoclav.certoscale.menu.ApplicationActivity;
import com.certoclav.certoscale.menu.LoginActivity;
import com.certoclav.certoscale.menu.MenuActivity;
import com.certoclav.certoscale.model.Scale;
import com.certoclav.certoscale.model.ScaleState;
import com.certoclav.certoscale.settings.application.PreferenceFragment;
import com.certoclav.library.application.ApplicationController;
import com.certoclav.library.util.DownloadUtils;
import com.certoclav.library.util.ExportUtils;
import com.certoclav.library.util.UpdateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SettingDevice extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
private SharedPreferences prefs = null;

    @Override
    public void onPause() {
        //unregister listeners

        prefs.unregisterOnSharedPreferenceChangeListener(this);
     //  Scale.getInstance().removeOnWifiListener(this);
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_device);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());




        //Language
        ((Preference) findPreference(getString(R.string.preferences_device_language))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

           /*     Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.settings", "com.android.settings.LanguageSettings");
                getActivity().startActivity(intent);
               // Intent intent = new Intent(getActivity(), SettingsLanguagePickerActivity.class);
               // getActivity().startActivity(intent);
            */

                Intent i = new Intent();
                i.setAction(android.provider.Settings.ACTION_LOCALE_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                getActivity().startActivity(i);

                return false;
            }
        });

//Device Key

        ((Preference) findPreference(getString(R.string.preferences_device_key))).setSummary(Scale.getInstance().getSafetyKey());

//Check for updates
        ((Preference) findPreference(getString(R.string.preferences_device_software_update))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                if (ApplicationController.getInstance().isNetworkAvailable()) {
                    List<String> downloadUrls = new ArrayList<String>();
                    downloadUrls.add(AppConstants.DOWNLOAD_LINK);
                    DownloadUtils downloadUtils= new DownloadUtils(getActivity());
                    downloadUtils.Download(downloadUrls);
                }else{
                    Toast.makeText(getActivity(), "Please connect to internet", Toast.LENGTH_LONG).show();
                }

                return false;
            }
        });

        //Install update from USB
        ((Preference) findPreference(getString(R.string.preferences_device_software_update_usb))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                preference.setEnabled(false);
                ExportUtils exportUtils = new ExportUtils();
                if(exportUtils.checkExternalMedia() == false){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.can_not_read_usb_flash_disk), Toast.LENGTH_LONG).show();
                }else{
                    boolean success = false;
                    try{
                        UpdateUtils updateUtils = new UpdateUtils(getActivity());
                        success = updateUtils.installUpdateZip(UpdateUtils.SOURCE_USB);
                    }catch(Exception e){
                        success = false;
                    }
                    if(success){
                        Toast.makeText(getActivity(), "Update successfull", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(), "Update failed", Toast.LENGTH_LONG).show();
                    }
                }
                preference.setEnabled(true);
                return false;
            }
        });



        //Install update from SDCARD
        ((Preference) findPreference(getString(R.string.preferences_device_software_update_sdcard))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                preference.setEnabled(false);
                ExportUtils exportUtils = new ExportUtils();
                if(exportUtils.checkExternalSDCard() == false){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.can_not_read_from_sd_card), Toast.LENGTH_LONG).show();
                }else{
                    boolean success = false;
                    try{
                        UpdateUtils updateUtils = new UpdateUtils(getActivity());
                        success = updateUtils.installUpdateZip(UpdateUtils.SOURCE_SDCARD);
                    }catch(Exception e){
                        success = false;
                    }
                    if(success){
                        Toast.makeText(getActivity(), "Update successfull", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(), "Update failed", Toast.LENGTH_LONG).show();
                    }
                }
                preference.setEnabled(true);
                return false;
            }
        });



//Factory Reset
        ((Preference) findPreference(getString(R.string.preferences_device_reset))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                try
                {



                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_yes_no);
                    dialog.setTitle(R.string.factory_reset);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("WARNING:" + " "+ getString(R.string.do_you_really_want_to) +" "+ getString(R.string.delete_all_data_));
                    ImageView image = (ImageView) dialog.findViewById(R.id.dialog_image);
                    image.setVisibility(View.GONE);
                    Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
                    dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                            // closing Entire Application
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("clear_cache", Context.MODE_PRIVATE).edit();
                            editor.clear();
                            editor.commit();
                            ApplicationController.getInstance().clearApplicationData();

                            //android.os.Process.killProcess(android.os.Process.myPid());


                            //User will be logged out to avoid bugs
                            Scale.getInstance().setScaleState(ScaleState.ON_AND_MODE_GRAM);
                            Intent intent = new Intent( getContext() ,LoginActivity.class);
                            startActivity(intent);

                            getActivity().finish();
                            android.os.Process.killProcess(android.os.Process.myPid());






                        }
                    });

                    dialog.show();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                return false;
            }
        });
//Date and Time


        ((Preference) findPreference(getString(R.string.preferences_device_date))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));

                return false;
            }
        });

        //Adjust the volume
        ((Preference) findPreference(getString(R.string.preferences_device_volume))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {


                startActivity(new Intent(Settings.ACTION_SOUND_SETTINGS));

                return false;
            }
        });

        //Adjust the screen brightness
        ((Preference) findPreference(getString(R.string.preferences_device_brightness))).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {


                startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));

                return false;
            }
        });

    }






    @Override
    public void onResume() {



        /*
        //INIT DEVICE SETTING
        String key = "preferences_device_themes";
        Preference devicePref = findPreference(key);
        devicePref.setSummary(getResources().getStringArray(R.array.preferences_device_themes_string_array)[Integer.parseInt(prefs.getString(key, ""))-1]);
        */

        //Display Language

        ((Preference) findPreference(getString(R.string.preferences_device_language))).setSummary(String.format("%s (%s)", toTitleCase(Locale.getDefault().getDisplayLanguage()), toTitleCase(Locale.getDefault().getDisplayCountry())));


        //show date and time

        // get the current date and time
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        ((Preference) findPreference(getString(R.string.preferences_device_date))).setSummary(  new StringBuilder()
                .append(mMonth + 1)                // Month is 0 based so add 1
                .append("/")
                .append(mDay)
                .append("/")
                .append(mYear)
                .append("    ")
                .append(pad(mHour))
                .append(":")
                .append(pad(mMinute)));


        //Storage
        ((Preference) findPreference(getString(R.string.preferences_device_storage)))
                .setSummary(getString(R.string.free_memory)+": "
                        + Long.toString(FreeMemory())
                        + " MB");
        //Software Version
        PackageInfo pInfo;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName + " (" + pInfo.versionCode + ")";
            ((Preference) findPreference(getString(R.string.preferences_device_software_version))).setSummary(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        //serial number
        try {
            ((Preference) findPreference(getString(R.string.preferences_device_serial_number))).setSummary(Scale.getInstance().getSerialnumber());
        } catch (Exception e) {
            try {
                ((Preference) findPreference(getString(R.string.preferences_device_serial_number))).setSummary(getString(R.string.please_connect_to_autoclave_first));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        //firmware version
        try {
            ((Preference) findPreference(getString(R.string.preferences_device_firmware_version))).setSummary(Scale.getInstance().getFirmwareVersion());
        } catch (Exception e) {
            try {
                ((Preference) findPreference(getString(R.string.preferences_device_firmware_version))).setSummary(getString(R.string.please_connect_to_autoclave_first));
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }






        //register Listeners
        prefs.registerOnSharedPreferenceChangeListener(this);








        super.onResume();


    }

    @SuppressWarnings("deprecation")
    public long FreeMemory()
    {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long   Free   = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576; //Long() is not supported on android 2.3.4
        return Free;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }







    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(preference.hasKey()){
            if(preference.getKey().equals(getString(R.string.preferences_device_wlan_manage))){
                startWifiPicker();
            }




        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    /*
     *
     * Starts an Activity for Picking and Connecting to Wifi
     */
    private void startWifiPicker() {
        startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
    }








    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.preferences_device_wlan))){
            WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

            if(wifiManager !=null){
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                wifiManager.setWifiEnabled(sharedPrefs.getBoolean(getString(R.string.preferences_device_wlan), false));
            }
        }



        if(key.equals(getString(R.string.preferences_device_volume))){
            AudioManager  audioManager= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            if(audioManager !=null){
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            }

        }

        if(key.equals(getString(R.string.preferences_device_theme))){
            String theme = prefs.getString(getString(R.string.preferences_device_theme), "1");
            if (theme.equals("1")){
                //Toast.makeText(getActivity(), "1", Toast.LENGTH_LONG).show();

            }
            if (theme.equals("2")){
                //Toast.makeText(getActivity(), "2", Toast.LENGTH_LONG).show();

            }

            //Toast.makeText(getActivity(),"3",Toast.LENGTH_LONG).show();
        }
    };

    private static String toTitleCase(String s) {
        if (s.length() == 0) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }


}