package com.maxprof90.redditarticles;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.maxprof90.redditarticles.utils.DownloadImageTask;
import com.maxprof90.redditarticles.utils.TouchImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class FullScreenViewActivity extends AppCompatActivity {
    Bitmap bitmap;
    final static int STORAGE_PERMISSION_REQUEST_CODE = 10;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            saveImage(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);
        ImageView closeIv = findViewById(R.id.closeIv);
        Intent intent = getIntent();
        String imgUrl = intent.getStringExtra("IMG_URL");
        closeIv.setOnClickListener((View view) -> {
            FullScreenViewActivity.this.finish();


        });


        TouchImageView touchImageView = findViewById(R.id.imgDisplay);

        try {
            bitmap = new DownloadImageTask(touchImageView).execute(imgUrl).get();
            if (bitmap == null) { // check whether the link is an image or no
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imgUrl));
                startActivity(browserIntent);
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        ImageView downloadIv = findViewById(R.id.downloadIv);
        downloadIv.setOnClickListener((View view) -> {
            if (bitmap != null) {
                if (checkStoragePermission())
                    saveImage(bitmap);
                else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
                }
            }

        });


    }

    private void saveImage(Bitmap bitmap) {

        String root = String.format("%s/%s/", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString(), "Reddit");

        File myDir = new File(root);
        if (!myDir.exists())
            myDir.mkdirs();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDD_HHmmss", Locale.ENGLISH);
        String imageName = sdf.format(new Date(System.currentTimeMillis()));
        String fname = "IMG_" + imageName + ".jpg";
        File file = new File(myDir, fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            //Make img available in the gallery after saving
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            out.flush();
            out.close();
            Toast.makeText(this, getString(R.string.img_saved), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}

