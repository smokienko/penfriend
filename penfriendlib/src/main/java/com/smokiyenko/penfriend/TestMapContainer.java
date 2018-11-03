package com.smokiyenko.penfriend;

import java.util.HashMap;
import java.util.Map;

public class TestMapContainer {

    private static TestMapContainer instance;

    private Map<String, Object> penFriends = new HashMap<>();

    private TestMapContainer(){

    }

    public static TestMapContainer getInstance(){
        if (instance == null) {
            instance = new TestMapContainer();
        }
        return instance;
    }

    public Object getPenFriend(String name) {
        return penFriends.get(name);
    }

    public void putPenFriend(String name, Object penFriend) {
        penFriends.put(name, penFriend);
    }
}
