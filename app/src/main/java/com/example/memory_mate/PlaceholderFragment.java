package com.example.memory_mate;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlaceholderFragment extends Fragment {

    public static PlaceholderFragment newInstance(String tabName) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putString("tabName", tabName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
        // You can customize the fragment layout here
        return rootView;
    }
}
