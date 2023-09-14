package com.example.myhealthapp.search;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myhealthapp.MainActivity;
import com.example.myhealthapp.R;
import com.example.myhealthapp.network.model.Food;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    SearchViewModel gd;
    String type;
    String date;

    public SearchFragment(String tp, String Date) {
        type = tp;
        date = Date;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.gd = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        View my = inflater.inflate(R.layout.fragment_search, container, false);
        ArrayList<Food> al = new ArrayList<>();
        SearchAdapter adapter = new SearchAdapter(requireActivity(), al, type, date);

        RecyclerView rcv = my.findViewById(R.id.search_recyclerview);
        ProgressBar pb = my.findViewById(R.id.search_progressbar);
        pb.setVisibility(ProgressBar.INVISIBLE);
        TextView tv = my.findViewById(R.id.tv_not_found);
        EditText sv = my.findViewById(R.id.search_edittext);
        Button b = my.findViewById(R.id.btn_search);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gd.getFood(sv.getText().toString());
            }
        });

        rcv.setAdapter(adapter);

        adapter.setOnClickListener((MainActivity) getActivity());

        gd.dataStatus().observe(getViewLifecycleOwner(), data -> {
            pb.setVisibility(!data ? ProgressBar.VISIBLE : ProgressBar.INVISIBLE);
        });

        gd.getSearched(sv.getText().toString()).observe(getViewLifecycleOwner(), data -> {
            adapter.foodList = data;
            rcv.setAdapter(adapter);

            if (data.size() == 0) {
                tv.setVisibility(TextView.VISIBLE);
            } else {
                tv.setVisibility(TextView.INVISIBLE);
            }
        });

        return my;
    }
}