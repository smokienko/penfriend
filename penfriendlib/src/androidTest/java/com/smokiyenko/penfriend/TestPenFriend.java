package com.smokiyenko.penfriend;


import com.smokiyenko.penfriend.annotation.PenFriend;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@PenFriend
public class TestPenFriend {

    CountDownLatch primitivesLatch = new CountDownLatch(1);
    CountDownLatch primitiveArraysLatch = new CountDownLatch(1);
    CountDownLatch parcelableLatch = new CountDownLatch(1);
    CountDownLatch serializableLatch = new CountDownLatch(1);
    CountDownLatch strinngListLatch = new CountDownLatch(1);
    CountDownLatch sparseArrayLatch = new CountDownLatch(1);

    public void primitiveArgs(byte b, short s, char ch, boolean bool, int i, long l, double d, float f, String st) {
        primitivesLatch.countDown();
    }

    public void primitiveArrayArgs(byte[] bArr, short[] sArr, char[] chArr, boolean[] boolArr, int[] iArr, long[] lArr, double[] dArr, float[] fArr, String[] st){
        primitiveArraysLatch.countDown();
    }

    public void testParcelable(Owl owl) {
        parcelableLatch.countDown();
    }

    public void testSerilializable(Map<String, String> stringMap){
        serializableLatch.countDown();
    }

    public void testStringArrayList(ArrayList<String> arrayListWithStrings){
        strinngListLatch.countDown();
    }
}
