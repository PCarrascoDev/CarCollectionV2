package com.desafiolatam.carcollectionv2.views.car;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.desafiolatam.carcollectionv2.R;
import com.desafiolatam.carcollectionv2.data.CurrentUser;
import com.desafiolatam.carcollectionv2.model.Car;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class CarActivity extends AppCompatActivity {

    private DatabaseReference carsReference;
    private CurrentUser currentUser = new CurrentUser();
    private MagicalCamera magicalCamera;
    private MagicalPermissions magicalPermissions;
    private static int RESIZE_PHOTO_PIXELS_PERCENTAGE = 10;
    private CircularImageView carCiv;
    private StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://carcollectionv2-62675.appspot.com");
    private Car car;
    private String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);


        car = (Car) getIntent().getSerializableExtra("CAR");

        final EditText brandEt = (EditText) findViewById(R.id.brandEt);
        final EditText modelEt = (EditText) findViewById(R.id.modelEt);
        final Spinner yearSpin = (Spinner) findViewById(R.id.yearSpin);
        final Spinner bodySpin = (Spinner) findViewById(R.id.bodySpin);
        final CheckBox absCb = (CheckBox) findViewById(R.id.absCb);
        ImageButton saveBtn = (ImageButton) findViewById(R.id.saveBtn);
        ImageButton deleteBtn = (ImageButton) findViewById(R.id.deleteBtn);
        carCiv = (CircularImageView) findViewById(R.id.carCiv);

        if (car != null)
        {
            brandEt.setText(car.getBrand());
            modelEt.setText(car.getModel());
            yearSpin.setSelection(getYearPos(car.getYear()));
            bodySpin.setSelection(getBodyPos(car.getBody()));
            absCb.setChecked(car.isAbs());
            deleteBtn.setVisibility(View.VISIBLE);
            if (car.getPhoto() != null)
            {
                photo = car.getPhoto();
                Picasso.with(this).load(car.getPhoto()).fit().centerCrop().into(carCiv);
            }
        }

        String[] permissions = new String[] {
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        magicalPermissions = new MagicalPermissions(this, permissions);
        magicalCamera = new MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);

        carCiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicalCamera.takePhoto();
                //carCiv.setImageBitmap(magicalCamera.getPhoto());
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = brandEt.getText().toString();
                String model = modelEt.getText().toString();
                if (brand.matches("") || model.matches(""))
                {
                    Toast.makeText(CarActivity.this, "There's an empty field!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String uid = new CurrentUser().uid();
                    carsReference  = FirebaseDatabase.getInstance().getReference().child(uid);
                    int year = Integer.valueOf(yearSpin.getSelectedItem().toString());
                    String body = bodySpin.getSelectedItem().toString();
                    boolean abs = absCb.isChecked();
                    String key;
                    if (car != null)
                    {
                        key = car.getKey();
                    }
                    else
                    {
                        key = carsReference.push().getKey();
                    }
                    carsReference.child(key).setValue(new Car(key, brand, model, body, year, abs, photo));
                    Toast.makeText(CarActivity.this, "Car Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(car != null)
                {
                    currentUser.getReference().child(car.getKey()).removeValue();
                    Toast.makeText(CarActivity.this, "Car Deleted :(", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        magicalPermissions.permissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        magicalCamera.resultPhoto(requestCode, resultCode, data);

        if (RESULT_OK == resultCode)
        {
            Bitmap image = magicalCamera.getPhoto();
            String path = magicalCamera.savePhotoInMemoryDevice(image,"car", "carcollection", MagicalCamera.JPEG, true);
            Uri fileUri = Uri.fromFile(new File(path));
            String fileName = fileUri.getLastPathSegment();

            carCiv.setImageBitmap(image);

            StorageReference carsReference = reference.child("cars/" + fileName);
            final ProgressDialog dialog = new ProgressDialog(CarActivity.this);
            dialog.setTitle("Progress");
            dialog.setMessage("Uploading photo...");
            dialog.setMax(100);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.show();
            UploadTask uploadTask = carsReference.putFile(fileUri);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    int progress = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    dialog.setProgress(progress);
                    if (progress == 100)
                    {
                        dialog.dismiss();
                        Toast.makeText(CarActivity.this, "Photo uploaded", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                String url = taskSnapshot.getDownloadUrl().toString();
                photo = url;
            }
        });
        }
    }

    public int getYearPos(int year){
        int pos = year;
        pos -= 2010;

        if (pos < 0 || pos > 7){
            return 0;
        }
        else {
            return pos;
        }
    }

    public int getBodyPos(String body){
        switch (body){
            case "SUV":
                return 0;
            case "HB":
                return 1;
            case "SEDAN":
                return 2;
            case "COUPE":
                return 3;
            case "PICKUP":
                return 4;
            default:
                return 0;
        }
    }
}
