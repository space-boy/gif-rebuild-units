package com.sunapp.gifwid.gifwid;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AutoSearchFragment extends Fragment {

    private static final String TAG = "AutoSearchFr";
    private static final String GIF_ARRAY_KEY = "com.sunapp.gifwid.gifwid.ARRAY_KEY";
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

    class GifMetaViewHolder extends RecyclerView.ViewHolder {

        private GifMeta mGifMeta;
        private TextView mPathTextview;

        public GifMetaViewHolder(View itemView) {
            super(itemView);
            mPathTextview = (TextView) itemView.findViewById(R.id.auto_list_textview);

        }

        public void onBind(GifMeta gifMeta){
            mGifMeta = gifMeta;
            mPathTextview.setText(mGifMeta.getFileName());

        }
    }

    class GifListAdapter extends RecyclerView.Adapter<GifMetaViewHolder>{

        private ArrayList<GifMeta> mGifMetaArrayListAdapter;

        public GifListAdapter(ArrayList<GifMeta> gifMetaList){

            mGifMetaArrayListAdapter = gifMetaList;

        }

        @Override
        public GifMetaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = getActivity().getLayoutInflater().inflate(R.layout.auto_list_item,parent,false);

            return new GifMetaViewHolder(v);
        }

        @Override
        public void onBindViewHolder(GifMetaViewHolder holder, int position) {
            holder.onBind(mGifMetaArrayListAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return mGifMetaArrayListAdapter.size();
        }
    }

}
