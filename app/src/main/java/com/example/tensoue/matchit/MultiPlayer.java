package com.example.tensoue.matchit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.dionsegijn.konfetti.KonfettiView;


public class MultiPlayer extends AppCompatActivity{
    private void sendToFirstScreen(){
        Intent intent = new Intent (getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    private void signOutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MultiPlayer.this);
        builder.setMessage("Are you sure you want to sign out?").setTitle("Sign out");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Methods.signOut();
                sendToFirstScreen();
            }
        }).show();
        AlertDialog dialog = builder.create();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout) {
            signOutDialog();



        }
        else if (item.getItemId() == R.id.action_scoreboard){
            FragmentManager fm = getSupportFragmentManager();
            RecyclerView rv_scoreboard = findViewById(R.id.recycler_view);
            TabbedDialog dialog = new TabbedDialog(rv_scoreboard);
            dialog.show(fm,"Dialog");
        }

        else if (item.getItemId() == R.id.action_sound) {
            if (activated == false) {
                item.setIcon(R.drawable.sound_on);
                Methods.resumeMusic(mPlayer,musicLength);
                activated = true;
            } else {
                item.setIcon(R.drawable.sound_off);
                musicLength=Methods.pauseMusic(mPlayer);
                activated=false;
            }



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu,menu);
        invalidateOptionsMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(activated == true) {
            menu.findItem(R.id.action_sound).setIcon(R.drawable.sound_on);
            mPlayer.start();
        }
        else {
            menu.findItem(R.id.action_sound).setIcon(R.drawable.sound_off);
            musicLength=Methods.pauseMusic(mPlayer);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    KonfettiView viewKonfetti;
    String spname="mainsp";
    SharedPreferences prefs;
    String musicKey = "musicactivated";
    boolean activated;
    ImageView arrow;
    ObjectAnimator animator;
    MediaPlayer mPlayer;

    int musicLength=0;
    ImageView curView = null;
    boolean canContinue = true;
    int countPair = 0;
    final int[] timeCards = new int[] {
            R.drawable.time_1_1,
            R.drawable.time_1_2,
            R.drawable.time_2_1,
            R.drawable.time_2_2,
            R.drawable.time_3_1,
            R.drawable.time_3_2,
            R.drawable.time_4_1,
            R.drawable.time_4_2,
            R.drawable.time_5_1,
            R.drawable.time_5_2,
            R.drawable.time_6_1,
            R.drawable.time_6_2,
            R.drawable.time_7_1,
            R.drawable.time_7_2,
            R.drawable.time_8_1,
            R.drawable.time_8_2,
            R.drawable.time_9_1,
            R.drawable.time_9_2,
            R.drawable.time_10_1,
            R.drawable.time_10_2,
            R.drawable.time_11_1,
            R.drawable.time_11_2,
            R.drawable.time_12_1,
            R.drawable.time_12_2,
            R.drawable.time_1b_1,
            R.drawable.time_1b_2,
            R.drawable.time_2b_1,
            R.drawable.time_2b_2,
            R.drawable.time_3b_1,
            R.drawable.time_3b_2,
            R.drawable.time_4b_1,
            R.drawable.time_4b_2,
            R.drawable.time_5b_1,
            R.drawable.time_5b_2,
            R.drawable.time_6b_1,
            R.drawable.time_6b_2,
            R.drawable.time_7b_1,
            R.drawable.time_7b_2,
            R.drawable.time_8b_1,
            R.drawable.time_8b_2,
            R.drawable.time_12b_1,
            R.drawable.time_12b_2

    };

    final int[] mathCards = new int[] {
            R.drawable.num_1_1,
            R.drawable.num_1_2,
            R.drawable.num_2_1,
            R.drawable.num_2_2,
            R.drawable.num_3_1,
            R.drawable.num_3_2,
            R.drawable.num_4_1,
            R.drawable.num_4_2,
            R.drawable.num_5_1,
            R.drawable.num_5_2,
            R.drawable.num_6_1,
            R.drawable.num_6_2,
            R.drawable.num_7_1,
            R.drawable.num_7_2,
            R.drawable.num_8_1,
            R.drawable.num_8_2,
            R.drawable.num_9_1,
            R.drawable.num_9_2,
            R.drawable.num_10_1,
            R.drawable.num_10_2,
            R.drawable.num_11_1,
            R.drawable.num_11_2,
            R.drawable.num_12_1,
            R.drawable.num_12_2,
            R.drawable.num_13_1,
            R.drawable.num_13_2,
            R.drawable.num_14_1,
            R.drawable.num_14_2,
            R.drawable.num_15_1,
            R.drawable.num_15_2,
            R.drawable.num_16_1,
            R.drawable.num_16_2,
            R.drawable.num_17_1,
            R.drawable.num_17_2,
            R.drawable.num_18_1,
            R.drawable.num_18_2,
            R.drawable.num_19_1,
            R.drawable.num_20_2,
            R.drawable.num_20_1,
            R.drawable.num_21_2,
            R.drawable.num_21_1

    };
    final int[] flagToPlace = new int[] {

            R.drawable.flag_israel_1,
            R.drawable.flag_israel_2,
            R.drawable.flag_brazil_1,
            R.drawable.flag_brazil_2,
            R.drawable.flag_china_1,
            R.drawable.flag_china_2,
            R.drawable.flag_england_1,
            R.drawable.flag_england_2,
            R.drawable.flag_france_1,
            R.drawable.flag_france_2,
            R.drawable.flag_italy_1,
            R.drawable.flag_italy_2,
            R.drawable.flag_usa_1,
            R.drawable.flag_usa_2,
            R.drawable.flag_argentina_1,
            R.drawable.flag_argentina_2,
            R.drawable.flag_australia_1,
            R.drawable.flag_australia_2,
            R.drawable.flag_egypt_1,
            R.drawable.flag_egypt_2,
            R.drawable.flag_russia_1,
            R.drawable.flag_russia_2,
            R.drawable.flag_spain_1,
            R.drawable.flag_spain_2,
            R.drawable.flag_croatia_1,
            R.drawable.flag_croatia_2,
            R.drawable.flag_cyprus_1,
            R.drawable.flag_cyprus_2,
            R.drawable.flag_denmark_1,
            R.drawable.flag_denmark_2,
            R.drawable.flag_georgia_1,
            R.drawable.flag_georgia_2,
            R.drawable.flag_germany_1,
            R.drawable.flag_germany_2,
            R.drawable.flag_greece_1,
            R.drawable.flag_greece_2,
            R.drawable.flag_iceland_1,
            R.drawable.flag_iceland_2,
            R.drawable.flag_irland_1,
            R.drawable.flag_irland_2,
            R.drawable.flag_bulgaria_1,
            R.drawable.flag_bulgaria_2,
            R.drawable.flag_japan_1,
            R.drawable.flag_japan_2,
            R.drawable.flag_portugal_1,
            R.drawable.flag_portugal_2,
            R.drawable.flag_sweeden_1,
            R.drawable.flag_sweeden_2,
            R.drawable.flag_switzerland_1,
            R.drawable.flag_switzerland_2,
            R.drawable.flag_thailand_1,
            R.drawable.flag_thailand_2,
            R.drawable.flag_ukrine_1,
            R.drawable.flag_ukrine_2

    };

    int currentPos = -1;
    int secondPos = -1;
    int score_home = 0;
    int score_guest = 0;
    int numOfCards;
    int hidden_image;
    int[] drawable;
    Boolean homeFlag =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        prefs = getSharedPreferences(spname,MODE_PRIVATE);
        activated = prefs.getBoolean(musicKey,true);
        invalidateOptionsMenu();

        setContentView(R.layout.multi_player);
        final MediaPlayer mPlayerCardFlip = MediaPlayer.create(MultiPlayer.this,R.raw.card_flip_new);
        final MediaPlayer mPlayerWin = MediaPlayer.create(MultiPlayer.this,R.raw.win_sound);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
        CircleImageView profilePic = findViewById(R.id.game_profilepic);
        profilePic.setBorderColor(getResources().getColor(R.color.Black));
        profilePic.setBorderWidth(6);
        Methods.setProfilePicMethod(profilePic);
        viewKonfetti = findViewById(R.id.viewKonfetti_multiplayer);


        ImageAdapter imageAdapter = new ImageAdapter(this);
        GridView gridView = (GridView)findViewById(R.id.gv_game);
        String level = getIntent().getStringExtra("level");
        String theme = getIntent().getStringExtra("theme");
        int numColumns;

        // Set game parameters:
        switch (level) {
            case "medium":
                imageAdapter.setCount(30);
                numOfCards = 30;
                numColumns = 5;
                imageAdapter.setSize(180,180);
                break;

            case "hard":
                imageAdapter.setCount(42);
                numOfCards = 42;
                numColumns = 6;
                imageAdapter.setSize(150,150);
                break;
            default: // Easy
                imageAdapter.setCount(18);
                numOfCards = 18;
                numColumns = 3;
                imageAdapter.setSize(215,215);
        }

        switch (theme) {
            case "math":
                imageAdapter.setImage(R.drawable.hidden_math);
                hidden_image = R.drawable.hidden_math;
                drawable = mathCards;
                getWindow().setBackgroundDrawableResource(R.drawable.background_math);
                actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorMath)));
                break;
            case "time":
                imageAdapter.setImage(R.drawable.hidden_time);
                hidden_image = R.drawable.hidden_time;
                drawable = timeCards;
                getWindow().setBackgroundDrawableResource(R.drawable.background_time);
                actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTime)));

                break;
            default: // flags, country, places
                imageAdapter.setImage(R.drawable.hidden_flag);
                hidden_image = R.drawable.hidden_flag;
                drawable = flagToPlace;
                getWindow().setBackgroundDrawableResource(R.drawable.background_flags);
                actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorFlags)));


        }




        final int[] pos = new int[numOfCards];


        final TextView tv_score_home = findViewById(R.id.tv_score_home);
        final TextView tv_score_guest = findViewById(R.id.tv_score_guest);
        arrow=findViewById(R.id.iv_arrow);



        final ArrayList<Integer> matchedlist = new ArrayList<>(numOfCards); // list of matched cards

        // Randomly init cards
        ArrayList<Integer> list = new ArrayList<>(numOfCards);
        for (int i = 0; i < numOfCards; i++){ // fill first
            list.add(i);
        }


        for (int count = 0; count < numOfCards; count++){
            pos[count] = list.remove((int)(Math.random() * list.size()));
        }

        gridView.setNumColumns(numColumns);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                if (!matchedlist.contains(pos[position])) {
                    if (currentPos < 0 & canContinue) { // first card flipped
                        Methods.flipTheView((RoundedImageView)view,hidden_image,drawable[pos[position]]);
                        Methods.playMusicSingle(mPlayerCardFlip);
                        currentPos = position;
                        secondPos = position;
                        curView = (ImageView) view;
                    } else if (currentPos >= 0 && canContinue) { // Second card flipped

                        if (currentPos == position) { // same card as flipped already
                            //Do nothing


                        }
                        // There is no match
                        else if ((Math.abs(pos[currentPos] - pos[position]) != 1) || (Math.floor((pos[currentPos] + pos[position]) / 2) % 2 != 0)) { // There is no match
                            Methods.flipTheView((RoundedImageView)view,hidden_image,drawable[pos[position]]);
                            Methods.playMusicSingle(mPlayerCardFlip);
                            canContinue = false;
                            // show for a while...
                            // then:
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do this after a while
                                    Methods.flipTheView((RoundedImageView)view,drawable[pos[position]],hidden_image);
                                    Methods.flipTheView((RoundedImageView)curView,drawable[pos[secondPos]],hidden_image);
                                    canContinue = true;


                                }
                            }, 1000);
                            turnChange();
                            currentPos = -1;
                        } else { // There is a match
                            Methods.playMusicSingle(mPlayerCardFlip);
                            Methods.flipTheView((RoundedImageView)view,hidden_image,drawable[pos[position]]);
                            countPair++;

                            // Add the matched cards to list
                            matchedlist.add(pos[currentPos]);
                            matchedlist.add(pos[position]);

                            if (homeFlag) {
                                tv_score_home.setText(Integer.toString(++score_home));
                            } else {
                                tv_score_guest.setText(Integer.toString(++score_guest));
                            }

                            if (countPair == numOfCards / 2) {
                                Methods.winConfetti(viewKonfetti);
                                Methods.playMusicSingle(mPlayerWin);
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MultiPlayer.this);
                                View dialogView = getLayoutInflater().inflate(R.layout.win_dialog, null);

                                TextView tv_won = dialogView.findViewById(R.id.tv_youwon);
                                ImageButton btn_playAgain = dialogView.findViewById(R.id.btn_playagain);
                                ImageButton btn_goToMainScreen = dialogView.findViewById(R.id.btn_gotomainscreen);
                                ImageButton btn_win_scoreboard = dialogView.findViewById(R.id.btn_win_scoreBoard);
                                CircleImageView iv_pic_win = dialogView.findViewById(R.id.iv_pic_win);

                                iv_pic_win.setBorderColor(getResources().getColor(R.color.White));
                                iv_pic_win.setBorderWidth(20);


                                tv_won.setText(getString(R.string.youWon));
                                btn_playAgain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    }
                                });

                                btn_goToMainScreen.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                });

                                btn_win_scoreboard.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FragmentManager fm = getSupportFragmentManager();
                                        RecyclerView rv_scoreboard = findViewById(R.id.recycler_view);
                                        TabbedDialog dialog = new TabbedDialog(rv_scoreboard);
                                        dialog.show(fm,"Dialog");

                                    }
                                });
                                builder.setView(dialogView);
                                android.app.AlertDialog winDialog = builder.create();
                                winDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                winDialog.show();

                                if (score_home > score_guest) {
                                    Methods.setProfilePicMethod(iv_pic_win);
                                } else {
                                    iv_pic_win.setImageResource(R.drawable.guest1);
                                }

                            }
                            turnChange();
                            currentPos = -1;


                        }

                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        (mPlayer=Methods.createMusic(this,R.raw.game_music)).pause();
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(activated==true) {
            Methods.resumeMusic(mPlayer, musicLength);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(musicKey,activated).commit();
        if (activated==true)
            musicLength=Methods.pauseMusic(mPlayer);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }

    public void turnChange() {

        homeFlag = !homeFlag;
        if (homeFlag) { // Now its the host turn
            RotateAnimation rotateAnimation = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setRepeatCount(0);
            rotateAnimation.setDuration(300);
            rotateAnimation.setFillAfter(true);
            arrow.startAnimation(rotateAnimation);

        }
        else { // Now its the guest turn
            RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setRepeatCount(0);
            rotateAnimation.setDuration(300);
            rotateAnimation.setFillAfter(true);
            arrow.startAnimation(rotateAnimation);

        }

    }
}




