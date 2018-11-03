package com.smokiyenko.penfriend;

import android.app.IntentService;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

public class PostBoy {

    private static final String TAG = PostBoy.class.getSimpleName();

    public static final String PROXY = "Proxy";
    private Context context;
    private Class<?> mailService;


    private PostBoy(Context context, Class<?> mailService) {
        this.context = context;
        this.mailService = mailService;
    }

    public static class Builder {

        private Context context;
        private Class<? extends IntentService> mailService;


        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder mailService(Class<? extends IntentService> mailService){
            this.mailService = mailService;
            return this;
        }

        public PostBoy build() {
            //TODO check for nulls
            return new PostBoy(context, mailService);
        }
    }

    public <T> T mailTo(Class<?> penFriend) {
        //TODO check whether class has an annotated value
        try {
            return (T) getProxy(penFriend).getConstructor(Context.class, Class.class).newInstance(context, mailService);
        } catch (InstantiationException e) {
            Log.e(TAG, "mailTo: ", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "mailTo: ", e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "mailTo: ", e);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "mailTo: ", e);
        }
        throw new IllegalArgumentException("No proxy " + penFriend.getName() +" found" );
    }



    private Class<?> getProxy(Class<?> penFriend) {
        String claasName = penFriend.getCanonicalName();
        claasName = claasName.concat(PROXY);
        try {
            return Class.forName(claasName);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "getProxy: ", e);
        }
        throw new IllegalArgumentException("No proxy " + claasName +" found" );
    }


}
