package com.a4bit.meilani.jamury4;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.TimingLogger;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import com.a4bit.meilani.jamury4.utility.JamurHelper;
import com.a4bit.meilani.jamury4.utility.JamurModel;
import com.a4bit.meilani.jamury4.utility.WarnaModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import ALI.ImageLib;
import ALI.VectorLib;


import javax.imageio.ImageIO;
import javax.imageio.ImageIO.*;

import static com.a4bit.meilani.jamury4.CardViewJamurAdapter.EXTRA_JAMUR;
import org.opencv.core.*;


/**
 * Created by Meilani Wulandari on 20-Jan-18.
 */

public class CameraActivity extends AppCompatActivity{
    ImageView quick_start_cropped_image;
    private Bitmap bitmap, bitmapCropped, medianBitmap, img,gg,resized;
    Button prepoBtn,eksBtn;
    File file;

    //private Context context;
    //String file_name;

    ContextWrapper cw;
    // path to /data/data/yourapp/app_data/imageDir
    File directory;
    // Create imageDir
    File mypath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_layout);

        cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        mypath=new File(directory,"jamur.jpg");

        quick_start_cropped_image = (ImageView) findViewById(R.id.quick_start_cropped_image);
        prepoBtn = (Button) findViewById(R.id.prepoBtn);
        prepoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img = bitmapCropped.copy(Bitmap.Config.ARGB_8888, true);

                int[] pixel = new int[9];
                int[] R = new int[9];
                int[] B = new int[9];
                int[] G = new int[9];

                for (int i = 1; i < img.getWidth() - 1; i++) {
                    for (int j = 1; j < img.getHeight() - 1; j++) {
                        pixel[0] = img.getPixel(i - 1, j - 1);
                        pixel[1] = img.getPixel(i - 1, j);
                        pixel[3] = img.getPixel(i, j + 1);
                        pixel[2] = img.getPixel(i - 1, j + 1);
                        pixel[4] = img.getPixel(i + 1, j + 1);
                        pixel[5] = img.getPixel(i + 1, j);
                        pixel[6] = img.getPixel(i + 1, j - 1);
                        pixel[7] = img.getPixel(i, j - 1);
                        pixel[8] = img.getPixel(i, j);
                        for (int k = 0; k < 9; k++) {
                            R[k] = Color.red(pixel[k]);
                            B[k] = Color.blue(pixel[k]);
                            G[k] = Color.green(pixel[k]);
                        }
                        Arrays.sort(R);
                        Arrays.sort(G);
                        Arrays.sort(B);
                        img.setPixel(i, j, Color.rgb(R[4], G[4], B[4]));
                    }
                }

                //resizing
                resized = Bitmap.createScaledBitmap(img, 320, 320, true);
                System.out.println(resized.getWidth());
                System.out.println(resized.getHeight());


                quick_start_cropped_image.setImageBitmap(resized);
                try {
                    saveImage(resized,"jamurku");
                    Log.d("gambar","berhasil , lebar : " + img.getWidth() + " tinggi :" + img.getHeight());
                    Log.d("gambar","resized, lebar: "+ resized.getWidth() + "tinggi : " + resized.getHeight());

                }catch (Exception e){
                    Log.d("gambar", e.toString());
                }


            }
        });

        eksBtn = (Button) findViewById(R.id.eksBtn);

        eksBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                VectorLib vlib = new VectorLib();
                ImageLib imgsearch = new ImageLib();
                double[] cvq=null;
                double [][] hasil=null;
                double[] w1=null;

                int [][][] rgb_colors = null;

                JamurHelper jamurHelper = new JamurHelper(getApplicationContext());
                jamurHelper.open();
                double[][] warnaDataset = jamurHelper.getAllWarna();
                ArrayList<JamurModel> jamurModels = jamurHelper.getAllData();
                jamurHelper.close();

                    Log.d("gambar", "x: " + warnaDataset.length + "|y:" + warnaDataset[0].length);

                    Log.d("gambar","mulai get rgb");
//
//                    //yang bener nih/////////////////////////////////////////////////
//
//                    Bitmap kk = BitmapFactory.decodeFile(file.getCanonicalPath());
//
//                    ////////////////////////////////////////////////////////////////


                Long tsRGB = System.nanoTime();
                    Log.d("rgb","mulai get RGB");
                    rgb_colors = getRGB(file.getAbsolutePath()); //[256][256][3]
                    Log.d("rgb",rgb_colors.toString());
                Long teRGB = System.nanoTime();
                Log.d("rgb","waktu get RGB :" + (teRGB-tsRGB)/0.000001 +"ms");

                Long tsCVQ = System.nanoTime();
                    Log.d("rgb","ekstraksi dimulai");
                    cvq = imgsearch.ColorFeatureExtraction(rgb_colors);
                    Log.d("rgb","hasil ekstraksi :" + cvq);
                Long teCVQ = System.nanoTime();
                Log.d("rgb","waktu get CVQ: " + (teCVQ-tsCVQ)/0.000001 +"ms");



                    //loading color features

                    //nyari hasil
                Long tsCosine = System.nanoTime();

                    hasil = imgsearch.SimilarityMeasurement("cosine", cvq, warnaDataset);
                    int similiarPosition = (int)hasil[1][0];

                    Log.d("gambar" , "similar position : " + similiarPosition);
                    Log.d("gambar" , "hasil[0]" + hasil[0].length);
                    Log.d("gambar" , "hasil[1]" + hasil[1].length);

                    for(int i = 0; i< hasil.length; i++){
                        String temp = "";
                        temp += ("hasil baris "+i + ": ");
                        for(int j = 0; j < hasil[i].length; j++){
                            temp+=(hasil[i][j] + " ");
                        }

                        Log.d("gambar", "hasil " + temp);
                    }

                    int posisi = 0;
                    for(int i = 0 ; i< jamurModels.size(); i++){
                        if(similiarPosition <= Integer.parseInt((jamurModels.get(i)).getRange())){
                            posisi = i;
                            break;
                        }
                    }

                Long teCosine = System.nanoTime();
                Log.d("rgb","waktu get Similarity: " + (teCosine-tsCosine)/0.000001 +"ms");

//                    //coba
//                int hasilquery[]=new int[hasil[0].length];
//                for(int j=0;j<hasil[0].length;j++) {
//                    hasilquery[j] = (int) hasil[1][j];
//                    String te = "";
//                    te+=hasilquery[j]+"";
//                    Log.d("gambar","hasil query :" + te);
//                }
//
//                vlib.view(hasilquery);
//                vlib.view(vlib.double_to_int(hasil[1]));




//                Log.d("gambar" , "posisi : " +posisi);

//                    System.out.println("Cintaaaaa " + posisi);

                    Intent intent = new Intent(CameraActivity.this, HasilActivity.class);
                    intent.putExtra("CaptureImg", img);
                    intent.putExtra(EXTRA_JAMUR, jamurModels.get(posisi));
                    startActivity(intent);

            }
        });
    }



    /** Start pick image activity with chooser. */
    public void onSelectImageClick(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("My Crop")
                .setCropShape(CropImageView.CropShape.OVAL)
                .setCropMenuCropButtonTitle("Done")
                .setRequestedSize(320, 320)
                .setCropMenuCropButtonIcon(R.drawable.ic_launcher)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                Toast.makeText(
                        this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
                        .show();

                try {
                    bitmapCropped = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri() );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //createImageFromBitmap(bitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


    public void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Please";
        File myDir = new File(root);
        if (!myDir.mkdirs()){
            myDir.mkdirs();
        }
        String fname = "Bismillah-" + image_name+ ".png";
        file = new File(myDir, fname);
//        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.d("imageku",file.getCanonicalPath());
            Log.d("imageku",file.getAbsolutePath());
            Log.d("imageku",file.getPath());
            Log.d("imageku","berhasil");

            //DEBUG

            Log.d("Haha", "Hehe");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("imageku","gagal");
        }
    }


    public int [][][] getRGB(String filename) {
        Bitmap _img = null;

        try{
            _img = BitmapFactory.decodeFile(filename);
        }catch (Exception e){
            e.getMessage();
        }

        int _height = _img.getHeight();
        int _width = _img.getWidth();

        int [][][] _pixel_rgb_color = new int [_height][_width][3];

        int _rgb;
        int _rr,_gg,_bb;

        for(int _hh = 0;_hh < _height;_hh++){
            for(int _ww = 0;_ww < _width;_ww++){
                _rgb = _img.getPixel(_ww,_hh);
                _rr = (_rgb >> 16) & 0x000000FF;
                _gg = (_rgb >> 8) & 0x000000FF;
                _bb = (_rgb) & 0x000000FF;

                _pixel_rgb_color[_hh][_ww][0] = _rr;
                _pixel_rgb_color[_hh][_ww][1] = _gg;
                _pixel_rgb_color[_hh][_ww][2] = _bb;

            }
        }
        return _pixel_rgb_color;
    }



}
