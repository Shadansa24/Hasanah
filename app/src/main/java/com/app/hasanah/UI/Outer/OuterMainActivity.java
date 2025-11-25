package com.app.hasanah.UI.Outer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import com.app.hasanah.R;
import com.app.hasanah.Utils.LocalLanguage.HelperLocalLanguage;

public class OuterMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        HelperLocalLanguage.setLocale(this,"en");
        setContentView(R.layout.activity_outer_main);

    }
}