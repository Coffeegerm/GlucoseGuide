package io.github.coffeegerm.materiallogbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Activity for a new Entry into the Database
 */

public class NewEntryActivity extends AppCompatActivity {

    @BindView(R.id.cancelBtn)
    Button cancelBtn;

    @BindView(R.id.saveBtn)
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry_activity);
        ButterKnife.bind(this);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    // TODO Create Realm database

    // TODO saveBtn onClickListener saves data given to database

}
