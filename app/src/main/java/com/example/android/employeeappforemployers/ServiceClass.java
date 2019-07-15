package com.example.android.employeeappforemployers;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;

public class ServiceClass extends IntentService {


    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private Timer t;//=new Timer(String.valueOf(Looper.getMainLooper()));
    String lattitude,longitude;
    RequestQueue requestQueue;
    private Context context;
    int id;


    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue= Volley.newRequestQueue(this);
        t=new Timer(String.valueOf(Looper.getMainLooper()));
    }


    public ServiceClass(Context context)
    {
        super("Sample");
        this.context=context;
    }

    public ServiceClass()
    {
        super("Sample");
        this.context=context;


    }
    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {

//        id=intent.getExtras().getInt("ID");
        requestQueue= Volley.newRequestQueue(this);
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                ResultReceiver rr=intent.getParcelableExtra("receiver");
                Bundle b=new Bundle();
                getLocation();
                b.putString("res_lat",lattitude);
                b.putString("res_lng",longitude);
                rr.send(MainActivity.RESULT_CODE,b);
//                getLocation();
            }
        },0,3000);


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//                MainActivity.AskforPermission();
        } else {
            double latti,longi;
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                latti = location.getLatitude();
                longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);

            } else  if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);


            } else  if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);

            }else{

                System.out.println("Unble to Trace your location");
                // Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
            performupload();
        }
    }


    public void performupload()
    {
//        String url="http://www.thantrajna.com/sjec_task/insert_loc.php";
//        StringRequest name=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ServiceClass.this, "Err: " + error, Toast.LENGTH_SHORT).show();
//
//            }
//        })
//        {
//            @Override
//            protected Map<String,String> getParams()
//            {
//                Map<String,String> params=new HashMap<String, String>();
//                params.put("ID",id+"");
//                params.put("Latitude",lattitude);
//                params.put("Longitude",longitude);
//                return params;
//            }
//        };
//        requestQueue.add(name);
    }

}
