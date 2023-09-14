package com.example.myhealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myhealthapp.network.model.DailyLimit;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class TargetFragment extends Fragment {
int target;
    public TargetFragment() {



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_target, container, false);

        EditText ed1=(EditText)  v.findViewById(R.id.setTarget);




        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        DocumentReference docRef = db.collection("foodLimit").document(user.getUid()).collection(date).document("daily");
        DocumentReference docRef2=db.collection("user-info").document(user.getUid());
//        docRef.get().addOnCompleteListener((new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                Log.d("IMAD", "DocumentSnapshot data: " + document.getData());
//
//                                DailyLimit dl = document.toObject(DailyLimit.class);
//                                ed1.setText(String.valueOf(dl.getDaily_limit()));
//                            }
//                        } else {
//                            Log.d("IMAD", "No such document");
//                        }
//
//                    }
//                }));


        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("userInfo", "DocumentSnapshot data: " + document.getData());
                        int tgt = Integer.parseInt(document.get("daily-Limit").toString());

                        if(tgt>=0)
                            ed1.setText(String.valueOf(tgt));
                        Log.d("ayus", String.valueOf(tgt));
                        target=tgt;

                    } else {
                        Log.d("userInfo", "get failed with ", task.getException());
                    }
                }
            }
        });




        Button b = v.findViewById(R.id.logOut);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogout();
            }
        });
        Button  bt=(Button) v.findViewById(R.id.tarButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  ed1.setText(ed1.getText());
                int val=Integer.parseInt(ed1.getText().toString());
                if(val<0)
                {
                    Toast.makeText(getContext(),"Please Enter Positive Number", Toast.LENGTH_SHORT).show();
                    ed1.setText(String.valueOf(target));
                }

                else {
                    updateTarget(val);
                    ed1.setText(String.valueOf(val));
                    Toast.makeText(getContext(), "Target Updated to " + String.valueOf(val), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;
    }


    public void updateTarget(int val)
    {
//
//        EditText ed1=(EditText)  v.findViewById(R.id.setTarget);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef2=db.collection("user-info").document(user.getUid());
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        docRef2.update("daily-Limit",val);


        db.collection("foodLimit").document(user.getUid()).collection(date).document("daily")
                .update("daily_limit", val)
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


    public void handleLogout() {
        AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(requireContext(), LoginActivity.class);
                    startActivity(i);
                    requireActivity().finish();
                }
            }
        });
    }



}