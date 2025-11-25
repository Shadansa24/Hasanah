package com.app.hasanah.Utils.Validation;

public class ValidationWebLink {
    public static Boolean validateLink(String link) {

        if (!link.isEmpty()) {
            // Valid link
            return  true;
        } else {
            // Invalid link
            return  false;
        }
    }
}
