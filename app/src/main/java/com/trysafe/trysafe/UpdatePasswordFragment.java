package com.trysafe.trysafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;


public class UpdatePasswordFragment extends Fragment {


    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    private TextInputLayout oldPassword, newPassword, confirmPassword;
    private Button updatePasswordBtn;

    private ProgressDialog progressDialog;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);

        oldPassword = view.findViewById(R.id.oldPd);
        newPassword = view.findViewById(R.id.newPd);
        confirmPassword = view.findViewById(R.id.confirmPd);

        updatePasswordBtn = view.findViewById(R.id.updatePasswordBtn);

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });

        //loading dialog//

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        email = getArguments().getString("email");

        oldPassword.getEditText().addTextChangedListener(new TextWatcher() {
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

        newPassword.getEditText().addTextChangedListener(new TextWatcher() {
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

        confirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
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

        return view;
    }

    private void checkInput() {
        if (!TextUtils.isEmpty(oldPassword.getEditText().getText()) && oldPassword.getEditText().length() >= 8) {
            if (!TextUtils.isEmpty(newPassword.getEditText().getText()) && newPassword.getEditText().length() >= 8) {
                if (!TextUtils.isEmpty(confirmPassword.getEditText().getText()) && confirmPassword.getEditText().length() >= 8) {

                    updatePasswordBtn.setEnabled(true);
                    updatePasswordBtn.setTextColor(Color.parseColor("#54596e"));

                } else {
                    updatePasswordBtn.setEnabled(false);
                    updatePasswordBtn.setTextColor(Color.parseColor("#5054596e"));
                }
            } else {
                updatePasswordBtn.setEnabled(false);
                updatePasswordBtn.setTextColor(Color.parseColor("#5054596e"));
            }
        } else {
            updatePasswordBtn.setEnabled(false);
            updatePasswordBtn.setTextColor(Color.parseColor("#5054596e"));
        }
    }

    private void checkEmailAndPassword() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (newPassword.getEditText().getText().toString().equals(confirmPassword.getEditText().getText().toString())) {

            progressDialog.show();

            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, oldPassword.getEditText().getText().toString());

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword.getEditText().getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    oldPassword.getEditText().setText(null);
                                                    newPassword.getEditText().setText(null);
                                                    confirmPassword.getEditText().setText(null);
                                                    Toast.makeText(getContext(), "Password Updated SuccessFully!", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                }
                                                progressDialog.dismiss();
                                            }
                                        });
                            }else {
                                progressDialog.dismiss();
                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            confirmPassword.setError("Password doesn't matched");
        }

    }

}
