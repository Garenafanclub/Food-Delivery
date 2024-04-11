package com.example.epicure.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.epicure.R;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<Boolean> _isReady = new MutableLiveData<>(false);
    public LiveData<Boolean> isReady = _isReady;

    public MainViewModel() {
        // Simulate a 1-second delay for readiness
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
                _isReady.postValue(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
//        splashScreen.setOnExitAnimationListener(screen -> {
//            ObjectAnimator zoomX = ObjectAnimator.ofFloat(
//                    screen.getIconView(),
//                    View.SCALE_X,
//                    0.6f,
//                    0.0f
//            );
//            zoomX.setInterpolator(new OvershootInterpolator());
//            zoomX.setDuration(500L);
//            zoomX.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    screen.remove();
//                }
//            });
//
//            ObjectAnimator zoomY = ObjectAnimator.ofFloat(
//                    screen.getIconView(),
//                    View.SCALE_Y,
//                    0.6f,
//                    0.0f
//            );
//            zoomY.setInterpolator(new OvershootInterpolator());
//            zoomY.setDuration(500L);
//            zoomY.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    screen.remove();
//                }
//            });
//
//            zoomX.start();
//            zoomY.start();
//        });

//    private Runnable changeColorRunnable = new Runnable() {
//        @Override
//        public void run() {
//            // Generate random color
//            colorIndex = (colorIndex + 1) % colors.length;
//            recipe_button.setBackgroundColor(colors[colorIndex]);
//            recipe_button.setBackgroundResource(R.drawable.login_page_button_recipe);
//
//            // Schedule the next color change after 5 seconds
//            handler.postDelayed(changeColorRunnable, 5000);
//        }
//    };


