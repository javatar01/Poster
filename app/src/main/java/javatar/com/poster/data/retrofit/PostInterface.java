package javatar.com.poster.data.retrofit;

import java.util.List;

import io.reactivex.Single;
import javatar.com.poster.pojo.Comment;
import javatar.com.poster.pojo.Post;
import javatar.com.poster.pojo.User;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostInterface {

    @GET("posts")
    Single<List<Post>> getPosts();

    @GET("users/{id}")
    Single<User> getUser(@Path("id") String id);

    @GET("comments")
    Single<List<Comment>> getComments(@Query("postId") String id);
}
