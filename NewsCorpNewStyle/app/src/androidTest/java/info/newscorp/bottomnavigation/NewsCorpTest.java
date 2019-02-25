package info.newscorp.bottomnavigation;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewsCorpTest {
    RecyclerView recyclerView;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void NewsCorpTest() {

        recyclerView= mActivityTestRule.getActivity().findViewById(R.id.recycler_view);

        int itemCount = recyclerView.getAdapter().getItemCount();

        sleepTimer(Constants.TIMER_THREE_THOUSAND);

        //click on a ImageView
        clickWidget(R.id.navigation_gifts);
        clickWidget(R.id.navigation_cart);
        clickWidget(R.id.navigation_profile);
        clickWidget(R.id.navigation_shop);

        sleepTimer(Constants.TIMER_THREE_THOUSAND);



        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.scrollToPosition(itemCount - 1));
        sleepTimer(Constants.TIMER_THREE_THOUSAND);
    }




    /*sleep timer*/
    void sleepTimer(int timer) {
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*ClickWidget*/
    void clickWidget(int id) {

        onView(withId(id))
                .perform(click());

    }
}
