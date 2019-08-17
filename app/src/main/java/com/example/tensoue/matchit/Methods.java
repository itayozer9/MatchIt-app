package com.example.tensoue.matchit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.service.autofill.FieldClassification;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.facebook.Profile;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class Methods {


    public static MediaPlayer createMusic(Context context ,int path)
    {

        MediaPlayer mPlayer = MediaPlayer.create(context,path);
        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(100, 100);
        }
        mPlayer.start();
        return mPlayer;
    }

    public static void winConfetti(KonfettiView viewKonfetti){
        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);

    }

    public static void flipTheView(final RoundedImageView image, final int firstImage , final int secondImage) {
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(image, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(image, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                image.setImageResource(secondImage);
                oa2.start();
            }
        });
        oa1.start();
    }

    public static void playMusicSingle(MediaPlayer mPlayer){
        mPlayer.start();

    }

    public static int pauseMusic(MediaPlayer mPlayer)
    {
        int length=0;
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
             length = mPlayer.getCurrentPosition();
        }
        return length;
    }

    public static void resumeMusic(MediaPlayer mPlayer,int length)
    {
        if (mPlayer.isPlaying() == false) {
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }



    public static void setProfilePicMethod(final CircleImageView iv){

        if(FirebaseAuth.getInstance().getCurrentUser().getProviders().toString().contains("facebook")) {
            String fb_profilePic = "https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?width=200&height=200";
            Glide
                    .with(Matchit.getAppContext())
                    .load(fb_profilePic)
                    .asBitmap()
                    .into(iv);
        }


        else {
            String userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
            StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child(("images/" + userUUID));

            Glide.with(Matchit.getAppContext())
                    .using(new com.firebase.ui.storage.images.FirebaseImageLoader())
                    .load(profilePicRef)
                    .override(300, 300)
                    .into(iv);
        }
        iv.setVisibility(View.VISIBLE);
    }


    public static void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }


//    public static String getUserProfilePic(){
//
//         final String userProfilePic;
//
//        if(FirebaseAuth.getInstance().getCurrentUser().getProviders().toString().contains("facebook")) {
//            userProfilePic = "https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?width=200&height=200";
//            return userProfilePic;
//        }
//
//
//        else {
//            String userUUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
//            StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child(("images/" + userUUID));
//            userProfilePic = profilePicRef.getDownloadUrl().toString();
//        }
//            return userProfilePic;
//
//    }


    public static void createNewUserRecord(scoreboardRow row)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReferenceFromUrl("https://matchit-8abc0.firebaseio.com/");
        DatabaseReference scoreboardRef = ref.child("users").child(row.getUserUUID());
//        DatabaseReference newRef = scoreboardRef.push();
        scoreboardRef.setValue(row);
    }
    public static void createNewUserRecord(final scoreboardRow row,String level)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReferenceFromUrl("https://matchit-8abc0.firebaseio.com/");
        final DatabaseReference scoreboardRef = ref.child("scoreboard_"+level).child(row.getUserUUID());
        scoreboardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    if (row.getHighestScore() < dataSnapshot.getValue(scoreboardRow.class).getHighestScore())
                        scoreboardRef.setValue(row);
                }
                else
                    scoreboardRef.setValue(row);
                    

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void showScoreboardRecycler(final RecyclerView rv_scoreboard, String level){
        Log.e("GEVER", "on scoreboard");
        final ArrayList<scoreboardRow> list = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReferenceFromUrl("https://matchit-8abc0.firebaseio.com/").child("scoreboard_" + level);
        dbRef.orderByChild("highestScore").limitToLast(100).startAt(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                list.clear();
                for (DataSnapshot unit : dataSnapshot.getChildren()) {
                    scoreboardRow row = unit.getValue(scoreboardRow.class);
                    list.add(row);

                }
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(Matchit.getAppContext(),list);
                rv_scoreboard.setAdapter(adapter);
                rv_scoreboard.setLayoutManager(new LinearLayoutManager(Matchit.getAppContext()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void signOut(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
    }

}
