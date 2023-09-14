package com.example.myhealthapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myhealthapp.add.AddFoodFragment;
import com.example.myhealthapp.add.CameraFragment;
import com.example.myhealthapp.add.VoiceFragment;
import com.example.myhealthapp.log.AddFoodOptionFragment;
import com.example.myhealthapp.log.CalenderFragment;
import com.example.myhealthapp.log.LogFragment;
import com.example.myhealthapp.log.ShowLogFragment;
import com.example.myhealthapp.network.model.Food;
import com.example.myhealthapp.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnv;

    DashBoardFragment dsbF = new DashBoardFragment();
    LogFragment logF = new LogFragment();
    TargetFragment tarF = new TargetFragment();

    Context context;

    FragmentManager fm;

    TextView tv_food ,tv_goal,tv_remaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startLogin();
        }

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mainFrag, dsbF)
                .addToBackStack("dashB").commit();

        bnv = findViewById(R.id.bottomNavigationView);
        context = getApplicationContext();

        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case (R.id.dash): {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.mainFrag, dsbF)
                                .addToBackStack("dashB").commit();
                        return true;
                    }
                    case (R.id.log): {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.mainFrag, logF)
                                .addToBackStack("logF").commit();
                        return true;
                    }
                    case (R.id.tar): {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.mainFrag, tarF)
                                .addToBackStack("tarF").commit();
                        return true;
                    }
                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        int len = fm.getBackStackEntryCount();
        if (len <= 1) {
            super.onBackPressed();
            finish();
        } else {
            fm.popBackStack();
            FragmentManager.BackStackEntry backStackEntry = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 2);

            if (backStackEntry.getName() == null) {
                return;
            }

            String frag = backStackEntry.getName();

            switch (frag) {
                case "showLogF":
                case "searchF":
                case "logF":
                case "addF":
                case "addOptions":
                case "calenderF":
                    bnv.getMenu().findItem(R.id.log).setChecked(true);
                    break;
                case "tarF":
                    bnv.getMenu().findItem(R.id.tar).setChecked(true);
                    break;
                default:
                    bnv.getMenu().findItem(R.id.dash).setChecked(true);
                    break;
            }
        }
    }

    void startLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        this.finish();
    }

    public void addOptions(String type, String date) {
        fm.beginTransaction()
                .replace(R.id.mainFrag, new AddFoodOptionFragment(type, date))
                .addToBackStack("addOptions").commit();
    }

    public void goToLog(String type, String l) {
        fm.beginTransaction()
                .replace(R.id.mainFrag, new ShowLogFragment(type, l))
                .addToBackStack("showLogF").commit();
    }

    public void searchFood(String type, String date) {
        fm.beginTransaction()
                .replace(R.id.mainFrag, new SearchFragment(type, date))
                .addToBackStack("searchF").commit();

    }

    public void addFood(Food f, String type,String date) {
        fm.beginTransaction()
                .replace(R.id.mainFrag, new AddFoodFragment(f, type, date))
                .addToBackStack("addF").commit();
    }

    public void goToCalender(String type) {
        fm.beginTransaction()
                .replace(R.id.mainFrag, new CalenderFragment(type))
                .addToBackStack("calenderF").commit();
    }

    public void goToCam(String type, String date) {
        fm.beginTransaction()
                .replace(R.id.mainFrag, new CameraFragment(context, type, date))
                .addToBackStack("camF").commit();
    }

    public void goToVoice(String type, String date) {
        fm.beginTransaction()
                .replace(R.id.mainFrag, new VoiceFragment(context, type, date))
                .addToBackStack("voiceF").commit();
    }
}