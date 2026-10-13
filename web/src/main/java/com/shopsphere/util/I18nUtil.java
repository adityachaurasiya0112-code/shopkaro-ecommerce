package com.shopsphere.util;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Utility for Internationalization (i18n).
 * Demonstrates RTU syllabus: Locale, ResourceBundle, and formatted values.
 */
public class I18nUtil {

    private static final String BUNDLE_BASE_NAME = "messages";

    public static ResourceBundle getBundle(Locale locale) {
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        return ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
    }

    public static String getMessage(String key, Locale locale) {
        try {
            return getBundle(locale).getString(key);
        } catch (Exception e) {
            return "???" + key + "???";
        }
    }

    public static String formatCurrency(double amount, Locale locale) {
        if (locale == null) locale = new Locale("en", "IN");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(amount);
    }
}
