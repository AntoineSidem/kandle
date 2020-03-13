package ch.epfl.sdp.kandle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private static ClickListener clickListener;
    private List<Post> mPosts;

    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }


    public void setOnItemClickListener(ClickListener clickListener) {
        PostAdapter.clickListener = clickListener;
    }
    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View postsView = inflater.inflate(R.layout.post_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(postsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // Set item views based on your views and data model
        TextView titleView = holder.mtitleText;
        titleView.setText(String.valueOf(post.getPost_id()));
        TextView dateView = holder.mdate;
        dateView.setText((dateFormat.format(post.getDate())));
        TextView likeView = holder.mlikes;
        likeView.setText(String.valueOf(post.getLikes()));


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mtitleText;
        public TextView mlikes;
        public TextView mdate;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mtitleText = (TextView) itemView.findViewById(R.id.title);
            mlikes = (TextView) itemView.findViewById(R.id.flames);
            mdate = (TextView) itemView.findViewById(R.id.date_and_time);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(),v);
        }
    }

}