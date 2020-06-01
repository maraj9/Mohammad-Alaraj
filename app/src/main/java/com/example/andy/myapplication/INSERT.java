package com.example.andy.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class INSERT extends AppCompatActivity {
    private EditText name,price, storage, spefecation;
    ProgressDialog progressDialog;
    final  String URL_INSERT ="insert.php";
    // Folder path for Firebase Storage.
    String Storage_Path = "Uploads/";

    // Root Database Name for Firebase Database.
    static String Database_Path = "Project_Database";

    // Creating button.
    Button ChooseButton, UploadButton,
            DisplayImagesButton;

    // Creating EditText.
    EditText ImageName ;

    // Creating ImageView.
    ImageView SelectImage;

    // Creating URI.
    Uri FilePathUri;

    Uri downUri;
    String imageUrl;

    // Creating StorageReference and DatabaseReference object.
    //StorageReference storageReference;
  //  DatabaseReference databaseReference;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
       name = (EditText) findViewById(R.id.name);
      price = (EditText) findViewById(R.id.price);
        storage = (EditText) findViewById(R.id.editText3);
       spefecation = (EditText) findViewById(R.id.editText4);
       progressDialog =new ProgressDialog(this);


    }

    public void insert(View view) {
        final String name1 = name.getText().toString().trim();
        final String price2 = price.getText().toString().trim();
        final String storage2 = storage.getText().toString().trim();
        final String spefe2= spefecation.getText().toString().trim();
        progressDialog.setMessage("Registering user...");
        progressDialog.show();



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL_INSERT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

        b                  try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "تم التسجيل بنجاح", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name1);

                params.put("price",price2);
                params.put("storage",storage2);
                params.put("spefication",spefe2);
                return params;
            }
        };


        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void choose(View view) {
        // Creating intent.
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                ChooseButton.setText("Image Selected");
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            final StorageReference storageReference2 =
                    storageReference.child(Storage_Path +
                            System.currentTimeMillis() + "." +
                            GetFileExtension(FilePathUri));

            // Adding CompleteListener to second StorageReference.
            storageReference2.putFile(FilePathUri).continueWithTask(
                    new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw task.getException();
                            }

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                            return storageReference2.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){

                        // Getting image download Url
                        downUri = task.getResult();
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Getting image name from EditText and store into string variable.
                    String TempImageName = ImageName.getText().toString().trim();

                    // Hiding the progressDialog after done uploading.
                    progressDialog.dismiss();

                    // Showing toast message after done uploading.
                    Toast.makeText(getApplicationContext(),
                            "Image Uploaded Successfully ",
                            Toast.LENGTH_LONG).show();

                    @SuppressWarnings("VisibleForTests")
                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(
                            TempImageName, downUri.toString());

                    // Getting image upload ID.
                    String ImageUploadId = databaseReference.push().getKey();

                    // Adding image upload id s child element into databaseReference.
                    databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                }
            })
                    // If something goes wrong
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception error message.
                            Toast.makeText(getApplicationContext(),
                                    exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else {
            Toast.makeText(this,
                    "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
        }
    }
}
