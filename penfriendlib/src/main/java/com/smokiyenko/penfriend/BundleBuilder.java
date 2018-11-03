package com.smokiyenko.penfriend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class BundleBuilder {

    private static HashMap<Class<?>, Bundler> BUNDLERS = new HashMap<>();

    public static final String METHOD_NAME = "MethodName";
    public static final String METHOD_PARAMETERS = "MethodParameters";
    public static final String INTERFACE_NAME = "InterfaceName";
    public static final String ADDRESS = "address";
    public static final String PROXY = "Proxy";

    static {
        BUNDLERS.put(Boolean.class, new Bundler.BooleanBundler());
        BUNDLERS.put(boolean.class, new Bundler.BooleanBundler());
        BUNDLERS.put(boolean[].class, new Bundler.BooleanArrayBundler());
        BUNDLERS.put(Byte.class, new Bundler.ByteBundler());
        BUNDLERS.put(byte.class, new Bundler.ByteBundler());
        BUNDLERS.put(byte[].class, new Bundler.ByteArrayBundler());
        BUNDLERS.put(Character.class, new Bundler.CharBundler());
        BUNDLERS.put(char.class, new Bundler.CharBundler());
        BUNDLERS.put(char[].class, new Bundler.CharArrayBundler());
        BUNDLERS.put(Double.class, new Bundler.DoubleBundler());
        BUNDLERS.put(double.class, new Bundler.DoubleBundler());
        BUNDLERS.put(double[].class, new Bundler.DoubleArrayBundler());
        BUNDLERS.put(Float.class, new Bundler.FloatBundler());
        BUNDLERS.put(float.class, new Bundler.FloatBundler());
        BUNDLERS.put(float[].class, new Bundler.FloatArrayBundler());
        BUNDLERS.put(Integer.class, new Bundler.IntBundler());
        BUNDLERS.put(int.class, new Bundler.IntBundler());
        BUNDLERS.put(int[].class, new Bundler.IntArrayBundler());
        BUNDLERS.put(Long.class, new Bundler.LongBundler());
        BUNDLERS.put(long.class, new Bundler.LongBundler());
        BUNDLERS.put(long[].class, new Bundler.LongArrayBundler());
        BUNDLERS.put(Short.class, new Bundler.ShortBundler());
        BUNDLERS.put(short.class, new Bundler.ShortBundler());
        BUNDLERS.put(short[].class, new Bundler.ShortArrayBundler());
        BUNDLERS.put(CharSequence.class, new Bundler.CharSequenceBundler());
        BUNDLERS.put(CharSequence[].class, new Bundler.CharSequenceArrayBundler());
        BUNDLERS.put(String.class, new Bundler.StringBundler());
        BUNDLERS.put(String[].class, new Bundler.StringArrayBundler());
        BUNDLERS.put(ArrayList.class, new Bundler.ArrayListBundler());
        BUNDLERS.put(Parcelable.class, new Bundler.ParcelableBundler());
        BUNDLERS.put(Serializable.class, new Bundler.SerializableBundler());
    }

    public static void buildMethodBundle(Intent intent, Method method, Object[] args) {
        intent.putExtra(METHOD_NAME, method.getName());
        intent.putExtra(INTERFACE_NAME, getRealClassName(method.getDeclaringClass().getSimpleName()));
        intent.putExtra(ADDRESS, getRealClassName(method.getDeclaringClass().getCanonicalName()));
        Bundle methodParams = new Bundle();
        for (int i = 0; i < args.length; i++) {
            BUNDLERS.get(args[i].getClass()).put(String.valueOf(i), methodParams, args[i]);
        }
        intent.putExtra(METHOD_PARAMETERS, methodParams);
    }

    public static void buildMethodDescriptionBundle(Intent intent, String methodName, Class<?> typeName) {
        intent.putExtra(METHOD_NAME, methodName);
        intent.putExtra(INTERFACE_NAME, typeName.getSimpleName());
        intent.putExtra(ADDRESS, typeName.getCanonicalName());
    }

    public static void addPrimitiveMethodArg(Intent intent, Class<?> type, Object value) {
        Bundle paramsBundle = getParamsBundle(intent);
        BUNDLERS.get(type).put(String.valueOf(paramsBundle.size()), paramsBundle, value);
    }

    public static void addMethodArg(Intent intent, Object value) {
        Bundle paramsBundle = getParamsBundle(intent);
        if (value == null) {
            paramsBundle.putParcelable(String.valueOf(paramsBundle.size()), null);
            return;
        }
        Bundler bundler = BUNDLERS.get(value.getClass());
        if (bundler == null && containsInterface(value.getClass(), Parcelable.class)) {
            bundler = BUNDLERS.get(Parcelable.class);
        }

        //In future I will add support for parsed arrays
//        if (bundler == null && value.getClass().isArray() && containsInterface(value.getClass().getComponentType(), Parcelable.class)) {
//            bundler = BUNDLERS.get(Parcelable[].class);
//        }

        if (bundler == null && containsInterface(value.getClass(), Serializable.class)) {
            bundler = BUNDLERS.get(Serializable.class);
        }

        bundler.put(String.valueOf(paramsBundle.size()), paramsBundle, value);
    }

    private static boolean containsInterface(Class<?> valueType, Class<?> interfaceType) {
        for (Class<?> aClass : valueType.getInterfaces()) {
            if (aClass.equals(interfaceType)) {
                return true;
            }
        }
        return false;
    }

    private static Bundle getParamsBundle(Intent intent) {
        Bundle bundle = intent.getBundleExtra(METHOD_PARAMETERS);
        if (bundle == null) {
            bundle = new Bundle();
            intent.putExtra(METHOD_PARAMETERS, bundle);
        }
        return bundle;
    }

    private static String getRealClassName(String name) {
        return name.substring(0, name.length() - PROXY.length());
    }

    public static String getMethodName(Intent intent) {
        return intent.getExtras().getString(METHOD_NAME);
    }

    public static String getPenFriendName(Intent intent) {
        return intent.getExtras().getString(INTERFACE_NAME);
    }

    public static String getPenFriendAddress(Intent intent) {
        return intent.getExtras().getString(ADDRESS);
    }

    public static Object[] getMethodArgs(Intent intent) {
        Bundle bundle = intent.getExtras().getBundle(METHOD_PARAMETERS);
        Object[] args = new Object[bundle.size()];
        for (int i = 0; i < bundle.size(); i++) {
            args[i] = bundle.get(String.valueOf(i));
        }
        return args;
    }


}
