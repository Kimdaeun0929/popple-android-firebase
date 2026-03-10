package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class chat extends AppCompatActivity {

    // 채팅 목록을 저장할 SharedPreferences 이름
    private static final String PREFS_NAME = "chat_list_pref";
    // SharedPreferences에서 채팅 목록을 가져오거나 저장할 때 사용할 키
    private static final String KEY_CHAT_LIST = "chat_list";
    // 다른 액티비티에서 채팅 액티비티로 요청할 때 사용할 요청 코드
    private static final int CHAT_REQUEST_CODE = 1;

    private ListView chatListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> chatListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        // 뒤로 가기 이미지 클릭 시 현재 액티비티 종료
        View imageView = findViewById(R.id.imageViewBottom1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.imageViewBottom2).setOnClickListener(v -> startActivity(new Intent(chat.this, googlemap.class)));
        findViewById(R.id.imageViewBottom3).setOnClickListener(v -> startActivity(new Intent(chat.this, home.class)));
        findViewById(R.id.imageViewBottom4).setOnClickListener(v -> startActivity(new Intent(chat.this, chat.class)));
        findViewById(R.id.imageViewBottom5).setOnClickListener(v -> startActivity(new Intent(chat.this, mypage.class)));

        // 채팅 목록을 보여주는 리스트뷰 설정
        chatListView = findViewById(R.id.chat_list_view);
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭한 채팅 항목의 이름을 가져옴
                String popupStoreName = chatListItems.get(position);

                // 채팅 액티비티로 이동하고 선택한 채팅 항목의 이름을 전달
                Intent intent = new Intent(chat.this, chat2.class);
                intent.putExtra("popup_store_name", popupStoreName);
                startActivityForResult(intent, CHAT_REQUEST_CODE);
            }
        });

        // 채팅 목록 초기화
        chatListItems = new ArrayList<>();
        // 저장된 채팅 목록을 불러옴
        loadChatList();

        // 어댑터 설정
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chatListItems);
        chatListView.setAdapter(adapter);

        // 새로운 채팅 항목이 있는지 확인하고 있다면 목록에 추가
        Intent intent = getIntent();
        if (intent.hasExtra("new_chat_item")) {
            String newChatItem = intent.getStringExtra("new_chat_item");
            addChatList(newChatItem);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 채팅 액티비티에서 삭제된 채팅 항목을 처리
        if (requestCode == CHAT_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("remove_chat_item")) {
                String removeChatItem = data.getStringExtra("remove_chat_item");
                removeChatList(removeChatItem);
            }
        }
    }

    // 채팅 목록에 새로운 채팅 항목 추가
    public void addChatList(String chat) {
        if (!chatListItems.contains(chat)) {
            chatListItems.add(chat);
            adapter.notifyDataSetChanged();
            saveChatList();
        }
    }

    // 채팅 목록에서 채팅 항목 삭제
    private void removeChatList(String chat) {
        if (chatListItems.contains(chat)) {
            chatListItems.remove(chat);
            adapter.notifyDataSetChanged();
            saveChatList();
        }
    }

    // 채팅 목록을 SharedPreferences에서 불러오는 메서드
    private void loadChatList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> chatList = sharedPreferences.getStringSet(KEY_CHAT_LIST, null);

        if (chatList != null) {
            chatListItems.addAll(chatList);
        }
    }

    // 채팅 목록을 SharedPreferences에 저장하는 메서드
    private void saveChatList() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> chatListSet = new HashSet<>(chatListItems);
        editor.putStringSet(KEY_CHAT_LIST, chatListSet);
        editor.apply();
    }
}
