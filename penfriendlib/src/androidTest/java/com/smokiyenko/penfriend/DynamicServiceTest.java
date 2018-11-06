package com.smokiyenko.penfriend;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ServiceTestRule;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class DynamicServiceTest {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Before
    public void setup() {

    }

    @Test
    public void testDummyService() throws InterruptedException {

        TestPenFriend penFriend = new TestPenFriend();
        TestMapContainer.getInstance().putPenFriend(penFriend.getClass().getSimpleName(), penFriend);

        PostBoy postBoy = new PostBoy.Builder(getApplicationContext())
                .mailService(DefaultPostService.class).build();

        TestPenFriend penFriendProxy = postBoy.mailTo(TestPenFriend.class);
        penFriendProxy.primitiveArgs((byte) 1, (short) 2, 'c', true, 1, 1, 2.0, 1.0f, "Test");
        penFriend.primitivesLatch.await();
        penFriendProxy.primitiveArrayArgs(new byte[2], null, new char[2], new boolean[2], null,
                new long[2], new double[2], new float[2], new String[2]);
        penFriend.primitiveArraysLatch.await();
        penFriendProxy.testParcelable(new Owl("Hedwig",6));
        penFriend.parcelableLatch.await();
        penFriendProxy.testSerilializable(new HashMap<String, String>());
        penFriend.serializableLatch.await();
        penFriendProxy.testStringArrayList(new ArrayList<String>());
        penFriend.strinngListLatch.await();
        penFriendProxy.testStringArrayList(new ArrayList<String>());
        penFriend.strinngListLatch.await();
    }

}