//package com.example.tensoue.matchit;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Priority;
//import com.bumptech.glide.Registry;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.load.DataSource;
//import com.bumptech.glide.load.Key;
//import com.bumptech.glide.load.Option;
//import com.bumptech.glide.load.data.DataFetcher;
//import com.bumptech.glide.load.model.ModelLoader;
//import com.bumptech.glide.load.model.ModelLoaderFactory;
//import com.bumptech.glide.load.model.MultiModelLoaderFactory;
//import com.bumptech.glide.module.AppGlideModule;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.StreamDownloadTask;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//import java.security.MessageDigest;
//
//
//
//@GlideModule
//public class MyAppGlideModule extends AppGlideModule {
//
//    @Override
//    public void registerComponents(Context context, Glide glide, Registry registry) {
//        // Register FirebaseImageLoader to handle StorageReference
//        registry.append(StorageReference.class, InputStream.class,
//                new FirebaseImageLoader.Factory());
//    }
//}