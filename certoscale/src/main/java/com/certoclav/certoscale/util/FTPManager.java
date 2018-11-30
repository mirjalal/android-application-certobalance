package com.certoclav.certoscale.util;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.certoclav.library.application.ApplicationController;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

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
    private FTPClient ftp;
    private String address;
    private int port;
    private String username;
    private String password;
    private String folder;
    private String folderILIMS;

    private FTPManager() {
        ftp = new FTPClient();
        ftp.setControlEncoding("UTF-8");
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

    public void saveFTPInformation(String address, int port, String username, String password, String folder, String folderILIMS) {

        address = address.trim();
        username = username.trim();
        folder = folder.trim();
        folderILIMS = folderILIMS.trim();
        try {
            if (folderILIMS.charAt(0) != '/')
                folderILIMS = "/" + folderILIMS;
        }catch (Exception e){
            folderILIMS = "/IFP_ILIMS";
            e.printStackTrace();
        }

        try {
            if (folder.charAt(0) != '/')
                folder = "/" + folder;
        }catch (Exception e){
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

        editor.putString(ADDRESS, address);
        editor.putInt(PORT, port);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putString(FOLDER, folder);
        editor.putString(FOLDER_ILIMS, folderILIMS);
        editor.commit();
    }


    public void updateAll(final FTPListener listener) {
        Needle.onBackgroundThread().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        ftp.connect(address);
                                                        if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                                                            ftp.disconnect();
                                                            if (listener != null)
                                                                listener.onConnection(false, "FTP server refused connection.");
                                                            return;
                                                        }
                                                        if (ftp.login(username, password)) {
                                                            try {
                                                                ftp.makeDirectory(folder);
                                                                ftp.changeWorkingDirectory(folder);
                                                                FTPFile[] files = ftp.listFiles(folder, filter);
                                                                File dir = new File(android.os.Environment.getExternalStorageDirectory(), "IFP_CERTO_CONTROL/RAW_DATA");
                                                                dir.mkdirs();
                                                                ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);

                                                                for (File file : dir.listFiles(csvFilter)) {
                                                                    FileInputStream srcFileStream = new FileInputStream(file);
                                                                    if(file.getName().contains("-deletemenow")){
                                                                        ftp.deleteFile(file.getName().replace("-deletemenow",""));
                                                                        file.delete();
                                                                        srcFileStream.close();
                                                                        continue;
                                                                    }
                                                                    ftp.deleteFile(file.getName());
                                                                    ftp.storeFile(file.getName(), srcFileStream);
                                                                    srcFileStream.close();
                                                                }
                                                                dir = new File(dir, "/IFP_UPLOADING");
                                                                for (File file : dir.listFiles(csvFilter)) {
                                                                    FileInputStream srcFileStream = new FileInputStream(file);
                                                                    ftp.deleteFile(file.getName());
                                                                    if (ftp.storeFile(file.getName(), srcFileStream))
                                                                        file.delete();
                                                                    srcFileStream.close();
                                                                }

                                                                dir = new File(android.os.Environment.getExternalStorageDirectory(), "IFP_CERTO_CONTROL");
                                                                ftp.makeDirectory(folderILIMS);
                                                                ftp.changeWorkingDirectory(folderILIMS);
                                                                FTPFile[] filesiLIMS = ftp.listFiles(folderILIMS, filter);
                                                                for (CustomFile file : findNotUploadedFiles(filesiLIMS, dir.listFiles(csvFilter))) {
                                                                    FileInputStream srcFileStream = new FileInputStream(file);
                                                                    ftp.storeFile(file.getName(), srcFileStream);
                                                                    srcFileStream.close();
                                                                }
                                                                if (listener != null)
                                                                    listener.onUpdated();
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

        try {
            if (folderILIMS.charAt(0) != '/')
                folderILIMS = "/" + folderILIMS;
        }catch (Exception e){
            folderILIMS = "/IFP_ILIMS";
            e.printStackTrace();
        }

        try {
            if (folder.charAt(0) != '/')
                folder = "/" + folder;
        }catch (Exception e){
            folder = "/IFP";
            e.printStackTrace();
        }
    }

    public static interface FTPListener {
        void onConnection(boolean isConnected, String message);

        void onUpdated();

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

    public String getPassword() {
        return password;
    }


    public String getUsername() {
        return username;
    }
}
