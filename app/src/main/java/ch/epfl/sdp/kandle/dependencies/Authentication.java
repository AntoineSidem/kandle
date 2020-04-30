package ch.epfl.sdp.kandle.dependencies;

import com.google.android.gms.tasks.Task;

import ch.epfl.sdp.kandle.User;

public interface Authentication {

    Task<User> createUserWithEmailAndPassword(String username, String email, String password);

    Task<User> signInWithEmailAndPassword(String email, String password);

    Task<Void> reAuthenticate(String password);

    Task<Void> updatePassword(String password);

    void signOut();

    User getCurrentUser();

    boolean getCurrentUserAtApplicationStart();

}
