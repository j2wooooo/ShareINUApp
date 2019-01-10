package com.example.shareinuapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PostActivity extends AppCompatActivity {


    ImageButton tapImage;
    ImageButton nanumi;
    ImageButton bilimi;
    ImageButton all;
    ImageButton electronic;
    ImageButton room;
    ImageButton lifegoods;
    ImageButton fashion;
    ImageButton done;
    boolean nanumiclicked=false;
    boolean bilimiclicked=false;
    boolean allclicked=false;
    boolean electronicclicked=false;
    boolean roomclicked=false;
    boolean lifegoodsclicked=false;
    boolean fashionclicked=false;
    boolean gesigleclicked=false;
    final Intent album_intent=new Intent(Intent.ACTION_PICK);

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postjj);

        done=(ImageButton)findViewById(R.id.done_btn);
        tapImage=(ImageButton)findViewById(R.id.imagetab_btn);
        nanumi=(ImageButton)findViewById(R.id.nanumi_btn);
        bilimi=(ImageButton)findViewById(R.id.bilimi_btn);
        all=(ImageButton)findViewById(R.id.all_btn);
        electronic=(ImageButton)findViewById(R.id.electronic_btn);
        room=(ImageButton)findViewById(R.id.room_btn);
        lifegoods=(ImageButton)findViewById(R.id.lifegoods_btn);
        fashion=(ImageButton)findViewById(R.id.fashion_btn);

        done.setOnClickListener(new View.OnClickListener() // 불러오기 버튼에 대한 이벤트
        {
            @Override
            public void onClick(View v)
            {   //  마지막 done (체크) 버튼을 누르면.

            }
        });
        fashion.setOnClickListener(new View.OnClickListener() // 불러오기 버튼에 대한 이벤트
        {
            @Override
            public void onClick(View v)
            {   // 레이아웃에 있는 불러오기 버튼을 누르면
                if(fashionclicked==true)
                {
                    fashion.setImageResource(R.drawable.fashion_off);
                    fashionclicked=false;
                }
                else if(fashionclicked==false)
                {
                    fashion.setImageResource(R.drawable.fashion_on);
                    fashionclicked=true;
                }
            }
        });
        lifegoods.setOnClickListener(new View.OnClickListener() // 불러오기 버튼에 대한 이벤트
        {
            @Override
            public void onClick(View v)
            {   // 레이아웃에 있는 불러오기 버튼을 누르면
                if(lifegoodsclicked==true)
                {
                    lifegoods.setImageResource(R.drawable.life_goods_off);
                    lifegoodsclicked=false;
                }
                else if(lifegoodsclicked==false)
                {
                    lifegoods.setImageResource(R.drawable.life_goods_on);
                    lifegoodsclicked=true;
                }
            }
        });
        room.setOnClickListener(new View.OnClickListener() // 불러오기 버튼에 대한 이벤트
        {
            @Override
            public void onClick(View v)
            {   // 레이아웃에 있는 불러오기 버튼을 누르면
                if(roomclicked==true)
                {
                    room.setImageResource(R.drawable.room_off);
                    roomclicked=false;
                }
                else if(roomclicked==false)
                {
                    room.setImageResource(R.drawable.room_on);
                    roomclicked=true;
                }
            }
        });
        all.setOnClickListener(new View.OnClickListener() // 불러오기 버튼에 대한 이벤트
        {
            @Override
            public void onClick(View v)
            {   // 레이아웃에 있는 불러오기 버튼을 누르면
                if(allclicked==true)
                {
                    all.setImageResource(R.drawable.all_off);
                    allclicked=false;
                }
                else if(allclicked==false)
                {
                    all.setImageResource(R.drawable.all_on);
                    allclicked=true;
                }
            }
        });
        electronic.setOnClickListener(new View.OnClickListener() // 불러오기 버튼에 대한 이벤트
        {
            @Override
            public void onClick(View v)
            {   // 레이아웃에 있는 불러오기 버튼을 누르면
                if(electronicclicked==true)
                {
                    electronic.setImageResource(R.drawable.el_goods_off);
                    electronicclicked=false;
                }
                else if(electronicclicked==false)
                {
                    electronic.setImageResource(R.drawable.el_goods_on);
                    electronicclicked=true;
                }
            }
        });
        nanumi.setOnClickListener(new View.OnClickListener() // 불러오기 버튼에 대한 이벤트
        {
            @Override
            public void onClick(View v)
            {   // 레이아웃에 있는 불러오기 버튼을 누르면
                if(gesigleclicked==false ) // 게시글이 하나도 클릭되어있지 않았을 때.
                {
                    if (nanumiclicked == true)
                    {
                        nanumi.setImageResource(R.drawable.nanumi_off);
                        nanumiclicked = false;
                        gesigleclicked=false;
                    }
                    else if (nanumiclicked == false)
                    {
                        nanumi.setImageResource(R.drawable.nanumi_on);
                        nanumiclicked = true;
                        gesigleclicked=true;
                    }
                }
                else if(gesigleclicked==true && nanumiclicked==true)// 게시글이 이미 하나 클릭되어있고, 나누미 클릭이 on일때
                {
                    if (nanumiclicked == true)
                    {
                        nanumi.setImageResource(R.drawable.nanumi_off);
                        nanumiclicked = false;
                        gesigleclicked=false;
                    }
                }
            }
        });
        bilimi.setOnClickListener(new View.OnClickListener() // 불러오기 버튼에 대한 이벤트
        {
            @Override
            public void onClick(View v)
            {   // 레이아웃에 있는 불러오기 버튼을 누르면
                if(gesigleclicked==false )
                {
                    if (bilimiclicked == true)
                    {
                        bilimi.setImageResource(R.drawable.bilimi_off);
                        bilimiclicked = false;
                        gesigleclicked=false;
                    }
                    else if (bilimiclicked == false)
                    {
                        bilimi.setImageResource(R.drawable.bilimi_on);
                        bilimiclicked = true;
                        gesigleclicked=true;
                    }
                }
                else if(gesigleclicked==true && bilimiclicked==true)
                {
                    if (bilimiclicked == true)
                    {
                        bilimi.setImageResource(R.drawable.bilimi_off);
                        bilimiclicked = false;
                        gesigleclicked=false;
                    }
                }
            }
        });
        tapImage.setOnClickListener(new View.OnClickListener() // 불러오기 버튼에 대한 이벤트
        {
            @Override
            public void onClick(View v)
            {   // 레이아웃에 있는 불러오기 버튼을 누르면
                album_intent.setType(MediaStore.Images.Media.CONTENT_TYPE);  // 앨범 목록에있는 사진을 클릭하는 타입을 만든다
                startActivityForResult(album_intent, 2); // 앨범목록으로 이동.
            }
        });

        /*
        ImageButton share_btn = (ImageButton) findViewById(R.id.activity_post_share_button);
        share_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this,CheckActivity.class));
               // startActivity(new Intent(PostActivity.this,ChatActivity.class));
            }
        });
        */
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent album_intent)
    {
        super.onActivityResult(requestCode, resultCode, album_intent);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==2)
            {
                tapImage.setImageURI(album_intent.getData());
            }
        }
    }
}