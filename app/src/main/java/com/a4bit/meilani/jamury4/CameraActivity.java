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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import static java.lang.Double.NaN;
import static org.opencv.imgproc.Imgproc.moments;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;


/**
 * Created by Meilani Wulandari on 20-Jan-18.
 */

public class CameraActivity extends AppCompatActivity{

    static {
        OpenCVLoader.initDebug();

    }


    ImageView quick_start_cropped_image;
    private Bitmap bitmap, bitmapCropped, medianBitmap, img,gg,resized;
    Button prepoBtn,eksBtn;
    File file;
    BufferedImage jmr = null;
    BufferedImage gmbk=null;
    Bitmap bmp;
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


        //EKSTRAKSI FITUR////
        eksBtn = (Button) findViewById(R.id.eksBtn);

        eksBtn.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                //PRE PROCESSING
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


                quick_start_cropped_image.setImageBitmap(img);
                try {
                    saveImage(resized,"jamurku");
                    Log.d("gambar","berhasil");

                }catch (Exception e){
                    Log.d("gambar", e.toString());
                }


                //FEATURE EKSTRACTION

                VectorLib vlib = new VectorLib();
                ImageLib imgsearch = new ImageLib();
                double[] cvq=null;
                double [][] hasilSimilarityWarna=null;
                double [][] hasilSimilarityBentuk=null;
//                double[] w1=null;

                int [][][] rgb_colors = null;

                double[][] combineWarna = new double[1021][125];
                double[][] combineBentuk = new double[1021][7];

                //proses akses db

                JamurHelper jamurHelper = new JamurHelper(getApplicationContext());
                jamurHelper.open();

                double[][] dataWarna = jamurHelper.getAllWarna();
                double[][] dataBentuk = jamurHelper.getAllBentuk();
                ArrayList<JamurModel> jamurModels = jamurHelper.getAllData();
                jamurHelper.close();

                //db ditutup
                //proses ekstraksi warna
                rgb_colors = getRGB(file.getAbsolutePath()); //[256][256][3]
                cvq = imgsearch.ColorFeatureExtraction(rgb_colors);

                //Normalisasi//
                //ambil data training warna
                for(int i = 0; i< dataWarna.length; i++){
                    for(int j = 0; j < dataWarna[i].length; j++){
                        combineWarna[i][j] = dataWarna[i][j];
                    }
                }

                //tambahkan hasil ekstraksi data testing ke array combine

                for(int j = 0; j < dataWarna[0].length; j++){
                    combineWarna[1020][j] = cvq[j];
                }

                //normalisasi
                double[][] normalisasiWarna=MinMax(combineWarna);

                //proses pemindahan indeks terakhir normalisasi(array hasil kamera) ke array baru
                double[] hasilWarnaKu= normalisasiWarna[normalisasiWarna.length-1];

                //Proses pemotongan index terakhir
                normalisasiWarna[normalisasiWarna.length-1]=null;

                double[][]datatrainingWarna = new double[normalisasiWarna.length-1][normalisasiWarna[0].length];

                for(int u=0;u<normalisasiWarna.length-1;u++){
                    for(int s=0;s<normalisasiWarna[u].length;s++){
                        datatrainingWarna[u][s]=normalisasiWarna[u][s];
                    }
                }

                //similarity//
                hasilSimilarityWarna = imgsearch.SimilarityMeasurement("cosine", hasilWarnaKu, datatrainingWarna);
                for(int i = 0; i< hasilSimilarityWarna.length; i++){
                    String temp = "";
                    temp += ("hasil baris "+i + ": ");
                    for(int j = 0; j < hasilSimilarityWarna[i].length; j++){
                        temp+=(hasilSimilarityWarna[i][j] + " ");
                    }

                    Log.d("ayam", "hasil similarity warna : " + temp);
                }

                ///EKSTRAKSI BENTUK ////

                double[] momentResult = Hu();

                //Normalisasi//

                //ambil data training warna

                int counterdata=0;
                for(int i = 0; i< dataBentuk.length; i++){
                    for(int j = 0; j < dataBentuk[i].length; j++){
                        combineBentuk[i][j] = dataBentuk[i][j];
                        counterdata++;
                    }
                }

                //tambahkan data testing

                for(int j = 0; j < dataBentuk[0].length; j++){
                    combineBentuk[1020][j] = momentResult[j];
                }

                //combine[][] -> array hasil kombinasi data test dgn data baru

                //normalisasi

                double [][] normalisasiBentuk = vlib.Normalization("minmax",combineBentuk,0,1);

                //hasilku -> array hasil kamera setelah normalisasi

                //proses pemindahan indeks terakhir combine(array hasil kamera) ke array baru
                double[] hasilBentukKu= normalisasiBentuk[normalisasiBentuk.length-1];

                //proses penghapusan array terakhir, sehingga combine sekarang berisi data test yang sudah dinormalisasi
                //combine[combine.length-1][0]= Double.parseDouble(null);

                normalisasiBentuk[normalisasiBentuk.length-1]= null;
//
                double[][]datatrainingBentuk = new double[normalisasiBentuk.length-1][normalisasiBentuk[0].length];

                for(int u=0;u<normalisasiBentuk.length-1;u++){
                    for(int s=0;s<normalisasiBentuk[u].length;s++){
                        datatrainingBentuk[u][s]=normalisasiBentuk[u][s];
                    }
                }

                //nyari hasil
                Long tsCosine = System.nanoTime();

                hasilSimilarityBentuk = imgsearch.SimilarityMeasurement("cosine", hasilBentukKu , datatrainingBentuk);

                //pembuatan map untuk sorting index


                Map<Integer, Double> mapBentuk = new TreeMap<Integer, Double>();

// data hasil similarity diurutkan berdasarkan index
                for (int s = 0; s < hasilSimilarityBentuk[0].length; s++) {
                    mapBentuk.put((int) hasilSimilarityBentuk[1][s], hasilSimilarityBentuk[0][s]);
                }

                Map<Integer, Double> mapWarna = new TreeMap<Integer, Double>();
// data hasil similarity diurutkan berdasarkan index
                for (int s = 0; s < hasilSimilarityWarna[0].length; s++) {
                    mapWarna.put((int) hasilSimilarityWarna[1][s], hasilSimilarityWarna[0][s]);
                }

                double w1 =1;
                double w2 =0;
                //perkalian antara weight dengan matrix warna
                for (int h = 0; h < mapWarna.size(); h++) {
                    mapWarna.replace(h, mapWarna.get(h) * w1);

                }


                //perkalian antara weight dengan matrix bentuk
                for (int p = 0; p < mapBentuk.size(); p++) {
                    mapBentuk.replace(p, mapBentuk.get(p) * w2);
                }

                //ubah ke map untuk sorting index
                Map<Integer, Double> mapJumlah = new TreeMap<Integer, Double>();

                for (int s = 0; s < mapWarna.size(); s++) {
                    mapJumlah.put(s, mapWarna.get(s) + mapBentuk.get(s));
                }

                Map<Double,Integer> mapHasilSort = new TreeMap<Double,Integer>();

                for (int s = 0; s < mapJumlah.size(); s++) {
                    mapHasilSort.put(mapJumlah.get(s),s);
                }
                TreeMap<Double,Integer> newMapSorting = new TreeMap(Collections.reverseOrder());
                newMapSorting.putAll(mapHasilSort);

                Log.d("ayam","hasil map sorting:" + newMapSorting);

                //access treemap
                Integer first = newMapSorting.firstEntry().getValue();

                int posisiGambar = first;

                //mencari posisi gambar

                int posisi = 0;
                for(int i = 0 ; i< jamurModels.size(); i++){
                    if(posisiGambar <= Integer.parseInt((jamurModels.get(i)).getRange())){
                        posisi = i;
                        break;
                    }
                }

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

            //DEBUG

        } catch (Exception e) {
            e.printStackTrace();
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

    private static double max(double[][] data,int j) {
        double max = 0;
        int jumdat=1021;
        for(int i=0;i<jumdat;i++){

            if(data[i][j]>max){
                max=data[i][j];

            }
        }
        //System.out.println(max);
        return max;
    }

    private static double min(double[][] data,int j){
        double min=1000;

        int jumdat=1021;
        for(int i=0;i<jumdat;i++){
            if(min>data[i][j]){
                min=data[i][j];
            }

        }
        //System.out.println(min);
        return min;
    }


    public double[][] MinMax (double[][] data){
        double[][] newdata=new double[1021][125];
        String s = null;
        int j,i,jumdat=1021;
        int newmax=1;
        int newmin=0;
        for(j=0;j<125;j++){
            double max=max(data,j);
            double min=min(data,j);
            for(i=0;i<jumdat;i++){
                newdata[i][j] = ((data[i][j]-min)*(newmax-newmin))/((max-min)+newmin);
                if (Double.isNaN(newdata[i][j]))
                    newdata[i][j]=0.0;
            }

        }return newdata;

    }


    public double[] Hu() {

        Mat imagenOriginal;
        imagenOriginal = new Mat();
        Mat gris = new Mat();
        Mat hh = new Mat();

        Utils.bitmapToMat(img, imagenOriginal);

        Imgproc.cvtColor(imagenOriginal, gris, Imgproc.COLOR_RGB2GRAY);
        Moments moment = moments(gris,false);
        //Moments moment = Imgproc.moments(gris, false);

        double[] humm;

        Imgproc.HuMoments(moment, hh);
        System.out.println("nilai hh :" + hh.dump());

        hh.dump();


        double[]  data = {0,1,2,3,4,5,6};

        int k=0;
        for (int i = 0; i < hh.rows(); i++) {
            for (int j = 0; j < hh.cols(); j++) {
                data[k++]=(double)hh.get(i,j)[0];

            }
        }
        return data;

    }


}
