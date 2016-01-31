package com.sunapp.gifwid.gifwid;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SearchPickerDialog extends DialogFragment {

    private static final String TAG = "SearchDialog";
    Button mManual;
    Button mAuto;
    public static final int REQUEST_MEDIA = 101;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.search_picker_dialog,null);

        mManual = (Button) v.findViewById(R.id.manual_search_button);
        mAuto = (Button) v.findViewById(R.id.auto_search_button);

        mManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                try {
                    startActivityForResult(i, REQUEST_MEDIA);
                } catch(ActivityNotFoundException anfe){
                    Toast.makeText(getActivity(),R.string.no_activity,Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        mAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
              .setTitle(R.string.search)
              .create();
    }

    private String resolveMediaUri(Uri uri) {
        String path = "";
        String [] projection = {MediaStore.MediaColumns.DATA};

        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();

        if(cr == null)
            return path;

        if ( ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        Cursor metaCursor = cr.query(uri,projection,null,null,null);

        if (metaCursor != null){
            try {
                if (metaCursor.moveToFirst())
                    path = metaCursor.getString(0);
            } finally {
                metaCursor.close();
            }
        }
        Log.i(TAG, "return path: " + path);
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_MEDIA && resultCode == Activity.RESULT_OK)
            resolveMediaUri(data.getData());

        //pass the resulting string into a new instance of DetailSetupActivity/DetailSetupFragment
    }
}