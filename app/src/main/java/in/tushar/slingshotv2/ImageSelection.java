package in.tushar.slingshotv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageSelection extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    ImageView userImage;
    Button  retake, submit;
    String image, fname, uName, uMobile;
    Bitmap myBitmap, bMapRotate;
    Boolean isSaved = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);
        image = getIntent().getStringExtra("Image");
        userImage = findViewById(R.id.image);
        retake = findViewById(R.id.retake);
        submit = findViewById(R.id.submit);
        File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/SlingShot/" + image);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Matrix mat = new Matrix();
            mat.postRotate(-90);
            mat.preScale(0.9f, -0.9f);
            bMapRotate = Bitmap.createBitmap(myBitmap, 0, 0,
                    (int) (myBitmap.getWidth()),(int) (myBitmap.getHeight()), mat, true);
            userImage.setImageBitmap(bMapRotate);

        }
        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retake.setScaleX((float) 0.9);
                retake.setScaleY((float) 0.9);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        retake.setScaleX((float) 1.0);
                        retake.setScaleY((float) 1.0);
                        Intent intent = new Intent(getApplicationContext(), CaptureSelfie.class);
//                        intent.putExtra("name", uName);
//                        intent.putExtra("mobile", uMobile);
                        startActivity(intent);
                        finish();
                    }
                }, 300);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setScaleX((float) 0.9);
                submit.setScaleY((float) 0.9);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        submit.setScaleX((float) 1.0);
                        submit.setScaleY((float) 1.0);
                        if (isSaved) {
//                            Intent intent = new Intent(getApplicationContext(), CaptureSelfie.class);
//                            intent.putExtra("name", uName);
//                            intent.putExtra("mobile", uMobile);
//                            intent.putExtra("Image", fname);
//                            startActivity(intent);
//                            finish();
                            saveBitmapImage(bMapRotate);
                        }
                    }
                }, 300);
            }
        });
    }
    private void saveBitmapImage(Bitmap finalBitmap) {
        View v1 = userImage;
        Log.e("--","Width : "+v1.getWidth()+" -- "+v1.getHeight());
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/SlingShot");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fname = "SlingShot_" + timeStamp + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            isSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
