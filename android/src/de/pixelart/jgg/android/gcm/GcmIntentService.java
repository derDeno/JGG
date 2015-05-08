package de.pixelart.jgg.android.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.pixelart.jgg.android.R;

public class GcmIntentService extends IntentService {
    NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String username;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        username = pref.getString("username",null);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	if(extras.getString("message") != null){
            		sendMessageNotification(extras.getString("message"));
            	}else if(extras.getString("update") != null) {
            		sendUpdateNotification(extras.getString("update"));
            	}else if(extras.getString("new_post") != null) {
            		sendNewNotification(extras.getString("new_post"));
            	}else if(extras.getString("vp") != null) {
            		if(extras.getString("vp").equals("New VP")) {
            			sendNewVpNotification();
            		}else if(extras.getString("vp").equals("Update VP")) {
            			sendUpdateVpNotification();
            		}
            	}
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendMessageNotification(String msg) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			
        Intent intent = new Intent(GcmIntentService.this, GcmIntentHandle.class);
		intent.putExtra("msg", msg);
		intent.putExtra("todo","message");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_CANCEL_CURRENT);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificate = prefs.getBoolean("notifications", true);
        boolean vibrate = prefs.getBoolean("notifications_vibrate", true);
        Uri uri = Uri.parse(prefs.getString("notifications_ringtone", "none"));
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		
		
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
 		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setLargeIcon(bm)
					.setSmallIcon(R.drawable.ic_notification)
					.setSound(uri)
					.setAutoCancel(true)
					.setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(username+" neue Nachricht f�r dich:<br>"+msg) ))
					.setContentText(Html.fromHtml(username+" neue Nachricht f�r dich:<br>"+msg) )
					.setContentTitle(Html.fromHtml( getString(R.string.app_name)+"&nbsp;Neue Nachricht") )
					.setContentIntent(contentIntent);
 		
        if (vibrate && notificate) {
        	mBuilder.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE);
        	mNotificationManager.notify(0, mBuilder.build());
 		}else if(!vibrate && notificate){
 			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
 			mNotificationManager.notify(0, mBuilder.build());
 		}
    }
    
    private void sendUpdateNotification(String post) {
    	String p = post;
    	String[] ps = p.split(";");
    	String title = ps[0];
    	String url = ps[1];
    	String id = ps[2];
    	String author = ps[3];
    	
        Intent intent = new Intent(GcmIntentService.this, GcmIntentHandle.class);
        intent.putExtra("todo","updatePost");
 		intent.putExtra("post_title", title);
 		intent.putExtra("post_url", url);
 		intent.putExtra("post_id", id);
 		intent.putExtra("post_author", author);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_CANCEL_CURRENT);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificate = prefs.getBoolean("notifications", true);
        boolean vibrate = prefs.getBoolean("notifications_vibrate", true);
        Uri uri = Uri.parse(prefs.getString("notifications_ringtone", "none"));
 		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
 		
 		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
 		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setLargeIcon(bm)
					.setSmallIcon(R.drawable.ic_notification)
					.setSound(uri)
					.setAutoCancel(true)
					.setStyle(new NotificationCompat.BigTextStyle().bigText(title+getString(R.string.msg_post_update)))
					.setContentText(title+getString(R.string.msg_post_update))
					.setContentTitle(getString(R.string.app_name))
					.setContentIntent(contentIntent);
 		
        if (vibrate && notificate) {
        	mBuilder.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE);
        	mNotificationManager.notify(0, mBuilder.build());
 		}else if(!vibrate && notificate){
 			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
 			mNotificationManager.notify(0, mBuilder.build());
 		}
    }
    
    private void sendNewNotification(String post) {
    	String p = post;
    	String[] ps = p.split(";");
    	String title = ps[0];
    	String url = ps[1];
    	String id = ps[2];
    	String author = ps[3];
    	
        Intent intent = new Intent(GcmIntentService.this, GcmIntentHandle.class);
        intent.putExtra("todo","updatePost");
 		intent.putExtra("post_title", title);
 		intent.putExtra("post_url", url);
 		intent.putExtra("post_id", id);
 		intent.putExtra("post_author", author);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_CANCEL_CURRENT);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificate = prefs.getBoolean("notifications", true);
        boolean vibrate = prefs.getBoolean("notifications_vibrate", true);
        Uri uri = Uri.parse(prefs.getString("notifications_ringtone", "none"));
 		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
 		
 		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
 		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setLargeIcon(bm)
					.setSmallIcon(R.drawable.ic_notification)
					.setSound(uri)
					.setAutoCancel(true)
					.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.msg_new_post)+title))
					.setContentText(getString(R.string.msg_new_post)+title)
					.setContentTitle(getString(R.string.app_name))
					.setContentIntent(contentIntent);
 		
        if (vibrate && notificate) {
        	mBuilder.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE);
        	mNotificationManager.notify(0, mBuilder.build());
 		}else if(!vibrate && notificate){
 			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
 			mNotificationManager.notify(0, mBuilder.build());
 		}
    }
    
    private void sendNewVpNotification() {
    	Intent intent = new Intent(GcmIntentService.this, GcmIntentHandle.class);
    	intent.putExtra("todo","vpNew");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_CANCEL_CURRENT);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificate = prefs.getBoolean("notifications", true);
        boolean vibrate = prefs.getBoolean("notifications_vibrate", true);
        Uri uri = Uri.parse(prefs.getString("notifications_ringtone", "none"));
 		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
 		
 		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
 		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setLargeIcon(bm)
					.setSmallIcon(R.drawable.ic_notification)
					.setSound(uri)
					.setAutoCancel(true)
					.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.msg_new_vp)))
					.setContentText(getString(R.string.msg_new_vp))
					.setContentTitle(getString(R.string.app_name))
					.setContentIntent(contentIntent);
 		
        if (vibrate && notificate) {
        	mBuilder.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE);
        	mNotificationManager.notify(0, mBuilder.build());
 		}else if(!vibrate && notificate){
 			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
 			mNotificationManager.notify(0, mBuilder.build());
 		}
    }
    
    private void sendUpdateVpNotification() {
    	Intent intent = new Intent(GcmIntentService.this, GcmIntentHandle.class);
    	intent.putExtra("todo","vpUpdate");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_CANCEL_CURRENT);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificate = prefs.getBoolean("notifications", true);
        boolean vibrate = prefs.getBoolean("notifications_vibrate", true);
        Uri uri = Uri.parse(prefs.getString("notifications_ringtone", "none"));
 		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
 		
 		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
 		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this)
					.setLargeIcon(bm)
					.setSmallIcon(R.drawable.ic_notification)
					.setSound(uri)
					.setAutoCancel(true)
					.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.msg_update_vp)))
					.setContentText(getString(R.string.msg_update_vp))
					.setContentTitle(getString(R.string.app_name))
					.setContentIntent(contentIntent);
 		
        if (vibrate && notificate) {
        	mBuilder.setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE);
        	mNotificationManager.notify(0, mBuilder.build());
 		}else if(!vibrate && notificate){
 			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
 			mNotificationManager.notify(0, mBuilder.build());
 		}
    }
}