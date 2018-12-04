package ceg4110.seefood;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import java.io.IOException;
import java.util.ArrayList;
import result.Result;


public class GalleryActivity extends AppCompatActivity {


    ScrollView sView;
    ArrayList<Result> imgList;
    static float GALLERY_SIZE = 200;
    static int row[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Bundle receiveArgs = getIntent().getExtras();
        GALLERY_SIZE = receiveArgs.getInt("GALLERY_SIZE");

        sView = (ScrollView) findViewById(R.id.scrollView);

    }

    public void addImages() throws IOException {

        int imageCounter = 0;
        int size = imgList.size();

        for(int i = imageCounter/3; imageCounter<size;i = imageCounter/3) {

            LinearLayout row = (LinearLayout) findViewById(GalleryActivity.row[i]);

            ImageView imagView = (ImageView) ((FrameLayout) row.getChildAt(imageCounter%3)).getChildAt(1);
            ImageView result = (ImageView) ((FrameLayout) row.getChildAt(imageCounter%3)).getChildAt(2);

            Result imgData = imgList.get(imageCounter++);
            Bitmap img = BitmapFactory.decodeByteArray(imgData.getImage(), 0, imgData.getImage().length);
            imagView.setImageBitmap(img);
            result.setBackgroundResource(imgData.getFood() == 1 ? R.drawable.checkmark : R.drawable.x);

        }

    }


}


