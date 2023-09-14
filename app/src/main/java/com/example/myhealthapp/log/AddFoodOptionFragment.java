package com.example.myhealthapp.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myhealthapp.MainActivity;
import com.example.myhealthapp.R;

public class AddFoodOptionFragment extends Fragment {
    String type;
    String date;

    public AddFoodOptionFragment(String type, String date) {
        // Required empty public constructor
        this.type = type;
        this.date = date;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_add_food_option, container, false);

        Button man = thisView.findViewById(R.id.manually);
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).searchFood(type, date);
            }
        });

        Button b = thisView.findViewById(R.id.usingCam);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToCam(type, date);
            }
        });

        Button b1 = thisView.findViewById(R.id.usingVoice);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToVoice(type, date);
            }
        });

        return thisView;
    }
}