package com.example.myhealthapp.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myhealthapp.MainActivity;
import com.example.myhealthapp.R;
import com.example.myhealthapp.search.SearchViewModel;

public class LogFragment extends Fragment {
    SearchViewModel k;

    public LogFragment() {
        this.k = new SearchViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_log, container, false);

        Button bFast = myView.findViewById(R.id.bFast);
        bFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToCalender("breakfast");
            }
        });

        Button lunch = myView.findViewById(R.id.lunch);
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToCalender("lunch");
            }
        });

        Button dinner = myView.findViewById(R.id.dinner);
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToCalender("dinner");
            }
        });

        Button miscellaneous = myView.findViewById(R.id.miscellaneous);
        miscellaneous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToCalender("miscellaneous");
            }
        });

        return myView;
    }

}