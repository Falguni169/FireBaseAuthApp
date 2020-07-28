package com.example.firebaseauthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    GoogleSignInClient googleSignInClient;

    ImageView img;
    Button signout;
    Button revoke;
TextView txt1,txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        img = (ImageView) findViewById(R.id.img);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        signout = (Button) findViewById(R.id.signout);
        revoke = (Button) findViewById(R.id.revoke);

        Intent i = getIntent();
        String login = i.getStringExtra("Login");

        if(login.equals("Facebook")){
            Glide.with(this)
                    .load(i.getStringExtra("profileImage"))
                    .into(img);
            txt1.setText(i.getStringExtra("userName"));
            txt2.setText(i.getStringExtra("email"));

        }else if(login.equals("Google")){

            googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(img);
            txt1.setText(user.getDisplayName());
            txt2.setText(user.getEmail());
        }

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signout(view);
            }
        });
        revoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess(view);
            }
        });
    }

    private void revokeAccess(final View view) {
        auth.signOut();
        googleSignInClient.revokeAccess().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                Snackbar.make(view, "Revoked Access",Snackbar.LENGTH_SHORT);
            }
        });
    }

    private void signout(final View view) {
        auth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("msg", "User Sign out of google ");
//                Toast.makeText(ProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                Snackbar.make(view, "Sign Out Successfully",Snackbar.LENGTH_SHORT);
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (auth.getCurrentUser() == null) {
//            finish();
//            startActivity(new Intent(this, MainActivity.class));
//        }
    }
}