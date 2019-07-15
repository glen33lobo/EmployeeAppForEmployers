package com.example.android.employeeappforemployers;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    public static final int RESULT_CODE =12 ;

    Spinner dropdown;
    RequestQueue requestQueue;
    Button ref,sub;
    EditText desc;
    String username;
    LocationManager locationManager;
    String lattitude="0.098",longitude="0.9876";
    Integer sendid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dropdown = findViewById(R.id.spinner1);
        ref=(Button)findViewById(R.id.refrsh);
        sub=(Button)findViewById(R.id.submit);
        desc=(EditText)findViewById(R.id.emp_descrip);

        requestQueue= Volley.newRequestQueue(this);

        performretrievalUserNames();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startMyService();
        }

        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performretrievalUserNames();
            }
        });


        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //perform to store data
                updation();


            }
        });

    }


    public void startMyService()
    {
//        ServiceClass sc=new ServiceClass(this);
//        Intent intent=new Intent(this,sc.getClass()/*ServicesClass.class*/);
        Intent intent=new Intent(this,ServiceClass.class);
        ResultReceiver r=new myreceiver(null);
//        intent.putExtra("ID",id);
//        intent.putExtra("lngg",b);
        intent.putExtra("receiver",r);

        startService(intent);
    }


    public void performretrievalUserNames()
    {
        String url="http://www.thantrajna.com/sjec_task/Employee_details/Retrive_all_emp.php";
        JsonObjectRequest name=new JsonObjectRequest( Request.Method.POST,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String U_NAME[];
                Integer ID[];

                try {
                    JSONArray jsonArray = response.getJSONArray("val");
                    U_NAME = new String[jsonArray.length()];
                    ID=new Integer[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        ID[i] = obj.getInt("ID");
                        U_NAME[i] = obj.getString("USERNAME");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, U_NAME);
                    dropdown.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        username = parent.getItemAtPosition(position).toString();
//                        desc.setText(username);
//                        id=position;
//                        Toast.makeText(MainActivity.this, position+" "+id+" ", Toast.LENGTH_SHORT).show();
//
//
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Err: " + error, Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(name);

    }


    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();

    }

    public void callforgetlocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

    }

    public void AskforPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    AskforPermission();

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

            }else {

                System.out.println("Unble to Trace your location");
                // Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void updation()
    {

        Toast.makeText(this, "updation", Toast.LENGTH_SHORT).show();
        callforgetlocation();

//        Toast.makeText(MainActivity.this, " "+id+" ", Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "going to start", Toast.LENGTH_SHORT).show();
        String url="http://www.thantrajna.com/sjec_task/For_Employers/insert_work.php";
        StringRequest name=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                desc.setText("success:"+response+"");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Err: " + error, Toast.LENGTH_SHORT).show();
                desc.setText(error+"");
            }
        })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> params=new HashMap<String, String>();
                sendid=dropdown.getSelectedItemPosition();
                params.put("ID",sendid+"");
                params.put("LATITUDE",lattitude+"");
                params.put("LONGITUDE",longitude+" ");
                params.put("DESCRIPTION",desc.getText().toString()+" ");
                return params;
            }
        };
        requestQueue.add(name);

    }


    public class myreceiver extends ResultReceiver{

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public myreceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode==RESULT_CODE){

                if(resultData!=null)
                {
                    lattitude=resultData.getString("res_lat");
                    longitude=resultData.getString("res_lng");
                }
            }
        }

    }

}
