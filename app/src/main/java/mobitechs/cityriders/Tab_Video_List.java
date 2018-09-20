package mobitechs.cityriders;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import mobitechs.cityriders.SessionManger.SessionManager;
import mobitechs.cityriders.adapter.VideoListAdapter;
import mobitechs.cityriders.model.VideoItems;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Tab_Video_List extends Fragment {
	
	private View v;
    private RecyclerView videoListRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter videoListAdapter;
    private List<VideoItems> videoItems = new ArrayList<VideoItems>();
    private String channelID;
    private String apiKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_video_list, container, false);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        channelID = Config.CHANNEL_ID;
        apiKey = Config.DEVELOPER_KEY;

        videoListRecyclerView = (RecyclerView) v.findViewById(R.id.videoListRecyclerView);
        videoListRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        videoListRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(v.getContext());
        videoListRecyclerView.setLayoutManager(linearLayoutManager);

        videoListRecyclerView.smoothScrollToPosition(0);
        videoListAdapter = new VideoListAdapter(videoItems, getActivity());
        videoListRecyclerView.setAdapter(videoListAdapter);

        getChannelList();
    }

    private void getChannelList() {
        String url = "https://www.googleapis.com/youtube/v3/search?key=" + apiKey + "&channelId=" + channelID + "&part=snippet,id&order=date&maxResults=50";
        AndroidNetworking.get(url)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray videoItemsArray = response.getJSONArray("items");
                            for(int i = 0; i < videoItemsArray.length(); i ++) {
                                JSONObject object = videoItemsArray.getJSONObject(i);
                                String idString = object.getString("id");
                                JSONObject idObject = new JSONObject(idString);
                                String kind = idObject.getString("kind");
                                if(kind.equals("youtube#video")) {
                                    VideoItems videoItem = new VideoItems();
                                    videoItem.setVideoId(idObject.getString("videoId"));

                                    String snippetString = object.getString("snippet");
                                    JSONObject snippetObject = new JSONObject(snippetString);
                                    videoItem.setPublishedDate(snippetObject.getString("publishedAt"));
                                    videoItem.setTitle(snippetObject.getString("title"));
                                    videoItem.setDescription(snippetObject.getString("description"));

                                    String thumbnailString = snippetObject.getString("thumbnails");
                                    JSONObject thumbnailObject = new JSONObject(thumbnailString);
                                    String highThumbnailString = thumbnailObject.getString("high");
                                    JSONObject highThumbnailObject = new JSONObject(highThumbnailString);
                                    videoItem.setVideoImage(highThumbnailObject.getString("url"));
                                    videoItems.add(videoItem);
                                }
                            }
                            videoListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
