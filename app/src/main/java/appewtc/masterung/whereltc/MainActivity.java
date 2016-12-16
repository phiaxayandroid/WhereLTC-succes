package appewtc.masterung.whereltc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;
    private String[] loginStrings;
    private boolean aBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        userEditText = (EditText) findViewById(R.id.editText4);
        passwordEditText = (EditText) findViewById(R.id.editText5);


    }   // Main Method


    public void clickPass(View view) {
        String[] strings = new String[]{"1", "Name", "User", "Password"};
        Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
        intent.putExtra("Login", strings);
        startActivity(intent);
    }


    public void clickSignIn(View view) {

        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        if (userString.equals("") || passwordString.equals("")) {
            //Have Space
            MyAlert myAlert = new MyAlert(MainActivity.this,
                    getResources().getString(R.string.title_have_space),
                    getResources().getString(R.string.message_have_space),
                    R.drawable.nobita48);
            myAlert.myDialog();

        } else {
            //No Space
            try {

                SynUser synUser = new SynUser(MainActivity.this);
                synUser.execute();
                String s = synUser.get();
                Log.d("14decV2", "JSoN ==> " + s);

                JSONArray jsonArray = new JSONArray(s);

                loginStrings = new String[4];

                for (int i = 0; i < jsonArray.length(); i += 1) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (userString.equals(jsonObject.getString("User"))) {
                        loginStrings[0] = jsonObject.getString("id");
                        loginStrings[1] = jsonObject.getString("Name");
                        loginStrings[2] = jsonObject.getString("User");
                        loginStrings[3] = jsonObject.getString("Password");
                        aBoolean = false;
                    }

                }   // for

                if (aBoolean) {
                    MyAlert myAlert = new MyAlert(MainActivity.this,
                            getResources().getString(R.string.title_user_fale),
                            getResources().getString(R.string.message_user_false),
                            R.drawable.rat48);
                    myAlert.myDialog();
                } else if (passwordString.equals(loginStrings[3])) {
                    //Password True
                    Toast.makeText(MainActivity.this, "Welcome " + loginStrings[1],
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                    intent.putExtra("Login", loginStrings);
                    startActivity(intent);
                    finish();

                } else {
                    //Password False
                    MyAlert myAlert = new MyAlert(MainActivity.this,
                            getResources().getString(R.string.title_pass_false),
                            getResources().getString(R.string.message_pass_false),
                            R.drawable.kon48);
                    myAlert.myDialog();
                }


            } catch (Exception e) {
                Log.d("14devV2", "e Main ==> " + e.toString());
            }

        }

    }   // clickSignIn

    public void clickSignUpMain(View view) {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }

}   // Main Class
