package com.rickh.redditapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SubredditAdapter extends RecyclerView.Adapter<SubredditAdapter.SubmissionHolder> {


    private static final DecimalFormat THOUSANDS_FORMATTER = new DecimalFormat("#.#k");
    private static final DecimalFormat MILLIONS_FORMATTER = new DecimalFormat("#.#m");

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
        submissionHolder.bind(submission);
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
        private ImageView thumbnailImageView;
        private TextView titleTextView;
        private TextView detailTextView;

        public SubmissionHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.submission_item_icon);
            titleTextView = itemView.findViewById(R.id.submission_item_title);
            detailTextView = itemView.findViewById(R.id.submission_item_byline);
        }

        private void bind(Submission submission) {
            String thumbnail = submission.getThumbnail();
            if (thumbnail.equals("self") || thumbnail.equals("")) {
                thumbnailImageView.setVisibility(View.GONE);
            } else {
                Glide.with(mContext)
                        .load(submission.getThumbnail())
                        .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(100)))
                        .into(thumbnailImageView);
            }

            String scoreText = abbreviateScore(submission.getScore());
            String titleText = scoreText + " " + submission.getTitle();

            Spannable spannable = new SpannableString(titleText);
            spannable.setSpan(new ForegroundColorSpan(mContext.getColor(R.color.colorScore)), 0, scoreText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleTextView.setText(spannable, TextView.BufferType.SPANNABLE);

            String detailText = "R/" + submission.getSubreddit().toUpperCase()
                    + " • " + submission.getAuthor()
                    + " • " + submission.getCommentCount() + " COMMENTS";
            detailTextView.setText(detailText);
        }
    }

    public static String abbreviateScore(float score) {
        if (score % 1 != 0) {
            throw new UnsupportedOperationException("Decimals weren't planned to be supported");
        }

        if (score < 1_000) {
            return String.valueOf((int) score);
        } else if (score < 1_000_000) {
            return THOUSANDS_FORMATTER.format(score / 1_000);
        } else {
            return MILLIONS_FORMATTER.format(score / 1_000_000f);
        }
    }
}
