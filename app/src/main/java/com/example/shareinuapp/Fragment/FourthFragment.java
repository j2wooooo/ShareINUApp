package com.example.shareinuapp.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shareinuapp.R;
import com.example.shareinuapp.chat.MessageActivity;
import com.example.shareinuapp.model.ChatModel;
import com.example.shareinuapp.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class FourthFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public FourthFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);

        RecyclerView recyclerView  = (RecyclerView) view.findViewById(R.id.chatfragment_recyclerview);
        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        // 리스트로 보여주겠다.
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        return view;
    }


    // 어댑터
    class ChatRecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<ChatModel> chatModels = new ArrayList<>();
        private List<String> keys = new ArrayList<>();
        private String uid;
        // 대화상대방들에 대한 데이터가 담겨있다.
        private ArrayList<String> destinationUsers = new ArrayList<>();

        // 채팅목록을 가져온다.
        public ChatRecyclerViewAdapter() {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // 채팅룸에 대한 정보를 가져온다. orderByChild("users/"+uid) 내가 소속된 방을 가져올 수 있다.
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // 데이터를 쌓기 위해 클리어를 해준다.
                    chatModels.clear();
                    for (DataSnapshot item :dataSnapshot.getChildren()){
                        chatModels.add(item.getValue(ChatModel.class));
                        keys.add(item.getKey());
                    }
                    // 새로고침
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }

        // 보여주는 부분
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);


            return new CustomViewHolder(view);
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


            final CustomViewHolder customViewHolder = (CustomViewHolder) holder;
            //대화 상대방
            String destinationUid = null;

            // 일일히 챗방에 있는 유저를 체크
            for (String user : chatModels.get(position).users.keySet()) {
                // 유저 중에 내가 아닌 사람을 뽑아온다.
                if (!user.equals(uid)) {
                    destinationUid = user;
                    destinationUsers.add(destinationUid);
                }
            }
            // 상대방에 대한 정보를 가져온다.
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // 유저모델에 이미지와 주소가 있다
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    Glide.with(customViewHolder.itemView.getContext())
                            .load(userModel.profileImageUrl) // 가져올 이미지
                            .apply(new RequestOptions().circleCrop()) // 이미지 모양 동그라미로 적용
                            .into(customViewHolder.imageView); // 커스텀뷰홀더의 이미지뷰에 넣는다.

                    // 상대방의 이름으로 채팅방 이름을 결정한다
                    customViewHolder.textView_title.setText(userModel.userName);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            // 채팅방 목록에
            // 마지막 메세지를 보여줄 수 있도록 한다.
            //메시지를 내림 차순으로 정렬 후 마지막 메세지의 키값을 가져옴
            Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            // 채팅에 대한 내용을 넣어준다.
            commentMap.putAll(chatModels.get(position).comments);

            if (commentMap.keySet().toArray().length > 0) {
                // 마지막 채팅 내용을 가져와야 한다.
                String lastMessageKey = (String) commentMap.keySet().toArray()[0];
                customViewHolder.textView_last_message.setText(chatModels.get(position).comments.get(lastMessageKey).message);


            }

            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;

                        intent = new Intent(v.getContext(), MessageActivity.class);
                        intent.putExtra("destinationUid", destinationUsers.get(position));


                    // 화면이 오른쪽에서 왼쪽으로 밀리면서 전환된다.
                    ActivityOptions activityOptions = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.fromright, R.anim.toleft);
                        startActivity(intent, activityOptions.toBundle());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        // 채팅 목록을 올린다.
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView_title;
            public TextView textView_last_message;
            public CustomViewHolder(View view) {
                super(view);

                imageView = (ImageView) view.findViewById(R.id.chatitem_imageview);
                textView_title = (TextView)view.findViewById(R.id.chatitem_textview_title);
                textView_last_message = (TextView)view.findViewById(R.id.chatitem_textview_lastMessage);
            }
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}