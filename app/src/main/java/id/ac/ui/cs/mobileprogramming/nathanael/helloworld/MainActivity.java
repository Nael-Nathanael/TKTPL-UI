package id.ac.ui.cs.mobileprogramming.nathanael.helloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText name_field = findViewById(R.id.name_field);
        Button update_name = findViewById(R.id.send_name_button);
        final TextView hello_world = findViewById(R.id.hello_name);

        update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name_field.getText().toString().isEmpty()) {
                    hello_world.setText(
                            name_field.getText().toString()
                    );
                }
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}