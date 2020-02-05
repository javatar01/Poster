package javatar.com.poster.data.retrofit;

import java.util.List;

import io.reactivex.Single;
import javatar.com.poster.pojo.Comment;
import javatar.com.poster.pojo.Post;
import javatar.com.poster.pojo.User;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostClient {
    private static final String BUSE_URL = "https://jsonplaceholder.typicode.com/";
    private PostInterface postInterface;
    private static PostClient postInstance;

    public PostClient(){
        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BUSE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        postInterface = retrofit.create(PostInterface.class);
    }

    public static PostClient getPostInstance() {
        if (postInstance == null)
            postInstance = new PostClient();
        return postInstance;
    }

    public Single<List<Post>> getPosts(){
        return postInterface.getPosts();
    }

    public Single<User> getUser(String id){
        return postInterface.getUser(id);
    }

    public Single<List<Comment>> getComments(String id){
        return postInterface.getComments(id);
    }
}
