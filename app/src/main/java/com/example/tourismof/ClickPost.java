package com.example.tourismof;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Iterator;

public class ClickPost extends AppCompatActivity {

    private TextView text1, text2, text3,text4,text5,text6;
    private ImageView imageView;
    private String image, price, description, location,postKey,title, ownerID;
    private DatabaseReference postRef;
    private String newString = "New String";
    private LinearLayout linearLayout,BedRooms_linearLayout, location_linear, bathrooms;
    private ImageButton chat_button;
    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        text1 = findViewById(R.id.location_post);
        text2 = findViewById(R.id.price_post);
        text3 = findViewById(R.id.description_post);
        text4 = findViewById(R.id.title_post);
        text6 = findViewById(R.id.sharing);
        linearLayout = findViewById(R.id.product_page_return_view);
        // BedRooms_linearLayout = findViewById(R.id.bedRooms_linear_layout);
        location_linear = findViewById(R.id.location_linera_layout);
        // bathrooms = findViewById(R.id.bathrooms_layout_click);
        chat_button = findViewById(R.id.chat_with_owner_icon);
        viewFlipper = findViewById(R.id.viewFlipper_slide_show);



        postKey = getIntent().getExtras().get("postKey").toString();
        ownerID = getIntent().getExtras().get("ownerID").toString();






        postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey);

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    description = dataSnapshot.child("description").getValue().toString();
                    price = dataSnapshot.child("price").getValue().toString();
                    location = dataSnapshot.child("location").getValue().toString();
                    title = dataSnapshot.child("title").getValue().toString();

                    text1.setText(location);
                    text2.setText(price);
                    text3.setText(description);
                    text4.setText(title);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postRef.child("postimages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                while (items.hasNext()){
                    DataSnapshot item = items.next();
                    String link = item.getValue().toString();

                    ImageView imageView = new ImageView(ClickPost.this);
                    Picasso.get().load(link).into(imageView);

                    viewFlipper.addView(imageView);

                    viewFlipper.setFlipInterval(4999);
                    viewFlipper.setAutoStart(true);

                    viewFlipper.startFlipping();
                    viewFlipper.setInAnimation(ClickPost.this, android.R.anim.slide_in_left);
                    viewFlipper.setOutAnimation(ClickPost.this, android.R.anim.slide_out_right);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserTodelete();
            }
        });
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToContactsActivity();
            }
        });
    }

    private void sendUserToContactsActivity() {
        Intent intent = new Intent(ClickPost.this, Chat.class);
        intent.putExtra("ownerID",ownerID);
        startActivity(intent);
    }

    private void sendownerTochat() {

        Intent intent = new Intent(ClickPost.this, Contacts.class);
        startActivity(intent);


    }

    private void sendUserTodelete() {
        postRef.removeValue();
        sendUserToMainActivity();
        Toast.makeText(this, "post is deleted", Toast.LENGTH_SHORT).show();
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(ClickPost.this,FirstActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}