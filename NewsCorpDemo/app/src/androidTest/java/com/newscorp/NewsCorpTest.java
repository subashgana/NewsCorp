package com.newscorp;


import android.support.test.espresso.Espresso;

import org.junit.Test;

public class NewsCorpTest extends BaseTest {

    @Test
    public void newsCorpTest() {
        sleepTimer(Constants.TIMER_THREE_THOUSAND);

        //click recycle view item
        clickItem(R.id.imagesRecyclerView, 2);
        sleepTimer(Constants.THOUSAND);

        //click on a ImageView
        clickWidget(R.id.fullscreen_image);
        sleepTimer(Constants.TIMER_THREE_THOUSAND);

        //Back
        Espresso.pressBack();
        sleepTimer(Constants.THOUSAND);

    }

    //Select Span
    @Test
    public void newsCorpTest1() {
        sleepTimer(Constants.TIMER_THREE_THOUSAND);

        //click spinner1
        clickSpinner(R.id.grid_spans, 0);
        sleepTimer(Constants.TIMER_FIVE_THOUSAND);

        //click spinner2
        clickSpinner(R.id.grid_spans, 1);
        sleepTimer(Constants.TIMER_FIVE_THOUSAND);


        //click spinner3
        clickSpinner(R.id.grid_spans, 2);
        sleepTimer(Constants.TIMER_FIVE_THOUSAND);


        //click spinner4
        clickSpinner(R.id.grid_spans, 3);
        sleepTimer(Constants.TIMER_FIVE_THOUSAND);


    }


}
