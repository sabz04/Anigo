import android.os.CountDownTimer;

import com.example.anigo.R;

public class Timer {

    public static long milliSeconds = 0;

    public static void  ActivateTimer(){



        new CountDownTimer(60 * 1000, 1000) { // 5 минут в миллисекундах, 1000 миллисекунд (1 секунда) интервал
            public void onTick(long millisUntilFinished) {
                milliSeconds = millisUntilFinished;
            }

            public void onFinish() {

            }
        }.start();
    }
}
