package ceg4110.seefood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class DisplaySelection extends FragmentActivity {

    static int ITEM_NUMBER;
    MyAdapter mAdapter;
    static String[] path = new String[5];
    ViewPager mPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_selection);

        Bundle receiveArgs = getIntent().getExtras();
        path[0] = receiveArgs.getString("img1");
        path[1] = receiveArgs.getString("img2");
        path[2] = receiveArgs.getString("img3");
        ITEM_NUMBER = receiveArgs.getInt("selectionCount");

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        final ImageView button = (ImageView)findViewById(R.id.addMoreImages);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {

                switch(event.getAction()) {
                    case ACTION_UP:
                        button.setImageResource(R.drawable.addmore);
                        Intent intent = new Intent();
                        intent.putExtra("OK", false);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case ACTION_DOWN:
                        button.setImageResource(R.drawable.addmore2);
                        break;
                }
                return true;
            }
        });
        final ImageView button2 = (ImageView)findViewById(R.id.sendImages);
        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {

                switch(event.getAction()) {
                    case ACTION_UP:
                        button2.setImageResource(R.drawable.send);
                        Intent intent = new Intent();
                        intent.putExtra("OK", true);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case ACTION_DOWN:
                        button2.setImageResource(R.drawable.send2);
                        break;
                }
                return true;
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.75),(int)(height*.6));

    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fragManag) {
            super(fragManag);
        }

        @Override
        public int getCount() {
            return ITEM_NUMBER;
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }
    }

    public static class ArrayListFragment extends ListFragment {
        int num;

        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            num = getArguments() != null ? getArguments().getInt("num") : 1;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View inflateView = inflater.inflate(R.layout.list_fragment, container, false);
            View textView = inflateView.findViewById(R.id.text);
            String tvText = "Image " +(num +1)+"/"+ ITEM_NUMBER;
            ((TextView)textView).setText(tvText);

            ViewGroup vg = (ViewGroup) inflateView.findViewById(R.id.faragmentContainer);

            if(num == 0 && path[0] != null) {
                Bitmap img = BitmapFactory.decodeFile(path[0]);
                BitmapDrawable imgDrawable = new BitmapDrawable(img);
                ((ImageView) ((FrameLayout) vg.findViewById(R.id.resultImageHolder)).getChildAt(0)).setBackground(imgDrawable);
                ((FrameLayout) vg.findViewById(R.id.resultImageHolder)).removeViewAt(1);

            } else if(num == 1 && path[1] != null) {

                Bitmap img = BitmapFactory.decodeFile(path[1]);
                BitmapDrawable imgDrawable = new BitmapDrawable(img);
                ((ImageView) ((FrameLayout) vg.findViewById(R.id.resultImageHolder)).getChildAt(0)).setBackground(imgDrawable);
                ((FrameLayout) vg.findViewById(R.id.resultImageHolder)).removeViewAt(1);

            } else if(num == 2 && path[2] != null) {

                Bitmap img = BitmapFactory.decodeFile(path[2]);
                BitmapDrawable imgDrawable = new BitmapDrawable(img);
                ((ImageView) ((FrameLayout) vg.findViewById(R.id.resultImageHolder)).getChildAt(0)).setBackground(imgDrawable);
                ((FrameLayout) vg.findViewById(R.id.resultImageHolder)).removeViewAt(1);

            } else if(num == 3 && path[3] != null) {

                Bitmap img = BitmapFactory.decodeFile(path[3]);
                BitmapDrawable imgDrawable = new BitmapDrawable(img);
                ((ImageView) ((FrameLayout) vg.findViewById(R.id.resultImageHolder)).getChildAt(0)).setBackground(imgDrawable);
                ((FrameLayout) vg.findViewById(R.id.resultImageHolder)).removeViewAt(1);

            }
            return inflateView;
        }
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }
}

