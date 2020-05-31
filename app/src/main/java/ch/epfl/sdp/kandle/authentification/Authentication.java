package ch.epfl.sdp.kandle.authentification;

import com.google.android.gms.tasks.Task;

import java.util.Map;

import ch.epfl.sdp.kandle.entities.user.User;

public interface Authentication {

    Task<User> createUserWithEmailAndPassword(String username, String email, String password, Map<String, Object> usernameMap, Map<String, Object> deviceMap);

    Task<User> signInWithEmailAndPassword(String email, String password);

    Task<Void> reAuthenticate(String password);

    Task<Void> updatePassword(String password);

    void signOut();

    User getCurrentUser();

    boolean getCurrentUserAtApplicationStart();

    Task<Void> deleteUser();

}
