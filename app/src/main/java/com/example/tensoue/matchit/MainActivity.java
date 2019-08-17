package com.example.tensoue.matchit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaCas;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.se.omapi.Session;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference storageReference;
    CallbackManager mCallbackManager;
    String nickname;
    Uri filePath;
    final int PICK_IMAGE_REQUEST = 1;
    Button btn_register;
    Button btn_login;
    Button reg_btn_upload;
    Button reg_btn_register;

    EditText login_et_email;
    EditText login_et_password;
    EditText reg_et_email;
    EditText reg_et_password;
    EditText reg_et_nickname;

    ImageView reg_iv_profilePic;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener authStateListener;


    private void AfterLoginActivity(){
        Intent intent = new Intent (getApplicationContext(),AfterLoginScreen.class);
        intent.putExtra("nickname",nickname);
        startActivity(intent);
        finish();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(){

        if(filePath != null)
        {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getString(R.string.Uploading));
            progressDialog.show();
            final String userUUID = firebaseAuth.getCurrentUser().getUid().toString();
            final StorageReference ref = storageReference.child("images/" + userUUID);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            scoreboardRow row = new scoreboardRow(userUUID,firebaseAuth.getCurrentUser().getDisplayName(),uri.toString());
                            Methods.createNewUserRecord(row);
                            AfterLoginActivity();
                            Toast.makeText(MainActivity.this, getString(R.string.ImageUploaded), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(getString(R.string.Uploaded) + (int)progress+"%");

                        }
                    });
        }

    }

    private void handleFacebookAccessToken(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String username = firebaseAuth.getCurrentUser().getDisplayName();
                            FirebaseUser user = firebaseAuth.getCurrentUser();


                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(isNewUser){
                                String picString = "https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?width=200&height=200";
                                scoreboardRow row = new scoreboardRow(user.getUid(),user.getDisplayName(), picString);
                                Methods.createNewUserRecord(row);
                            }

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, getString(R.string.Authentication_failed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                reg_iv_profilePic.setImageBitmap(bitmap);
                reg_iv_profilePic.setVisibility(View.VISIBLE);
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("");
        final Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Simplehandwritting-Regular.ttf");
        final ViewGroup mainActivityLayout = findViewById(R.id.layout_main_activity);
        Methods.setFont(mainActivityLayout,font);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton btn_facebook_login = findViewById(R.id.btn_facebook_login);
        btn_facebook_login.setReadPermissions("email", "public_profile");

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, getString(R.string.CancelLoginfacebook), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, getString(R.string.ErrorLoginFacebook), Toast.LENGTH_SHORT).show();

            }
        });


        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        login_et_email = findViewById(R.id.et_email);
        login_et_password = findViewById(R.id.et_password);




        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Inflating the dialog and getting the inputs//
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View dialogView = getLayoutInflater().inflate(R.layout.register_dialog,null);

                reg_et_email = dialogView.findViewById(R.id.reg_et_email);
                reg_et_password = dialogView.findViewById(R.id.reg_et_password);
                reg_et_nickname = dialogView.findViewById(R.id.reg_et_nickname);
                reg_btn_upload = dialogView.findViewById(R.id.reg_btn_upload);
                reg_iv_profilePic = dialogView.findViewById(R.id.reg_iv_profilePic);
                reg_btn_register = dialogView.findViewById(R.id.reg_btn_register);
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                reg_btn_register.setTypeface(font);
                reg_btn_upload.setTypeface(font);

                reg_btn_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chooseImage();

                    }
                });

                reg_btn_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = reg_et_email.getText().toString().trim();

                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                        if (reg_et_nickname.getText().toString().trim().length() == 0) {
                            Toast.makeText(MainActivity.this, getString(R.string.ValidName), Toast.LENGTH_SHORT).show();
                        }
                        else if (reg_et_password.getText().toString().trim().length() < 6) {
                            Toast.makeText(MainActivity.this, getString(R.string.ValidPass), Toast.LENGTH_SHORT).show();
                        }
                        else if (reg_et_email.getText().toString().trim().length() == 0) {
                            Toast.makeText(MainActivity.this, getString(R.string.ValidEmail), Toast.LENGTH_SHORT).show();
                        }
                        else if (!email.matches(emailPattern)) {
                            Toast.makeText(MainActivity.this, getString(R.string.ValidEmail), Toast.LENGTH_LONG).show();
                        }
                        else if (reg_iv_profilePic.getDrawable() == null) {
                            Toast.makeText(MainActivity.this, getString(R.string.ValidPic), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            final String reg_email = reg_et_email.getText().toString();
                            String reg_password = reg_et_password.getText().toString();
                            nickname = reg_et_nickname.getText().toString();
                            //register the user//
                            firebaseAuth.createUserWithEmailAndPassword(reg_email, reg_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                    } else {
                                        nickname = "";
                                    }
                                }
                            });


                        }
                    }
                    });
                builder.setView(dialogView).setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog reg_dialog = builder.create();

                reg_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                reg_dialog.getWindow().setLayout(300,1650);
                reg_dialog.show();
            }
        });

                btn_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String email = login_et_email.getText().toString();
                        String password = login_et_password.getText().toString();
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                        if (email.trim().length() == 0) {
                            Toast.makeText(MainActivity.this, getString(R.string.ValidEmail), Toast.LENGTH_SHORT).show();
                        }
                        else if (!email.trim().matches(emailPattern)) {
                            Toast.makeText(MainActivity.this, getString(R.string.ValidEmail), Toast.LENGTH_SHORT).show();
                        }
                        else if (password.trim().length() < 6) {
                            Toast.makeText(MainActivity.this, getString(R.string.ValidPass), Toast.LENGTH_SHORT).show();
                        }

                        else{
                            //signin in the user
                            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {}
                                    else
                                        Toast.makeText(MainActivity.this, getString(R.string.IncorrectUsernamePass), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });



        //On every auth status change//
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    if(nickname != null){  //user has just been registered
                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(nickname).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Toast.makeText(MainActivity.this, getString(R.string.Welcome) + nickname, Toast.LENGTH_SHORT).show();
                            }
                        });
                        nickname = user.getDisplayName();

                        uploadImage();
                    }
                    else {
                        nickname = user.getDisplayName();

                        AfterLoginActivity();
                    }

                }
                else {
                }
            }
        };


    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
