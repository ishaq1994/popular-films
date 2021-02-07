package com.ishaq.popularfilm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import androidx.test.espresso.contrib.RecyclerViewActions;

import com.ishaq.popularfilm.activities.SettingActivity;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SettingActivityTest {


    @Rule
    public ActivityScenarioRule<SettingActivity> mActivityScenarioRule =new ActivityScenarioRule<>(SettingActivity.class);

    @Test
    public void sortByTest(){

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(R.string.sort_label)),
                        click()));

        onView(withText("Popular")).perform(click());

        Context appContext= ApplicationProvider.getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        String sortByCategory = preferences.getString(appContext.getString(R.string.sort_key), "");

        assertThat(sortByCategory, equalTo("popular"));

    }
}
