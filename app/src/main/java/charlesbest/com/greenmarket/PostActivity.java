package charlesbest.com.greenmarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.jar.*;

import id.zelory.compressor.Compressor;

public class PostActivity extends AppCompatActivity {

    Spinner category;
    EditText title,description,location,price;
    ImageView image;
    Button upload;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mDatabase ;
    private FirebaseAuth auth;
    File compressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        auth = FirebaseAuth.getInstance();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        category = findViewById(R.id.pCategory);
        title = findViewById(R.id.pName);
        description = findViewById(R.id.pDescription);
        location = findViewById(R.id.pLocation);
        price = findViewById(R.id.pPrice);
        upload = findViewById(R.id.upload);

        image  = findViewById(R.id.pImage);

        category.setAdapter(load());
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");

              if (ActivityCompat.checkSelfPermission(PostActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(PostActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                  startActivityForResult(photoPickerIntent, 1);

              }else{
                  String [] permission =
                          { android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE};
                  ActivityCompat.requestPermissions(PostActivity.this,permission,1);
                  startActivityForResult(photoPickerIntent, 1);
              }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(PostActivity.this, "Failed ", Toast.LENGTH_SHORT).show();
               // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
             if(auth!=null) { upLoadImage(compressed);}
            }
        });




    }

    ArrayAdapter load(){
        ArrayList<String> listLevel = new ArrayList();

            listLevel.add("Fruit");
            listLevel.add("Vegetables");
            listLevel.add("Root and Tubers");
            listLevel.add("Grains");
        return new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,listLevel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                File f = new File(filePath);
                compressed = compress(f);

                Bitmap bitmap = BitmapFactory.decodeFile(compressed.getPath());
                image.setImageBitmap(bitmap);

            }

    }

    String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
       int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
       // imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    public File compress(File f){

        try {

        File compressedImage = new Compressor(this)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(70)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .compressToFile(f);

        return compressedImage;

      } catch (IOException e) {
            e.printStackTrace();

      }
      return f;
    }

    public void upLoadImage(File f){

        Uri contentUri = Uri.fromFile(f);

        if(contentUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" +contentUri.getLastPathSegment()+ UUID.randomUUID());
            ref.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String d = uri.toString();
                            upLoadToDatabase(d);
                            progressDialog.dismiss();
                            Toast.makeText(PostActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PostActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })/*.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            })*/;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

   void upLoadToDatabase(String imageUri){

       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
       String  seller = prefs.getString("email",null);
       String  number = prefs.getString("phone",null);

       mDatabase = FirebaseDatabase.getInstance().getReference("Products").child(category.getSelectedItem().toString());

       String key = mDatabase.push().getKey();
        if(key==null){
           key = seller+number;
        }
       ProductModel product = new ProductModel(title.getText().toString(),imageUri,price.getText().toString(),location.getText().toString(),number,key,seller,description.getText().toString());
       mDatabase.child(key).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
                finish();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Log.e(this.getClass().getName(),e.getMessage());
           }
       });
    }


}
