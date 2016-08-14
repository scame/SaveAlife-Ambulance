package com.example.scame.savealife.firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.scame.savealife.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(this, MapSelectionActivity.class));

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.i(TAG, "Key: " + key + " Value: " + value);
            }
        }
    }

    @OnClick(R.id.subscribeButton)
    public void onSubscribe(View view) {
        String topic = "test_message";
        FirebaseMessaging.getInstance().subscribeToTopic(topic);

        Log.i(TAG, topic);
        Toast.makeText(MainActivity.this, topic, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.log_token_btn)
    public void onLogToken(View view) {
        String token = FirebaseInstanceId.getInstance().getToken();

        Log.i(TAG, token);
        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
    }
}
