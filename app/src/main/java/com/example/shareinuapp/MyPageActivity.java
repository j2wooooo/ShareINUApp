package com.example.shareinuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shareinuapp.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyPageActivity extends AppCompatActivity {

    private TextView name;
    private TextView department;
    private ImageView face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        name = (TextView)findViewById(R.id.name);
        department = (TextView)findViewById(R.id.department);
        face = (ImageView)findViewById(R.id.face);

//        ImageButton profilebtn;
//        profilebtn=(ImageButton)findViewById(R.id.profileBtn);

        // 내가 추가한 부분 mypage에 내 계정의 학번, 이름, 사진 넣기

        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    UserModel userModel = snapshot.getValue(UserModel.class);

                    // usermodel의 id가 내 id일 경우
                    if (userModel.uid.equals(myUid)) {
                        name.setText(userModel.userName);
                        department.setText(userModel.userdep);
                        //userModel.userNum;
                        Glide.with(MyPageActivity.this)
                                .load(userModel.profileImageUrl) // 가져올 이미지
                                .apply(new RequestOptions().circleCrop()) // 이미지 모양 동그라미로 적용
                                .into(face); // 커스텀뷰홀더의 이미지뷰에 넣는다.


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        profilebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent ReadImgIntent = new Intent(Intent.ACTION_PICK);
//                ReadImgIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//                ReadImgIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                //startActivityForResult(ReadImgIntent, GET_PICTURE_URI);
//
//            }
//        });
    }
}



