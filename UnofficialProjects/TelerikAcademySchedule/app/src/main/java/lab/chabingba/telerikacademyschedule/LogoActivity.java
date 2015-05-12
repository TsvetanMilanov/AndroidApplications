package lab.chabingba.telerikacademyschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class LogoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_layout);

        ImageView imageView = (ImageView) findViewById(R.id.imageLogo);

        imageView.setImageResource(R.drawable.logo_edited);

        Thread logoThread = new Thread() {
            public void run() {
                try {
                    // TODO: return to 1000/1500.
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent startMain = new Intent(LogoActivity.this, MainActivity.class);
                    startActivity(startMain);
                    finish();
                }
            }
        };

        logoThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
