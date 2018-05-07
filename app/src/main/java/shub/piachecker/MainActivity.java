package shub.piachecker;

import android.app.AlarmManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final Result<Boolean> _vpnStatus = new Result<Boolean>();
    final IEventHandler _onCheckComplete = new EventHandler(new Runnable() {
        public void run() { printResult(); }
    });

    TextView _text;
    Button _buttonCheckNow;
    Button _buttonCheckOnSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _text = (TextView) this.findViewById(R.id.myString);
        _text.setText("VPN Status: Unknown");

        _buttonCheckNow = (Button) this.findViewById(R.id.checkNow);
        _buttonCheckNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVpnStatusAsync();
            }
        });

        _buttonCheckOnSchedule = (Button) this.findViewById(R.id.checkOnSchedule);
        _buttonCheckOnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationEventReceiver.setupAlarm(getApplicationContext(), AlarmManager.INTERVAL_FIFTEEN_MINUTES / (15 * 6));
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    // prevents crash on resuming activity
    // http://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit-state-loss.html
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    private void checkVpnStatusAsync(){
        _text.setText("VPN Status: Checking...");

        try {
            new PIARequester(_vpnStatus, _onCheckComplete).getPIAStatusAsync();
        }catch (Exception e) {
            printException(e);
        }
    }

    private void printResult(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(_vpnStatus.exception != null)
                {
                    printException(_vpnStatus.exception);
                    return;
                }

                if(_vpnStatus.result)
                    _text.setText("VPN Status: CONNECTED");
                else
                    _text.setText("VPN Status: NOT CONNECTED");
            }
        });
    }

    private void printException(Exception e){
        String message = "unable to check VPN status"
                + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + e.getMessage()
                + System.getProperty("line.separator")
                + System.getProperty("line.separator")
                + e.getClass().getName();

        _text.setText(message);
    }
}
