package com.example.shareinuapp.Fragment;

import android.app.ActivityOptions;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shareinuapp.R;
import com.example.shareinuapp.chat.MessageActivity;
import com.example.shareinuapp.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PeopleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.peoplefragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());

//        // floatingaction버튼을 눌렀을 때 단체 채팅을 할 친구 목록이 들어있는 액티비티가 실행된다.
//        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.peoplefragment_floatingButton);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(view.getContext(),SelectFriendActivity.class));
//            }
//        });
        return view;
    }

    // recyclerview를 적용할 클래스를 만든다.
    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<UserModel> userModels;

        // 생성자가 필요하다.
        public PeopleFragmentRecyclerViewAdapter() {
            userModels = new ArrayList<>();

            // 내 uid가 있을 경우 list에서 제거한다 => 친구목록에서 나를 지우자
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            // 데이터베이스를 검색한다.
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {

                // 서버에서 넘어온 데이터
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // 데이터 모델이 바뀔 수 있기 때문에 clear를 넣어준다.
                    // clear하지 않으면 ex) 1 2 3 에서 4 가 들어올 경우 1 2 3 1 2 3 4 가 된다.
                    userModels.clear();
                    // 데이터가 쌓인다.
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                        UserModel userModel = snapshot.getValue(UserModel.class);

                        // usermodel의 id가 내 id일 경우 continue
                        if(userModel.uid.equals(myUid)){
                            continue;
                        }
                        userModels.add(userModel);
                    }
                    // 새로고침!!!!! 꼭 해야한다.
                    // 데이터가 쌓이고 나서 새로고침을 해야 친구목록이 뜬다.
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);


            return new CustomViewHolder(view);
        }

        // 이미지와 이름을 넣어주는 부분
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            Glide.with
                    (holder.itemView.getContext())
                    .load(userModels.get(position).profileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder)holder).imageView);
            ((CustomViewHolder)holder).textView.setText(userModels.get(position).userName);

            // 친구 목록창 item View을 누르면 채팅창 activity으로 이동하도록 한다.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getView().getContext(), MessageActivity.class);
                    // 사용자 목록에서 채팅할 대상을 찾는다.
                    // uid는 userModels안에 들어있다.
                    intent.putExtra("destinationUid",userModels.get(position).uid);

                    // 화면 전환을 할 때 애니메이션 효과를 넣어준다.
                    ActivityOptions activityOptions = null;
                    activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(),R.anim.fromright, R.anim.toleft);
                    startActivity(intent, activityOptions.toBundle());
                }
            });


        }

        // 친구 목록이 쌓인 것
        @Override
        public int getItemCount() {
            return userModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            // 친구 목록을 보여주는 아이템들
            public ImageView imageView;
            public TextView textView;
            public TextView textView_comment;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.frienditem_imageview);
                textView = (TextView) view.findViewById(R.id.frienditem_textview);
           //     textView_comment = (TextView)view.findViewById(R.id.frienditem_textview_comment);
            }
        }
    }

}