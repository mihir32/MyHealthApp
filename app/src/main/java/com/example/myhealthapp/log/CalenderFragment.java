package com.example.myhealthapp.log;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.myhealthapp.MainActivity;
import com.example.myhealthapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalenderFragment extends Fragment {
    String type;
    String dt;

    public CalenderFragment(String type) {
        this.type = type;
        dt = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calender, container, false);

        CalendarView cv = v.findViewById(R.id.calendarview);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                dt = String.format("%d-%d-%d", i2, i1 + 1, i);
            }
        });

        Button b = v.findViewById(R.id.btn_select_date);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToLog(type, dt);
            }
        });

        return v;
    }
}