package appewtc.masterung.whereltc;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

public class ServiceActivity extends FragmentActivity implements OnMapReadyCallback {

    //Explicit
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private double latADouble, lngADouble, updateLatADouble, updateLngADouble;
    private String[] loginStrings;
    private TextView textView;
    private EditText editText;
    private ImageView imageView, takePhotoImageView;
    private String nameImageString, pathImageString, urlImageString;
    private Uri uri;
    private boolean aBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);

        //Bind Widget
        textView = (TextView) findViewById(R.id.textView5);
        editText = (EditText) findViewById(R.id.editText6);
        imageView = (ImageView) findViewById(R.id.imageView2);
        takePhotoImageView = (ImageView) findViewById(R.id.imageView4);

        //Setup
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        loginStrings = getIntent().getStringArrayExtra("Login");

        //Show View
        textView.setText(loginStrings[1]);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Take Photo Controller
        takePhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        //Image Controller
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please Choose App"), 1);
            }
        });


    }   // Main Method

    public void clickListView(View view) {
        startActivity(new Intent(ServiceActivity.this, LTClistView.class));
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Log.d("16decV1", "Result OK");

            uri = data.getData();
            changeImage(uri);

            aBoolean = false;

        }   // if

    }   // onActivityResult

    private void changeImage(Uri uri) {
        try {

            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            imageView.setImageBitmap(bitmap);


        } catch (Exception e) {
            Log.d("16decV1", "e changeImage ==> " + e.toString());
        }
    }

    public void clickSave(View view) {

        nameImageString = editText.getText().toString().trim();

        //Check Space
        if (nameImageString.equals("")) {
            //Have Space
            MyAlert myAlert = new MyAlert(ServiceActivity.this,
                    getResources().getString(R.string.title_have_space),
                    getResources().getString(R.string.message_have_space),
                    R.drawable.doremon48);
            myAlert.myDialog();
        } else if (aBoolean) {
            //Non Choose Image
            MyAlert myAlert = new MyAlert(ServiceActivity.this,
                    getResources().getString(R.string.title_noimage),
                    getResources().getString(R.string.message_noimage),
                    R.drawable.kon48);
            myAlert.myDialog();
        } else {
            //Data OK
            uploadImage();
            uploadString();

        }

    }   // clickSave

    private void uploadString() {

        urlImageString = "http://lao-hosting.com/ltc/Image" +
                pathImageString.substring(pathImageString.lastIndexOf("/"));
        Log.d("16decV2", "urlImage ==> " + urlImageString);

        try {

            UpdateLTC updateLTC = new UpdateLTC(ServiceActivity.this,
                    nameImageString, urlImageString,
                    Double.toString(updateLatADouble),
                    Double.toString(updateLngADouble));
            updateLTC.execute();

            if (Boolean.parseBoolean(updateLTC.get())) {
                Toast.makeText(ServiceActivity.this, "Save OK", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ServiceActivity.this, "Cannot Save Data", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void uploadImage() {

        try {

            //Permission
            StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                    .Builder().permitAll().build();
            StrictMode.setThreadPolicy(threadPolicy);

            //Find Path of Image
            String[] strings = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, strings, null, null, null);
            if (cursor != null) {

                cursor.moveToFirst();
                int i = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                pathImageString = cursor.getString(i);

            } else {
                pathImageString = uri.getPath();
            }
            Log.d("16decV2", "path ==> " + pathImageString);

            SimpleFTP simpleFTP = new SimpleFTP();
            simpleFTP.connect("ftp.lao-hosting.com", 21, "ltc@lao-hosting.com", "Abc12345");
            simpleFTP.bin();
            simpleFTP.cwd("Image");
            simpleFTP.stor(new File(pathImageString));
            simpleFTP.disconnect();
            Toast.makeText(ServiceActivity.this, "Upload Image Finish", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Log.d("16decV2", "e upload ==> " + e.toString());
        }


    }   // upload

    @Override
    protected void onResume() {
        super.onResume();

        latADouble = 17.969857;
        lngADouble = 102.612190;

        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {
            latADouble = networkLocation.getLatitude();
            lngADouble = networkLocation.getLongitude();
        }

        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            latADouble = gpsLocation.getLatitude();
            lngADouble = gpsLocation.getLongitude();
        }

        Log.d("15decV1", "lat ==> " + latADouble);
        Log.d("15decV1", "lng ==> " + lngADouble);

    }   // onResume

    @Override
    protected void onStop() {
        super.onStop();

        locationManager.removeUpdates(locationListener);

    }

    public Location myFindLocation(String strProvider) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {

            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        }


        return location;
    }


    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            latADouble = location.getLatitude();
            lngADouble = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng latLng = new LatLng(latADouble, lngADouble);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        myAddMarker(latADouble, lngADouble);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                myAddMarker(latLng.latitude, latLng.longitude);
            }
        });


    }   // onMap

    private void myAddMarker(double latADouble, double lngADouble) {

        LatLng latLng = new LatLng(latADouble, lngADouble);
        mMap.addMarker(new MarkerOptions().position(latLng)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.build4)));

        updateLatADouble = latADouble;
        updateLngADouble = lngADouble;
        Log.d("15decV2", "updateLat ==> " + updateLatADouble);
        Log.d("15decV2", "updateLng ==> " + updateLngADouble);


    }

}   // Main Class
