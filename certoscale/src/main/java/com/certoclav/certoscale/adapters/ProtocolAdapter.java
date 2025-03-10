package com.certoclav.certoscale.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.certoclav.certoscale.R;
import com.certoclav.certoscale.database.DatabaseService;
import com.certoclav.certoscale.database.Protocol;
import com.certoclav.certoscale.settings.protocol.MenuProtocolActivity;
import com.certoclav.certoscale.supervisor.ProtocolManager;
import com.certoclav.certoscale.util.FTPManager;
import com.certoclav.certoscale.util.Log;
import com.certoclav.certoscale.view.QuickActionItem;
import com.certoclav.library.application.ApplicationController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * The ProfileAdapter class provides access to the profile data items. <br>
 * ProfileAdapter is also responsible for making a view for each item in the
 * data set.
 */
public class ProtocolAdapter extends ArrayAdapter<Protocol> implements Filterable {


    private final Context mContext;

    private QuickActionItem actionItemPrint;
    private QuickActionItem actionItemView;
    private QuickActionItem actionItemDelete;
    private List<Protocol> protocols;
    private List<Protocol> protocolsAll;
    private boolean isViewOnly;
    private DatabaseService db;

    public ProtocolAdapter(Context context, List<Protocol> values, boolean isViewOnly) {
        super(context, R.layout.list_element_user, values);
        this.mContext = context;
        this.protocols = values;
        this.protocolsAll = values;
        this.isViewOnly = isViewOnly;
        db = new DatabaseService(context);
    }

    @Override
    public int getCount() {
        return protocols.size();
    }

    @Nullable
    @Override
    public Protocol getItem(int position) {
        protocols.get(position).parseJson();
        return protocols.get(position);
    }


    public class MyViewHolder {
        public TextView firstLine, secondLine;
        public LinearLayout containerItems;
        private LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        public QuickActionItem actionItemPrint;
        public QuickActionItem actionItemView;
        public QuickActionItem actionItemDelete;
        public ImageView imageCloud;
        private Protocol protocol;
        long lastNotifiedTime;

        public MyViewHolder(View view) {
            firstLine = view.findViewById(R.id.first_line);
            secondLine = view.findViewById(R.id.second_line);
            containerItems = view.findViewById(R.id.container_button);
            imageCloud = view.findViewById(R.id.list_element_protocol_image_cloud);


            actionItemPrint = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
            actionItemPrint.setImageResource(R.drawable.ic_menu_print);
            if (!isViewOnly)
                containerItems.addView(actionItemPrint);
            actionItemView = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
            actionItemView.setImageResource(R.drawable.ic_menu_view);
            containerItems.addView(actionItemView);

            actionItemDelete = (QuickActionItem) inflater.inflate(R.layout.quickaction_item, containerItems, false);
            actionItemDelete.setImageResource(R.drawable.ic_menu_upload);
            if (!isViewOnly)
                containerItems.addView(actionItemDelete);

//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//            actionItemDelete.setEnabled(prefs.getBoolean(ApplicationController.getContext().getString(R.string.preferences_lockout_protocols),
//                    ApplicationController.getContext().getResources().getBoolean(R.bool.preferences_lockout_protocols)));

            actionItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        actionDetected();
                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.dialog_yes_no);
                        dialog.setTitle("Bestätigen Sie den Upload");
                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText(mContext.getString(R.string.do_you_really_want_to_upload_this_protocol) + " " + protocol.getName());
                        Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNO);
                        dialogButtonNo.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                actionDetected();
                                dialog.dismiss();
                            }
                        });
                        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                        // if button is clicked, close the custom dialog
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                actionDetected();
                                dialog.dismiss();
                                if (!FTPManager.getInstance().hasOnGoingUploading())
                                    FTPManager.getInstance().uploadProtocols(new FTPManager.FTPListener() {
                                        public void onConnection(boolean isConnected, final String message) {
                                            android.util.Log.d("FTP_SERVER", "connection " + isConnected + " " + (message != null ? message : ""));
                                            if (!isConnected) {
                                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toasty.error(ApplicationController.getContext(), (message != null && !message.isEmpty())
                                                                ? message : mContext.getString(R.string.can_not_connect_to_ftp), Toast.LENGTH_LONG, true).show();
                                                    }
                                                });

                                            }
                                        }

                                        @Override
                                        public void onUploaded(Protocol protocol) {
                                            if (db != null)
                                                db.updateProtocolIsUploaded(protocol, true);
                                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toasty.success(ApplicationController.getContext(),
                                                            mContext.getString(R.string.uploaded_sucessfully), Toast.LENGTH_SHORT, true).show();
                                                    notifyDataSetChanged();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onUploading(boolean isUploaded, final String message) {
                                            android.util.Log.d("FTP_SERVER", "uploading " + isUploaded + " " + (message != null ? message : ""));
                                            if (!isUploaded && lastNotifiedTime + 10 * 1000 < Calendar.getInstance().getTimeInMillis()) {
                                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toasty.error(ApplicationController.getContext(), (message != null && !message.isEmpty())
                                                                ? message : mContext.getString(R.string.can_not_uploaded), Toast.LENGTH_SHORT, true).show();
                                                    }
                                                });

                                                lastNotifiedTime = Calendar.getInstance().getTimeInMillis();
                                            }
                                        }
                                    }, Arrays.asList(new Protocol[]{protocol}), true);
                            }
                        });

                        dialog.show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

            actionItemPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionDetected();
                    ProtocolManager protocolPrinterUtils = new ProtocolManager();
                    protocolPrinterUtils.printText(protocol.getContent());
                    Toast.makeText(mContext, "Protocol printed", Toast.LENGTH_LONG).show();
                }
            });


            actionItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        actionDetected();
                        final Dialog dialog = new Dialog(mContext);
                        dialog.setContentView(R.layout.dialog_protocol);
                        protocol.parseJson();
                        dialog.setTitle(getContext().getString(R.string.protocol) + protocol.getName());
                        TextView textView = dialog.findViewById(R.id.dialog_protocol_text);
                        textView.setText(protocol.getContent());

                        textView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                actionDetected();
                                return true;
                            }
                        });

                        Button dialogButtonClose = dialog.findViewById(R.id.dialog_button_close);
                        dialogButtonClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                actionDetected();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }

        public void setProtocol(Protocol protocol) {
            this.protocol = protocol;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_element_protocol, parent, false);
            viewHolder = new MyViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        Protocol protocol = getItem(position);
        viewHolder.setProtocol(protocol);

        viewHolder.firstLine.setText(protocol.getAshBeakerName());
        viewHolder.secondLine.setText(protocol.getDate());

        viewHolder.imageCloud.setVisibility(protocol.getIsPending() ? View.GONE : View.VISIBLE);
        viewHolder.actionItemDelete.setVisibility(protocol.getIsPending() ? View.GONE : View.VISIBLE);

        viewHolder.imageCloud.setImageResource(protocol.isUploaded() ? R.drawable.cloud_ok_white :
                R.drawable.cloud_no_white);


        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                protocols = (List<Protocol>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                if (constraint.length() == 0) {
                    results.count = protocolsAll.size();
                    results.values = protocolsAll;
                    return results;
                }

                ArrayList<Protocol> filteredProtocols = new ArrayList<>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (Protocol protocol : protocolsAll) {
                    if (protocol.getAshSampleName().toLowerCase().startsWith(constraint.toString()) ||
                            protocol.getAshBeakerName().toLowerCase().startsWith(constraint.toString())) {
                        filteredProtocols.add(protocol);
                    }
                }

                results.count = filteredProtocols.size();
                results.values = filteredProtocols;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
    }

    private void actionDetected() {
        if (mContext instanceof MenuProtocolActivity) {
            ((MenuProtocolActivity) mContext).actionDetected();
        }
    }
}