package com.example.myhealthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    int AUTHUI_REQ_CODE=10111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            this.finish();
        }




        List<AuthUI.IdpConfig> provider = Collections.singletonList(

                new AuthUI.IdpConfig.GoogleBuilder().build()

        );


        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provider)
                .setTosAndPrivacyPolicyUrls("https://example.com","https://example.com")
                .setAlwaysShowSignInMethodScreen(true)
                .build();

        startActivityForResult(intent,AUTHUI_REQ_CODE);

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AUTHUI_REQ_CODE){
            if(resultCode==RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("LoginActivity", "LoginActivity: "+user.getEmail());
                if(user.getMetadata().getCreationTimestamp()== user.getMetadata().getLastSignInTimestamp())
                    Toast.makeText(this,"Welcome Back!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this,"Welcome New User!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
            else{
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response == null)
                    Log.d("LoginActivity", "Login is cancelled by the user" );
                else
                    Log.e("LoginActivity", "Error: ", response.getError() );

            }

        }
    }
}