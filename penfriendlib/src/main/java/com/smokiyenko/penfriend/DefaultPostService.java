package com.smokiyenko.penfriend;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class DefaultPostService extends IntentService {

    private static final String TAG = DefaultPostService.class.getSimpleName();
    private Map<String, Object> penFriends = new HashMap<>();


    public DefaultPostService() {
        super(DefaultPostService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        invokeMethodFromIntent(intent);
    }

    private void invokeMethodFromIntent(Intent intent) {
        try {
            Object penFriend = findPenFriend(intent);
            Method method = findMethod(penFriend.getClass(), intent);
            method.invoke(penFriend, BundleBuilder.getMethodArgs(intent));
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "invokeMethodFromIntent: " + e);
        } catch (IllegalAccessException e) {
            Log.d(TAG, "invokeMethodFromIntent: " + e);
        } catch (InvocationTargetException e) {
            Log.d(TAG, "invokeMethodFromIntent: " + e);
        }
    }

    private Object findPenFriend(Intent intent) {
        String penFriendName = BundleBuilder.getPenFriendName(intent);
        Object penFriend = penFriends.get(penFriendName);

        if (BuildConfig.DEBUG && penFriend == null) {
            penFriend = TestMapContainer.getInstance().getPenFriend(penFriendName);
        }

        if (penFriend == null) {
            String penFriendAddress = BundleBuilder.getPenFriendAddress(intent);
            penFriend = createPenFriend(penFriendAddress);
            penFriends.put(penFriendName, penFriend);
        }

        return penFriend;
    }

    private Object createPenFriend(String address) {
        try {
            Class penFriendType = Class.forName(address);
            return penFriendType.getConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "createPenFriend: " + e);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "createPenFriend: " + e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "createPenFriend: " + e);
        } catch (InstantiationException e) {
            Log.e(TAG, "createPenFriend: " + e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "createPenFriend: " + e);
        }
        throw new IllegalArgumentException("No PenFriend found on this address " + address);
    }

    private Method findMethod(Class<?> penFriendType, Intent intent) {
        String methodName = BundleBuilder.getMethodName(intent);
        for (Method method : penFriendType.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new InvalidParameterException("Did not find method with name " + methodName + " within " + penFriendType);
    }


}
