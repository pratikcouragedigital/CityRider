package mobitechs.cityriders.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mobitechs.cityriders.R;
import mobitechs.cityriders.YouTubeFragmentActivity;
import mobitechs.cityriders.expandableText.ExpandableText;
import mobitechs.cityriders.model.VideoItems;

public class VideoListAdapter extends RecyclerView.Adapter {

    private final FragmentActivity activity;
    List<VideoItems> videoItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private static final int VIEW_TYPE_EMPTY = 1;

    public VideoListAdapter(List<VideoItems> items, FragmentActivity activity) {
        this.videoItems = items;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_EMPTY) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_layout, viewGroup, false);
            EmptyViewHolder emptyViewHolder = new EmptyViewHolder(v);
            return emptyViewHolder;
        }
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tab_video_list_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            VideoItems itemOfVideos = videoItems.get(position);
            vHolder.bindListDetails(itemOfVideos);
        }
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (videoItems.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout videoListRecyclerView;
        public ImageView videoImage;
        public ImageView channelImage;
        public TextView videoTitle;
        public TextView channelTitle;
        public TextView publishedDate;
        public TextView description;

        VideoItems videoItems = new VideoItems();
        private String videoId;

        public ViewHolder(View itemView) {
            super(itemView);

            videoImage = (ImageView) itemView.findViewById(R.id.videoImage);
            channelImage = (ImageView) itemView.findViewById(R.id.channelImage);
            videoTitle = (TextView) itemView.findViewById(R.id.videoTitle);
            channelTitle = (TextView) itemView.findViewById(R.id.channelTitle);
            publishedDate = (TextView) itemView.findViewById(R.id.publishedDate);
            description = (TextView) itemView.findViewById(R.id.description);
            videoListRecyclerView = (RelativeLayout) itemView.findViewById(R.id.videoListRecyclerView);
            videoListRecyclerView.setOnClickListener(this);
        }

        public void bindListDetails(VideoItems videoItems) {
            this.videoItems = videoItems;

            final ProgressBar imageLoader;
            imageLoader = (ProgressBar) itemView.findViewById(R.id.loading);

            String imagepath = videoItems.getVideoImage();
            if (imagepath == null || imagepath.equals("")){
                videoImage.setImageResource(R.drawable.no_thumbnail);
                imageLoader.setVisibility(View.GONE);
            }
            else{
                RequestOptions options = new RequestOptions().centerCrop().error(R.drawable.no_thumbnail);

                Glide.with(videoImage.getContext())
                        .asBitmap()
                        .load(videoItems.getVideoImage())
                        .apply(options)
                        .listener(new RequestListener<Bitmap>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                imageLoader.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                imageLoader.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(new BitmapImageViewTarget(videoImage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                videoImage.setImageBitmap(resource);
                            }
                        });


            }

            channelImage.setImageResource(R.drawable.ic_launcher_48);
            videoTitle.setText(videoItems.getTitle());
            channelTitle.setText("City Riders");
            description.setText(videoItems.getDescription());

            String strDate = videoItems.getPublishedDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            try {
                Date date = dateFormat.parse(strDate);
                String fDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
                publishedDate.setText(fDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.videoListRecyclerView){
                videoId = videoItems.getVideoId();
                Intent fragIntent = new Intent(activity, YouTubeFragmentActivity.class);
                fragIntent.putExtra("videoID", videoId);
                activity.startActivity(fragIntent);
//                activity.startActivity(YouTubeStandalonePlayer.createVideoIntent(activity, DEVELOPER_KEY, videoId));
//                fragmentYoutubeView.setVisibility(View.VISIBLE);
//                if(videoBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//
//                    videoBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                }
//                Bundle args = new Bundle();
//                args.putString("videoId", videoId);
//                VideoFragmentBottomSheet videoFragmentBottomSheet = new VideoFragmentBottomSheet();
//                videoFragmentBottomSheet.setArguments(args);
//                videoFragmentBottomSheet.show(activity.getSupportFragmentManager(),"BottomSheet Fragment");
            }
        }


    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View v) {
            super(v);
            TextView emptyTextView;
            emptyTextView = (TextView) v.findViewById(R.id.emptyTextView);
            emptyTextView.setText("Youtube List Not Available");
        }
    }
}
