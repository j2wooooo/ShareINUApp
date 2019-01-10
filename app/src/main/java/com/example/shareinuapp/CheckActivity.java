package com.example.shareinuapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.shareinuapp.chat.MessageActivity;

public class CheckActivity extends Activity implements CompoundButton.OnCheckedChangeListener
{
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check);

        cb1 = (CheckBox)findViewById(R.id.checkBox1);
        cb2 = (CheckBox)findViewById(R.id.checkBox2);
        cb3 = (CheckBox)findViewById(R.id.checkBox3);

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);

        Button button = (Button)findViewById(R.id.activity_check_button);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("tag","click");
                Intent intent = null;

                intent = new Intent(v.getContext(), MessageActivity.class);
                intent.putExtra("destinationUid", "sSMruTqq4FNdkVjI7in4RbdO3zj1");


                // 화면이 오른쪽에서 왼쪽으로 밀리면서 전환된다.
                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.fromright, R.anim.toleft);
                    startActivity(intent, activityOptions.toBundle());
                }
                // startActivity(new Intent(CheckActivity.this,MessageActivity.class));
                finish();

            } // end onClick
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

        if ((cb1.isChecked()) && (cb2.isChecked()) && (cb3.isChecked())) {
            Log.d("tag", "enabld");

            Button button = (Button)findViewById(R.id.activity_check_button);
            button.setEnabled(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

//    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//        if ((cb1.isChecked()) && (cb2.isChecked()) && (cb3.isChecked()) && (cb4.isChecked())) {
//            Log.d("tag", "enabld");
//
//            Button b1 = (Button)findViewById(R.id.activity_check_button);
//            b1.setEnabled(true);
//        }
//    }
}
