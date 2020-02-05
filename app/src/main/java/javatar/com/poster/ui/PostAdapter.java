package javatar.com.poster.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javatar.com.poster.R;
import javatar.com.poster.data.retrofit.PostClient;
import javatar.com.poster.pojo.Post;
import javatar.com.poster.pojo.User;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private static final String TAG = "PostAdapter";

    private List<Post> listPost = new ArrayList<>();

    private Activity activity;

    public PostAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post itemPost = listPost.get(position);
        holder.title.setText(itemPost.getTitle());
        holder.body.setText(itemPost.getBody());
        holder.user.setText(itemPost.getUserId()+"");

        holder.user.setOnClickListener(view -> {
            @SuppressLint("InflateParams")
            View layout = activity.getLayoutInflater().inflate(R.layout.user_info_layout, null);
            AlertDialog dialog = new AlertDialog.Builder(activity).create();
            dialog.setView(layout);

            TextView user_name = layout.findViewById(R.id.user_name);
            TextView user_email = layout.findViewById(R.id.user_email);
            TextView user_phone = layout.findViewById(R.id.user_phone);
            TextView user_website = layout.findViewById(R.id.user_website);

            Single<User> single = PostClient.getPostInstance().getUser(itemPost.getUserId()+"")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            single.subscribe(
                    user-> {
                        user_name.setText(user.getName());
                        user_phone.setText(user.getPhone());
                        user_email.setText(user.getEmail());
                        user_website.setText(user.getWebsite());
                    },
                    e-> Log.d(TAG, "onBindViewHolder: "+e)
            );

            Button cancel = layout.findViewById(R.id.cancel_dialog);
            cancel.setOnClickListener(view1 -> dialog.dismiss());

            dialog.show();
        });

        holder.comments.setOnClickListener(view ->{
            HomeActivity.postViewModel.getComments(itemPost.getId()+"");
            HomeActivity.postViewModel.setState();
        });

        holder.save.setOnClickListener(view -> {
            HomeActivity.postViewModel.insert(itemPost);
            Toast.makeText(activity, "Post saved", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public void setList(List<Post> listPost) {
        this.listPost = listPost;
        notifyDataSetChanged();
    }

    public Post getPostAt(int position) {
        return listPost.get(position);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title,body,user,save,comments;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_textView);
            body = itemView.findViewById(R.id.body_textView);
            user = itemView.findViewById(R.id.user_textView);
            save = itemView.findViewById(R.id.save_textView);
            comments = itemView.findViewById(R.id.comments_textView);
        }
    }
}