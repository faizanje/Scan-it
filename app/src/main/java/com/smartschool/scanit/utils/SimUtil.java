package com.smartschool.scanit.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;

import com.smartschool.scanit.activities.BottomNavActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SimUtil {

    @SuppressLint("MissingPermission")
    public static void sendDirectSMS(Context context, String to, String message) {

        String SENT = "SMS_SENT", DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

        // SEND BroadcastReceiver
        BroadcastReceiver sendSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(arg0, "SMS Sent", Toast.LENGTH_SHORT).show();
//                        showSnackBar(getString(R.string.sms_sent));
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(arg0, "RESULT_ERROR_GENERIC_FAILURE", Toast.LENGTH_SHORT).show();
//                        showSnackBar(getString(R.string.sms_send_failed_try_again));
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(arg0, "RESULT_ERROR_NO_SERVICE", Toast.LENGTH_SHORT).show();

//                        showSnackBar(getString(R.string.no_service_sms_failed));
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(arg0, "RESULT_ERROR_NULL_PDU", Toast.LENGTH_SHORT).show();

//                        showSnackBar(getString(R.string.no_service_sms_failed));
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(arg0, "RESULT_ERROR_RADIO_OFF", Toast.LENGTH_SHORT).show();

//                        showSnackBar(getString(R.string.no_service_sms_failed));
                        break;
                }
            }
        };

        // DELIVERY BroadcastReceiver
        BroadcastReceiver deliverSMS = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        context.registerReceiver(sendSMS, new IntentFilter(SENT));
        context.registerReceiver(deliverSMS, new IntentFilter(DELIVERED));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(context);
            if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                List localList = localSubscriptionManager.getActiveSubscriptionInfoList();

                SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(0);
                SubscriptionInfo simInfo2 = (SubscriptionInfo) localList.get(1);

                //SendSMS From SIM One
                SmsManager.getSmsManagerForSubscriptionId(simInfo1.getSubscriptionId()).sendTextMessage(to, null, message, sentPI, deliveredPI);

                //SendSMS From SIM Two
                SmsManager.getSmsManagerForSubscriptionId(simInfo2.getSubscriptionId()).sendTextMessage(to, null, message, sentPI, deliveredPI);
            }
        } else {
            SmsManager.getDefault().sendTextMessage(to, null, message, sentPI, deliveredPI);
            Toast.makeText(context, "Sending sms", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean sendSMS(Context ctx, int simID, String toNum, String centerNum, String smsText, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        String name;

        try {
            if (simID == 0) {
                name = "isms0";
            } else if (simID == 1) {
                name = "isms1";
            } else {
                throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
            }

            try {
                Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
                method.setAccessible(true);
                Object param = method.invoke(null, name);
                if (param == null) {
                    throw new RuntimeException("can not get service which is named '" + name + "'");
                }
                method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
                method.setAccessible(true);
                Object stubObj = method.invoke(null, param);
                method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                method.invoke(stubObj, ctx.getPackageName(), toNum, centerNum, smsText, sentIntent, deliveryIntent);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            return true;
        } catch (ClassNotFoundException e) {
            Log.e("Exception", "ClassNotFoundException:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e("Exception", "NoSuchMethodException:" + e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e("Exception", "InvocationTargetException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("Exception", "IllegalAccessException:" + e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", "Exception:" + e);
        }
        return false;
    }

}