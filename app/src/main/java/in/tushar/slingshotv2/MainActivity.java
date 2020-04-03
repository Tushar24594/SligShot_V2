package in.tushar.slingshotv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    TextView textName, textEmail, textPhone;
    EditText editName, editEmail, editPhone;
    String userName, userEmail, userPhone;
    Button submitBtn;
    CSV csv = new CSV();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textName = findViewById(R.id.fullName);
        textEmail = findViewById(R.id.email);
        textPhone = findViewById(R.id.phone);
        editName = findViewById(R.id.userName);
        editEmail = findViewById(R.id.userEmail);
        editPhone = findViewById(R.id.userMobile);
        submitBtn = findViewById(R.id.submitBtn);
    }

    @Override
    protected void onStart() {
        overridePendingTransition(0, 0);
        super.onStart();
        checkMultiplePermission();
    }

    public void submitDetail(final View e) {
        userName = editName.getText().toString().trim();
        userEmail = editEmail.getText().toString().trim();
        userPhone = editPhone.getText().toString().trim();
        submitBtn.setScaleX((float) 0.9);
        submitBtn.setScaleY((float) 0.9);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                submitBtn.setScaleX((float) 1.0);
                submitBtn.setScaleY((float) 1.0);
                if (userName.isEmpty()) {
                    editName.setError("Please Enter Your Name");
                    editName.requestFocus();
                    return;
                }
                if (userEmail.isEmpty() && !checkMailValidation(userEmail)) {
                    editEmail.setError("Please Enter Your Name");
                    editEmail.requestFocus();
                    return;
                }
                if (userPhone.isEmpty() && userPhone.length() < 10) {
                    editPhone.setError("Please Enter Your Mobile Number");
                    editPhone.requestFocus();
                    return;
                } else if (!userName.isEmpty() && !userEmail.isEmpty() && !userPhone.isEmpty()) {
                    if (csv.saveDatatoCSV(userName, userPhone, userEmail)) {
                        startActivity(new Intent(getApplicationContext(), CaptureSelfie.class));
                    }
                }
            }
        }, 300);


    }

    private Boolean checkMailValidation(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public Boolean checkMultiplePermission(){
        boolean res = false;
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckCam = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED && permissionCheckCam != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 3);
            res = true;
        }
        else {
            res = false;
        }
        return res;
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
