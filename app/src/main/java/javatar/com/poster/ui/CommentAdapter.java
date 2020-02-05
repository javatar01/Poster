package javatar.com.poster.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javatar.com.poster.R;
import javatar.com.poster.pojo.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> listComment = new ArrayList<>();

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment itemComment = listComment.get(position);

        holder.comment_text.setText(itemComment.getBody());
    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    public void setList(List<Comment> listComment) {
        this.listComment = listComment;
        notifyDataSetChanged();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView comment_text;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_text = itemView.findViewById(R.id.comment_text);
        }
    }

    public void clear(){
        listComment.clear();
        notifyDataSetChanged();
    }
}