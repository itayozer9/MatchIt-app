//package com.example.tensoue.matchit;
//
//import android.app.Service;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnErrorListener;
//import android.net.Uri;
//import android.os.Binder;
//import android.os.IBinder;
//import android.widget.Toast;
//
//import java.io.IOException;
//
//public class SoundService extends Service  implements MediaPlayer.OnErrorListener {
//
//    private final IBinder mBinder = new ServiceBinder();
//    MediaPlayer mPlayer;
//    private int length = 0;
//
//    public SoundService() {
//    }
//
//    public class ServiceBinder extends Binder {
//        SoundService getService() {
//            return SoundService.this;
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent arg0) {
//        return mBinder;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mPlayer = MediaPlayer.create(this,R.raw.menu);
//        if (mPlayer != null) {
//            mPlayer.setLooping(true);
//            mPlayer.setVolume(100, 100);
//        }
//
//
//        mPlayer.setOnErrorListener(this);
//
//
//        mPlayer.setOnErrorListener(new OnErrorListener() {
//
//            public boolean onError(MediaPlayer mp, int what, int
//                    extra) {
//
//                onError(mPlayer, what, extra);
//                return true;
//            }
//        });
//
////        int sound = (int)intent.getExtras().get("music");
////        mPlayer = MediaPlayer.create(this, sound);
//////        mPlayer = MediaPlayer.create(this,R.raw.menu);
////        if (mPlayer != null) {
////            mPlayer.setLooping(true);
////            mPlayer.setVolume(100, 100);
////            try {
////                Uri soundUri = Uri.parse("android.resource://com.example.tensoue.matchit/raw/menu");
////                mPlayer.setDataSource(this,soundUri);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
////
////
////
////        mPlayer.setOnErrorListener(this);
////
////
////        mPlayer.setOnErrorListener(new OnErrorListener() {
////
////            public boolean onError(MediaPlayer mp, int what, int
////                    extra) {
////
////                onError(mPlayer, what, extra);
////                return true;
////            }
////        });
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        mPlayer.start();
//        return START_STICKY;
//    }
//
//    public void changeMusic()
//    {
//        if(mPlayer.isPlaying()) {
//            Uri soundUri = Uri.parse("android.resource://com.example.tensoue.matchit/raw/game_music");
//            try {
//                mPlayer.setDataSource(this,soundUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public void pauseMusic() {
//        if (mPlayer.isPlaying()) {
//            mPlayer.pause();
//            length = mPlayer.getCurrentPosition();
//
//        }
//    }
//
//    public void resumeMusic() {
//        if (mPlayer.isPlaying() == false) {
//            mPlayer.seekTo(length);
//            mPlayer.start();
//        }
//    }
//
//    public boolean isPlaying(){
//        return mPlayer.isPlaying();
//    }
//
//    public void stopMusic() {
//        mPlayer.stop();
//        mPlayer.release();
//        mPlayer = null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mPlayer != null) {
//            try {
//                mPlayer.stop();
//                mPlayer.release();
//            } finally {
//                mPlayer = null;
//            }
//        }
//    }
//
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//
//       // Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
//        if (mPlayer != null) {
//            try {
//                mPlayer.stop();
//                mPlayer.release();
//            } finally {
//                mPlayer = null;
//            }
//        }
//        return false;
//    }
//}