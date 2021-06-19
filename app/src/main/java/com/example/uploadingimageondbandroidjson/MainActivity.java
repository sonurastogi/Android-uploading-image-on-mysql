package com.example.uploadingimageondbandroidjson;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 4655;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filepath;
    Uri uri;
    Button CaptureImageFromCamera,UploadImageToServer;

    ImageView ImageViewHolder;

    EditText imageName;

    ProgressDialog progressDialog ;

    Intent intent ;
    //Bitmap bitmap = new Bitmap("jpeg","ImageViewHolder");

    public  static final int RequestPermissionCode  = 1 ;

    Bitmap bitmap;

    boolean check = true;

    String GetImageNameFromEditText;

    String ImageNameFieldOnServer = "image_name" ;

    String ImagePathFieldOnServer = "image_path" ;

    String ImageUploadPathOnSever ="http://webtgroup.online/Mysql/Registeration/images/capture_img_upload_to_server.php" ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CaptureImageFromCamera = (Button)findViewById(R.id.button);
        ImageViewHolder = (ImageView)findViewById(R.id.imageView);
        UploadImageToServer = (Button) findViewById(R.id.button2);
        imageName = (EditText)findViewById(R.id.editText);

        EnableRuntimePermissionToAccessCamera();

        CaptureImageFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                //intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                //startActivityForResult(intent, 7);
                //onActivityResult(intent, 7);
                //onActivityResult(7, RESULT_OK,intent);

//below written is my code I am trying from stack overflow
              //  someActivityResultLauncher.launch(intent);

            }
        });



        UploadImageToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetImageNameFromEditText = imageName.getText().toString();
//Toast.makeText(MainActivity.this,"HIII",Toast.LENGTH_SHORT).show();
                ImageUploadToServerFunction();
              }

        });  //End of onclicklistner


        //below code is my written
    /*    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                           // doSomeOperations();

                            Toast.makeText(getApplicationContext(),"Hello Everyone",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //above code is my written code  is my written
*/



    }//End of onCreate






    // Star activity for result method to Set captured image on image view after click.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
       // Toast.makeText(getApplicationContext(),data.getData()+"Hello Everyone",Toast.LENGTH_SHORT).show();

//below code is my written so later remove that
        if (requestCode == 1&& resultCode == RESULT_OK&& data != null && data.getData() != null) {
        //if (requestCode == 7 && resultCode == RESULT_OK && data != null && data.getData() != null) {
           // Toast.makeText(getApplicationContext(),"Above uri",Toast.LENGTH_SHORT).show();
             uri = data.getData();
           // Toast.makeText(getApplicationContext(),"Below uri",Toast.LENGTH_SHORT).show();
            try {
                Toast.makeText(getApplicationContext(),"Hello Everyone3333",Toast.LENGTH_SHORT).show();

                // Adding captured image in bitmap.
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // adding captured image in imageview.
                ImageViewHolder.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }else{ Toast.makeText(getApplicationContext(),"Not working code",Toast.LENGTH_SHORT).show();}

    }// End of OnActivityResult




    // Requesting runtime permission to access camera.
    public void EnableRuntimePermissionToAccessCamera(){

       // if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
        ///        Manifest.permission.CAMERA))
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))
        {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(MainActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }//end of EnableRuntimePermissionToAccessCamera()



    // Upload captured image online on server function.


   //below functin is caaled from Image Upload to server button
    public void ImageUploadToServerFunction()  {

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

       // Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_SHORT).show();
       // bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
       // bitmap=new Bitmap("jpeg","ImageViewHolder");
        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
//I have to remove the above code


        //Toast.makeText(getApplicationContext(),"after bitmap Hi",Toast.LENGTH_SHORT).show();
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
        Toast.makeText(getApplicationContext(),"Hi  AsyncTaskUploadClass",Toast.LENGTH_SHORT).show();
        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                // Showing progress dialog at image upload time.
                progressDialog = ProgressDialog.show(MainActivity.this,"Image is Uploading","Please Wait",false,false);
          Window window =  progressDialog.getWindow();
                window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
            }


            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(MainActivity.this,string1+"this messsage is from server postexecute",Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                ImageViewHolder.setImageResource(android.R.color.transparent);


            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);

                HashMapParams.put(ImagePathFieldOnServer, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);

                return FinalData;
            }//End of doInBackground
        }//End of AsyncTaskUpload class

Log.d("Hi","Its Sonu");
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }//Image Upload to server function






    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;


                //I have to remove below comment I did it for practice
                url = new URL(requestURL);

                //url = new URL("http://webtgroup.online/Mysql/get.php");

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

               // httpURLConnectionObject.setReadTimeout(19000);

               // httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

              //  if (RC == HttpsURLConnection.HTTP_OK) {

                //below if statement is written by me just  for experiment

                //I have commented the multiline comment just for experiment I have to remove it later
                if (RC == HttpURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }


                //below code is written by me
              /*  BufferedReader reader = new BufferedReader(new
                        InputStreamReader(httpURLConnectionObject.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return sb.toString();     */







            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MYAPP", "exception", e);
                stringBuilder.append(e.toString());
            }
            return stringBuilder.toString();
        } // End of ImageHttpRequest




        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException, UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }//End of bufferedWriterDataFN

    }//End of Image Process Class



    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        super.onRequestPermissionsResult(RC, per, PResult);
        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }//End of switch case


    }//End of onRequestPermission
}//End of MainActivity