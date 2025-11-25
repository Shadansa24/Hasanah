package com.app.hasanah.UI.User;

import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.hasanah.R;
import com.app.hasanah.Utils.LocalLanguage.HelperLocalLanguage;

public class UserMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EdgeToEdge.enable(this);
        HelperLocalLanguage.setLocale(this,"en");
        setContentView(R.layout.activity_user_main);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.background_color1));
    }
}