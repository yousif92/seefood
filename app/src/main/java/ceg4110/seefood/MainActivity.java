package ceg4110.seefood;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.MotionEvent.ACTION_UP;
import static android.view.MotionEvent.ACTION_DOWN;


public class MainActivity extends AppCompatActivity {

    private String[] currentPath = new String[5];

    private static final int CODE_REQUESTED = 42;
    private static final int MENU_SELECT = 4;
    private static final int PHOTO_REQUESTED = 1;
    private String tempPath;

    private final int REQUEST_STORAGE = 1;
    private String[] PERMISSIONS_REQUEST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Intent intent;
    RelativeLayout mainLayout;
    boolean aModify = false;
    boolean send = false;
    int counter = 0;

    ImageView cameraButton;
    ImageView browseButton;
    ImageView galleryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (RelativeLayout)findViewById(R.id.mainActivityLayout);
        cameraButton = (ImageView)findViewById(R.id.cameraButton);
        browseButton = (ImageView)findViewById(R.id.browseButton);
        galleryButton = (ImageView)findViewById(R.id.galleryButton);

        cameraButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {

                switch(event.getAction()) {
                    case ACTION_UP:
                        cameraButton.setImageResource(R.drawable.camera);
                        dispatchTakePictureIntent();
                        break;
                    case ACTION_DOWN:
                        cameraButton.setImageResource(R.drawable.camera_icon_pressed);
                        break;
                }
                return true;
            }
        });

        browseButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {

                switch(event.getAction()) {
                    case ACTION_UP:
                        browseButton.setImageResource(R.drawable.browse);
                        performFileSearch();
                        break;
                    case ACTION_DOWN:
                        browseButton.setImageResource(R.drawable.gallery_icon_pressed);
                        break;
                }
                return true;
            }
        });

        galleryButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {

                switch(event.getAction()) {
                    case ACTION_UP:
                        galleryButton.setImageResource(R.drawable.gallery);
                        //new MyAsyncTask().execute("2");
                        intent = new Intent(MainActivity.this, GalleryActivity.class);
                        break;
                    case ACTION_DOWN:
                        galleryButton.setImageResource(R.drawable.uploaded_icon_pressed);

                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(aModify) {
            mainLayout.setAlpha(1);
            aModify = false;
        }
    }

    public void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "ceg4110.seefood",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PHOTO_REQUESTED);

            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, 
                ".jpg",
                storageDir
        );

        tempPath = image.getAbsolutePath();


        return image;
    }

    public void performFileSearch() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");

        startActivityForResult(intent, CODE_REQUESTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == CODE_REQUESTED && resultCode == Activity.RESULT_OK) { //browse

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                String fileName = "no-path-found";
                String[] filePathColumn = {MediaStore.Images.Media.SIZE};
                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                if(cursor.moveToFirst()){
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    fileName = cursor.getString(columnIndex);
                }
                cursor.close();

                String absolutePath = "";

                try {
                    InputStream inStr = getContentResolver().openInputStream(uri);

                    byte[]  buffer = new byte[Integer.parseInt(fileName)];
                    inStr.read(buffer);
                    inStr.close();

                    File myFile = createImageFile();
                    FileOutputStream out = new FileOutputStream(myFile);
                    out.write(buffer);
                    out.close();

                    absolutePath = myFile.getAbsolutePath();


                } catch (FileNotFoundException e) {
                } catch (IOException e) {}


                switch (counter) {
                    case 0:
                        currentPath[counter++] = absolutePath;
                        break;
                    case 1:
                        currentPath[counter++] = absolutePath;
                        break;
                    case 2:
                        currentPath[counter++] = absolutePath;
                        break;
                    case 3:
                        currentPath[counter++] = absolutePath;
                        send = true;
                }

                if(send) {

                    verfyPerms(this);


                } else {
                    Intent openSelection = new Intent(MainActivity.this,DisplaySelection.class);
                    openSelection.putExtra("image1Path", currentPath[0]);
                    openSelection.putExtra("image2Path", currentPath[1]);
                    openSelection.putExtra("image3Path", currentPath[2]);
                    openSelection.putExtra("image4Path", currentPath[3]);
                    openSelection.putExtra("image5Path", currentPath[4]);
                    openSelection.putExtra("selectionCount", counter);
                    mainLayout.setAlpha((float).5);
                    aModify = true;
                    startActivityForResult(openSelection, MENU_SELECT);
                }

            }
        } else if(requestCode == PHOTO_REQUESTED && resultCode == Activity.RESULT_OK) {

            switch (counter) {
                case 0:
                    currentPath[counter++] = tempPath;
                    break;
                case 1:
                    currentPath[counter++] = tempPath;
                    break;
                case 2:
                    currentPath[counter++] = tempPath;
                    break;
                case 3:
                    currentPath[counter++] = tempPath;
                    break;
                case 4:
                    currentPath[counter++] = tempPath;
                    send = true;
            }

        } else if(requestCode == MENU_SELECT && resultCode == Activity.RESULT_OK) {
            send = resultData.getBooleanExtra("DONE",false);
        }
    }

    public void verfyPerms(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQUEST,
                    REQUEST_STORAGE
            );
        }
    }

}

