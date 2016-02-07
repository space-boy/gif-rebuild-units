package com.sunapp.gifwid.gifwid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Observer;

public class HomeFragment extends Fragment {

    public static final int SELECT_AUTO_GIF = 102;
    private static final String TAG = "HomeFrag";
    private RecyclerView mRecyclerView;
    private ArrayList<GifMeta> mGifMetaArrayList;
    private int mScrollOffset = 4;
    private boolean mDecoding;
    private boolean mDeleteFlag;
    private UpdateMode mUpdateMode;
    private static String GIF_PATH_KEY = "com.sunapp.gifwid.gifwid.PATH";
    private static String GIF_FRAME_KEY = "com.sunapp.gifwid.gifwid.FRAME";
    private static String GIF_DELAY_KEY = "com.sunapp.gifwid.gifwid.DELAY";
    private static String GIF_NUMBER_KEY = "com.sunapp.gifwid.gifwid.NUMBER";


    public interface UpdateMode {
        void BeginDeleteMode();
        void EndDeleteMode();
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGifMetaArrayList = GifDataStore.get(getActivity()).getGifMetaArrayList();
        findExistingWidgets();
        
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_main_fragment, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.gif_widget_recyclerview);
        mRecyclerView.setAdapter(new GifItemAdapter(mGifMetaArrayList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final FloatingActionMenu fam = (FloatingActionMenu) v.findViewById(R.id.fab_menu1);
        final FloatingActionButton fabAdd = (FloatingActionButton) v.findViewById(R.id.fab_add_gif);
        final FloatingActionButton fabDelete = (FloatingActionButton) v.findViewById(R.id.fab_delete_gif);

        fam.getMenuIconView().setImageResource(R.drawable.ic_more_vert_white_24dp);
        fam.setIconAnimated(false);

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDeleteFlag){
                    mUpdateMode.EndDeleteMode();
                    mDeleteFlag = false;
                    return;
                }

                mDeleteFlag = true;
                mUpdateMode.BeginDeleteMode();

            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(mDecoding){
                    Toast.makeText(getActivity(),R.string.decoding_warn,Toast.LENGTH_SHORT).show();
                    return;
                }

                if(GifDataStore.get(getActivity()).getGifMetaArrayList().size() >= 3){
                    Toast.makeText(getActivity(),R.string.three_gif_limit,Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivityForResult(intent, SELECT_AUTO_GIF);
            }
        });

        //provide a proper fucking override for V7 if you don't want us using this plz google
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > mScrollOffset) {
                    if (dy > 0) {
                        fam.hideMenu(true);
                    } else {
                        fam.showMenu(true);
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mUpdateMode = (UpdateMode) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement Delete interface");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_AUTO_GIF && resultCode == Activity.RESULT_OK){
            Log.i(TAG, "launching async decode");
            new QuickDecodeGif(120,120).execute(data.getStringExtra(AutoSearchFragment.FILE_PATH_KEY));
            mDecoding = true;
        }
    }

    private void findExistingWidgets(){

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        for(int i = 1;i < 4;i++) {
            final String pathKey = GIF_PATH_KEY + String.valueOf(i);
            final String frameKey = GIF_FRAME_KEY + String.valueOf(i);
            final String delayKey = GIF_DELAY_KEY + String.valueOf(i);
            final String numberKey = GIF_NUMBER_KEY + String.valueOf(i);

            GifMeta gm = new GifMeta();
            gm.setFileName(pref.getString(pathKey,""));
            gm.setFrames(pref.getInt(frameKey,10));
            gm.setRefreshRate(pref.getInt(delayKey,100));
            gm.setWidgetNo(pref.getInt(numberKey,0));

            if(!gm.getFileName().equals(""))
                mGifMetaArrayList.add(gm);

        }
    }

    private void removeSharedPrefs(GifMeta gm){
        int widgetno = gm.getWidgetNo();
        final String pathKey = GIF_PATH_KEY + String.valueOf(widgetno);
        final String frameKey = GIF_FRAME_KEY + String.valueOf(widgetno);
        final String delayKey = GIF_DELAY_KEY + String.valueOf(widgetno);
        final String numberKey = GIF_NUMBER_KEY + String.valueOf(widgetno);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();

        editor.remove(pathKey);
        editor.remove(frameKey);
        editor.remove(delayKey);
        editor.remove(numberKey);
        editor.apply();
    }



    private void updateSharedPrefs(GifMeta gm){
        int widgetno = gm.getWidgetNo();
        final String pathKey = GIF_PATH_KEY + String.valueOf(widgetno);
        final String frameKey = GIF_FRAME_KEY + String.valueOf(widgetno);
        final String delayKey = GIF_DELAY_KEY + String.valueOf(widgetno);
        final String numberKey = GIF_NUMBER_KEY + String.valueOf(widgetno);
        Log.i(TAG,"saving widno: " + widgetno);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(pathKey,gm.getFileName());
        editor.putInt(frameKey, gm.getFrames());
        editor.putInt(delayKey, gm.getRefreshRate());
        editor.putInt(numberKey,gm.getWidgetNo());
        editor.apply();
    }

    private class QuickDecodeGif extends AsyncTask<String, Void, Void> {

        private int mDelayMs;
        private String mPath;
        private GifView mAsyncGifView;
        private int mWidth;
        private int mHeight;

        public QuickDecodeGif(int height, int width){
            mWidth = width;
            mHeight = height;
        }

        @Override
        protected Void doInBackground(String... params) {

            mPath = params[0];
            avoidLint();

            while (mAsyncGifView.decodeStatus != 2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public void avoidLint(){
            //not making a handler just for this, so found a way to get around lint warnings
            mAsyncGifView = new GifView(getActivity());
            mAsyncGifView.setGif(mPath, AutoSearchFragment.decodeSampledBitmapFromResource(mPath, mWidth ,mHeight));
            mAsyncGifView.decode();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            GifMeta gm = new GifMeta();

            int frames = mAsyncGifView.getFrameNum();
            int widgetNum = 0;
            boolean has1 = false;
            boolean has2 = false;
            boolean has3 = false;
            mDelayMs = mAsyncGifView.getDelayTime(frames - 1);

            gm.setFrames(frames);
            gm.setFileName(mPath);
            gm.setRefreshRate(mDelayMs);

            for(GifMeta gifMeta : mGifMetaArrayList){
                widgetNum = 0;
                int existingWidgetNums = gifMeta.getWidgetNo();

                if(existingWidgetNums == 1)
                    has1 = true;

                if(existingWidgetNums == 2)
                    has2 = true;

                if(existingWidgetNums == 3)
                    has3 = true;

                if(has1){
                    if(has2 && !has3)
                        widgetNum = 3;

                    if(has3 && !has2)
                        widgetNum = 2;

                    if(!has2 && !has3)
                        widgetNum = 2;

                }else if (has2){
                        widgetNum = 1;
                }else if (has3) {
                    widgetNum = 2;
                } else {
                    widgetNum = 0;
                }
            }

            if(GifDataStore.get(getActivity()).getGifMetaArrayList().size() == 0)
                widgetNum = 1;

            gm.setWidgetNo(widgetNum);
            GifDataStore.get(getActivity()).getGifMetaArrayList().add(gm);
            updateSharedPrefs(gm);
            mDecoding = false;
            mRecyclerView.setAdapter(new GifItemAdapter(mGifMetaArrayList));
        }
    }

    class GifItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        GifMeta mGifMeta;
        TextView mGifNumberText;
        TextView mGifFrameText;
        TextView mGifDelayText;
        ImageView mGifImageView;
        private int mWidth;
        private int mHeight;

        Resources res = getResources();
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoSlab-Regular.ttf");

        public GifItemViewHolder(View itemView) {
            super(itemView);
            mGifNumberText = (TextView) itemView.findViewById(R.id.gif_widget_number_text);
            mGifFrameText = (TextView) itemView.findViewById(R.id.frames_textview);
            mGifDelayText = (TextView) itemView.findViewById(R.id.refresh_rate_textview);
            mGifImageView = (ImageView) itemView.findViewById(R.id.cardview_image_main);

            itemView.setOnClickListener(this);
        }

        public void onBind(GifMeta gifMeta){
            mGifMeta = gifMeta;

            if(gifMeta.getWidgetNo() == 0) {
                mGifNumberText.setText(R.string.not_active_widget);
                mGifNumberText.setTextColor(ContextCompat.getColor(getActivity(),R.color.red_finished));
            } else {
                mGifNumberText.setText(res.getString(R.string.gif_widget_number, gifMeta.getWidgetNo()));
            }
            mGifFrameText.setText(res.getString(R.string.frame_count,gifMeta.getFrames()));
            mGifDelayText.setText(res.getString(R.string.delay_count, mGifMeta.getRefreshRate()));

            if(mWidth == 0)
                mWidth = 120;
            if(mHeight == 0)
                mHeight = 120;

            new DecodeBitmapTask(mGifImageView,
                                 mWidth,
                                 mHeight)
                    .execute(mGifMeta.getFileName());

            //not bothering with a custom view - this is a small use case, a custom view is overkill
            mGifNumberText.setTypeface(tf);
            mGifFrameText.setTypeface(tf);
            mGifDelayText.setTypeface(tf);
        }

        @Override
        public void onClick(View v){

            if(!mDeleteFlag)
                return;

            mGifMetaArrayList.remove(mGifMeta);
            removeSharedPrefs(mGifMeta);
            GifDataStore.get(getActivity()).getGifMetaArrayList().remove(mGifMeta);
            mRecyclerView.setAdapter(new GifItemAdapter(mGifMetaArrayList));

            mUpdateMode.EndDeleteMode();
            mDeleteFlag = false;
        }
    }

    class DecodeBitmapTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> weakReferenceImageView;
        private int mWidth;
        private int mHeight;

        public DecodeBitmapTask(ImageView imageview, int width, int height) {
            weakReferenceImageView = new WeakReference<>(imageview);
            mWidth = width;
            mHeight = height;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return AutoSearchFragment.decodeSampledBitmapFromResource(params[0], mWidth, mHeight);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null) {
                final ImageView imageView = weakReferenceImageView.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    class GifItemAdapter extends RecyclerView.Adapter<GifItemViewHolder>{

        private ArrayList<GifMeta> mGifMetas;

        public GifItemAdapter(ArrayList<GifMeta> gifMetaList){
            mGifMetas = gifMetaList;
        }

        @Override
        public GifItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView cv = (CardView) getActivity().getLayoutInflater().inflate(R.layout.gif_widget_home_item,parent,false);
            return new GifItemViewHolder(cv);
        }

        @Override
        public void onBindViewHolder(GifItemViewHolder holder, int position) {
            holder.onBind(mGifMetas.get(position));
        }

        @Override
        public int getItemCount() {
            return mGifMetas.size();
        }
    }
}
