package in.tushar.slingshotv2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageSling extends AppCompatActivity {
    String userPhoneNumber, userSignatureJPGPath, userSignatureSVGPath, userCapturedImage;
    Bitmap bitmapSignatureJPG, userSignatureBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_sling);
        userPhoneNumber = getIntent().getStringExtra("phone");
        userSignatureJPGPath = getIntent().getStringExtra("signatureJPG");
        userSignatureSVGPath = getIntent().getStringExtra("signatureSVG");
        userCapturedImage = getIntent().getStringExtra("capturedImage");
        checkImageIsExist();
    }

    private void checkImageIsExist() {
        File signature = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/SlingShot_Signature" + userSignatureJPGPath);
        if (signature.exists()) {
            bitmapSignatureJPG = BitmapFactory.decodeFile(signature.getAbsolutePath());
            Matrix matrix = new Matrix();
            matrix.preScale(0.9f, -0.9f, 0.9f, 0.9f);
            userSignatureBitmap = Bitmap.createBitmap(bitmapSignatureJPG, 0, 0, bitmapSignatureJPG.getWidth(), bitmapSignatureJPG.getHeight(), matrix, true);

        }
    }

    protected Bitmap createFinalImage(Bitmap b1, Bitmap b2) {
        Bitmap result = Bitmap.createBitmap(b1.getWidth(), b1.getHeight(), b1.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(b1, -250, -30, null);
        canvas.drawBitmap(b2, 0, 0, null);
        return result;
    }

    private void saveFinalBitmapImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/SlingShot");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = userPhoneNumber + "SlingShot_" + timeStamp + ".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onPostCreate(savedInstanceState);
    }
}
