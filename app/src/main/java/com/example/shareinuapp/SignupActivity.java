package com.example.shareinuapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shareinuapp.Fragment.FifthFragment;
import com.example.shareinuapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
// import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class SignupActivity extends AppCompatActivity implements FifthFragment.OnFragmentInteractionListener{


    private static final int PICK_FROM_ALBUM = 10;
    private EditText email;
    private EditText name;
    private EditText user_num;
    private EditText password;
    private EditText user_dep;
    private Button signup;
 //   private String splash_background;
    // 프로필 작업
    private ImageView profile;
    private Uri imageUri;

    // firebase를 이용할 떄 항상 중요한 것은 bradle에 dependency되는 api들의 version이다.
    // 서로 version이 맞는 지 확인하자!! error가 난다면 이곳에서 났을 확률이 99%이다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // splash_background에 res->values->string에 있는 id 값을 넣어준다.
        // id에는 splash_background라는 string이 들어있는데 이것은 firebase의 remoteconfig에서 변경가능한 값(색상)이다.
        // splash_background = mFirebaseRemoteConfig.getString(getString((R.string.rc_color)));

        // 밑의 코드는 롤리팝부터 적용 가능하다. 그 이하면 추가로 코드 써 주어야함.
        // 아이디 패스워드 밑에 있는 bar의 색을 적용시켜준다.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //        getWindow().setStatusBarColor(Color.parseColor(splash_background));
//        }


        // 회원가입을 하기 위해서는 firebase 권한 연동이 되어야 한다. 때문에
        // tool -> firebase -> authentication -> 2) add firebase authentication to your app을 해준다.
        // 권한을 얻는 데에 필요한 firebase api가 추가된다.
        profile = (ImageView)findViewById(R.id.signupActivity_imageview_profile);
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            // 프로필 클릭 시 앨범에 접근
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        // 회원가입에 필요한 email, name, password와 signup 버튼을 불러온다.
        email = (EditText) findViewById(R.id.signupActivity_edittext_email);
        name = (EditText) findViewById(R.id.signupActivity_edittext_name);
        user_num = (EditText) findViewById(R.id.signupActivity_edittext_user_num);
        user_dep = (EditText)findViewById(R.id.signupActivity_edittext_user_dep);
        password = (EditText) findViewById(R.id.signupActivity_edittext_password);
        signup = (Button) findViewById(R.id.signupActivity_button_signup);
//        signup.setBackgroundColor(Color.parseColor(splash_background));

        // signup button을 누르면 작성된 이메일, 비밀번호로 회원가입이 된다.
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // text가 비워져 있는 란이 있다면 return한다. 회원가입 xx
                // password가 6자리 미만일 때에는 에러가 난다.
                if (email.getText().toString() == null || name.getText().toString() == null
                        || password.getText().toString() == null || user_num.getText().toString() == null
                        || user_dep.getText().toString() == null || imageUri == null){
                    return; }

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {

                            // 회원가입이 완료되면 => firebase에 권한이 올라가고 나서 complete으로 넘어온다
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // 사용자마다 고유의 uid값이 나오기 때문에 중복될 일이 없다.
                                final String uid = task.getResult().getUser().getUid();

                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name.getText().toString()).build();

                                task.getResult().getUser().updateProfile(userProfileChangeRequest);

                                FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        // 파일이 저장되면 저장된 경로를 task로 보내준다
                                        String imageUrl = task.getResult().getDownloadUrl().toString();

                                        // 새로운 유저모델을 만들어 사용자의 uid으로 ui를 저장한다.
                                        UserModel userModel = new UserModel();
                                        userModel.userName = name.getText().toString();
                                        userModel.userNum = user_num.getText().toString();
                                        userModel.userdep = user_dep.getText().toString();
                                        userModel.profileImageUrl = imageUrl;
                                        // 회원가입시 uid를 담아서 회원가입을 한다.
                                        // 내가 원하는 사람과 채팅을 할 때 이 uid를 이용하여 채팅할 대상을 찾는다.
                                        userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {

                                            // 회원가입에 성공하면 창을 지운다.
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                SignupActivity.this.finish();
                                            }
                                        });

                                    }
                                });

                            }
                        });
              //  startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
    }

    // 앨범의 사진 선택하여 프로필 사진으로 가져온다, 이미지의 경로를 저장한다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_FROM_ALBUM && resultCode ==RESULT_OK){
            profile.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            imageUri = data.getData();// 이미지 경로 원본
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
