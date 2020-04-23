package ch.epfl.sdp.kandle.dependencies;

import ch.epfl.sdp.kandle.Storage.firebase.CloudStorage;
import ch.epfl.sdp.kandle.Storage.firebase.FirebaseAuthentication;
import ch.epfl.sdp.kandle.Storage.firebase.FirestoreDatabase;
import ch.epfl.sdp.kandle.Storage.room.LocalDatabase;
import ch.epfl.sdp.kandle.network.NetworkState;
import ch.epfl.sdp.kandle.network.UserNetworkStatus;
import ch.epfl.sdp.kandle.Storage.caching.InternalStorageHandler;

public final class DependencyManager {

    //Do not touch order
    private static Database db = FirestoreDatabase.getInstance();
    private static Storage storage = CloudStorage.getInstance();
    private static MyLocationProvider locationProvider = new GoogleLocationServices();
    private static NetworkState networkState = UserNetworkStatus.getInstance();
    private static InternalStorage internalStorage = InternalStorageHandler.getInstance();
    private static LocalDatabase localDatabase = LocalDatabase.getInstance();
    private static Authentication auth = FirebaseAuthentication.getInstance();


    public static void setFreshTestDependencies(Authentication auth, Database db, Storage storage, InternalStorage internalStorage, NetworkState networkState, LocalDatabase localDatabase) {
        setAuthSystem(auth);
        setDatabaseSystem(db);
        setStorageSystem(storage);
        setLocationProvider(new MockLocation());
        setInternalStorageSystem(internalStorage);
        setNetworkStateSystem(networkState);
        setLocalDatabase(localDatabase);

    }

    public static MyLocationProvider getLocationProvider() {
        return locationProvider;
    }

    public static void setLocationProvider(MyLocationProvider locationProvider) {
        DependencyManager.locationProvider = locationProvider;
    }

    public static Authentication getAuthSystem() {
        return auth;
    }

    public static void setAuthSystem(Authentication auth) {
        DependencyManager.auth = auth;
    }

    public static Database getDatabaseSystem() {
        return db;
    }

    public static void setDatabaseSystem(Database db) {
        DependencyManager.db = db;
    }

    public static Storage getStorageSystem() {
        return storage;
    }

    public static void setStorageSystem(Storage storage) {
        DependencyManager.storage = storage;
    }

    public static InternalStorage getInternalStorageSystem() {
        return internalStorage;
    }

    public static void setInternalStorageSystem(InternalStorage internalStorage) {
        DependencyManager.internalStorage = internalStorage;
    }

    public static NetworkState getNetworkStateSystem() {
        return networkState;
    }

    public static void setNetworkStateSystem(NetworkState networkState) {
        DependencyManager.networkState = networkState;
    }

    public static LocalDatabase getLocalDatabase() {
        return localDatabase;
    }

    public static void setLocalDatabase(LocalDatabase localDatabase) {
        DependencyManager.localDatabase = localDatabase;
    }


    private DependencyManager() {

    }

}
