package com.example.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class TextMe extends Fragment {

    private final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    private final String SENT="MESSAGE_SENT";
    private final String DELIVERED="MESSAGE_DELIVERED";
    PendingIntent sentIntent;
    PendingIntent deliveredIntent;
    BroadcastReceiver smsSentReceiver;
    BroadcastReceiver smsDeliveredReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            sentIntent= PendingIntent.getBroadcast(this.getContext(),0,new Intent(SENT),PendingIntent.FLAG_IMMUTABLE);
            deliveredIntent= PendingIntent.getBroadcast(this.getContext(),0,new Intent(DELIVERED),PendingIntent.FLAG_IMMUTABLE);
        }
        else
        {
            sentIntent= PendingIntent.getBroadcast(this.getContext(),0,new Intent(SENT),PendingIntent.FLAG_CANCEL_CURRENT);
            deliveredIntent= PendingIntent.getBroadcast(this.getContext(),0,new Intent(DELIVERED),PendingIntent.FLAG_CANCEL_CURRENT);
        }


        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.SEND_SMS )!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else
        {
            Bundle bundle = getArguments();
            String lat = bundle.getString("latitude");
            String lng= bundle.getString("longitude");
            StringBuilder message= new StringBuilder("Latitude: ");
            message.append(lat);
            message.append(", Longitude: ");
            message.append(lng);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("0783714896",null,message.toString(),sentIntent,deliveredIntent);
        }


        return inflater.inflate(R.layout.fragment_text_me, container, false);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        requireActivity().unregisterReceiver(smsSentReceiver);
        requireActivity().unregisterReceiver(smsDeliveredReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        smsSentReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context,"Message sent",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context,"No Service",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context,"Null PDU",Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context,"Air plane Mode or Radio off",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        smsDeliveredReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS Delivered",Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context,"SMS not delivered",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        requireActivity().registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        requireActivity().registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }
}