package com.example.homework2correction;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;


//Casper Fung
public class MainActivity extends AppCompatActivity {

    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.button);

        if (!OpenCVLoader.initDebug()) {
            Log.d("Casper test", "OpenCV has error");
        } else {
            Log.d("Casper test", "OpenCV is fine");
        }

        imgView = findViewById(R.id.imageView);

        final Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String string = decodeQRcode(bitmap);
                String[] output = string.split(";");

                Mat mat = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
                Utils.bitmapToMat(bitmap, mat);
                Scalar lineColor = new Scalar(255,0,0,255);
                int lineWidth = 3;

                for (int j = 0; j<5; j++){
                    Point [] points = {new Point(), new Point()};
                    String [] dots = output[j].split(" ");
                    for (int i = 0; i < dots.length; i++){
                        String [] finalreuslt = dots[i].split(",");
                        points[i] = new Point (Integer.parseInt(finalreuslt[0]),Integer.parseInt(finalreuslt[1]));
                    }

                    Imgproc.line(mat, points[0], points[1], lineColor, lineWidth);

                }

                final Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(mat, bitmap);
                ImageView imgView = findViewById(R.id.imageView);
                imgView.setImageBitmap(bitmap);

            }
        });
    }

    String decodeQRcode(Bitmap bitmap) {
        Mat mat = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, mat);
        QRCodeDetector qrCodeDetector = new QRCodeDetector();
        String result = qrCodeDetector.detectAndDecode(mat);
        Log.d("Casper test", ""+result);
        return result;
    }
}