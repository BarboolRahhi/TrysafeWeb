package com.trysafe.trysafe;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {


    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    private TextInputEditText registerMail;
    private Button resetPasswordBtn;
    private FloatingActionButton goBack;

    private FrameLayout frameLayout;

    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;


    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        frameLayout = getActivity().findViewById(R.id.register_framelayout);

        registerMail = view.findViewById(R.id.forgot_email);
        resetPasswordBtn = view.findViewById(R.id.resetPasswordBtn);
        goBack = view.findViewById(R.id.forgotBackBtn);

        emailIconContainer = view.findViewById(R.id.forgotLinearLayout);
        emailIcon = view.findViewById(R.id.forgetEmailIcon);
        emailIconText = view.findViewById(R.id.forgetEmailIconText);
        progressBar = view.findViewById(R.id.forgotProgressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerMail.addTextChangedListener(new TextWatcher() {
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

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailIcon.setImageResource(R.drawable.mailprogress);
                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIconText.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                resetPasswordBtn.setEnabled(false);
                resetPasswordBtn.setTextColor(Color.parseColor("#50ffffff"));

                firebaseAuth.sendPasswordResetEmail(registerMail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, emailIcon.getWidth() / 2, emailIcon.getHeight() / 2);
                                    scaleAnimation.setDuration(100);
                                    scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                    scaleAnimation.setRepeatMode(Animation.REVERSE);
                                    scaleAnimation.setRepeatCount(1);
                                    scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            emailIconText.setText("Recovery email sent successfully ! check your inbox");
                                            emailIconText.setTextColor(getResources().getColor(R.color.successGreen));
                                            TransitionManager.beginDelayedTransition(emailIconContainer);
                                            emailIconText.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                            emailIcon.setImageResource(R.drawable.mailsend);
                                        }
                                    });
                                    emailIcon.startAnimation(scaleAnimation);


                                } else {

                                    String error = task.getException().getMessage();

                                    emailIconText.setText(error);
                                    emailIconText.setTextColor(Color.parseColor("#9f0000"));
                                    emailIcon.setImageResource(R.drawable.mailerror);
                                    TransitionManager.beginDelayedTransition(emailIconContainer);
                                    emailIconText.setVisibility(View.VISIBLE);
                                }
                                progressBar.setVisibility(View.GONE);
                                resetPasswordBtn.setEnabled(true);
                                resetPasswordBtn.setTextColor(Color.parseColor("#ffffff"));
                            }
                        });
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });
    }

    private void checkInput() {
        if (TextUtils.isEmpty(registerMail.getText())) {
            resetPasswordBtn.setEnabled(false);
            resetPasswordBtn.setTextColor(Color.parseColor("#50ffffff"));
        } else {
            resetPasswordBtn.setEnabled(true);
            resetPasswordBtn.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.sidein_from_left,R.anim.sideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
