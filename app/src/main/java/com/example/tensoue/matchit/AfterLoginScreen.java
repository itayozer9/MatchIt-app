package com.example.tensoue.matchit;


import com.bumptech.glide.Glide;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.service.autofill.FieldClassification;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class AfterLoginScreen extends AppCompatActivity {

    Menu menu;
    String spname="mainsp";
    SharedPreferences prefs;
    String musicKey = "musicactivated";
    boolean activated;
    MediaPlayer mPlayer;
    int musicLength=0;
    String nickname;
    TextView tv_welcome;
    CircleImageView iv_profilePic;
    Bitmap profilePic;
    String chosen_theme = "";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
    CallbackManager mCallbackManager;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReferenceFromUrl("https://matchit-8abc0.firebaseio.com/");


    public Intent startGame(String level, String theme, String mode) {
        Intent intent;
        Log.e("theme", theme);
        if (mode == "solo")
            intent = new Intent(AfterLoginScreen.this, SinglePlayer.class);
        else
            intent = new Intent(AfterLoginScreen.this, MultiPlayer.class);

        intent.putExtra("level", level);
        intent.putExtra("theme", theme);
        return (intent);

    }

    private void sendToFirstScreen() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void signOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AfterLoginScreen.this);
        builder.setMessage(getString(R.string.AreYouSureOut)).setTitle(getString(R.string.SignOut));
        builder.setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
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


        if (item.getItemId() == R.id.action_signout) {
            signOutDialog();
        } else if (item.getItemId() == R.id.action_scoreboard) {
            FragmentManager fm = getSupportFragmentManager();
            RecyclerView rv_scoreboard = findViewById(R.id.recycler_view);
            TabbedDialog dialog = new TabbedDialog(rv_scoreboard);
            dialog.show(fm, "Dialog");
        } else if (item.getItemId() == R.id.action_sound) {
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
        getMenuInflater().inflate(R.menu.action_menu, menu);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(spname,MODE_PRIVATE);
        activated = prefs.getBoolean(musicKey,true);
        final Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Simplehandwritting-Regular.ttf");
        setContentView(R.layout.activity_after_login_screen);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("");
        final ViewGroup afterLoginLayout = findViewById(R.id.layout_afterloginscreen);
        Methods.setFont(afterLoginLayout, font);
        tv_welcome = findViewById(R.id.tv_welcome);
        iv_profilePic = findViewById(R.id.iv_profilePicAfterLogin);
        Button btn_solo = findViewById(R.id.btn_solo);
        Button btn_multi = findViewById(R.id.btn_multi);

        iv_profilePic.setBorderWidth(10);
        iv_profilePic.setBorderColor(getResources().getColor(R.color.colorSecondary));


        Intent intent = getIntent();
        nickname = firebaseUser.getDisplayName();
        Methods.setProfilePicMethod(iv_profilePic);
        tv_welcome.setText(getString(R.string.Welcome) + firebaseUser.getDisplayName());

        btn_solo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosen_theme = "country";
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AfterLoginScreen.this);
                View dialogView = getLayoutInflater().inflate(R.layout.startgame_dialog, null);

                Button btn_easy = dialogView.findViewById(R.id.btn_easy);
                Button btn_medium = dialogView.findViewById(R.id.btn_medium);
                Button btn_hard = dialogView.findViewById(R.id.btn_hard);
                NumberPicker np_theme = dialogView.findViewById(R.id.np_theme);
                np_theme.setMinValue(1);
                np_theme.setMaxValue(3);
                np_theme.setDisplayedValues(new String[]{getString(R.string.Country), getString(R.string.Math), getString(R.string.Time)});
                np_theme.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        switch (i1) {
                            case 1:
                                chosen_theme = "country";
                                break;
                            case 2:
                                chosen_theme = "math";
                                break;
                            case 3:
                                chosen_theme = "time";
                                break;
                            default:
                                chosen_theme = "country";
                                break;
                        }
                    }
                });
                btn_easy.setTypeface(font);
                btn_medium.setTypeface(font);
                btn_hard.setTypeface(font);


                TextView dialogStartGameTV = new TextView(AfterLoginScreen.this);
                dialogStartGameTV.setTypeface(font);
                dialogStartGameTV.setGravity(Gravity.CENTER);
                dialogStartGameTV.setText(getString(R.string.StartGame));
                dialogStartGameTV.setTextSize(20);
                dialogStartGameTV.setPadding(10, 10, 10, 10);
                builder.setView(dialogView).setCustomTitle(dialogStartGameTV).show().getWindow().setLayout(700, 1200);
                btn_easy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(startGame("easy", chosen_theme, "solo"));

                    }
                });
                btn_medium.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(startGame("medium", chosen_theme, "solo"));
                    }
                });
                btn_hard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(startGame("hard", chosen_theme, "solo"));
                    }
                });


            }
        });


        btn_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AfterLoginScreen.this);
                View dialogView = getLayoutInflater().inflate(R.layout.startgame_dialog, null);
                chosen_theme = "country";
                Button btn_easy = dialogView.findViewById(R.id.btn_easy);
                Button btn_medium = dialogView.findViewById(R.id.btn_medium);
                Button btn_hard = dialogView.findViewById(R.id.btn_hard);
                NumberPicker np_theme = dialogView.findViewById(R.id.np_theme);
                np_theme.setMinValue(1);
                np_theme.setMaxValue(3);
                np_theme.setDisplayedValues(new String[]{getString(R.string.Country), getString(R.string.Math), getString(R.string.Time)});
                np_theme.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        switch (i1) {
                            case 1:
                                chosen_theme = "country";
                                break;
                            case 2:
                                chosen_theme = "math";
                                break;
                            case 3:
                                chosen_theme = "time";
                                break;
                            default:
                                chosen_theme = "country";
                                break;
                        }
                    }
                });


                btn_easy.setTypeface(font);
                btn_medium.setTypeface(font);
                btn_hard.setTypeface(font);


                TextView dialogStartGameTV = new TextView(AfterLoginScreen.this);
                dialogStartGameTV.setTypeface(font);
                dialogStartGameTV.setGravity(Gravity.CENTER);
                dialogStartGameTV.setText(getString(R.string.StartGame));
                dialogStartGameTV.setTextSize(20);
                dialogStartGameTV.setPadding(10, 10, 10, 10);
                builder.setView(dialogView).setCustomTitle(dialogStartGameTV).show().getWindow().setLayout(700, 1200);
                btn_easy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(startGame("easy", chosen_theme, "multi"));

                    }
                });
                btn_medium.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(startGame("medium", chosen_theme, "multi"));
                    }
                });
                btn_hard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(startGame("hard", chosen_theme, "multi"));
                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        (mPlayer=Methods.createMusic(this,R.raw.menu)).pause();
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activated = prefs.getBoolean(musicKey,true);
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
        protected void onStop () {
            super.onStop();
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }
}


