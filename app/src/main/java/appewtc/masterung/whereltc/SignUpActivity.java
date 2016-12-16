package appewtc.masterung.whereltc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    //Explicit
    private EditText nameEditText, userEditText, passwordEditText;
    private String nameString, userString, passwordString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Bind Widget
        nameEditText = (EditText) findViewById(R.id.editText);
        userEditText = (EditText) findViewById(R.id.editText2);
        passwordEditText = (EditText) findViewById(R.id.editText3);


    }   // Main Method

    public void clickSignUpSign(View view) {

        nameString = nameEditText.getText().toString().trim();
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //Check Space
        if (nameString.equals("") || userString.equals("") || passwordString.equals("")) {

            //Have Space
            Log.d("13decV1", "Have Space");
            MyAlert myAlert = new MyAlert(SignUpActivity.this, "Have Space",
                    "Please Fill All Blank", R.drawable.doremon48);
            myAlert.myDialog();

        } else {

            try {

                UpdateUser updateUser = new UpdateUser(SignUpActivity.this,
                        nameString, userString, passwordString);
                updateUser.execute();

                String s = updateUser.get();
                Log.d("14decV1", "Result ==> " + s);

                if (Boolean.parseBoolean(s)) {
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Cannot Update User", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.d("14decV1", "e singUp ==> " + e.toString());
            }


        }


    }   // clickSign


}   // Main Class
