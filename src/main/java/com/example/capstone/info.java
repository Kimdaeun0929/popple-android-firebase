package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashSet;
import java.util.Set;

public class info extends AppCompatActivity {
    private ImageView wishheart;
    private boolean isWished = false;
    private DatabaseReference mDatabase;
    private String tableName;
    private String imageFileName;
    private String websiteLink;
    private String instagramLink;
    public static final String PREFS_NAME = "wishlist_prefs";
    public static final String LISTPREFS_NAME = "chat_list_pref";
    public static final String KEY_CHAT_LIST = "chat_list";
    private String popupStoreName;
    private ImageView wishhearttImageView;
    public static final String KEY_WISHHEART_STATE = "wishheart_state_" + FirebaseAuth.getInstance().getCurrentUser().getUid();  // 사용자별로 키를 다르게 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        wishheart = findViewById(R.id.wishheart);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        tableName = getIntent().getStringExtra("tableName");
        imageFileName = getIntent().getStringExtra("imageFileName");

        loadPopupInfo();
        loadCloudImage();

        wishhearttImageView = findViewById(R.id.wishheart);

        // 초기 상태 설정
        checkWishheartState();

        // wishhearttImageView 클릭 이벤트 설정
        wishhearttImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클릭한 정보를 데이터베이스에 저장하거나 삭제
                saveToDatabase();

                // 현재 상태에 따라 이미지를 변경
                if (isWished) {
                    wishhearttImageView.setImageResource(R.drawable.wishheart);
                    wishhearttImageView.setTag("wishheart");
                } else {
                    wishhearttImageView.setImageResource(R.drawable.wishheart2);
                    wishhearttImageView.setTag("wishheart2");
                }

                isWished = !isWished;
                saveWishheartState(isWished);
            }
        });

        View imageView = findViewById(R.id.backst);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView chatImageView = findViewById(R.id.message);
        chatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupStoreName != null) {
                    addChatRoomToPreferences(popupStoreName);
                    Intent intent = new Intent(info.this, chat2.class);
                    intent.putExtra("popup_store_name", popupStoreName);
                    startActivity(intent);
                } else {
                    Toast.makeText(info.this, "Popup store name is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView websiteTextView = findViewById(R.id.websiteTextView);
        websiteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (websiteLink != null && !websiteLink.isEmpty()) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteLink)));
                } else {
                    Toast.makeText(info.this, "Website link is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView instagramTextView = findViewById(R.id.instagramTextView);
        instagramTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instagramLink != null && !instagramLink.isEmpty()) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(instagramLink)));
                } else {
                    Toast.makeText(info.this, "Instagram link is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addChatRoomToPreferences(String chatRoomName) {
        SharedPreferences sharedPreferences = getSharedPreferences(LISTPREFS_NAME, Context.MODE_PRIVATE);
        Set<String> chatList = sharedPreferences.getStringSet(KEY_CHAT_LIST, new HashSet<>());

        if (chatList == null) {
            chatList = new HashSet<>();
        }

        chatList.add(chatRoomName);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_CHAT_LIST, chatList);
        editor.apply();
    }

    private void saveToDatabase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("wishlist");

        mDatabase.child(tableName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);

                    if (imageFileName != null && !imageFileName.isEmpty()) {
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imageFileName);
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();

                                userWishlistRef.orderByChild("title").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                childSnapshot.getRef().removeValue();
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    wishhearttImageView.setImageResource(R.drawable.wishheart);
                                                    wishhearttImageView.setTag("wishheart");
                                                    saveWishheartState(false);
                                                    Toast.makeText(info.this, "위시리스트에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            String key = userWishlistRef.push().getKey();
                                            userWishlistRef.child(key).child("title").setValue(title);
                                            userWishlistRef.child(key).child("location").setValue(location);
                                            userWishlistRef.child(key).child("imageUrl").setValue(imageUrl);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    wishhearttImageView.setImageResource(R.drawable.wishheart2);
                                                    wishhearttImageView.setTag("wishheart2");
                                                    saveWishheartState(true);
                                                    Toast.makeText(info.this, "위시리스트에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(info.this, "Failed to update wishlist", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(info.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(info.this, "Image file name is null or empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(info.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveWishheartState(boolean isWished) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_WISHHEART_STATE, isWished);
        editor.apply();

        Log.d("WishheartState", "Wishheart state saved: " + isWished);
    }

    private void checkWishheartState() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userWishlistRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("wishlist");

        mDatabase.child(tableName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = dataSnapshot.child("title").getValue(String.class);

                    userWishlistRef.orderByChild("title").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                wishhearttImageView.setImageResource(R.drawable.wishheart2);
                                wishhearttImageView.setTag("wishheart2");
                                isWished = true;
                            } else {
                                wishhearttImageView.setImageResource(R.drawable.wishheart);
                                wishhearttImageView.setTag("wishheart");
                                isWished = false;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("WishlistCheck", "Error checking wishlist state", error.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PopupInfo", "Error fetching popup info", error.toException());
            }
        });
    }

    private void loadPopupInfo() {
        mDatabase.child(tableName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String date = dataSnapshot.child("date").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String hoursWeekday = dataSnapshot.child("hours_weekday").getValue(String.class);
                    String hoursWeekend = dataSnapshot.child("hours_weekend").getValue(String.class);
                    websiteLink = dataSnapshot.child("popup_website").getValue(String.class);
                    instagramLink = dataSnapshot.child("instagram_website").getValue(String.class);

                    popupStoreName = title;

                    ((TextView) findViewById(R.id.titleTextView)).setText(title);
                    ((TextView) findViewById(R.id.dateTextView)).setText(date);
                    ((TextView) findViewById(R.id.locationTextView)).setText(location);
                    ((TextView) findViewById(R.id.descriptionTextView)).setText(description);
                    ((TextView) findViewById(R.id.hoursWeekdayTextView)).setText(hoursWeekday);
                    ((TextView) findViewById(R.id.hoursWeekendTextView)).setText(hoursWeekend);
                    ((TextView) findViewById(R.id.websiteTextView)).setText("팝업 홈페이지");
                    ((TextView) findViewById(R.id.instagramTextView)).setText("인스타그램");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PopupInfo", "Error fetching popup info", databaseError.toException());
            }
        });
    }

    private void loadCloudImage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(imageFileName);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                ImageView imageView = findViewById(R.id.cloudImageView);
                Glide.with(getApplicationContext()).load(imageUrl).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(info.this, "Failed to load image.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
