package com.multifunctional.reminder.captain.ki;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.multifunctional.reminder.captain.ki.adapters.ScreenSlidePagerAdapter;
import com.multifunctional.reminder.captain.ki.fragments.AlarmFragment;
import com.multifunctional.reminder.captain.ki.fragments.StopWatchFragment;
import com.multifunctional.reminder.captain.ki.fragments.TimerFragment;
import com.multifunctional.reminder.captain.ki.utils.AlarmAlertService;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Window window = MainActivity.this.getWindow();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(TimerFragment.newInstance());
        fragList.add(StopWatchFragment.newInstance());
        fragList.add(AlarmFragment.newInstance());

        if (!isServiceRunning(this, AlarmAlertService.class)) {
            Intent i = new Intent(this, AlarmAlertService.class);
            this.startService(i);
        }

        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(fragList, getSupportFragmentManager());

        final BubbleNavigationLinearView bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);


        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if(i==0) {
                    bubbleNavigationLinearView.setBackgroundColor(getResources().getColor(R.color.timer_bottom_background));
                    changeStatusBarColor(R.color.timer_bottom_background);

                }else if(i==1){
                    bubbleNavigationLinearView.setBackgroundColor(getResources().getColor(R.color.blue_active));
                    changeStatusBarColor(R.color.blue_active);

                }else if(i==2){
                    bubbleNavigationLinearView.setBackgroundColor(getResources().getColor(R.color.green_active));
                    changeStatusBarColor(R.color.green_active);
                }
            }

            @Override
            public void onPageSelected(int i) {
                bubbleNavigationLinearView.setCurrentActiveItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                viewPager.setCurrentItem(position, true);

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeStatusBarColor(int color){
        Window window = MainActivity.this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,color));
    }


    private boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
