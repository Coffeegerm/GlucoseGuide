package io.github.coffeegerm.materiallogbook.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.coffeegerm.materiallogbook.R;

/**
 * Created by David Yarzebinski on 6/25/2017.
 */

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        return settingsView;
    }
}
