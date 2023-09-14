package com.example.myhealthapp.log;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myhealthapp.MainActivity;
import com.example.myhealthapp.R;
import com.example.myhealthapp.network.model.FoodDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ShowLogFragment extends Fragment {
    String type;
    String date;
    ArrayList<FoodDataBase> data;
    FoodLogAdapter fla;

    public ShowLogFragment(String type, String l) {
        // Required empty public constructor
        this.type = type;
        this.date = l;
        data = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_show_log, container, false);

        TextView tv = thisView.findViewById(R.id.logTypeHeading);
        switch (this.type) {
            case "breakfast":
                tv.setText(getResources().getString(R.string.bFast));
                break;
            case "lunch":
                tv.setText(getResources().getString(R.string.lun));
                break;
            case "dinner":
                tv.setText(getResources().getString(R.string.din));
                break;
            case "miscellaneous":
                tv.setText(getResources().getString(R.string.misc));
                break;
            default:
                tv.setText(getResources().getString(R.string.logTitle));
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Button addFood = thisView.findViewById(R.id.addFood);
        if (!date.equals(LocalDate.now().format(df))) {
            addFood.setEnabled(false);
        }

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).addOptions(type, date);
            }
        });

        RecyclerView rcv = thisView.findViewById(R.id.rcv);
        data = new ArrayList<>();
        fla = new FoodLogAdapter(requireContext(), data);
        fla.setFood(data);
        rcv.setAdapter(fla);
        getMyData();
        return thisView;
    }

    public void getMyData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        //ArrayList<FoodDataBase> data1=new ArrayList<>();;
        CollectionReference docRef = db.collection("users")
                .document(uid).collection(date).document(type).collection(type);
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<FoodDataBase> downloadInfoList = task.getResult().toObjects(FoodDataBase.class);
                    data.addAll(downloadInfoList);
                    fla.notifyDataSetChanged();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}