package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

// 채팅방 어댑터
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<Map<String, String>> mChatList; // 채팅 메시지 목록을 저장하는 리스트
    private String mNick; // 현재 사용자의 닉네임

    // 뷰 홀더 클래스 정의
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout leftChatBubble; // 상대방 채팅 버블
        public TextView leftNicknameTextView; // 상대방 닉네임 텍스트뷰
        public TextView leftMessageTextView; // 상대방 메시지 텍스트뷰
        public LinearLayout rightChatBubble; // 자신의 채팅 버블
        public TextView rightNicknameTextView; // 자신의 닉네임 텍스트뷰
        public TextView rightMessageTextView; // 자신의 메시지 텍스트뷰

        public ChatViewHolder(View itemView) {
            super(itemView);
            // 뷰 아이템에서 각 뷰 요소를 찾아냄
            leftChatBubble = itemView.findViewById(R.id.left_chat_bubble);
            leftNicknameTextView = itemView.findViewById(R.id.left_nickname);
            leftMessageTextView = itemView.findViewById(R.id.left_message);
            rightChatBubble = itemView.findViewById(R.id.right_chat_bubble);
            rightNicknameTextView = itemView.findViewById(R.id.right_nickname);
            rightMessageTextView = itemView.findViewById(R.id.right_message);
        }
    }

    // 생성자
    public ChatAdapter(List<Map<String, String>> chatList, String nick) {
        mChatList = chatList;
        mNick = nick;
    }

    // 뷰 홀더 생성
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 레이아웃 인플레이션하여 뷰 홀더 생성
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(v);
    }

    // 뷰 홀더에 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Map<String, String> chat = mChatList.get(position); // 현재 위치의 채팅 메시지 가져오기
        String nickname = chat.get("nickname"); // 닉네임 가져오기
        String message = chat.get("message"); // 메시지 가져오기

        // 닉네임이 현재 사용자와 일치하는 경우
        if (nickname.equals(mNick)) {
            // 자신의 채팅 버블과 텍스트뷰를 보이도록 설정하고, 상대방의 것은 숨김
            holder.rightChatBubble.setVisibility(View.VISIBLE);
            holder.leftChatBubble.setVisibility(View.GONE);
            holder.rightNicknameTextView.setText(nickname); // 닉네임 설정
            holder.rightMessageTextView.setText(message); // 메시지 설정
        } else { // 닉네임이 현재 사용자와 일치하지 않는 경우
            // 상대방의 채팅 버블과 텍스트뷰를 보이도록 설정하고, 자신의 것은 숨김
            holder.leftChatBubble.setVisibility(View.VISIBLE);
            holder.rightChatBubble.setVisibility(View.GONE);
            holder.leftNicknameTextView.setText(nickname); // 닉네임 설정
            holder.leftMessageTextView.setText(message); // 메시지 설정
        }
    }

    // 채팅 메시지 개수 반환
    @Override
    public int getItemCount() {
        return mChatList.size();
    }
}
