package com.maxprof90.redditarticles.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxprof90.redditarticles.FullScreenViewActivity;
import com.maxprof90.redditarticles.R;
import com.maxprof90.redditarticles.models.Children;
import com.maxprof90.redditarticles.utils.DownloadImageTask;

import java.util.List;

public class RedditPostAdapter extends RecyclerView.Adapter<RedditPostAdapter.ViewHolder> {
    private static final String TAG = "Reddit";
    Context context;
    List<Children> childrenList;

    public RedditPostAdapter(Context context, List<Children> childrenList) {
        this.context = context;
        this.childrenList = childrenList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Children post = childrenList.get(position);
        bindPost(post, holder);

    }

    @Override
    public int getItemCount() {
        return childrenList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView authorName, createdAt, titleTv, commentNum;
        ImageView thumbIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            authorName = itemView.findViewById(R.id.author);
            createdAt = itemView.findViewById(R.id.publishedAt);
            commentNum = itemView.findViewById(R.id.comments_num);
            thumbIv = itemView.findViewById(R.id.img);
            titleTv = itemView.findViewById(R.id.title);


        }
    }

    private void bindPost(final Children children, ViewHolder holder) {
        String authorName = children.getData().getAuthor();

        long createdUtc = (long) children.getData().getCreatedUtc() * 1000;
        CharSequence ago = DateUtils.getRelativeTimeSpanString(createdUtc, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);

        String commentNum = String.valueOf(children.getData().getNumComments());
        final String thumbUri = children.getData().getThumbnail();
        String title = children.getData().getTitle();
        holder.authorName.setText(authorName);
        holder.createdAt.setText(ago);
        holder.commentNum.setText(commentNum);
        holder.titleTv.setText(title);
        if (children.getData().getThumbBitmap() == null)
            new DownloadImageTask(holder.thumbIv, children).execute(thumbUri);
        else {
            // used when the app restore the saved state, it get the thumbnail img from the object instead of downloading it again
            holder.thumbIv.setVisibility(View.VISIBLE);
            holder.thumbIv.setImageBitmap(children.getData().getThumbBitmap());
        }

        if (children.getData().getImgURl() != null) {
            holder.thumbIv.setOnClickListener((View view) -> {
                if (!children.getData().isVideo()) {
                    Intent intent = new Intent(context, FullScreenViewActivity.class);
                    intent.putExtra("IMG_URL", children.getData().getImgURl());
                    context.startActivity(intent);
                } else if (children.getData().isVideo()) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(children.getData().getImgURl()));
                    context.startActivity(browserIntent);

                }

            });
        }

    }
}
