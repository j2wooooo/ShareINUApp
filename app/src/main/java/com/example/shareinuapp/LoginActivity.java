package com.example.shareinuapp;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shareinuapp.Fragment.FifthFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class LoginActivity extends AppCompatActivity implements  FifthFragment.OnFragmentInteractionListener {

    private EditText id;
    private EditText password;

    private Button login;
    private Button signup;

    private FifthFragment fifthFragment;


    // 원격으로 테마를 받기 위해서 필요하다.
    // private FirebaseRemoteConfig firebaseRemoteConfig;

    private FirebaseAuth firebaseAuth;

    // 로그인 인터페이스 리스너


    // 리스너를 액티비티에 붙여주어야 로그인을 확인할 수 있다.


    // onstart()와 onstop()을 이용한다.
    // 로그인이 되었는지 확인해줄 수 있음
    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            // 로그인이 되었을 때
            if(user != null)
            {

                // fifthFragment = new FifthFragment();
                // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //로그인
                LoginKey.login = true;
                //로그인이 되었을 때 새로운 액티비티가 실행된다.
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                startActivity(intent);
                // 자신을 받아주면서 MainAcitivity를 연다.
                // transaction.replace(R.id.fifth_container, fifthFragment).commit();
                 finish();

            //}else{
                //로그아웃
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fifthFragment = new FifthFragment();


        // splash때와 마찬가지로 remoteconfig와 연동되는 변수를 받아온다.
        // firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // auth를 받아온다. <- login 위함
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signOut();

        // splash_background에 res->values->string에 있는 id 값을 넣어준다.
        // id에는 splash_background라는 string이 들어있는데 이것은 firebase의 remoteconfig에서 변경가능한 값(색상)이다.
        // String splash_background = firebaseRemoteConfig.getString(getString((R.string.rc_color)));

        // 밑의 코드는 롤리팝부터 적용 가능하다. 그 이하면 추가로 코드 써 주어야함.
        // 아이디 패스워드 밑에 있는 bar의 색을 적용시켜준다.
        // getWindow().setStatusBarColor(Color.parseColor(splash_background));

        // login과 password에 해당하는 text를 불러온다.
        id = (EditText)findViewById(R.id.loginActivity_edittext_id);
        password = (EditText)findViewById(R.id.loginActivity_edittext_password);


        // login과 signup에 해당하는 버튼을 불러온다.
        login = (Button)findViewById(R.id.loginActivity_button_login);
        signup = (Button)findViewById(R.id.loginActivity_button_signup);

        // 해당하는 버튼의 색깔을 적용시킨다.
       // login.setBackgroundColor(Color.parseColor(splash_background));
       // signup.setBackgroundColor(Color.parseColor(splash_background));

        // login button을 click했을 때 loginEvent method가 호출된다.
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loginEvent();
            }
        });

        //signup button을 click했을 때 signupActivity가 호출된다.
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

    }

    // 이 부분은 로그인이 정상적으로 됐는지 안됐는지 확인해주는 부분이다.
    // 로그인이 되어서 다음 화면으로 넘어가는 것은 authstatelistener로 진행한다.

    //이 이벤트는 로그인 버튼에 달아준다.
    void loginEvent(){
        firebaseAuth.signInWithEmailAndPassword(id.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 로그인이 실패했을 때 작동하는 부분
                        if(!task.isSuccessful()){
                            // 어떤 에러로 로그인에 실패했는지 띄워주는 부분
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    // listner부착
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
