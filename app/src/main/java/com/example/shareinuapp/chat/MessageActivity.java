package com.example.shareinuapp.chat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shareinuapp.CompleteActivity;
import com.example.shareinuapp.LockerActivity;
import com.example.shareinuapp.R;
import com.example.shareinuapp.model.ChatModel;
import com.example.shareinuapp.model.NotificationModel;
import com.example.shareinuapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

// 메세지창 액티비티
public class MessageActivity extends AppCompatActivity {

    private String destinationUid; // 대화 상대방의 이름
    private Button button; // 채팅방 맨 아래 오른쪽에 채팅 보내기 버튼
    private EditText editText; // 채팅방 맨 아래에 있는 채팅 텍스트창
    private String uid; // 접속한 계정 주인의 이름
    private String chatRoomUid; // // 채팅방의 이름

    private RecyclerView recyclerView; // 메세지 창
    private TextView nameeditText;

    private UserModel destinationUserModel; // 상대방 계정 주인의 유저모델
    private DatabaseReference databaseReference; // 참조할 데이터베이스
    private ValueEventListener valueEventListener;

    boolean visible = false;
 //   int peopleCount = 0;

    private RelativeLayout relativeLayout;

    private Button button_plus;

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        // 자신의 uid를 집어넣는다.
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 채팅을 요구하는 아이디 = 단말기에 로그인된 UID
        // 상대방의 uid를 집어넣는다.
        destinationUid = getIntent().getStringExtra("destinationUid"); // 채팅을 당하는 아이디
        // 메세지창화면 밑에 채팅 보내기 버튼
        button = (Button) findViewById(R.id.messageActivity_button);
        // 메세지창화면 밑에 채팅칠 텍스트창
        editText = (EditText) findViewById(R.id.messageActivity_editText);
        nameeditText = (TextView)findViewById(R.id.messageActivity_name);
        relativeLayout = (RelativeLayout)findViewById(R.id.messageActivity_relativelayout3);

        // 채팅이 보이는 리사이클러뷰
        recyclerView = (RecyclerView) findViewById(R.id.messageActivity_recyclerview);

        FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {

            // 메시지와 유저의 정보 - 이름을 동시에 띄워주기 위해 유저 정보 안에 메시지를 불러오는 메소드를 넣는다.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 유저에 대한 정보가 담긴다.
                //  userModel = dataSnapshot.getValue(UserModel.class);

                // 상대방의유저의 데이터들을 불러온다.
                destinationUserModel = dataSnapshot.getValue(UserModel.class);
                nameeditText.setText(destinationUserModel.userName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // 버튼을 눌렀을 때 채팅방이 있다면 글자만 전송해준다.
        // 채팅방이 없다면 채팅방에 대한 데이터베이스 구조를 만들어 준다.
        button.setOnClickListener(new View.OnClickListener() {

            // 버튼을 누르면 대화방이 만들어진다.
            @Override
            public void onClick(View v) {

                // 만들어준 구조인 Chat Model을 부른다.
                ChatModel chatModel = new ChatModel();

                // 자신의 uid를 집어넣는다.
                chatModel.users.put(uid, true);
                // 상대방의 uid를 집어넣는다.
                chatModel.users.put(destinationUid, true);
//                chatModel.destinationUid = destinationUid;

                // 데이터베이스
                // push()를 넣어서 채팅방의 이름이 임의적으로 생성되도록 한다.

                if (chatRoomUid == null) { // 채팅방이 없다면
                    // 버튼을 연속적으로 눌렀을 때에, 서버에 전송이 되기 전에 여러 번 눌리면 전송이 일부 되지 않을 수 있다.
                    // 때문에 성공적으로 방이 만들어졌을 때에만 버튼을 enable 시켜주어야 한다.
                    // 한 번 요청이 들어오고 나서 완료되었다는 콜백을 받았을 때에만 버튼을 활성화시켜준다.

                    button.setEnabled(false);

                    // chatrooms라는 데이터베이스 child에 chatModel의 값을 넣는다.
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // checkChatRoom을 실행시키면 setEnable이 true상태가 되어 버튼이 눌릴 수 있는 상태가 된다.
                            checkChatRoom();
                        }
                    });
                    // 이곳에서 check를 하면 인터넷이 끊겼다가 다시 되는 경우, 메시지가 들어오지 않았는데 방이 생길 수 있다.
                    // checkChatRoom();
                } else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();

                    // 부른 메시지를 초기화시킨다.
                    // 초기화 시키지 않으면 계속 메시지가 남아있다.
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sendGcm();
                            editText.setText("");
                        }
                    });

                }


            }
        });
        checkChatRoom();
    }

    // postman 이랑 똑같이 만들기
    void sendGcm(){

        Gson gson = new Gson();

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = destinationUserModel.pushToken;
        notificationModel.notification.title = userName;
        notificationModel.notification.text = editText.getText().toString();


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type","application/json")
                .addHeader("Authorization","key=AIzaSyATPcOIWcRSuEWDEex5xnVYExfEArvmykU")
                .url("https://gcm-http.googleapis.com/gcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    // 상대방 유저와 채팅을 하던 채팅창이 있다면 새로운 채팅방을 만들지 않는다.
    // 채팅방이 없다면 새로운 채팅방을 만든다.
    void checkChatRoom() {

        // chatrooms에서 user의 아이디가 들어이는 채팅방을 받아온다.
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {

            // 나의 아이디와 상대방의 아이디가 있는 채팅방이 있다면 새로운 채팅방을 만들지 않는다.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    // ChatModel은 나와 채팅을 하는 사람의 구조이다. 데이터베이스에서 상대방 유저의 모델을 가져온다.
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    //받아온 데이터베이스의 chatModel의 유저아이디가 지금 선택한 상대방 유저아이디와 같다면 새로운 채팅방을 만들지 않는다.
                    if (chatModel.users.containsKey(destinationUid) && chatModel.users.size() == 2) {
                        // 원래 있는 채팅방의 아이디를 가지고 온다.
                        chatRoomUid = item.getKey(); // item.getKey()는 채팅방의 id

                        // 버튼을 enable 시킨다.
                        button.setEnabled(true);

                        // 메세지 엑티비티창을 갱신해준다.
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // inner classs로 recyclerview의 adapter를 만들어준다
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ChatModel.Comment> comments;
       // UserModel userModel;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            // valueEventListener의 메소드로 onDataChange와 OnCancelled가 있다.

            // 파이어베이스 데이터베이스의 users라는 child에 있는 상대방의 유저아이디를 가져온다.
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {

                // 메시지와 유저의 정보 - 이름을 동시에 띄워주기 위해 유저 정보 안에 메시지를 불러오는 메소드를 넣는다.
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // 유저에 대한 정보가 담긴다.
                  //  userModel = dataSnapshot.getValue(UserModel.class);

                    // 상대방의유저의 데이터들을 불러온다.
                    destinationUserModel = dataSnapshot.getValue(UserModel.class);

                    //유저의 메시지를 불러온다.
                    getMessageList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        // 이미 나눴던 대화가 있으면 원래 있던 채팅방을 불러온다.
        // 원래 있던 채팅방의
        // 나와 상대방이 지금까지 했던 메세지들을 불러온다.
        // 지금까지 했던 메시지들을 가져온다.
        void getMessageList()
        {
            // 내가 원하는 방의 이름에서 comment를 읽어온다
            databaseReference = FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments");
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                // listener로 읽은 데이터는 comments로 넘어온다.
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // 서버에서 부를 때 모든 데이터를 부르기 때문에 데이터가 쌓이는 것을 막기 위해 CLEAR를 해주어야 한다
                    comments.clear();

                    Map<String, Object> readUsersMap = new HashMap<>();

                    for (DataSnapshot item : dataSnapshot.getChildren()) {

                        String key = item.getKey();
                        ChatModel.Comment comment_origin = item.getValue(ChatModel.Comment.class);
                        ChatModel.Comment comment_motify = item.getValue(ChatModel.Comment.class);
                        comment_motify.readUsers.put(uid, true);
                        readUsersMap.put(key, comment_motify);
                        comments.add(comment_origin);
                    }

//                    if (!comments.get(comments.size() - 1).readUsers.containsKey(uid)) {
//
//                        FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments")
//                                .updateChildren(readUsersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                notifyDataSetChanged();
//                                recyclerView.scrollToPosition(comments.size() - 1);
//                            }
//                        });
//                    } else
                        {
                        // 리스트를 새로 갱신해준다
                        // 새로고침
                        notifyDataSetChanged();

                        //메시지를 갱신시켜주면서 내가 지금 보낸 메세지가 보이도록 뷰를 맨밑으로 내려준다.
                        recyclerView.scrollToPosition(comments.size() - 1);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);

            // 뷰를 재사용할 때 만드는 클래스 - 뷰홀더
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);

            // 내가 보낸 메세지
            // comment의 uid와 내 uid가 같다면 - 내가 보낸 메시지이다.
            if(comments.get(position).uid.equals(uid)){
                messageViewHolder.textView_message.setText(comments.get(position).message); // 내 메세지
                messageViewHolder.textView_message.setBackgroundResource(R.color.orange); // 내 메세지의 버블이미지 <- 이름 잘못지정해준듯 ㅠ
                // 상대방 메시지 레이아웃을 감춘다.
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.relativeLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setTextSize(13); // 메세지 글자크기
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT); // 채팅메세지 위치 내가 보낸 건 오른쪽에 위치한다!
                //setReadCounter(position, messageViewHolder.textView_readCounter_left);
            }
            // 상대방이 보낸 메세지
            else{ // 상대방이 보낸 메시지

                Glide.with(holder.itemView.getContext())
                        .load(destinationUserModel.profileImageUrl)
                        .apply(new RequestOptions().circleCrop()) // 프로필을 동그라미 모양으로 shape 지정
                        .into(messageViewHolder.imageView_profile);
                messageViewHolder.textview_name.setText(destinationUserModel.userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.relativeLayout_destination.setVisibility(View.VISIBLE);
//                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.textView_message.setBackgroundResource(R.color.gray);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setTextSize(13);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
                //setReadCounter(position, messageViewHolder.textView_readCounter_right);
            }

        }
//        void setReadCounter(final int position, final TextView textView) {
//            if (peopleCount == 0) {
//
//
//                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Map<String, Boolean> users = (Map<String, Boolean>) dataSnapshot.getValue();
//                        peopleCount = users.size();
//                        int count = peopleCount - comments.get(position).readUsers.size();
//                        if (count > 0) {
//                            textView.setVisibility(View.VISIBLE);
//                            textView.setText(String.valueOf(count));
//                        } else {
//                            textView.setVisibility(View.INVISIBLE);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }else{
//                int count = peopleCount - comments.get(position).readUsers.size();
//                if (count > 0) {
//                    textView.setVisibility(View.VISIBLE);
//                    textView.setText(String.valueOf(count));
//                } else {
//                    textView.setVisibility(View.INVISIBLE);
//                }
//            }
//
//        }

        @Override
        public int getItemCount() {
            // 정확하게 돌아가는지 알 수 있다.
            return comments.size();
        }

        // 이름을 가져오기 위해
        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_message;
            public TextView textview_name;
            public ImageView imageView_profile;
            public LinearLayout linearLayout_destination;
            public RelativeLayout relativeLayout_destination;
            public LinearLayout linearLayout_main;
            //public TextView textView_timestamp;
//            public TextView textView_readCounter_left;
//            public TextView textView_readCounter_right;

            public MessageViewHolder(View view) {
                super(view);
                textView_message = (TextView) view.findViewById(R.id.messageItem_textView_message);
                textview_name = (TextView)view.findViewById(R.id.messageItem_textview_name);
                imageView_profile = (ImageView)view.findViewById(R.id.messageItem_imageview_profile);
                linearLayout_destination = (LinearLayout)view.findViewById(R.id.messageItem_linearlayout_destination);
                relativeLayout_destination = (RelativeLayout)view.findViewById(R.id.messageItem_relativelayout_destination);


                // 자기 안에 있는 contents를 왼쪽으로 정렬할건지 오른쪽으로 정렬할건지 다루기 위해 만듬
                // 말풍선 왼쪽정렬, 오른쪽 정렬
                linearLayout_main = (LinearLayout)view.findViewById(R.id.messageItem_linearlayout_main);
                //textView_readCounter_left = (TextView) view.findViewById(R.id.messageItem_textview_readCounter_left);
                //textView_readCounter_right = (TextView) view.findViewById(R.id.messageItem_textview_readCounter_right);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // getmessagelist를 할 때 null일때도 아래코드가 진행되면 에러가 발생한다.
        if(valueEventListener != null){
            databaseReference.removeEventListener(valueEventListener);
        }
        finish();
        // 왼쪽에서 오른쪽까지 swipe 되면서 화면이 전환된다.
        overridePendingTransition(R.anim.fromleft, R.anim.toright);
    }

    //버튼
    public void mOnPopupClick(View v){

        if(visible == false) {
            relativeLayout.setVisibility(View.VISIBLE);
            visible = true;
        }else{
            relativeLayout.setVisibility(View.GONE);
            visible = false;
        }
    }

    public void mOnPopupClick_drawer(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, LockerActivity.class);
        intent.putExtra("data", "해당 번호의");
        intent.putExtra("data2", "사물함을 이용해주세요.");
        startActivityForResult(intent, 1);
    }

    public void mOnPopupClick_complete(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, CompleteActivity.class);
        startActivityForResult(intent, 1);
    }


}