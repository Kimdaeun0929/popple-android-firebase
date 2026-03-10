package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class wishlist extends AppCompatActivity {
    private LinearLayout wishlistContainer; // 위시리스트 항목들을 담을 LinearLayout
    private static final String TAG = "WishlistActivity"; // 로그 태그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlist);

        wishlistContainer = findViewById(R.id.wishlistLinearLayout); // LinearLayout 초기화

        // Firebase에서 위시리스트 항목을 로드
        loadWishlistItems();

        // 하단 이미지뷰 클릭 이벤트 처리
        findViewById(R.id.imageViewBottom3).setOnClickListener(v -> startActivity(new Intent(wishlist.this, home.class)));
        findViewById(R.id.imageViewBottom4).setOnClickListener(v -> startActivity(new Intent(wishlist.this, chat.class)));
        findViewById(R.id.imageViewBottom5).setOnClickListener(v -> startActivity(new Intent(wishlist.this, mypage.class)));
    }

    // Firebase에서 위시리스트 항목을 로드하는 메서드
    private void loadWishlistItems() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 현재 사용자 ID를 가져옴
        DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("wishlist"); // Firebase 경로 설정

        userWishlistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.d(TAG, "No wishlist data found for user."); // 데이터가 없을 경우 로그 출력
                    return;
                }
                // 데이터가 존재할 경우 각 항목을 처리
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class); // 제목 가져오기
                    String location = snapshot.child("location").getValue(String.class); // 위치 가져오기
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class); // 이미지 URL 가져오기

                    Log.d(TAG, "Wishlist item: " + title + ", " + location + ", " + imageUrl); // 가져온 데이터 로그 출력
                    addWishlistItem(title, location, imageUrl); // 위시리스트 항목 추가
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(wishlist.this, "위시리스트를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show(); // 에러 메시지 출력
                Log.e(TAG, "DatabaseError: " + databaseError.getMessage()); // 에러 로그 출력
            }
        });
    }

    // 위시리스트 항목을 추가하는 메서드
    private void addWishlistItem(String title, String location, String imageUrl) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); // LayoutInflater 객체 가져오기
        RelativeLayout wishlistItemLayout = (RelativeLayout) inflater.inflate(R.layout.wishlist_item, null); // wishlist_item 레이아웃을 inflate

        ImageView wishImgView = wishlistItemLayout.findViewById(R.id.wishimg); // 이미지뷰 초기화
        TextView storeNameTextView = wishlistItemLayout.findViewById(R.id.popup_store_name); // 제목 텍스트뷰 초기화
        TextView locationTextView = wishlistItemLayout.findViewById(R.id.wish_location); // 위치 텍스트뷰 초기화

        // 가져온 데이터를 각 요소에 설정
        storeNameTextView.setText(title); // 제목 설정
        locationTextView.setText(location); // 위치 설정

        // 위시리스트 항목 클릭 이벤트 설정
        wishlistItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클릭한 뷰에서 제목 추출
                TextView storeNameTextView = view.findViewById(R.id.popup_store_name);
                String title = storeNameTextView.getText().toString();

                Intent intent = new Intent(wishlist.this, info.class); // info 액티비티로 이동하기 위한 인텐트 생성

                // 제목에 따라 정보 전달
                if ("A Cloud Traveler : 구름 위를 걷는 기분".equals(title)) {
                    intent.putExtra("tableName", "popup_info");
                    intent.putExtra("imageFileName", "store1.png");
                } else if ("It's Your Day: 이번 광고, 생일 카페 주인공은 바로 너!".equals(title)) {
                    intent.putExtra("tableName", "popup_info1");
                    intent.putExtra("imageFileName", "store2.png");
                } else if ("담곰이 카페".equals(title)) {
                    intent.putExtra("tableName", "popup_info2");
                    intent.putExtra("imageFileName", "store3.png");
                } else if ("담곰이 팝업스토어 <봄날의 담곰이>".equals(title)) {
                    intent.putExtra("tableName", "popup_info3");
                    intent.putExtra("imageFileName", "store4.png");
                } else if ("로에베 퍼퓸 팝업스토어".equals(title)) {
                    intent.putExtra("tableName", "popup_info4");
                    intent.putExtra("imageFileName", "store5.png");
                } else if ("IKEA 팝업스토어 더현대 서울".equals(title)) {
                    intent.putExtra("tableName", "popup_info5");
                    intent.putExtra("imageFileName", "store6.png");
                } else if ("엄브로 100주년 <MR.UM's CLEANERS>".equals(title)) {
                    intent.putExtra("tableName", "popup_info6");
                    intent.putExtra("imageFileName", "store7.png");
                } else if ("블레스문 팝업스토어".equals(title)) {
                    intent.putExtra("tableName", "popup_info7");
                    intent.putExtra("imageFileName", "store8.png");
                } else if ("요물, 우리를 홀린 고양이".equals(title)) {
                    intent.putExtra("tableName", "popup_info8");
                    intent.putExtra("imageFileName", "store9.png");
                } else if ("달리기 : 새는 날고 물고기는 헤엄치고 인간은 달린다".equals(title)) {
                    intent.putExtra("tableName", "popup_info9");
                    intent.putExtra("imageFileName", "store10.png");
                }
                // 필요한 만큼 else if 블록을 추가하여 다른 마커 제목에 맞는 테이블 이름을 설정할 수 있음

                startActivity(intent); // info 액티비티 시작
            }
        });

        // 이미지 URL이 유효한 경우에만 이미지를 로드
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(wishImgView); // 이미지를 로드하여 이미지뷰에 설정
        } else {
            wishImgView.setVisibility(View.GONE); // 이미지가 없는 경우 이미지뷰를 숨김
        }

        wishlistContainer.addView(wishlistItemLayout); // LinearLayout에 위시리스트 항목 추가
    }
}
