package in.tushar.slingshotv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.icu.util.Output;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Signature extends AppCompatActivity {
    public static final String TAG = "--Signature--";
    public static SignaturePad signaturePad;
    Button clear, proceed;
    String userPhoneNumber,userSignatureJPGPath,userSignatureSVGPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        signaturePad = findViewById(R.id.signaturePad);
        clear = findViewById(R.id.clear);
        proceed = findViewById(R.id.proceed);
        userPhoneNumber = getIntent().getStringExtra("phone");
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                clear.setEnabled(true);
                clear.setClickable(true);
                proceed.setEnabled(true);
                clear.setClickable(true);
            }

            @Override
            public void onClear() {
                clear.setEnabled(false);
                clear.setClickable(false);
                proceed.setEnabled(false);
                clear.setClickable(false);
            }
        });

    }

    public void signature(View v) {
        Log.e(TAG, "Id : " + v.getId());
        switch (v.getId()) {
            case R.id.clear:
                clear.setScaleX((float) 0.9);
                clear.setScaleY((float) 0.9);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clear.setScaleX((float) 1.0);
                        clear.setScaleY((float) 1.0);
                        signaturePad.clear();
                    }
                }, 100);
                break;
            case R.id.proceed:
                proceed.setScaleX((float) 0.9);
                proceed.setScaleY((float) 0.9);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        proceed.setScaleX((float) 1.0);
                        proceed.setScaleY((float) 1.0);
                        new NewThread().execute();
//                        saveSignatureImage();
                    }
                }, 100);
                break;
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }


    @Override
    protected void onStart() {
        overridePendingTransition(0, 0);
        super.onStart();
    }



    class NewThread extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Bitmap signatureImage = signaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureImage)) {
                    Log.e(TAG, "JPG Signature Saved into the Gallery");

                } else {
                    Log.e(TAG, "JPG Signature is not Saved into the Gallery");
                }
                if (addSvgSignatureToGallery(signaturePad.getSignatureSvg())) {
                    Log.e(TAG, "SVG Signature Saved into the Gallery");
                } else {
                    Log.e(TAG, "SVG Signature is not Saved into the Gallery");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


        public boolean addJpgSignatureToGallery(Bitmap bitmap) {
            boolean result = false;
            try {
                userSignatureJPGPath = userPhoneNumber+"Signature_%d.jpg";
                File photo = new File(getAlbumStorageDir("SlingShot_Signature"), String.format(userSignatureJPGPath, System.currentTimeMillis()));
                saveBitmapToJPG(bitmap, photo);
//            scanMediaFile(photo);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

//        private void scanMediaFile(File photo) {
//            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri contentUri = Uri.fromFile(photo);
//            mediaScanIntent.setData(contentUri);
//            Signature.this.sendBroadcast(mediaScanIntent);
//        }

        public boolean addSvgSignatureToGallery(String signatureSVG) {
            boolean result = false;
            try {
                userSignatureSVGPath = userPhoneNumber+"Signature_%d.svg";
                File svgFile = new File(getAlbumStorageDir("SlingShot_Signature"), String.format(userSignatureSVGPath, System.currentTimeMillis()));
                OutputStream outputStream = new FileOutputStream(svgFile);
                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                writer.write(signatureSVG);
                writer.close();
                outputStream.flush();
                outputStream.close();
//            scanMediaFile(svgFile);
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        public File getAlbumStorageDir(String dirName) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), dirName);
            if (!file.mkdir()) {
                Log.e(TAG, "Signature Directory not Created");
            }
            return file;
        }

        public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
            Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            OutputStream outputStream = new FileOutputStream(photo);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.close();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(new Intent(getApplicationContext(), CaptureSelfie.class).putExtra("signatureJPG",userSignatureJPGPath).putExtra("signatureSVG",userSignatureSVGPath).putExtra("phone",userPhoneNumber));
        }
    }
}
