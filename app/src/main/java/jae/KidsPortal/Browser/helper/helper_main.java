/*
    This file is part of the Browser WebApp.

    Browser WebApp is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Browser WebApp is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Browser webview app.

    If not, see <http://www.gnu.org/licenses/>.
 */

package jae.KidsPortal.Browser.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mobapphome.mahencryptorlib.MAHEncryptor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import jae.KidsPortal.Browser.Login;
import jae.KidsPortal.Browser.R;
import jae.KidsPortal.Browser.databases.DbAdapter_Bookmarks;
import jae.KidsPortal.Browser.databases.DbAdapter_ReadLater;
import jae.KidsPortal.Browser.databases.DbAdapter_Reports;

public class helper_main {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final int REQUEST_CODE_ASK_PERMISSIONS_1 = 1234;
    private static String pw;
    private static String protect;
    private static SharedPreferences sharedPref;

    public static void grantPermissionsStorage(final Activity activity) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int hasWRITE_EXTERNAL_STORAGE = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.app_permissions_title)
                            .setMessage(helper_main.textSpannable(activity.getString(R.string.app_permissions)))
                            .setNeutralButton(R.string.toast_notAgain, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    sharedPref.edit()
                                            .putBoolean("perm_notShow", false)
                                            .apply();
                                }
                            })
                            .setPositiveButton(activity.getString(R.string.toast_yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (android.os.Build.VERSION.SDK_INT >= 23)
                                        activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            })
                            .setNegativeButton(activity.getString(R.string.toast_cancel), null)
                            .show();
                    return;
                }
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
    }

    static void grantPermissionsLoc(final Activity activity) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            int hasACCESS_FINE_LOCATION = activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
                if (!activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.app_permissions_title)
                            .setMessage(helper_main.textSpannable(activity.getString(R.string.app_permissions)))
                            .setNeutralButton(R.string.toast_notAgain, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    sharedPref.edit()
                                            .putBoolean("perm_notShow", false)
                                            .apply();
                                }
                            })
                            .setPositiveButton(activity.getString(R.string.toast_yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (android.os.Build.VERSION.SDK_INT >= 23)
                                        activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                REQUEST_CODE_ASK_PERMISSIONS_1);
                                }
                            })
                            .setNegativeButton(activity.getString(R.string.toast_cancel), null)
                            .show();
                    return;
                }
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS_1);
            }
        }
    }

    public static String secString (String string) {
        if(TextUtils.isEmpty(string)){
            return "";
        }else {
            return  string.replaceAll("'", "\'\'");
        }
    }

    public static String createDate_sec () {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
        return  format.format(date);
    }

    public static String createDate_norm () {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return  format.format(date);
    }

    public static File newFile (String fileName) {
        return  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + fileName);
    }

    public static SpannableString textSpannable (String text) {
        SpannableString s;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            s = new SpannableString(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
        } else {
            //noinspection deprecation
            s = new SpannableString(Html.fromHtml(text));
        }
        Linkify.addLinks(s, Linkify.WEB_URLS);
        return s;
    }

    static void switchToActivity(Activity activity, Class to) {
        Intent intent = new Intent(activity, to);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
    }

    public static void closeApp (final Activity activity, WebView webView) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPref.edit().putString("started", "").apply();
        sharedPref.edit().putInt("closeApp", 1).apply();

        if (sharedPref.getBoolean ("clearCookies", false)){
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookies(null);
            cookieManager.flush();
        }

        if (sharedPref.getBoolean ("clearCache", false)){
            webView.clearCache(true);
        }

        if (sharedPref.getBoolean ("clearForm", false)){
            webView.clearFormData();
        }

        if (sharedPref.getBoolean ("history", false)){
            activity.deleteDatabase("history_DB_v01.db");
            webView.clearHistory();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        }, 500);
    }

    public static void setTheme (Activity activity) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        String theme = sharedPref.getString("theme", "0");

        switch (theme) {
            case "0":
                activity.setTheme(R.style.AppTheme_blue);
                break;
            case "1":
                activity.setTheme(R.style.AppTheme);
                break;
            case "2":
                activity.setTheme(R.style.AppTheme_pink);
                break;
            case "3":
                activity.setTheme(R.style.AppTheme_purple);
                break;
            case "4":
                activity.setTheme(R.style.AppTheme_teal);
                break;
            case "5":
                activity.setTheme(R.style.AppTheme_red);
                break;
            case "6":
                activity.setTheme(R.style.AppTheme_orange);
                break;
            case "7":
                activity.setTheme(R.style.AppTheme_brown);
                break;
            case "8":
                activity.setTheme(R.style.AppTheme_grey);
                break;
            case "9":
                activity.setTheme(R.style.AppTheme_darkGrey);
                break;
        }
    }

    public static void onStart (final Activity activity) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        if (sharedPref.getString ("fullscreen", "2").equals("1") || sharedPref.getString ("fullscreen", "2").equals("3")){
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }

        if (sharedPref.getString("orientation", "auto").equals("landscape")) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (sharedPref.getString("orientation", "auto").equals("portrait")) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (sharedPref.getString("orientation", "auto").equals("auto")) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }

        sharedPref.edit().putString("openURL", sharedPref.getString("startURL", "file:///android_asset/kidsportal.html")).apply();
        sharedPref.edit().putString("webView_url", "").apply();
        sharedPref.edit().putInt("keyboard", 0).apply();
        sharedPref.edit().putInt("tab", 0).apply();
        sharedPref.edit().putInt("closeApp", 0).apply();
        sharedPref.edit().putInt("appShortcut", 0).apply();
        sharedPref.getInt("keyboard", 0);

        boolean show = sharedPref.getBoolean("introShowDo_notShow", true);

        if (show){
            Intent intent = new Intent(activity,Activity_intro.class);
            activity.startActivity(intent);

            activity.finish();

        }else if(sharedPref.getString("user", "" ).equals("")){

        Intent intent = new Intent(activity, Login.class);
        activity.startActivity(intent);
        activity.finish();
    }

        if (sharedPref.getString("saved_key_ok", "no").equals("no")) {
            char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!§$%&/()=?;:_-.,+#*<>".toCharArray();
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 25; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            sharedPref.edit().putString("saved_key", sb.toString()).apply();
            sharedPref.edit().putString("saved_key_ok", "yes").apply();
        }
    }



    public static void open (String extension, Activity activity, File pathFile, View view) {
        File file = new File(pathFile.getAbsolutePath());
        String fileExtension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
        String text = activity.getString(R.string.toast_extension) + ": " + fileExtension;
        switch (extension) {
            case ".gif":case ".bmp":case ".tiff":case ".svg":case ".png":case ".jpg":case ".JPG":case ".jpeg":
                helper_main.openFile(activity, pathFile, "image/*", view);
                break;
            case ".m3u8":case ".mp3":case ".wma":case ".midi":case ".wav":case ".aac":case ".aif":case ".amp3":case ".weba":
                helper_main.openFile(activity, pathFile, "audio/*", view);
                break;
            case ".mpeg":case ".mp4":case ".ogg":case ".webm":case ".qt":case ".3gp":case ".3g2":case ".avi":case ".f4v":
            case ".flv":case ".h261":case ".h263":case ".h264":case ".asf":case ".wmv":
                helper_main.openFile(activity, pathFile, "video/*", view);
                break;
            case ".rtx":case ".csv":case ".txt":case ".vcs":case ".vcf":case ".css":case ".ics":case ".conf":case ".config":case ".java":
                helper_main.openFile(activity, pathFile, "text/*", view);
                break;
            case ".html":
                helper_main.openFile(activity, pathFile, "text/html", view);
                break;
            case ".apk":
                helper_main.openFile(activity, pathFile, "application/vnd.android.package-archive", view);
                break;
            case ".pdf":
                helper_main.openFile(activity, pathFile, "application/pdf", view);
                break;
            case ".doc":
                helper_main.openFile(activity, pathFile, "application/msword", view);
                break;
            case ".xls":
                helper_main.openFile(activity, pathFile, "application/vnd.ms-excel", view);
                break;
            case ".ppt":
                helper_main.openFile(activity, pathFile, "application/vnd.ms-powerpoint", view);
                break;
            case ".docx":
                helper_main.openFile(activity, pathFile, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", view);
                break;
            case ".pptx":
                helper_main.openFile(activity, pathFile, "application/vnd.openxmlformats-officedocument.presentationml.presentation", view);
                break;
            case ".xlsx":
                helper_main.openFile(activity, pathFile, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", view);
                break;
            case ".odt":
                helper_main.openFile(activity, pathFile, "application/vnd.oasis.opendocument.text", view);
                break;
            case ".ods":
                helper_main.openFile(activity, pathFile, "application/vnd.oasis.opendocument.spreadsheet", view);
                break;
            case ".odp":
                helper_main.openFile(activity, pathFile, "application/vnd.oasis.opendocument.presentation", view);
                break;
            case ".zip":
                helper_main.openFile(activity, pathFile, "application/zip", view);
                break;
            case ".rar":
                helper_main.openFile(activity, pathFile, "application/x-rar-compressed", view);
                break;
            case ".epub":
                helper_main.openFile(activity, pathFile, "application/epub+zip", view);
                break;
            case ".cbz":
                helper_main.openFile(activity, pathFile, "application/x-cbz", view);
                break;
            case ".cbr":
                helper_main.openFile(activity, pathFile, "application/x-cbr", view);
                break;
            case ".fb2":
                helper_main.openFile(activity, pathFile, "application/x-fb2", view);
                break;
            case ".rtf":
                helper_main.openFile(activity, pathFile, "application/rtf", view);
                break;
            case ".opml":
                helper_main.openFile(activity, pathFile, "application/opml", view);
                break;

            default:
                Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
        }
    }

    private static void openFile(Activity activity, File file, String string, View view) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", file);
            intent.setDataAndType(contentUri,string);

        } else {
            intent.setDataAndType(Uri.fromFile(file),string);
        }

        try {
            activity.startActivity (intent);
        } catch (ActivityNotFoundException e) {
            Snackbar.make(view, R.string.toast_install_app, Snackbar.LENGTH_LONG).show();
        }
    }

    public static void installShortcut (Activity activity, String title, String url, View view) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.setClassName(activity, "jae.KidsPortal.Browser.Activity_Main");
        i.setData(Uri.parse(url));

        Intent shortcut = new Intent();
        shortcut.putExtra("android.intent.extra.shortcut.INTENT", i);
        shortcut.putExtra("android.intent.extra.shortcut.NAME", "THE NAME OF SHORTCUT TO BE SHOWN");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(activity.getApplicationContext(), R.mipmap.ic_launcher));
        shortcut.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        activity.sendBroadcast(shortcut);
        Snackbar.make(view, R.string.menu_createShortcut_success, Snackbar.LENGTH_SHORT).show();
    }

    public static void save_readLater (Activity activity, String title, String url, View view) {
        DbAdapter_ReadLater db = new DbAdapter_ReadLater(activity);
        db.open();
        if(db.isExist(helper_main.secString(url))){
            Snackbar.make(view, R.string.toast_newTitle, Snackbar.LENGTH_LONG).show();
        }else{
            db.insert(helper_main.secString(title), helper_main.secString(url), "", "", helper_main.createDate_norm());
            Snackbar.make(view, R.string.bookmark_added, Snackbar.LENGTH_LONG).show();
        }
    }

    public static void save_bookmark (Activity activity, String title, String url, View view) {
        DbAdapter_Bookmarks db = new DbAdapter_Bookmarks(activity);
        db.open();
        if(db.isExist(helper_main.secString(url))){
            Snackbar.make(view, R.string.toast_newTitle, Snackbar.LENGTH_LONG).show();
        }else{
            db.insert(helper_main.secString(title), helper_main.secString(url), "", "", helper_main.createDate_norm());
            Snackbar.make(view, R.string.bookmark_added, Snackbar.LENGTH_LONG).show();
        }
    }

    public static void save_Report (Activity activity, String title, String url, View view) {
        DbAdapter_Reports db = new DbAdapter_Reports(activity);
        db.open();
        if(db.isExist(helper_main.secString(url))){
            Snackbar.make(view, R.string.toast_newTitle, Snackbar.LENGTH_LONG).show();
        }else{
            db.insert(helper_main.secString(title), helper_main.secString(url), "", "", helper_main.createDate_norm());
            Snackbar.make(view, R.string.bookmark_added, Snackbar.LENGTH_LONG).show();
        }
    }
}