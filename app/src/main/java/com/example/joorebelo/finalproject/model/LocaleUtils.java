package com.example.joorebelo.finalproject.model;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleUtils {
    public static void updateConfig(Context context, String selectedLocale) {
        Locale locale = new Locale(selectedLocale);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }
}
