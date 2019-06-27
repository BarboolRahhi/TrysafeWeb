package com.trysafe.trysafe;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private UpdateInfoFragment updateInfoFragment;
    private UpdatePasswordFragment updatePasswordFragment;

    private String name;
    private String email;
    private String des;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        tabLayout = findViewById(R.id.tab_layout);
        frameLayout = findViewById(R.id.frame_layout);

        updateInfoFragment = new UpdateInfoFragment();
        updatePasswordFragment = new UpdatePasswordFragment();

        name = getIntent().getStringExtra("fullname");
        email = getIntent().getStringExtra("email");
        des = getIntent().getStringExtra("des");
        image = getIntent().getStringExtra("image");

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    setFragment(updateInfoFragment,true);
                }else if (tab.getPosition() == 1){
                    setFragment(updatePasswordFragment,false );
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.getTabAt(0).select();
        setFragment(updateInfoFragment,true);

    }

    private void setFragment(Fragment fragment,boolean setBundle){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (setBundle){
            Bundle bundle = new Bundle();
            bundle.putString("fullname",name);
            bundle.putString("email",email);
            bundle.putString("des",des);
            bundle.putString("image",image);
            fragment.setArguments(bundle);
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("email",email);
            fragment.setArguments(bundle);
        }

        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.sidein_from_left, R.anim.sideout_from_right);
    }
}
