package com.certoclav.certoscale.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Protocol;
import com.certoclav.library.application.ApplicationController;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class AlarmReceiver extends BroadcastReceiver {

    long lastNotifiedTime;

    // onReceive must be very quick and not block, so it just fires up a Service
    @Override
    public void onReceive(Context context, Intent intent) {
        if (FTPManager.getInstance().hasOnGoingUploading())
            return;
        android.util.Log.e("TAG", "Uploading...");
        final DatabaseService databaseService = new DatabaseService(ApplicationController.getContext());
        FTPManager.getInstance().uploadProtocols(new FTPManager.FTPListener() {
            @Override
            public void onConnection(boolean isConnected, final String message) {

                try {
                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toasty.error(ApplicationController.getContext(), (message != null && !message.isEmpty())
                                            ? message : ApplicationController.getContext().getString(R.string.can_not_connect_to_ftp),
                                    Toast.LENGTH_LONG, true).show();
                        }
                    });
                } catch (Exception e) {

                }

            }

            @Override
            public void onUploaded(Protocol protocol) {
                Log.e("TAG", "HERE");
                if (databaseService != null)
                    databaseService.updateProtocolIsUploaded(protocol, true);
            }

            @Override
            public void onUploading(boolean isUploaded, final String message) {
                try {
                    if (!isUploaded && lastNotifiedTime + 10 * 1000 < Calendar.getInstance().getTimeInMillis()) {
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toasty.error(ApplicationController.getContext(), (message != null && !message.isEmpty())
                                        ? message : ApplicationController.getContext().getString(R.string.can_not_uploaded), Toast.LENGTH_SHORT, true).show();
                            }
                        });
                        lastNotifiedTime = Calendar.getInstance().getTimeInMillis();
                    }
                } catch (Exception e) {

                }
            }
        }, databaseService.getNotUploadedProtocols(), false);
    }
}