package com.example.udeys.theindianroute.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.udeys.theindianroute.R;

import java.util.ArrayList;

/**
 * Created by udeys on 6/17/2016.
 */

public class SearchFragment extends Fragment {

    View view;
    ArrayList<String> user_name;
    ArrayList<String> user_id;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        view = inflater.inflate(R.layout.searchfragment, container, false);
        listView = (ListView) view.findViewById(R.id.users);
        try {
            user_name = new ArrayList<>();
            user_id = new ArrayList<>();
            user_name = getArguments().getStringArrayList("user_name");
            user_id = getArguments().getStringArrayList("user_id");
            Log.e("search try", "success");
        } catch (Exception e) {
            Log.e("catch", e.toString());
        }

        displayUsers();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                String uid = user_id.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("user_id", uid);
                bundle.putString("user_name", itemValue);
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_1, profileFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

                // Show Alert
                //Toast.makeText(getActivity(), "Position :"+itemPosition+"  ListItem : " +itemValue  + "id: "+uid, Toast.LENGTH_LONG).show();

            }

        });

        return view;
    }

    public void displayUsers() {

        listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, user_name));

        /*for(int i = 0 ; i < users.size() ; i++){
            Toast.makeText(getActivity() , "DN: "+users.get(i),Toast.LENGTH_SHORT).show();
        }*/
    }


}
