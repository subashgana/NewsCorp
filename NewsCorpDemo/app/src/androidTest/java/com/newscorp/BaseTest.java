package com.newscorp;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BaseTest  {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /*sleep timer*/
    void sleepTimer(int timer) {
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*ClickRecyclerItem*/
    void clickItem(int id, int position) {
        onView(withId(id))
                .perform(actionOnItemAtPosition(position, click()));

    }


    /*ClickSpinner*/
    void clickSpinner(int id, int position) {
        onView(withId(id)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(position).perform(click());
    }

    /*ClickWidget*/
    void clickWidget(int id) {

        Espresso.onView(withId(id))
                .perform(click());

    }

    /*Test Starts Here*/
    @Test
    public void startTest() {
        NewsCorpTest test = new NewsCorpTest();
        test.newsCorpTest();
        test.newsCorpTest1();
    }

}
