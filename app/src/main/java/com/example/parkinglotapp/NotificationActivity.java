package com.example.parkinglotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.parkinglotapp.callbacks.CallBackListener;
import com.example.parkinglotapp.callbacks.callbackInterface;
import com.example.parkinglotapp.singleton.SingleTon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class NotificationActivity extends Activity {
    private Button btnYes,btnNo;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private SingleTon singleTon;
    private callbackInterface callbackItf;
    private CallBackListener callBackListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        this.setFinishOnTouchOutside(false);
        singleTon=SingleTon.getInstance();
//        callbackItf=(callbackInterface)this;
//        Toast.makeText(this, "notification activity", Toast.LENGTH_SHORT).show();
        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
//        Toast.makeText(this, "Notification in here", Toast.LENGTH_SHORT).show();
        btnYes=findViewById(R.id.btn_yes);
        btnNo=findViewById(R.id.btn_no);
        Intent intent=getIntent();
        String test=intent.getStringExtra("test");
        Toast.makeText(this, "try : "+test, Toast.LENGTH_SHORT).show();
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotificationActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                firebaseFirestore.collection("Users").document(mAuth.getUid()).update("haveNotification",false);
                singleTon.setUpdate(true);
                new postTask().execute("YES");
//                callBackListener.setData("142");
//                callbackItf.passData("142");

                finish();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotificationActivity.this, "No", Toast.LENGTH_SHORT).show();
                firebaseFirestore.collection("Users").document(mAuth.getUid()).update("haveNotification",false);
                singleTon.setUpdate(true);
                new postTask().execute("NO");
//                callBackListener.setData("142");
//                callbackItf.passData("142");
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private class postTask extends AsyncTask<String,Integer,Double>{

        @Override
        protected Double doInBackground(String... strings) {
            postData(strings[0]);
            return null;
        }

        public void postData(String val){
            HttpClient httpClient=new DefaultHttpClient();
//            String url="http://parkingdetect.ddns.net/auth/";
//            String mlem="http://192.168.1.11:5000/cc";
//            HttpPost httpPost=new HttpPost(mlem);
//            try {
//                JSONObject jsonObject=new JSONObject();
//                jsonObject.put("command",val);
//                List<NameValuePair> nameValuePairs=new ArrayList<>();
////                nameValuePairs.add(new BasicNameValuePair("command",val));
//                nameValuePairs.add(new BasicNameValuePair("req",jsonObject.toString()));
//                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                httpPost.setHeader("Content-Type", "application/json");
//                HttpResponse response = httpClient.execute(httpPost);
//                ResponseHandler<String> responseHandler = new BasicResponseHandler();
//                final String responseStr = httpClient.execute(httpPost, responseHandler);
//                Log.d("AAA1","done : "+responseStr);
//            } catch (IOException | JSONException e) {
//                Log.d("AAA1","exception : "+e.getMessage());
//                e.printStackTrace();
//            }
            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;
            try {
                //constants
                URL url = new URL("http://parkingdetect.ddns.net/predict/");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", val);
                String message = jsonObject.toString();

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(message.getBytes().length);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                conn.connect();

                //setup send
                os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();

                //do somehting with response
                is = conn.getInputStream();
                //String contentAsString = readIt(is,len);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                conn.disconnect();
            }
        }

    }
}