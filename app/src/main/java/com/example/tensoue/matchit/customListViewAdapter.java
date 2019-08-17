//package com.example.tensoue.matchit;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import org.w3c.dom.Text;
//
//import java.util.List;
//
//public class customListViewAdapter extends ArrayAdapter<scoreboardRow>{
//    private int layoutResource;
//
//    public customListViewAdapter(Context context, int layoutResource, List<scoreboardRow> rowList){
//        super(context, layoutResource, rowList);
//        this.layoutResource = layoutResource;
//
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = convertView;
//
//        if (view == null){
//            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
//            view = layoutInflater.inflate(layoutResource, null);
//        }
//
//        scoreboardRow row = getItem(position);
//
//        if (row != null){
//            TextView username = (TextView) view.findViewById(R.id.tv_username_scoreboard);
//            TextView highestScore = (TextView) view.findViewById(R.id.tv_highestscore_scoreboard);
//            ImageView profilePic = (ImageView) view.findViewById(R.id.iv_pic_scoreboard);
//
//            if(username != null)
//                username.setText(row.getUsername());
//            if(highestScore != null)
//                highestScore.setText(String.valueOf(row.getHighestScore()));
//            if(profilePic != null)
//                Methods.setProfilePicMethod(profilePic,row.getProfilePicURL());
//        }
//
//        return view;
//    }
//
//}
