package javatar.com.poster.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javatar.com.poster.data.retrofit.PostClient;
import javatar.com.poster.data.room.PostRepository;
import javatar.com.poster.pojo.Comment;
import javatar.com.poster.pojo.Post;

public class PostViewModel extends AndroidViewModel {

    MutableLiveData<List<Post>> postsMutableLiveData = new MutableLiveData<>();

    MutableLiveData<List<Comment>> CommentsMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Boolean> sheetState = new MutableLiveData<>();

    private static final String TAG = "PostViewModel";
    @SuppressLint("CheckResult")
    void getPosts(){
        Single<List<Post>> single = PostClient.getPostInstance().getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        single.subscribe(
          o-> postsMutableLiveData.setValue(o),
          e-> Log.d(TAG, "getPosts: "+e)
        );

    }

    @SuppressLint("CheckResult")
    void getComments(String id){
        Single<List<Comment>> single = PostClient.getPostInstance().getComments(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        single.subscribe(
          o-> CommentsMutableLiveData.setValue(o),
          e-> Log.d(TAG, "getPosts: "+e)
        );
    }

    void setState(){
        sheetState.setValue(true);
    }

    private PostRepository repository;
    private LiveData<List<Post>> allPosts;

    public PostViewModel(@NonNull Application application) {
        super(application);
        repository = new PostRepository(application);
        allPosts = repository.getAllPosts();
    }

    public void insert(Post Post) {
        repository.insert(Post);
    }

    public void update(Post Post) {
        repository.update(Post);
    }

    public void delete(Post Post) {
        repository.delete(Post);
    }

    public void deleteAllPosts() {
        repository.deleteAllPosts();
    }

    public LiveData<List<Post>> getAllPostsOffline() {
        return allPosts;
    }
}
