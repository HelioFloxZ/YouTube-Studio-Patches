package app.template.extension.extension;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ExamplePatch {
    public static final String GMS_CORE_PACKAGE_NAME = "app.revanced.android.gms";
    public static final String GMS_CORE_AUTH_ACTION = "app.revanced.android.gms.auth.GOOGLE_AUTH";

    public static String getGmsCorePackageName() {
        return GMS_CORE_PACKAGE_NAME;
    }

    public static Account[] getAccounts(Context context, String type) {
        try {
            AccountManager manager = AccountManager.get(context);
            return manager.getAccountsByType("com.google");
        } catch (Exception e) {
            return new Account[0];
        }
    }

    public static boolean handleStoreIntent(Context context, Intent intent) {
        if (intent == null) return false;
        Uri uri = intent.getData();
        if (uri != null) {
            String uriStr = uri.toString();
            if (uriStr.contains("market://details") || uriStr.contains("play.google.com/store/apps")) {
                return true;
            }
        }
        return false;
    }
}