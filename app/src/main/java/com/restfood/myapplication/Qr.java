package com.restfood.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Qr extends AppCompatActivity {

    ImageView qrImage;
    String shopId;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        qrImage=findViewById(R.id.qr_image);

        shopId=new Auth().getUId();
        createQr();

    }


    private void createQr()
    {
        MultiFormatWriter multiformetwriter=new MultiFormatWriter();

        try {
            BitMatrix bitMatrix=multiformetwriter.encode(shopId, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);

            qrImage.setImageBitmap(bitmap);


        }
        catch (Exception e)
        {

        }


    }
}
