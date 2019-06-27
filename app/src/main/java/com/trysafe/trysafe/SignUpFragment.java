package com.trysafe.trysafe;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;

    private TextInputEditText email;
    private TextInputEditText fullName;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;

    private ImageButton closeBtn;
    private Button signUpBtn;

    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        alreadyHaveAnAccount = view.findViewById(R.id.already_have_an_account);
        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);

        email = view.findViewById(R.id.signUp_email);
        fullName = view.findViewById(R.id.signUp_fullName);
        password = view.findViewById(R.id.signUp_password);
        confirmPassword = view.findViewById(R.id.signUp_confirmPassword);

       // closeBtn = view.findViewById(R.id.closeBtn);
        signUpBtn = view.findViewById(R.id.signInBtn);


        progressBar = view.findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.sidein_from_left,R.anim.sideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInput() {
        if (!TextUtils.isEmpty(email.getText())){
            if (!TextUtils.isEmpty(fullName.getText())){
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8){
                    if (!TextUtils.isEmpty(confirmPassword.getText())){
                        signUpBtn.setEnabled(true);
                        signUpBtn.setTextColor(Color.parseColor("#ffffff"));
                    }else {
                        signUpBtn.setEnabled(false);
                        signUpBtn.setTextColor(Color.parseColor("#50ffffff"));
                    }
                }else {
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.parseColor("#50ffffff"));
                }
            }else {
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.parseColor("#50ffffff"));
            }
        }else{
            signUpBtn.setEnabled(false);
            signUpBtn.setTextColor(Color.parseColor("#50ffffff"));
        }
    }

    private void checkEmailAndPassword(){
        if (email.getText().toString().matches(emailPattern)){
            if (password.getText().toString().equals(confirmPassword.getText().toString())){

                progressBar.setVisibility(View.VISIBLE);
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.parseColor("#50ffffff"));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful()){
                                   FirebaseUser currentUser  = firebaseAuth.getCurrentUser();

                                   DBquery.userUid = currentUser.getUid();

                                   Map<Object,String> userData = new HashMap<>();
                                   userData.put("fullname",fullName.getText().toString());
                                   userData.put("email",email.getText().toString());
                                   userData.put("image"," ");
                                   userData.put("description","Add your description here!");

                                   firebaseFirestore.collection("USERS")
                                           .document(currentUser.getUid())
                                           .set(userData)
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {

                                           if (task.isSuccessful()){
                                               Intent mainIntent = new Intent(getActivity(),MainActivity.class);
                                               startActivity(mainIntent);
                                               getActivity().finish();
                                           }else {
                                               progressBar.setVisibility(View.INVISIBLE);
                                               signUpBtn.setEnabled(true);
                                               signUpBtn.setTextColor(Color.parseColor("#ffffff"));
                                               String error = task.getException().getMessage();
                                               Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });

                               }else {
                                   progressBar.setVisibility(View.INVISIBLE);
                                   signUpBtn.setEnabled(true);
                                   signUpBtn.setTextColor(Color.parseColor("#ffffff"));
                                   String error = task.getException().getMessage();
                                   Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                               }
                            }
                        });
            }else {
                confirmPassword.setError("Password doesn't matched");
            }
        }else {
            email.setError("Email doesn't matched");
        }
    }

}
