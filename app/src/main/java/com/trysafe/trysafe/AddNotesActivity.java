package com.trysafe.trysafe;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class AddNotesActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.trysafe.trysafe.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.trysafe.trysafe.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.trysafe.trysafe.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.trysafe.trysafe.EXTRA_PRIORITY";

    private ImageView closeImage;
    private TextView addNoteBtn, titleToolbar;
    private EditText mTitle,mDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = findViewById(R.id.appBar);


        titleToolbar = findViewById(R.id.title_tool);


        Fade fade = new Fade();
        View decore = getWindow().getDecorView();
        fade.excludeTarget(decore.findViewById(R.id.action_bar_container),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);


        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);


        mTitle = findViewById(R.id.note_title);
        mDescription = findViewById(R.id.note_des);


        closeImage = findViewById(R.id.imageClose);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.sidein_from_left, R.anim.sideout_from_right);
            }
        });

        addNoteBtn = findViewById(R.id.add_notes_btn);

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });




        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
           titleToolbar.setText("Edit Note");
            mTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            mDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        } else {
            titleToolbar.setText("Add Note");
        }
    }

    private void saveNote() {
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();


        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);


        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
        overridePendingTransition(R.anim.sidein_from_left, R.anim.sideout_from_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.sidein_from_left, R.anim.sideout_from_right);
    }
}

