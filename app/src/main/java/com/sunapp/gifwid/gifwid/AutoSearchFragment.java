package com.sunapp.gifwid.gifwid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AutoSearchFragment extends Fragment {

    private static final String TAG = "AutoSearchFr";
    public static final String FILE_PATH_KEY = "AutoSearchFragment.FILE_PATH_KEY";
    private ArrayList<GifMeta> mGifMetaArrayList;
    private RecyclerView mRecyclerView;

    public static AutoSearchFragment newInstance() {
        Bundle args = new Bundle();
        AutoSearchFragment fragment = new AutoSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGifMetaArrayList = SearchActivity.mGifMetaArrayList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.auto_search_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.auto_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new GifListAdapter(mGifMetaArrayList));

        return v;
    }

    class GifMetaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private GifMeta mGifMeta;
        private TextView mPathTextView;
        private ImageView mIconImage;
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoSlab-Regular.ttf");

        public GifMetaViewHolder(View itemView) {
            super(itemView);
            mPathTextView = (TextView) itemView.findViewById(R.id.auto_list_textview);
            mIconImage = (ImageView) itemView.findViewById(R.id.img_preview);
            itemView.setOnClickListener(this);
        }

        public void onBind(GifMeta gifMeta) {
            mGifMeta = gifMeta;
            mPathTextView.setText(mGifMeta.getFileName());
            mPathTextView.setTypeface(tf);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.putExtra(FILE_PATH_KEY,mGifMeta.getFileName());
            getActivity().setResult(Activity.RESULT_OK,i);
            getActivity().finish();
        }
    }

    class GifListAdapter extends RecyclerView.Adapter<GifMetaViewHolder> {

        private ArrayList<GifMeta> mGifMetaArrayListAdapter;

        public GifListAdapter(ArrayList<GifMeta> gifMetaList) {
            mGifMetaArrayListAdapter = gifMetaList;
        }

        @Override
        public GifMetaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = getActivity().getLayoutInflater().inflate(R.layout.auto_list_item, parent, false);

            return new GifMetaViewHolder(v);
        }

        @Override
        public void onBindViewHolder(GifMetaViewHolder holder, int position) {
            holder.onBind(mGifMetaArrayListAdapter.get(position));

            int mWidth = holder.mIconImage.getWidth();
            int mHeight = holder.mIconImage.getHeight();

            if (mWidth == 0)
                mWidth = 120;

            if (mHeight == 0)
                mWidth = 120;

            GifMeta gm = mGifMetaArrayList.get(position);
            new DecodeBitmapTask(holder.mIconImage, mWidth, mHeight).execute(gm.getFileName());

        }

        @Override
        public int getItemCount() {
            return mGifMetaArrayListAdapter.size();
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(String filepath,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        Log.i(TAG,"sample size: " + options.inSampleSize);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filepath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
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
            return decodeSampledBitmapFromResource(params[0], mWidth, mHeight);
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
}
