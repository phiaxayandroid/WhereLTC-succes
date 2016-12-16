package appewtc.masterung.whereltc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class LTClistView extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ltclist_view);

        listView = (ListView) findViewById(R.id.livLTC);

        //Create ListView
        try {

            SynLTC synLTC = new SynLTC(LTClistView.this);
            synLTC.execute();
            String s = synLTC.get();
            Log.d("16decV3", "JSON ==> " + s);

            JSONArray jsonArray = new JSONArray(s);
            final String[] nameStrings = new String[jsonArray.length()];
            String[] latStrings = new String[jsonArray.length()];
            String[] lngStrings = new String[jsonArray.length()];
            String[] iconStrings = new String[jsonArray.length()];

            for (int i=0;i<jsonArray.length();i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                nameStrings[i] = jsonObject.getString("NameLogin");
                latStrings[i] = jsonObject.getString("Lat");
                lngStrings[i] = jsonObject.getString("Lng");
                iconStrings[i] = jsonObject.getString("Image");

            }   // for

            MyAdapter myAdapter = new MyAdapter(LTClistView.this,
                    nameStrings, latStrings, lngStrings, iconStrings);
            listView.setAdapter(myAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(LTClistView.this, nameStrings[i], Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }   // Main Method

}   // Main Class
