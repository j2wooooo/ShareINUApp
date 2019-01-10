package com.example.shareinuapp;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;

import com.example.shareinuapp.Fragment.FifthFragment;
import com.example.shareinuapp.Fragment.FirstFragment;
import com.example.shareinuapp.Fragment.FourthFragment;
import com.example.shareinuapp.Fragment.PeopleFragment;
import com.example.shareinuapp.Fragment.SecondFragment;
import com.example.shareinuapp.Fragment.ThirdFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements FirstFragment.OnFragmentInteractionListener,
        SecondFragment.OnFragmentInteractionListener, ThirdFragment.OnFragmentInteractionListener,
        FourthFragment.OnFragmentInteractionListener, FifthFragment.OnFragmentInteractionListener{

    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private FourthFragment fourthFragment;
    private FifthFragment fifthFragment;
    private PeopleFragment PeopleFragment;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 지금 보여주는 view
//
//        final ActionBar actionBar = getActionBar();
//        actionBar.setCustomView(R.layout.layout_actionbar);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);

        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        fourthFragment = new FourthFragment();
        fifthFragment = new FifthFragment();

        PeopleFragment = new PeopleFragment();

        if(LoginKey.login) {
            Log.d("lodg","true");
            initFragment2();  // firstfragment를 초기화면에 나타낸다.
        }else {
            Log.d("lodg","false");
            initFragment(); // 로그인 후 나타나는 메인엑티비티의 초기화면은 mypage이다.
        }
        // bottomBar 하단바의 핸들러 생성
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        // 하단바의 tab에 있는 button의 listener
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            // 메인액티비티에서 하단 tab버튼이 눌렸을 떄 fragment를 대체시킨다.
            public void onTabSelected(@IdRes int tabId) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(LoginKey.login && flag == 1){
                    tabId = R.id.tab_mypage;
                }


                if (tabId == R.id.tab_home) { // tab_home 이 눌리면
                    transaction.replace(R.id.contentContainer, firstFragment).commit(); // first_fragment로 화면 대체
                    Log.d("logd","home tab"); // log에 home tab이 찍힘
                }else if(tabId == R.id.tab_search) {
                    transaction.replace(R.id.contentContainer, secondFragment).commit();
                    Log.d("logd","search tab");
                }else if(tabId == R.id.tab_post){
                   // transaction.replace(R.id.contentContainer, thirdFragment).commit();
                   // Log.d("logd","post tab");
                    startActivity(new Intent(MainActivity.this,PostActivity.class));
                    //finish();
                }else if(tabId == R.id.tab_message){
                    if(LoginKey.login) {
                        transaction.replace(R.id.contentContainer, fourthFragment).commit();
                    }
                    else{
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        //finish();

                    }
                    //transaction.replace(R.id.contentContainer, fourthFragment).commit();
                    Log.d("logd","message tab");
                }else if(tabId == R.id.tab_mypage){

                    if(LoginKey.login) {
                        transaction.replace(R.id.contentContainer, fifthFragment).commit();
                    }
                    else{
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        //finish();

//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        transaction.replace(R.id.contentContainer, fifthFragment).commit();
                    }
                    Log.d("logd","mypage tab");
                    flag = 0;
                }
            }
        });

        if(LoginKey.login) {
            passPushTokenToServer();
            Log.d("logd","pushtoken");
        }
        //initView();
    }

    // 화면 시작시 초기화 부분
    public void initFragment(){
        Log.d("lodg","firstfragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentContainer, firstFragment); // 첫번째 fragment로 처음에 초기화 시켜준다.
        transaction.addToBackStack(null);
        transaction.commit();
        Log.d("lodg","firstfragment");
    }
    public void initFragment2(){
        Log.d("lodg","fifthfragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentContainer, fifthFragment); // 5번째 fragment로 처음에 초기화 시켜준다.
        transaction.addToBackStack(null);
        transaction.commit();
        flag = 1;
        Log.d("lodg","fifthfragment");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void messageFromParentFragment(Uri uri) {
        Log.i("TAG", "received communication from parent fragment");
    }

    //@Override
    public void messageFromChildFragment(Uri uri) {
        Log.i("TAG", "received communication from child fragment");
    }


    void passPushTokenToServer(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken",token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);


    }
}
