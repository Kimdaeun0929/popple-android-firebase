package com.example.capstone;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class chat2 extends AppCompatActivity {

    // RecyclerView 관련 변수 선언
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Map<String, String>> chatList; // 채팅 목록을 저장하는 리스트
    private String nick; // 사용자 닉네임

    // UI 요소 변수 선언
    private EditText EditText_chat; // 메시지 입력 창
    private Button send; // 메시지 전송 버튼
    private DatabaseReference myRef; // Firebase 데이터베이스 참조
    private TextView storeNameTextView; // 가게 이름 텍스트뷰
    private static final String PREF_NAME = "login_pref"; // SharedPreferences 이름
    private static final String KEY_USER_NAME = "user_name"; // 사용자 이름을 저장할 키
    private boolean isLeaving = false; // 채팅방을 나가는지 여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat2);

        // 뒤로 가기 이미지 클릭 시 이전 화면(chat)으로 이동하는 이벤트 처리
        ImageView backimageView = findViewById(R.id.chattingroom);
        backimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chat2.this, chat.class);
                startActivity(intent);
            }
        });

        // 가게 이름을 표시하는 텍스트뷰 초기화
        storeNameTextView = findViewById(R.id.popup_store_name);

        // 메시지 전송 버튼과 메시지 입력 창 초기화
        send = findViewById(R.id.sendText);
        EditText_chat = findViewById(R.id.messageEditText);

        // Intent로부터 가게 이름을 가져와 텍스트뷰에 설정
        String popupStoreName = getIntent().getStringExtra("popup_store_name");
        storeNameTextView.setText(popupStoreName);

        // SharedPreferences에서 사용자 이름을 가져옵니다.
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        nick = sharedPreferences.getString(KEY_USER_NAME, "Unknown User"); // 기본값은 "Unknown User"

        // RecyclerView 초기화
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true); // 아이템 크기 고정
        mLayoutManager = new LinearLayoutManager(this); // 레이아웃 매니저 설정
        mRecyclerView.setLayoutManager(mLayoutManager);

        // 채팅 목록과 어댑터 초기화
        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, nick); // 채팅 어댑터 생성
        mRecyclerView.setAdapter(mAdapter); // RecyclerView에 어댑터 설정

        // Firebase 데이터베이스 참조
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String sanitizedPopupStoreName = sanitizeFirebasePath(popupStoreName);  // 경로를 정리
        myRef = database.getReference("chats").child(sanitizedPopupStoreName);

        // 메시지 전송 버튼 클릭 이벤트 처리
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = EditText_chat.getText().toString().trim();
                if (!msg.isEmpty()) {
                    // 채팅 메시지를 Map으로 만들어 Firebase에 저장
                    Map<String, String> chat = new HashMap<>();
                    chat.put("nickname", nick);
                    chat.put("message", msg);
                    myRef.child("messages").push().setValue(chat); // Firebase에 메시지 저장
                    EditText_chat.setText(""); // 메시지 입력창 초기화
                }
            }
        });

        // Firebase ChildEventListener를 통해 새로운 메시지를 수신하고 처리
        myRef.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 새로운 채팅 메시지를 받았을 때의 처리
                Map<String, String> chat = (Map<String, String>) dataSnapshot.getValue();
                if (chat != null) {
                    chatList.add(chat); // 채팅 목록에 새로운 메시지 추가
                    mAdapter.notifyItemInserted(chatList.size() - 1); // 어댑터에 새로운 아이템 추가 알림
                    mRecyclerView.scrollToPosition(chatList.size() - 1); // 화면을 마지막 메시지로 스크롤
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 메시지가 변경됐을 때의 처리 (여기서는 필요하지 않음)
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // 메시지가 삭제됐을 때의 처리 (여기서는 필요하지 않음)
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 메시지가 이동됐을 때의 처리 (여기서는 필요하지 않음)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터베이스 오류 발생 시의 처리 (여기서는 필요하지 않음)
            }
        });

        // 채팅방에 사용자 추가
        addUserToChatRoom();

        // 참여자 목록 아이콘 클릭 시 다이얼로그 표시
        ImageView participantListImageView = findViewById(R.id.list);
        participantListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParticipantDialog();
            }
        });
    }

    //파이어베이스 데이터베이스에서 허용되지 않는 문자를 "_"로 대체
    private String sanitizeFirebasePath(String path) {
        return path.replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_");
    }

    private void showParticipantDialog() {
        // 대화 상자 생성
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_participants); // 대화 상자에 레이아웃 설정

        // 참여자 목록을 보여줄 RecyclerView 설정
        RecyclerView participantsRecyclerView = dialog.findViewById(R.id.participants2);
        participantsRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // 리사이클러뷰 레이아웃 매니저 설정

        // 참여자 목록을 담을 리스트 및 어댑터 초기화
        List<String> participantList = new ArrayList<>();
        ParticipantAdapter participantAdapter = new ParticipantAdapter(participantList);
        participantsRecyclerView.setAdapter(participantAdapter); // 어댑터 설정

        // Firebase에서 참여자 목록을 가져와서 RecyclerView에 업데이트하는 ValueEventListener 설정
        myRef.child("participants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantList.clear(); // 목록 초기화
                // 데이터베이스에서 각 참여자의 이름을 가져와 리스트에 추가
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String participantName = snapshot.getKey();
                    participantList.add(participantName);
                }
                participantAdapter.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터베이스 오류 처리
            }
        });

        // 대화 상자 내의 나가기 버튼 설정
        Button leaveButton = dialog.findViewById(R.id.leave);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLeaving = true; // 나가기 플래그 설정
                removeUserFromChatRoom(); // 채팅방에서 사용자 삭제
                dialog.dismiss(); // 대화 상자 닫기

                // 채팅 목록에서 항목 제거를 위해 SharedPreferences에 접근하여 제거
                removeChatListFromPreferences(storeNameTextView.getText().toString());

                // 채팅 목록에서 항목 제거를 위해 인텐트 설정
                Intent resultIntent = new Intent();
                resultIntent.putExtra("remove_chat_item", storeNameTextView.getText().toString());
                setResult(RESULT_OK, resultIntent);

                finish(); // 액티비티 종료
            }
        });

        dialog.show(); // 대화 상자 표시
    }

    private void addUserToChatRoom() {
        // 사용자를 참여자 노드에 추가
        myRef.child("participants").child(nick).setValue(true);
    }

    private void removeUserFromChatRoom() {
        // 사용자를 참여자 노드에서 제거
        myRef.child("participants").child(nick).removeValue();

        // 채팅방을 삭제하지 않고 메시지를 남겨두기 위해 해당 부분 제거
    }

    // 채팅 목록에서 채팅 항목 삭제
    private void removeChatListFromPreferences(String chat) {
        SharedPreferences sharedPreferences = getSharedPreferences("chat_list_pref", Context.MODE_PRIVATE);
        Set<String> chatListSet = sharedPreferences.getStringSet("chat_list", new HashSet<>());
        if (chatListSet != null && chatListSet.contains(chat)) {
            // 새로운 HashSet으로 복사하여 변경
            Set<String> newChatListSet = new HashSet<>(chatListSet);
            newChatListSet.remove(chat);
            // 변경된 Set을 다시 저장
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet("chat_list", newChatListSet);
            editor.apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isLeaving) {
            removeUserFromChatRoom(); // 액티비티가 종료되면서 나가기 플래그가 true일 경우 사용자를 채팅방에서 제거
        }
    }
}
