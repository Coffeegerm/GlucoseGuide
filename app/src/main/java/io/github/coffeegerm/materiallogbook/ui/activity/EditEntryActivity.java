package io.github.coffeegerm.materiallogbook.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.realm.Realm;

/**
 * Created by dyarz on 8/17/2017.
 * <p>
 * Activity created to allow user to edit their entries
 * already present in database
 */

public class EditEntryActivity extends AppCompatActivity {

    private static String itemId = "itemId";
    @BindView(R.id.itemId)
    TextView itemIdTest;
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        if (savedInstanceState == null) {
            Bundle itemIdBundle = getIntent().getExtras();
            if (itemIdBundle == null) {
                itemIdTest.setText("Something fucked up");
            } else {
                String itemIdString = itemIdBundle.getString(itemId);
                itemIdTest.setText(itemIdString);
            }
        } else {
            String itemIdString = (String) savedInstanceState.getSerializable(itemId);
            itemIdTest.setText(itemIdString);
        }
    }

    @Override
    public void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
