package com.acushlakoncept.marketmoni;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    Context _context;

    // Constructor
    public Utils(Context context){
       _context = context;
    }

    public static String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public byte[] processBlob(Bitmap bp){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] img = bos.toByteArray();

        return img;
    }


    public void emptyInputEditText(TextInputEditText... args) {
        for (TextInputEditText arg : args) {
            arg.setText(null);
        }
    }

    public boolean isSDCardMounted(){

        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    static public String getToken(int chars) {
        String CharSet = "abcdefghijkmnopqrstuvwxyz1234567890";
        String Token = "";
        for (int a = 1; a <= chars; a++) {
            Token += CharSet.charAt(new Random().nextInt(CharSet.length()));
        }
        return Token;
    }


}
