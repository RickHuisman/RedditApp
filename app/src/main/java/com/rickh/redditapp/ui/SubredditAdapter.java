package com.rickh.redditapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rickh.redditapp.R;

import net.dean.jraw.models.Submission;

import java.util.ArrayList;

public class SubredditAdapter extends RecyclerView.Adapter<SubredditAdapter.SubmissionHolder> {

    private ArrayList<Submission> mSubmissions = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public SubmissionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_submission_content, viewGroup, false);

        return new SubmissionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmissionHolder submissionHolder, int position) {
        Submission submission = mSubmissions.get(position);

        Glide.with(mContext)
                .load(submission.getThumbnail())
                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(100)))
                .into(submissionHolder.post_thumbnail);

        submissionHolder.post_title.setText(submission.getTitle() + " " + submission.getScore());

//        String detail = "R/" + submission.getSubredditName().toUpperCase()
//                + " • " + submission.getAuthor()
//                + " • " + submission.getCommentCount() + " COMMENTS";
//        submissionHolder.post_detail.setText(detail);
    }

    @Override
    public int getItemCount() {
        return mSubmissions.size();
    }

    public void setSubmissions(ArrayList<Submission> submissions) {
        this.mSubmissions = submissions;

        notifyDataSetChanged();
    }

    public class SubmissionHolder extends RecyclerView.ViewHolder {
        private ImageView post_thumbnail;
        private TextView post_title;
        private TextView post_detail;

        public SubmissionHolder(@NonNull View itemView) {
            super(itemView);
            post_thumbnail = itemView.findViewById(R.id.submission_thumbnail);
            post_title = itemView.findViewById(R.id.submission_title);
            post_detail = itemView.findViewById(R.id.submission_detail);
        }
    }
}
