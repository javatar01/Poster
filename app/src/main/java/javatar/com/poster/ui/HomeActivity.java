package javatar.com.poster.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import javatar.com.poster.R;
import javatar.com.poster.pojo.Post;

public class HomeActivity extends AppCompatActivity {

    public static PostViewModel postViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);

        postViewModel.getPosts();

        //Posts Online------------------------------------------------
        final PostAdapter adapter = new PostAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_post);

        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView.setAdapter(adapter);
        postViewModel.postsMutableLiveData.observe(this, adapter::setList);

        //Posts Offline------------------------------------------------
        final PostAdapter adapterOffline = new PostAdapter(this);
        RecyclerView recyclerPostOffline = findViewById(R.id.recycler_post_offline);

        recyclerPostOffline.setLayoutManager(new GridLayoutManager(this,1));
        recyclerPostOffline.setAdapter(adapterOffline);

        postViewModel.getAllPostsOffline().observe(this, adapterOffline::setList);

        //Comments------------------------------------------------
        final CommentAdapter adapterComments = new CommentAdapter();
        RecyclerView recyclerViewComments = findViewById(R.id.recycler_comment);

        recyclerViewComments.setLayoutManager(new GridLayoutManager(this,1));
        recyclerViewComments.setAdapter(adapterComments);

        postViewModel.CommentsMutableLiveData.observe(this, adapterComments::setList);

        //BottomSheet------------------------------------------------
        LinearLayout llBottomSheet =  findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setHideable(false);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {


                if (i==4) adapterComments.clear();
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        postViewModel.sheetState.observe(this,ch->{
            if (ch) bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        Switch aSwitch = findViewById(R.id.switch_offline);
        TextView clear = findViewById(R.id.clear);

        clear.setOnClickListener(view -> {
            postViewModel.deleteAllPosts();
            Toast.makeText(this, "All posts deleted", Toast.LENGTH_SHORT).show();
        });

        aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                recyclerView.setVisibility(View.GONE);
                recyclerPostOffline.setVisibility(View.VISIBLE);
                clear.setVisibility(View.VISIBLE);
            } else{
                recyclerView.setVisibility(View.VISIBLE);
                recyclerPostOffline.setVisibility(View.GONE);
                clear.setVisibility(View.GONE);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,@NonNull RecyclerView.ViewHolder viewHolder,@NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                postViewModel.delete(adapterOffline.getPostAt(viewHolder.getAdapterPosition()));
                Toast.makeText(HomeActivity.this, "Post deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerPostOffline);
    }
}
