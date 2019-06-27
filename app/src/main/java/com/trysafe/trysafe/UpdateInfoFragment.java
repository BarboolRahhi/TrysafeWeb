package com.trysafe.trysafe;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfoFragment extends Fragment {


    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    private CircleImageView circleImageView;
    private Button removeBtn;
    private Button changeBtn;
    private Button updateBtn;

    private EditText dialogEditText;
    private Button dialogBtn;

    private Dialog passwordDialog;

    private ProgressDialog progressDialog;
    TextInputLayout userName, userEmail, userDes;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private String name, email, des, image;

    private Uri imageUri;
    private boolean updatePhoto = false;

    static String checkEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);


        circleImageView = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.title);
        userEmail = view.findViewById(R.id.email);
        userDes = view.findViewById(R.id.description);
        removeBtn = view.findViewById(R.id.remove_btn);
        changeBtn = view.findViewById(R.id.image_change_btn);
        updateBtn = view.findViewById(R.id.updateProfileBtn);

        name = getArguments().getString("fullname");
        email = getArguments().getString("email");
        des = getArguments().getString("des");
        image = getArguments().getString("image");


        //loading dialog//

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        //password dialog//

        passwordDialog = new Dialog(getContext());
        passwordDialog.setContentView(R.layout.password_confirmation_dialog);
        passwordDialog.setCancelable(false);
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        dialogEditText = passwordDialog.findViewById(R.id.password);
        dialogBtn = passwordDialog.findViewById(R.id.done_btn);

        Glide.with(getContext()).load(image).placeholder(R.drawable.defaultavater).into(circleImageView);
        userName.getEditText().setText(name);
        userEmail.getEditText().setText(email);
        userDes.getEditText().setText(des);


        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);

                    } else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);
                }

                updateBtn.setEnabled(true);
                updateBtn.setTextColor(Color.parseColor("#54596e"));
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = null;
                updatePhoto = true;
                Glide.with(getContext()).load(R.drawable.defaultavater).into(circleImageView);
                updateBtn.setEnabled(true);
                updateBtn.setTextColor(Color.parseColor("#54596e"));
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });


        userEmail.getEditText().addTextChangedListener(new TextWatcher() {
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
        userName.getEditText().addTextChangedListener(new TextWatcher() {
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

        userDes.getEditText().addTextChangedListener(new TextWatcher() {
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
        if (!TextUtils.isEmpty(userEmail.getEditText().getText())) {
            if (!TextUtils.isEmpty(userName.getEditText().getText())) {
                if (!TextUtils.isEmpty(userDes.getEditText().getText())) {
                    updateBtn.setEnabled(true);
                    updateBtn.setTextColor(Color.parseColor("#54596e"));
                } else {
                    updateBtn.setEnabled(false);
                    updateBtn.setTextColor(Color.parseColor("#5054596e"));
                }
            } else {
                updateBtn.setEnabled(false);
                updateBtn.setTextColor(Color.parseColor("#5054596e"));
            }
        } else {
            updateBtn.setEnabled(false);
            updateBtn.setTextColor(Color.parseColor("#5054596e"));
        }
    }

    private void checkEmailAndPassword() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (userEmail.getEditText().getText().toString().matches(emailPattern)) {


            FirebaseFirestore.getInstance().collection("USERS").document(user.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        String checkEmail = task.getResult().getString("email");

                        if (userEmail.getEditText().getText().toString().toLowerCase().trim().equals(checkEmail.toLowerCase().trim())) {//do not update email
                            progressDialog.show();
                            userUpdatePhoto(user);
                            Log.d(TAG, "check" + "emailPattern Called");
                        } else { // email update
                            Log.d(TAG, "check" + "password");
                            passwordDialog.show();
                            dialogBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    progressDialog.show();
                                    String password = dialogEditText.getText().toString();
                                    passwordDialog.dismiss();

                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(email, password);

                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        user.updateEmail(userEmail.getEditText().getText().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            userUpdatePhoto(user);

                                                                        } else {
                                                                            progressDialog.dismiss();
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        progressDialog.dismiss();
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }
                            });
                        }


                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } else {
            userEmail.setError("Email doesn't matched");
        }
    }


    private void userUpdatePhoto(final FirebaseUser user) {

        ///update photo///

        if (updatePhoto) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("profile/" + user.getUid() + ".jpg");
            if (imageUri != null) {

                Glide.with(getContext()).asBitmap().load(imageUri).placeholder(R.drawable.defaultavater).circleCrop().into(new ImageViewTarget<Bitmap>(circleImageView) {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {

                                                imageUri = task.getResult();
                                                String url = task.getResult().toString();
                                                Glide.with(getContext()).load(url).placeholder(R.drawable.defaultavater).into(circleImageView);

                                                Map<String, Object> updateData = new HashMap<>();
                                                updateData.put("email", userEmail.getEditText().getText().toString());
                                                updateData.put("fullname", userName.getEditText().getText().toString());
                                                updateData.put("description", userDes.getEditText().getText().toString());
                                                updateData.put("image", url);

                                                updateField(user, updateData);

                                                email = userEmail.getEditText().getText().toString();

                                            } else {
                                                progressDialog.dismiss();
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        circleImageView.setImageResource(R.drawable.defaultavater);
                    }
                });

            } else {//remove photo
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("email", userEmail.getEditText().getText().toString());
                            updateData.put("fullname", userName.getEditText().getText().toString());
                            updateData.put("description", userDes.getEditText().getText().toString());
                            updateData.put("image", " ");

                            updateField(user, updateData);
                        } else {
                            progressDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } else {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("fullname", userName.getEditText().getText().toString());
            updateData.put("email", userEmail.getEditText().getText().toString());
            updateData.put("description", userDes.getEditText().getText().toString());
            updateField(user, updateData);
        }
    }

    private void updateField(FirebaseUser user, Map<String, Object> updateData) {

        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid()).update(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {

                Uri uri = data.getData();

                CropImage.activity(uri)
                        .setAspectRatio(1, 1)
                        .start(getContext(), this);
//                if (data != null) {
//                    imageUri = data.getData();
//                    updatePhoto = true;
//                    Glide.with(getContext()).load(imageUri).into(circleImageView);
//                } else {
//                    Toast.makeText(getActivity(), "Image not found!", Toast.LENGTH_SHORT).show();
//                }
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                imageUri = result.getUri();
                updatePhoto = true;

                Glide.with(getContext()).load(resultUri).into(circleImageView);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
