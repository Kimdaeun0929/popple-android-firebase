package com.example.capstone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// 참여자 명단 어댑터 클래스
public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private List<String> participantsList; // 참여자 명단을 저장하는 리스트

    // 생성자: 참여자 명단 리스트를 받아와 초기화
    public ParticipantAdapter(List<String> participantsList) {
        this.participantsList = participantsList;
    }

    // ViewHolder 생성
    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // XML 레이아웃을 인플레이트하여 ViewHolder 객체 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ParticipantViewHolder(view);
    }

    // ViewHolder에 데이터를 바인딩
    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        holder.bind(participantsList.get(position)); // ViewHolder의 bind 메서드 호출하여 데이터 설정
    }

    // 목록의 아이템 개수 반환
    @Override
    public int getItemCount() {
        return participantsList.size();
    }

    // ViewHolder 클래스 정의
    class ParticipantViewHolder extends RecyclerView.ViewHolder {

        private TextView participantTextView; // 참여자 이름을 표시하는 텍스트뷰

        // ViewHolder 생성자
        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            participantTextView = itemView.findViewById(android.R.id.text1); // 텍스트뷰 초기화
        }

        // ViewHolder에 데이터를 바인딩하는 메서드
        public void bind(String participant) {
            participantTextView.setText(participant); // 텍스트뷰에 참여자 이름 설정
        }
    }
}
