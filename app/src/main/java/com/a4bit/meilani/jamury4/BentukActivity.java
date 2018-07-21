package com.a4bit.meilani.jamury4;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.a4bit.meilani.jamury4.utility.JamurHelper;
import com.a4bit.meilani.jamury4.utility.JamurModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.DoubleStream;

import ALI.ImageLib;
import ALI.VectorLib;

import static com.a4bit.meilani.jamury4.CardViewJamurAdapter.EXTRA_JAMUR;
import static java.lang.Double.NaN;
import static org.opencv.imgproc.Imgproc.moments;


/**
 * Created by root on 7/10/18.
 */

public class BentukActivity extends AppCompatActivity {
    static {
        OpenCVLoader.initDebug();
        //OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, this, mLoaderCallback); //this keyword and "mLoaderCallback" are not defined in this scope

    }
    ImageView quick_start_cropped_image;
    private Bitmap bitmap, bitmapCropped, medianBitmap, img,gg,resized;
    Button prepoBtn,eksBtn;
    File file;
    double[] momentResult;
    Mat imagenOriginal;
    Mat gris;
    Mat hh;

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
                double[][] combine = new double[1021][7];

                int [][][] rgb_colors = null;

                JamurHelper jamurHelper = new JamurHelper(getApplicationContext());
                jamurHelper.open();
                double[][] bentukDataset = jamurHelper.getAllBentuk();
                double[][] norm = jamurHelper.getAllBentuk();
                ArrayList<JamurModel> jamurModels = jamurHelper.getAllData();
                jamurHelper.close();

                Log.d("gambar", "x: " + bentukDataset.length + "|y:" + bentukDataset[0].length);


                Long tsHu = System.nanoTime();
                Log.d("rgb","mulai get Shape");
                Log.d("rgb","img : " +img.getHeight());
                //executeHueMoment(file.getAbsolutePath());

                //HU MOMENT
                 momentResult = Hu();

//                    Log.d("rgb",rgb_colors.toString());
                Long teHu = System.nanoTime();
                Log.d("rgb","waktu get Shape :" + (teHu-tsHu)/0.000001 +"ms");


                //Normalisasi//

                //ambil data training warna

                int counterdata=0;
                for(int i = 0; i< norm.length; i++){
                    for(int j = 0; j < norm[i].length; j++){
                        combine[i][j] = norm[i][j];
                        counterdata++;
                    }
                }

                Log.d("ayam","counter data: "+counterdata);
                //tambahkan data testing

                for(int j = 0; j < norm[0].length; j++){
                    combine[1020][j] = momentResult[j];
                }

                //combine[][] -> array hasil kombinasi data test dgn data baru


                //print data
                for(int i = 0; i< combine.length; i++){
                    String temp1 = "";
                    temp1 += ("combine "+i + ": ");
                    for(int j = 0; j < combine[i].length; j++){
                        temp1+=(combine[i][j] + " ");
                    }

                    Log.d("ayam", "combine sebelum" + temp1);
                }


                //normalisasi
               double[][] hasilnorm =  MinMax(combine);

                double [][] normalisasi = vlib.Normalization("minmax",combine,0,1);

                //print data
                for(int i = 0; i< hasilnorm.length; i++){
                    String temp1 = "";
                    temp1 += ("hasilnorm "+i + ": ");
                    for(int j = 0; j < hasilnorm[i].length; j++){
                        temp1+=(hasilnorm[i][j] + " ");
                    }
                    Log.d("ayam", "hasil norm " + temp1);
                }
                //print data
                for(int i = 0; i< normalisasi.length; i++){
                    String temp1 = "";
                    temp1 += ("normalisasi "+i + ": ");
                    for(int j = 0; j < normalisasi[i].length; j++){
                        temp1+=(normalisasi[i][j] + " ");
                    }
                    Log.d("ayam", "hasil normalisasi " + temp1);
                }

                //hasilku -> array hasil kamera setelah normalisasi

                //proses pemindahan indeks terakhir combine(array hasil kamera) ke array baru
                double[] hasilku= hasilnorm[hasilnorm.length-1];
                Log.d("ayam","hasilku: " +hasilku.toString());
                Log.d("ayam","hasilku: " +hasilku.length);
                String tempo=" ";
                for(int y=0;y<hasilku.length;y++){

                    tempo+=(hasilku[y]+" ");
                }
                Log.d("ayam","isi hasilku: "+ tempo);
                Log.d("ayam","combine lama: "+hasilnorm.length);


                //proses penghapusan array terakhir, sehingga combine sekarang berisi data test yang sudah dinormalisasi
                //combine[combine.length-1][0]= Double.parseDouble(null);

                hasilnorm[hasilnorm.length-1]=null;
                Log.d("ayam","isi hasilnorm terakhir: " + hasilnorm[hasilnorm.length-1]);


                Log.d("ayam","hasilnorm baru: "+hasilnorm.length );

                double[][]datatraining = new double[hasilnorm.length-1][hasilnorm[0].length];

                for(int u=0;u<hasilnorm.length-1;u++){
                    for(int s=0;s<hasilnorm[u].length;s++){
                        datatraining[u][s]=hasilnorm[u][s];
                    }
                }

                //datatraining=hasilnorm;

                Log.d("ayam","panjang datatraining: "+datatraining.length);

                //print data
                for(int i = 0; i< datatraining.length; i++){
                    String temp1 = "";
                    temp1 += ("datatraining "+i + ": ");
                    for(int j = 0; j < datatraining[i].length; j++){
                        temp1+=(datatraining[i][j] + " ");
                    }
                    Log.d("ayam", "isi data training " + temp1);
                }


                //nyari hasil
                Long tsCosine = System.nanoTime();

                hasil = imgsearch.SimilarityMeasurement("cosine", hasilku , datatraining);

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

                Log.d("gambar","posisi gambar : "+posisi);

                Long teCosine = System.nanoTime();
                Log.d("rgb","waktu get Similarity: " + (teCosine-tsCosine)/0.000001 +"ms");

                double[][] hasil2 = new double[1020][2];
                //penggabungan

                //perkalian antara weight dengan matrix warna
                for(int h=0;h<hasil.length;h++){
                    hasil[h][0]= hasil[h][0]*0.6;
                }

                //perkalian antara weight dengan matrix bentuk
                for(int p=0;p<hasil.length;p++){
                    hasil[p][0]=hasil[p][0]*0.6;
                }

                double[][]penjumlahan = new double[1020][2];

                for(int q=0;q<hasil.length;q++){
                    penjumlahan[q][0]=hasil[q][0]+hasil2[q][0];
                }




                Intent intent = new Intent(BentukActivity.this, HasilActivity.class);
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

//    double centMoment(int p, int q, String filename) {
//
//        Bitmap imageBitmap = BitmapFactory.decodeFile(filename);
//
//        int bitmapWidth = imageBitmap.getWidth();
//        int bitmapHeight = imageBitmap.getHeight();
//        int[][] blackWhiteBitmap = new int[bitmapWidth][bitmapHeight];
//        int moo = 0;
//        for (int i = 0;i < bitmapWidth;i++) {
//            for (int j = 0;j < bitmapHeight;j++) {
//                int color = imageBitmap.getPixel(i,j);
//                int grayscale = (Color.red(color) + Color.green(color) + Color.blue(color)) / 3;
//                if (grayscale > 128) {
//                    blackWhiteBitmap[i][j] = 1;
//                    moo++;
//                } else {
//                    blackWhiteBitmap[i][j] = 0;
//                }
//            }
//        }
//        double m1o=0;
//        double mo1=0;
//        for (int i = 0;i < bitmapWidth-1;i++) {
//            for (int j = 0;j < bitmapHeight-1;j++) {
//                m1o=m1o+(i)*blackWhiteBitmap[i+1][j+1];
//                mo1=mo1+(j)*blackWhiteBitmap[i+1][j+1];
//            }
//        }
//        double xx=m1o/moo;
//        double yy=mo1/moo;
//        double mu_pq=0;
//        for (int i = 0;i < bitmapWidth-1;i++) {
//            double x=i-xx;
//            for (int j = 0;j < bitmapHeight-1;j++) {
//                double y=j-yy;
//                mu_pq=mu_pq+Math.pow(x, p)*Math.pow(y, q)*blackWhiteBitmap[i+1][j+1];
//            }
//        }
//
//        double gamma=0.5*(p+q)+1;
//        double n_pq=mu_pq/Math.pow(moo, gamma);
//
//        return  n_pq;
//    }
//    public void executeHueMoment(String filename) {
//        momentResult = new double[7];
//        double n20=centMoment(2,0,filename);
//        double n02=centMoment(0,2,filename);
//        momentResult[0]=n20+n02;
//        double n11=centMoment(1,1,filename);
//        momentResult[1]=Math.pow(n20-n02,2)+4*Math.pow(n11,2);
//        double n30=centMoment(3,0,filename);
//        double n12=centMoment(1,2,filename);
//        double n21=centMoment(2,1,filename);
//        double n03=centMoment(0,3,filename);
//        momentResult[2]=Math.pow(n30-3*n12, 2)+Math.pow(3*n21-n03,2);
//        momentResult[3]=Math.pow(n30+n12, 2)+Math.pow(n21+n03,2);
//        momentResult[4]=(n30-3*n21)*(n30+n12)*(Math.pow(n30+n12, 2)-3*Math.pow(n21+n03, 2))+(3*n21-n03)*(n21+n03)*(3*Math.pow(n30+n12, 2)-Math.pow(n21+n03, 2));
//        momentResult[5]=(n20-n02)*(Math.pow(n30+n12, 2)-Math.pow(n21+n03, 2))+4*n11*(n30+n12)*(n21+n03);
//        momentResult[6]=(3*n21-n03)*(n30+n12)*(Math.pow(n30+n12, 2)-3*Math.pow(n21+n03, 2))-(n30+3*n12)*(n21+n03)*(3*Math.pow(n30+n12, 2)-Math.pow(n21+n03, 2));
//
//        for(int i = 0; i< momentResult.length; i++){
//            String temp = "";
//            temp += ("hasil moment result "+i + ": ");
//                temp+=(momentResult[i] + "  ");
//
//            Log.d("rgb", "hasil " + temp);
//        }
//    }H

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


    static double[] addElement(double[] a, double e) {
        a = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
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
        double[][] newdata=new double[1021][7];
        String s = null;
        int j,i,jumdat=1021;
        int newmax=1;
        int newmin=0;
        for(j=0;j<7;j++){
            double max=max(data,j);
            double min=min(data,j);
            for(i=0;i<jumdat;i++){
                newdata[i][j] = ((data[i][j]-min)*(newmax-newmin))/((max-min)+newmin);
                if (newdata[i][j] == NaN)
                    newdata[i][j]=0;
            }
        }return newdata;

    }

}
