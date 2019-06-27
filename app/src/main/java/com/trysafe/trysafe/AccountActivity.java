package com.trysafe.trysafe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView userName;
    TextView userEmail;
    TextView userDes;
    Button signOut;
    private String TAG;
    FirebaseUser currentUser;

    private  String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileImage = findViewById(R.id.profile_image);
        userEmail = findViewById(R.id.user_email);
        userName = findViewById(R.id.user_name);
        signOut = findViewById(R.id.sign_out_btn);
        userDes = findViewById(R.id.userDes);


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent signOutIntent = new Intent(AccountActivity.this,RegisterActivity.class);
                startActivity(signOutIntent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    String name = task.getResult().getString("fullname");
                    String email = task.getResult().getString("email");
                    String description = task.getResult().getString("description");
                    imageUrl = task.getResult().getString("image");

                    userName.setText(name);
                    userEmail.setText(email);
                    userDes.setText(description);
                    Glide.with(AccountActivity.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.defaultavater)
                            .into(profileImage);

                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(AccountActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.account_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.edit_profile:
                Intent intent = new Intent(AccountActivity.this,UpdateUserInfoActivity.class);
                intent.putExtra("fullname",userName.getText());
                intent.putExtra("email",userEmail.getText());
                intent.putExtra("des",userDes.getText());
                intent.putExtra("image",imageUrl);
                startActivity(intent);
                overridePendingTransition(R.anim.side_from_right,R.anim.sideout_from_left);
                break;

        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.sidein_from_left, R.anim.sideout_from_right);
    }
}
