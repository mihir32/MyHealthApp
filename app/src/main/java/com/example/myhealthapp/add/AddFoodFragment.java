package com.example.myhealthapp.add;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myhealthapp.R;
import com.example.myhealthapp.network.model.DailyLimit;
import com.example.myhealthapp.network.model.Food;
import com.example.myhealthapp.network.model.FoodDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class AddFoodFragment extends Fragment {
    Food selectedFood;
    FirebaseFirestore db;
    String type;
    String date;
    int curCons;

    public AddFoodFragment(Food food, String type, String date) {
        // Required empty public constructor
        selectedFood = food;
        db = FirebaseFirestore.getInstance();
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

        View myView = inflater.inflate(R.layout.fragment_add_food, container, false);

        LinearLayout carbLLY = myView.findViewById(R.id.chart_value_carbs);
        LinearLayout proteinLLY = myView.findViewById(R.id.chart_value_proteins);
        LinearLayout fatsLLY = myView.findViewById(R.id.chart_value_fats);

        TextView tv = myView.findViewById(R.id.tv_kcal_per_100);

        TextView carbTv = myView.findViewById(R.id.tv_current_carbs);
        TextView proteinTv = myView.findViewById(R.id.tv_current_proteins);
        TextView fatsTv = myView.findViewById(R.id.tv_current_fats);
        TextView totalTv = myView.findViewById(R.id.tv_current_kcal);
        TextView carbTvPer = myView.findViewById(R.id.tv_percent_carbs);
        TextView proteinTvPer = myView.findViewById(R.id.tv_percent_proteins);
        TextView fatsTvPer = myView.findViewById(R.id.tv_percent_fats);

        TextView foodTv = myView.findViewById(R.id.tv_food_name);

        foodTv.setText(selectedFood.getKnownAs());

        tv.setText(getResources().getString(R.string.has_kcal, selectedFood.getNutrients().getEnercKcal().intValue()));

        carbTv.setText(getResources().getString(R.string.weight, selectedFood.getNutrients().getChocdf().intValue()));
        proteinTv.setText(getResources().getString(R.string.weight, selectedFood.getNutrients().getProcnt().intValue()));
        fatsTv.setText(getResources().getString(R.string.weight, selectedFood.getNutrients().getFat().intValue()));
        totalTv.setText(getResources().getString(R.string.cals, selectedFood.getNutrients().getEnercKcal().intValue()));

        double totalNutrients = Math.ceil(selectedFood.getNutrients().getChocdf()
                + selectedFood.getNutrients().getFat()
                + selectedFood.getNutrients().getProcnt());

        int carb = (int) Math.ceil(selectedFood.getNutrients().getChocdf() * 100 / totalNutrients);
        int protein = (int) Math.ceil(selectedFood.getNutrients().getProcnt() * 100 / totalNutrients);
        int fats = 100 - protein - carb;

        carbTvPer.setText(getResources().getString(R.string.percent, carb));
        proteinTvPer.setText(getResources().getString(R.string.percent, protein));
        fatsTvPer.setText(getResources().getString(R.string.percent, fats));

        carbLLY.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, carb));
        proteinLLY.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, protein));
        fatsLLY.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, fats));

        EditText ed = myView.findViewById(R.id.addfood_current_grams);

        Button b = myView.findViewById(R.id.submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = ed.getText().toString();
                if (Pattern.matches("[0-9]+", s)) {
                    addFoodToDataBase(Integer.parseInt(s));
                } else {
                    Toast.makeText(requireContext(), "Enter a valid amount", Toast.LENGTH_LONG).show();
                }
            }
        });

        return myView;
    }

    public void addFoodToDataBase(int quantity) {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String uid = user.getUid();
            FoodDataBase food = new FoodDataBase(selectedFood.getKnownAs(), (int)(selectedFood.getNutrients().getEnercKcal() * quantity / 100.0), quantity);

            DocumentReference docRef = db.collection("foodLimit").document(user.getUid()).collection(date).document("daily");
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DailyLimit dl = documentSnapshot.toObject(DailyLimit.class);
                    curCons=dl.getConsumption();
                    Log.d("idd" ,documentSnapshot.getData().toString());


                }
            });

            db.collection("users")
                    .document(uid).collection(date).document(String.valueOf(type)).collection(String.valueOf(type))
                    //.set(item, SetOptions.merge())
                    .add(food).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            updateConsumption((int)(selectedFood.getNutrients().getEnercKcal() * quantity / 100.0));
                            Log.d("IMAD", "DocumentSnapshot successfully written!");
                            Toast.makeText(requireContext(), "Food Added successfully", Toast.LENGTH_LONG).show();
                            requireActivity().getSupportFragmentManager().popBackStack("showLogF", 0);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("IMAD","Failure");
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }



    public void updateConsumption(int val) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        Log.d("isd",String.valueOf(curCons) +" "+ String.valueOf(val));

        db.collection("foodLimit").document(user.getUid()).collection(date).document("daily")
                .update("consumption", val+curCons)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("IMAD","Firebase Updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("IMAD",e.toString());
                    }
                });
    }
}