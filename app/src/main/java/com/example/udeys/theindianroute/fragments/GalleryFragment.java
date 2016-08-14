package com.example.udeys.theindianroute.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.udeys.theindianroute.CompressFilter;
import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.adapters.GalleryAdapter;

import java.util.ArrayList;

/**
 * Created by Malhotra G on 7/3/2016.
 */

public class GalleryFragment extends Fragment {
    ArrayList<String> imagePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_fragment, container, false);

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        imagePath = new ArrayList<>();

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String imagefilename = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            imagePath.add(imagefilename);
            Log.e("TAG", "filepath: " + imagefilename);
        }
        if (imagePath.size() == 0) {
            Toast.makeText(getActivity(), "0 Images in gallery", Toast.LENGTH_SHORT).show();
        }
        gridview.setAdapter(new GalleryAdapter(getActivity(), imagePath));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.e("Path", imagePath.get(position));
                Intent intent = new Intent(getActivity(), CompressFilter.class);
                intent.putExtra("path", imagePath.get(position));
                startActivity(intent);
            }
        });


        return view;
    }
}
