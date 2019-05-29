package com.certoclav.certoscale.util;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.certoclav.certoscale.database.Protocol;
import com.certoclav.certoscale.supervisor.ApplicationManager;
import com.certoclav.library.application.ApplicationController;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import needle.Needle;

//import com.adeel.library.easyFTP;

/**
 * Created by musaq on 11/18/2018.
 */

public class FTPManager {

    private static FTPManager ftpManager = null;
    private SharedPreferences preferences;
    private static final String ADDRESS = "ftp_address";
    private static final String PORT = "ftp_address_port";
    private static final String USERNAME = "ftp_username";
    private static final String PASSWORD = "ftp_password";
    private static final String FOLDER = "ftp_folder";
    private static final String FOLDER_ILIMS = "ftp_folder_ilims";
    private static final String FTP_STATUS = "ftp_status";
    private FTPClient ftp;
    private String address;
    private int port;
    private String username;
    private String password;
    private String folder;
    private String folderILIMS;
    private boolean isEnabled;

    private FTPManager() {
        ftp = new FTPClient();
        preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getContext());
        updateFTPInformation();
    }

    public static FTPManager getInstance() {
        if (ftpManager == null)
            ftpManager = new FTPManager();
        return ftpManager;
    }

    public void testConnection(final String address, final int port, final String username, final String password, final FTPListener listener) {
        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ftp.setConnectTimeout(5000);
                    ftp.connect(address, port);
                    ftp.login(username, password);
                    if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                        ftp.disconnect();
                        if (listener != null)
                            listener.onConnection(false, "FTP server refused connection. " + ftp.getReplyString());
                        return;
                    }
                    if (listener != null)
                        listener.onConnection(ftp.isConnected(), "");
                    ftp.disconnect();
                } catch (Exception e) {
                    if (listener != null)
                        listener.onConnection(false, e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveFTPInformation(String address, int port, String username, String password, String folder, String folderILIMS, Boolean isEnabled) {

        address = address.trim();
        username = username.trim();
        folder = folder.trim();
        folderILIMS = folderILIMS.trim();
        try {
            if (folderILIMS.charAt(0) != '/')
                folderILIMS = "/" + folderILIMS;
        } catch (Exception e) {
            folderILIMS = "/IFP_ILIMS";
            e.printStackTrace();
        }

        try {
            if (folder.charAt(0) != '/')
                folder = "/" + folder;
        } catch (Exception e) {
            folder = "/IFP";
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = preferences.edit();
        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
        this.folder = folder;
        this.folderILIMS = folderILIMS;
        this.isEnabled = isEnabled;

        editor.putString(ADDRESS, address);
        editor.putInt(PORT, port);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putString(FOLDER, folder);
        editor.putString(FOLDER_ILIMS, folderILIMS);
        editor.putBoolean(FTP_STATUS, isEnabled);
        editor.commit();
    }
    /*
     *  Uploading to other folder read upload from the folder if the uploading is success remove from the folder.
     *   There are two folder
     *   IFP_UPLOADING are used for raw data
     *   IFP_ILIMS_UPLOADING are used for ash data for iLims
     */

    public void uploadProtocols(final FTPListener listener, final List<Protocol> protocols, final boolean isForce) {
        if (!isEnabled) return;
        Needle.onBackgroundThread().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        ftp.connect(address, port);
                                                        if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                                                            ftp.disconnect();
                                                            if (listener != null)
                                                                listener.onConnection(false, "FTP server refused connection.");
                                                            return;
                                                        }
                                                        if (ftp.login(username, password)) {
                                                            try {

                                                                ftp.setControlEncoding("UTF-8");
                                                                ftp.setAutodetectUTF8(true);
                                                                ftp.makeDirectory(folder);
                                                                if (!ftp.changeWorkingDirectory(folder)) {
                                                                    if (listener != null)
                                                                        listener.onUploading(false, "Es gibt ein Problem mit " + folder + " Ordner!");
                                                                    return;
                                                                }
                                                                ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);

                                                                boolean isUploaded;
                                                                for (Protocol protocol : protocols) {
                                                                    protocol.generateCSV();
                                                                    isUploaded = protocol.isUploaded();
                                                                    if ((!isForce && isUploaded) || protocol.getIsPending())
                                                                        continue;
                                                                    isUploaded = false;
                                                                    //Uploading not uploaded protocols
                                                                    File file = createTempProtocolFile(protocol);
                                                                    if (file == null)
                                                                        continue;
                                                                    FileInputStream srcFileStream = new FileInputStream(file);
                                                                    ftp.deleteFile(file.getName());
                                                                    if (ftp.storeFile(file.getName(), srcFileStream)) {
                                                                        file.delete();
                                                                        isUploaded = true;
                                                                    } else {
                                                                        if (listener != null)
                                                                            listener.onUploading(false, null);
                                                                    }
                                                                    srcFileStream.close();


                                                                    //Uploading iLIMS protocols
                                                                    ftp.makeDirectory(folderILIMS);
                                                                    ftp.changeWorkingDirectory(folderILIMS);
                                                                    file = createTempProtocolFileForIlims(protocol);
                                                                    if (file == null) {
                                                                        Log.e("nuuuuuuuuuul", "nuuuuuuuuuuuuul");
                                                                        continue;
                                                                    }
                                                                    srcFileStream = new FileInputStream(file);
                                                                    ftp.deleteFile(file.getName());
                                                                    if (ftp.storeFile(file.getName(), srcFileStream)) {
                                                                        file.delete();
                                                                    } else {
                                                                        isUploaded = false;
                                                                        if (listener != null)
                                                                            listener.onUploading(false, null);
                                                                    }
                                                                    srcFileStream.close();

                                                                    if (isUploaded) {
                                                                        if (listener != null)
                                                                            listener.onUploaded(protocol);
                                                                    } else {
                                                                        if (listener != null)
                                                                            listener.onUploading(false, "Das Hochladen ist fehlgeschlagen!");
                                                                    }
                                                                }

                                                            } catch (Exception e) {
                                                                if (listener != null)
                                                                    listener.onUploading(false, e.getMessage());
                                                                throw e;
                                                            }
                                                        } else {
                                                            if (listener != null)
                                                                listener.onConnection(false, "Login Error");
                                                        }
                                                    } catch (Exception e) {
                                                        if (listener != null)
                                                            listener.onConnection(false, e.getMessage());
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
        );
    }

    private File createTempProtocolFile(Protocol protocol) {

        String data = protocol.generateCSV();
        File file = null;

        String[] contents = protocol.getContent().split("\n");
        String date = null;
        if (contents != null && contents.length > 2) {
            Log.d("content", contents[1]);
            date = contents[1];
            date = date.replaceAll("-", "").replaceAll(":", "").trim().replaceAll(" ", "_");
        }
        if (date == null) {
            DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
            date = df.format(new Date(System.currentTimeMillis()));
        }
//28 May 2019 08:56:50 GMT
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
//        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//        Date protocolDate = null;
//        try {
//            protocolDate = dateFormat.parse(protocol.getDate());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Log.d("date", protocol.getDate() + " " + df.format(protocolDate));


        String protocolName = protocol.getAshSampleName()
                + "_" + date;

        file = new File(ApplicationController.getContext().getCacheDir(), protocolName + ".csv");


        if (file == null)
            return null;
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.print(data);
            pw.flush();
            pw.close();
            f.close();
            return file;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            android.util.Log.e("ExportUtils", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
            Toasty.error(ApplicationController.getContext(), "Can't write to storage", Toast.LENGTH_SHORT, true).show();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Export Utils", "io exception: " + e.toString());
            return null;
        }
    }

    private File createTempProtocolFileForIlims(Protocol protocol) {

        protocol.generateCSV();
        File file = null;

        String[] contents = protocol.getContent().split("\n");
        String date = null;
        if (contents != null && contents.length > 2) {
            Log.d("content", contents[1]);
            date = contents[1];
            date = date.replaceAll("-", "").replaceAll(":", "").trim().replaceAll(" ", "_");
        }
        if (date == null) {
            DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
            date = df.format(new Date(System.currentTimeMillis()));
        }


        String protocolName = protocol.getAshSampleName()
                + "_" + date;
        file = new File(ApplicationController.getContext().getCacheDir(), protocolName + ".csv");


        if (file == null) {
            Log.e("3", "333333333333");
            return null;
        }
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);

            StringBuilder sb = new StringBuilder();
            sb.append("samplenumber" + "," + protocol.getAshSampleName() + "\r\n");
            sb.append("sampleweight" + "," + ApplicationManager.getInstance().getTransformedWeightAsString(
                    protocol.getSampleWeight())
                    .replaceAll(",", ".") + "\r\n");
            sb.append("ashweight" + "," + ApplicationManager.getInstance().getTransformedWeightAsString(
                    protocol.getLastAshWeight()).replaceAll(",", ".") + "\r\n");
            sb.append("ashpercent" + "," + protocol
                    .getAshResultPercentageAsString().replaceAll(",", ".") + "\r\n");

            String data = sb.toString();
            pw.print(data);
            pw.flush();
            pw.close();
            f.close();
            return file;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            android.util.Log.e("ExportUtils", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
            Toasty.error(ApplicationController.getContext(), "Can't write to storage",
                    Toast.LENGTH_SHORT, true).show();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Export Utils", "io exception: " + e.toString());
            return null;
        }
    }


    private List<CustomFile> findNotUploadedFiles(FTPFile[] inFtp, File[] inLocal) {

        List<CustomFile> ftpFiles = new ArrayList<>();
        List<CustomFile> localFiles = new ArrayList<>();
        if (inFtp != null)
            for (FTPFile file : inFtp) {
                ftpFiles.add(new CustomFile(file.getName()));
            }
        if (inLocal != null)
            for (File file : inLocal) {
                localFiles.add(new CustomFile(file.getAbsolutePath()));
            }

        localFiles.removeAll(ftpFiles);

        return localFiles;
    }

    private FileFilter csvFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return file.getName().contains(".csv");
        }
    };

    private class CustomFile extends File {


        public CustomFile(@NonNull String pathname) {
            super(pathname);
        }

        @Override
        public boolean equals(Object obj) {
            return ((File) obj).getName().equals(getName());
        }

        @Override
        public int hashCode() {
            return getName().hashCode();
        }
    }

    private FTPFileFilter filter = new FTPFileFilter() {

        @Override
        public boolean accept(FTPFile ftpFile) {
            return (ftpFile.isFile() && ftpFile.getName().endsWith(".csv"));
        }
    };

    private void updateFTPInformation() {
        address = preferences.getString(ADDRESS, "localhost");
        port = preferences.getInt(PORT, 21);
        username = preferences.getString(USERNAME, "admin");
        password = preferences.getString(PASSWORD, "admin");
        folder = preferences.getString(FOLDER, "/IFP");
        folderILIMS = preferences.getString(FOLDER_ILIMS, "/IFP_ILIMS");
        isEnabled = preferences.getBoolean(FTP_STATUS, true);

        try {
            if (folderILIMS.charAt(0) != '/')
                folderILIMS = "/" + folderILIMS;
        } catch (Exception e) {
            folderILIMS = "/IFP_ILIMS";
            e.printStackTrace();
        }

        try {
            if (folder.charAt(0) != '/')
                folder = "/" + folder;
        } catch (Exception e) {
            folder = "/IFP";
            e.printStackTrace();
        }
    }

    public static interface FTPListener {
        void onConnection(boolean isConnected, String message);

        void onUploaded(Protocol protocol);

        void onUploading(boolean isUploaded, String message);
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getFolder() {
        return folder;
    }

    public String getFolderIlims() {
        return folderILIMS;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getPassword() {
        return password;
    }


    public String getUsername() {
        return username;
    }
}
