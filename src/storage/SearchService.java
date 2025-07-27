package storage;


public class SearchService<T> {
    public T findUserByName(String name) {
        StorageManager sm = StorageManager.getStorageManager();
        System.out.println(sm.findUserBy("name", name));
        return (T)sm.findUserBy("name", name);
    }

    public T findUserById(String id) {
        StorageManager sm = StorageManager.getStorageManager();
        return (T)sm.findUserBy("id", id);
    }
}
