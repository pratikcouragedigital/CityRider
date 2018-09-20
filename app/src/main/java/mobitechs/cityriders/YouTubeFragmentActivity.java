package mobitechs.cityriders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YouTubeFragmentActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView youtubeView;
    private String videoId;
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayer player;
    private TextView videoTitle;
    private TextView videoViews;
    private TextView videoDescription;
    private String apiKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_fragment_view);

        apiKey = Config.DEVELOPER_KEY;
        videoId = getIntent().getStringExtra("videoID");

        youtubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        videoViews = (TextView) findViewById(R.id.videoViews);
        videoDescription = (TextView) findViewById(R.id.videoDescription);

        youtubeView.initialize(apiKey, this);

        getVideoDetails();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            player = youTubePlayer;
            //player.cueVideo(videoId);
            setVideoId(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String errorMessage = String.format("There was an error initializing the YouTubePlayer (%1$s)", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    public void setVideoId(String videoId) {
        if (player != null) {
            player.loadVideo(videoId);
        }
    }

    private void getVideoDetails() {
        String url = "https://www.googleapis.com/youtube/v3/videos?key=" + apiKey + "&part=snippet,id,statistics&id=" + videoId + "";
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
                                String kind = object.getString("kind");
                                if(kind.equals("youtube#video")) {
                                    String snippetString = object.getString("snippet");
                                    JSONObject snippetObject = new JSONObject(snippetString);
                                    videoTitle.setText(snippetObject.getString("title"));
                                    videoDescription.setText(snippetObject.getString("description"));

                                    String statisticsString = object.getString("statistics");
                                    JSONObject statisticsObject = new JSONObject(statisticsString);
                                    videoViews.setText(statisticsObject.getString("viewCount") + " views");
                                }
                            }
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
