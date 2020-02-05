package javatar.com.poster.data.room;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import javatar.com.poster.pojo.Post;

public class PostRepository {

    private PostDao postDao;
    private LiveData<List<Post>> allPosts;

    public PostRepository(Application application) {
        PostDatabase database = PostDatabase.getInstance(application);
        postDao = database.postDao();
        allPosts = postDao.getAllPosts();
    }

    public void insert(Post post) {
        new InsertPostAsyncTask(postDao).execute(post);
    }

    public void update(Post post) {
        new UpdatePostAsyncTask(postDao).execute(post);
    }

    public void delete(Post post) {
        new DeletePostAsyncTask(postDao).execute(post);
    }

    public void deleteAllPosts() {
        new DeleteAllPostsAsyncTask(postDao).execute();
    }

    public LiveData<List<Post>> getAllPosts() {
        return allPosts;
    }

    private static class InsertPostAsyncTask extends AsyncTask<Post, Void, Void> {
        private PostDao postDao;

        private InsertPostAsyncTask(PostDao postDao) {
            this.postDao = postDao;
        }

        @Override
        protected Void doInBackground(Post... posts) {
            try {
                postDao.insert(posts[0]);
            }catch (SQLiteConstraintException e){
                System.out.println(e.getMessage());
            }
            return null;
        }
    }

    private static class UpdatePostAsyncTask extends AsyncTask<Post, Void, Void> {
        private PostDao postDao;

        private UpdatePostAsyncTask(PostDao postDao) {
            this.postDao = postDao;
        }

        @Override
        protected Void doInBackground(Post... posts) {
            postDao.update(posts[0]);
            return null;
        }
    }

    private static class DeletePostAsyncTask extends AsyncTask<Post, Void, Void> {
        private PostDao postDao;

        private DeletePostAsyncTask(PostDao postDao) {
            this.postDao = postDao;
        }

        @Override
        protected Void doInBackground(Post... posts) {
            postDao.delete(posts[0]);
            return null;
        }
    }

    private static class DeleteAllPostsAsyncTask extends AsyncTask<Void, Void, Void> {
        private PostDao postDao;

        private DeleteAllPostsAsyncTask(PostDao postDao) {
            this.postDao = postDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            postDao.deleteAllPosts();
            return null;
        }
    }
}
