package ch.epfl.sdp.kandle.dependencies;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.kandle.entities.post.Post;
import ch.epfl.sdp.kandle.entities.user.LoggedInUser;
import ch.epfl.sdp.kandle.entities.user.User;
import ch.epfl.sdp.kandle.storage.Database;

/**
 * A mocked database. Upon creation, it contains:
 * - a single user `admin`, with all-zero userID.
 * - to be extended for posts, etc...
 */

public class MockDatabase implements Database {


    private Map<String, User> users;
    private Map<String, Follow> followMap;
    private Map<String, Post> posts;

    public MockDatabase(boolean isConnected, Map<String, User> users, Map<String, Follow> followMap, Map<String, Post> posts) {
        this.users = users;
        this.posts = posts;
        this.followMap = followMap;
        if (isConnected) {
            users.put(LoggedInUser.getInstance().getId(), LoggedInUser.getInstance());
            followMap.put(LoggedInUser.getInstance().getId(), new Follow());
        }


    }


    @Override
    public Task<User> getUserByName(String username) {

        TaskCompletionSource<User> task = new TaskCompletionSource<>();
        User result = null;

        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                result = user;
                break;
            }
        }
        task.setResult(result);
        return task.getTask();

    }


    @Override
    public Task<User> getUserById(String userId) {

        TaskCompletionSource<User> source = new TaskCompletionSource<>();

        if (users.containsKey(userId)) {
            source.setResult(users.get(userId));
        } else {
            source.setException(new IllegalArgumentException("No such user with id: " + userId + "with users containing"));
        }

        return source.getTask();
    }


    @Override
    public Task<Void> createUser(User user) {

        TaskCompletionSource<Void> task = new TaskCompletionSource<>();

        users.put(user.getId(), user);
        followMap.put(user.getId(), new Follow());
        return task.getTask();
    }

    @Override
    public Task<List<User>> searchUsers(String prefix, int maxNumber) {
        List<User> results = new ArrayList<>();

        for (User u : users.values()) {
            if (u.getUsername().startsWith(prefix)) {
                results.add(u);
            }
        }
        Collections.sort(results, (o1, o2) -> o1.getUsername().compareTo(o2.getUsername()));

        TaskCompletionSource<List<User>> source = new TaskCompletionSource<>();
        source.setResult(new ArrayList<User>(results.subList(0, Math.min(maxNumber, results.size()))));
        return source.getTask();

    }

    @Override
    public Task<List<User>> usersList() {
        List<User> results = new ArrayList<>();
        results.addAll(users.values());
        TaskCompletionSource<List<User>> source = new TaskCompletionSource<>();
        source.setResult(new ArrayList<User>(results.subList(0, results.size())));
        return source.getTask();
    }

    @Override
    public Task<Void> follow(String userFollowing, String userFollowed) {

        Follow follow = followMap.get(userFollowing);
        Follow follow2 = followMap.get(userFollowed);

        if (!follow.following.contains(userFollowed)) {
            follow.addFollowing(userFollowed);
            follow2.addFollower(userFollowing);
            followMap.put(userFollowing, follow);
            followMap.put(userFollowed, follow2);
        }

        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        source.setResult(null);
        return source.getTask();
    }

    @Override
    public Task<Void> unFollow(String userUnFollowing, String userUnFollowed) {
        Follow follow = followMap.get(userUnFollowing);
        Follow follow2 = followMap.get(userUnFollowed);

        if (follow.following.contains(userUnFollowed)) {
            follow.removeFollowing(userUnFollowed);
            follow2.removeFollower(userUnFollowing);
            followMap.put(userUnFollowing, follow);
            followMap.put(userUnFollowed, follow2);
        }
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        source.setResult(null);
        return source.getTask();
    }

    @Override
    public Task<Void> setCloseFollower(String userFollowing, String userFollowed) {
        Follow follow = followMap.get(userFollowing);
        Follow follow2 = followMap.get(userFollowed);
        if (!follow2.closeFollowers.contains(userFollowing) && follow.following.contains(userFollowed)) {
            follow2.addCloseFollowe(userFollowing);
            followMap.put(userFollowed, follow2);
        }

        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        source.setResult(null);
        return source.getTask();
    }

    @Override
    public Task<Void> unsetCloseFollower(String userFollowing, String userFollowed) {
        Follow follow2 = followMap.get(userFollowed);

        if (follow2.closeFollowers.contains(userFollowing)) {
            follow2.removeCLoseFollower(userFollowing);
            followMap.put(userFollowed, follow2);
        }
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        source.setResult(null);
        return source.getTask();
    }

    @Override
    public Task<List<String>> userIdFollowingList(String userId) {
        TaskCompletionSource<List<String>> source = new TaskCompletionSource<>();
        source.setResult(new ArrayList<String>(followMap.get(userId).following));
        return source.getTask();

    }

    @Override
    public Task<List<String>> userIdFollowersList(String userId) {
        TaskCompletionSource<List<String>> source = new TaskCompletionSource<>();
        source.setResult(new ArrayList<String>(followMap.get(userId).followers));
        return source.getTask();
    }

    @Override
    public Task<List<String>> userIdCloseFollowersList(String userId) {
        TaskCompletionSource<List<String>> source = new TaskCompletionSource<>();
        source.setResult(new ArrayList<String>(followMap.get(userId).closeFollowers));
        return source.getTask();
    }

    @Override
    public Task<List<User>> userFollowingList(String userId) {
        TaskCompletionSource<List<User>> source = new TaskCompletionSource<>();
        ArrayList<User> following = new ArrayList<>();

        for (String id : followMap.get(userId).following) {
            following.add(users.get(id));
        }
        source.setResult(following);
        return source.getTask();
    }

    @Override
    public Task<List<User>> userFollowersList(String userId) {
        TaskCompletionSource<List<User>> source = new TaskCompletionSource<>();
        ArrayList<User> followers = new ArrayList<>();

        for (String id : followMap.get(userId).followers) {
            followers.add(users.get(id));
        }
        source.setResult(followers);
        return source.getTask();
    }

    @Override
    public Task<List<User>> userCloseFollowersList(String userId) {
        TaskCompletionSource<List<User>> source = new TaskCompletionSource<>();
        ArrayList<User> closeFollowers = new ArrayList<>();
        if (followMap.get(userId) != null) {
            for (String id : followMap.get(userId).closeFollowers) {
                closeFollowers.add(users.get(id));
            }
        }
        source.setResult(closeFollowers);
        return source.getTask();
    }

    @Override
    public Task<Void> updateProfilePicture(String uri) {
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        User user = users.get(LoggedInUser.getInstance().getId());
        user.setImageURL(uri);
        source.setResult(null);
        return source.getTask();
    }


    @Override
    public Task<Void> updateNickname(String nickname) {
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        User user = users.get(LoggedInUser.getInstance().getId());
        user.setNickname(nickname);
        source.setResult(null);
        return source.getTask();
    }


    @Override
    public Task<Void> addPost(Post p) {
        posts.put(p.getPostId(), p);
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        source.setResult(null);
        return source.getTask();
    }

    @Override
    public Task<Void> editPost(Post p, String postId) {
        if (users.get(p.getUserId()).getPostsIds().contains(postId)) {
            posts.remove(postId);
            posts.put(postId, p);
        }
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        source.setResult(null);
        return source.getTask();
    }

    @Override
    public Task<Void> deletePost(Post p) {
        posts.remove(p.getPostId());
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        source.setResult(null);
        return source.getTask();
    }

    @Override
    public Task<Void> likePost(String userId, String postId) {
        if (!posts.get(postId).getLikers().contains(userId)) {
            posts.get(postId).likePost(userId);
        }
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        source.setResult(null);
        return source.getTask();
    }

    @Override
    public Task<Void> unlikePost(String userId, String postId) {
        if (posts.get(postId).getLikers().contains(userId)) {
            posts.get(postId).unlikePost(userId);
        }
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        source.setResult(null);
        return source.getTask();
    }

    @Override
    public Task<List<User>> getLikers(String postId) {
        TaskCompletionSource<List<User>> source = new TaskCompletionSource<>();

        List<String> userIds = posts.get(postId).getLikers();

        List<User> usersList = new ArrayList<>();

        for (Map.Entry<String, User> entry : users.entrySet()) {
            if (userIds.contains(entry.getKey())) {
                usersList.add(entry.getValue());
            }
        }

        source.setResult(usersList);

        return source.getTask();

    }

    @Override
    public Task<List<Post>> getPostsByUserId(String userId) {
        List<Post> postsList = new ArrayList<Post>();
        for (Map.Entry<String, Post> entry : posts.entrySet()) {
            if (userId.equals(entry.getValue().getUserId())) {
                postsList.add(entry.getValue());
            }
        }
        TaskCompletionSource<List<Post>> source = new TaskCompletionSource<>();
        source.setResult(postsList);
        return source.getTask();
    }

    @Override
    public Task<List<Post>> getNearbyPosts(double latitude, double longitude, double distance) {
        TaskCompletionSource<List<Post>> source = new TaskCompletionSource<>();
        List<String> userSmall = new ArrayList<>();
        List<String> userMedium = new ArrayList<>();
        List<String> userLarge = new ArrayList<>();

        userSmall.add("mock0");
        userMedium.add("mock0");
        userMedium.add("mock1");
        userMedium.add("mock2");
        userMedium.add("mock3");
        userMedium.add("mock4");
        userMedium.add("mock6");
        userLarge.add("mock0");
        userLarge.add("mock1");
        userLarge.add("mock2");
        userLarge.add("mock3");
        userLarge.add("mock4");
        userLarge.add("mock5");
        userLarge.add("mock6");
        userLarge.add("mock7");
        userLarge.add("mock8");
        userLarge.add("mock9");
        userLarge.add("mock10");

        Post post1 = new Post("nearby post 1 ", null, new Date(), "mock user id", 0.0001, 0.0001);
        post1.setLikers(userSmall);

        Post post2 = new Post("nearby post 2 ", null, new Date(), "mock user id", 0.0001, 0.0001);
        post2.setLikers(userMedium);

        Post post3 = new Post("nearby post 3 ", null, new Date(), "mock user id", 0.0001, 0.0001);
        post3.setLikers(userLarge);

        List<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        posts.add(new Post("nearby post 4 ", null, new Date(), "mock user id", 0.0001, 0.0001));
        posts.add(new Post("nearby post 5 ", null, new Date(), "mock user id", 0.0001, 0.0001));

        source.setResult(posts);
        return source.getTask();
    }

    @Override
    public Task<Post> getPostByPostId(String postId) {

        TaskCompletionSource<Post> source = new TaskCompletionSource<>();
        if (posts.containsKey(postId)) {
            source.setResult(posts.get(postId));
        } else {
            source.setException(new IllegalArgumentException("No such user with id: " + postId + "with users containing"));
        }

        return source.getTask();
    }

    @Override
    public Task<List<Post>> getParticipatingEvents() {
        TaskCompletionSource<List<Post>> source = new TaskCompletionSource<>();
        List<Post> events = new ArrayList<>();

        for (Post p : posts.values()) {
            if (p.getType() != null && p.getType().equals(Post.EVENT)
                    && p.getLikers().contains(DependencyManager.getAuthSystem().getCurrentUser().getId())) {
                events.add(p);
            }
        }
        source.setResult(events);
        return source.getTask();
    }

    @Override
    public Task<Void> updateHighScore(int highScore) {
        TaskCompletionSource<Void> source = new TaskCompletionSource<>();
        User user = users.get(LoggedInUser.getInstance().getId());
        user.setHighScore(highScore);
        source.setResult(null);
        return source.getTask();
    }

    public static class Follow {
        public List<String> following;

        public List<String> followers;

        public List<String> closeFollowers;


        public Follow(List<String> following, List<String> followers, List<String> closeFollowers) {
            this.following = following;
            this.followers = followers;
            this.closeFollowers = closeFollowers;
        }

        public Follow(List<String> following, List<String> followers) {
            this.following = following;
            this.followers = followers;
            this.closeFollowers = new LinkedList<>();
        }

        public Follow() {
            this.followers = new LinkedList<>();
            this.following = new LinkedList<>();
            this.closeFollowers = new LinkedList<>();
        }

        public void addFollowing(String s) {
            following.add(s);
        }

        public void addFollower(String s) {
            followers.add(s);
        }

        public void removeFollowing(String s) {
            following.remove(s);
        }

        public void removeFollower(String s) {
            followers.remove(s);
        }

        public void addCloseFollowe(String s) {
            closeFollowers.add(s);
        }

        public void removeCLoseFollower(String s) {
            closeFollowers.remove(s);
        }

    }

}
