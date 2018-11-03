package com.smokiyenko.penfriend;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public interface Bundler<T> {

    void put(@NonNull String key, @NonNull Bundle bundle, @NonNull T value);

    class ArrayListBundler implements Bundler<ArrayList> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull ArrayList value) {
            if (!value.isEmpty()) {
                Object subValue = value.get(0);
                if (subValue instanceof String) {
                    bundle.putStringArrayList(key,value);
                } else if (subValue instanceof Parcelable) {
                    bundle.putParcelableArrayList(key,value);
                } else if (subValue instanceof Integer) {
                    bundle.putIntegerArrayList(key,value);
                } else if (subValue instanceof CharSequence) {
                    bundle.putCharSequenceArrayList(key, value);
                } else {
                    bundle.putSerializable(key, value);
                }

            } else {
                bundle.putParcelableArrayList(key, value);
            }
        }
    }

    class BooleanArrayBundler implements Bundler<boolean[]> {
        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull boolean[] value) {
            bundle.putBooleanArray(key, value);
        }
    }

    class BooleanBundler implements Bundler<Boolean> {
        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull Boolean value) {
            bundle.putBoolean(key, value);
        }
    }

    class ByteArrayBundler implements Bundler<byte[]> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull byte[] value) {
            bundle.putByteArray(key, value);
        }
    }

    class ByteBundler implements Bundler<Byte> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull Byte value) {
            bundle.putByte(key, value);
        }

    }

    class CharArrayBundler implements Bundler<char[]> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull char[] value) {
            bundle.putCharArray(key, value);
        }
    }

    class CharBundler implements Bundler<Character> {
        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull Character value) {
            bundle.putChar(key, value);
        }

    }

    class CharSequenceArrayBundler implements Bundler<CharSequence[]> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull CharSequence[] value) {
            bundle.putCharSequenceArray(key, value);
        }

    }

    class CharSequenceBundler implements Bundler<CharSequence> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull CharSequence value) {
            bundle.putCharSequence(key, value);
        }

    }

    class DoubleBundler implements Bundler<Double> {
        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull Double value) {
            bundle.putDouble(key, value);
        }
    }

    class DoubleArrayBundler implements Bundler<double[]> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull double[] value) {
            bundle.putDoubleArray(key, value);
        }
    }

    class FloatBundler implements Bundler<Float> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull Float value) {
            bundle.putFloat(key, value);
        }
    }

    class FloatArrayBundler implements Bundler<float[]> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull float[] value) {
            bundle.putFloatArray(key, value);
        }

    }

    class IntBundler implements Bundler<Integer> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull Integer value) {
            bundle.putInt(key, value);
        }
    }

    class IntArrayBundler implements Bundler<int[]> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull int[] value) {
            bundle.putIntArray(key, value);
        }

    }

    class LongBundler implements Bundler<Long> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull Long value) {
            bundle.putLong(key, value);
        }

    }

    class LongArrayBundler implements Bundler<long[]> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull long[] value) {
            bundle.putLongArray(key, value);
        }

    }

    class ShortBundler implements Bundler<Short> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull Short value) {
            bundle.putShort(key, value);
        }

    }

    class ShortArrayBundler implements Bundler<short[]> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull short[] value) {
            bundle.putShortArray(key, value);
        }

    }

    class StringBundler implements Bundler<String> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull String value) {
            bundle.putString(key, value);
        }

    }

    class StringArrayBundler implements Bundler<String[]> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull String[] value) {
            bundle.putStringArray(key, value);
        }

    }

    class ParcelableBundler implements Bundler<Parcelable> {

        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull Parcelable value) {
            bundle.putParcelable(key, value);
        }

    }

    class SerializableBundler implements Bundler<Serializable> {
        @Override
        public void put(@NonNull String key, @NonNull Bundle bundle, @NonNull Serializable value) {
            bundle.putSerializable(key,value);
        }

    }
}
