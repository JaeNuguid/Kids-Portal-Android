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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mobapphome.mahencryptorlib.MAHEncryptor;

import java.util.ArrayList;
import java.util.List;

import jae.KidsPortal.Browser.R;
import jae.KidsPortal.Browser.databases.DbAdapter_Pass;

public class helper_editText {

    public static void editText_savePass(final Activity activity, final View view, final String title, final String url) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogView = View.inflate(activity, R.layout.dialog_login, null);

        final EditText pass_title = (EditText) dialogView.findViewById(R.id.pass_title);
        final EditText pass_userName = (EditText) dialogView.findViewById(R.id.pass_userName);
        final EditText pass_userPW = (EditText) dialogView.findViewById(R.id.pass_userPW);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pass_title.setText(title);
            }
        }, 100);

        builder.setView(dialogView);
        builder.setTitle(R.string.pass_edit);
        builder.setPositiveButton(R.string.toast_yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {

                String input_pass_title = pass_title.getText().toString().trim();

                try {

                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                    MAHEncryptor mahEncryptor = MAHEncryptor.newInstance(sharedPref.getString("saved_key", ""));
                    String encrypted_userName = mahEncryptor.encode(pass_userName.getText().toString().trim());
                    String encrypted_userPW = mahEncryptor.encode(pass_userPW.getText().toString().trim());

                    DbAdapter_Pass db = new DbAdapter_Pass(activity);
                    db.open();
                    if(db.isExist(helper_main.secString(input_pass_title))){
                        Snackbar.make(view, activity.getString(R.string.toast_newTitle), Snackbar.LENGTH_LONG).show();
                    }else{
                        db.insert(helper_main.secString(input_pass_title), helper_main.secString(url), helper_main.secString(encrypted_userName), helper_main.secString(encrypted_userPW), helper_main.createDate_norm());
                        Snackbar.make(view, R.string.pass_success, Snackbar.LENGTH_LONG).show();
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(pass_title.getWindowToken(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(view, R.string.toast_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.toast_cancel, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        helper_editText.showKeyboard(activity, pass_title, 0, "", activity.getString(R.string.pass_title));
    }

    public static void editText_searchWeb (final EditText editText, final Activity activity) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        List<String> listItems = new ArrayList<>();

        if (sharedPref.getBoolean("Google", true)) {
            listItems.add("Google");
        }if (sharedPref.getBoolean("Kiddle", true)) {
            listItems.add("Kiddle");
        }if (sharedPref.getBoolean("Kidrex", true)) {
            listItems.add("Kidrex");
        }if (sharedPref.getBoolean("Youtube", true)) {
            listItems.add("Youtube");
        }
        final CharSequence[] options = listItems.toArray(new CharSequence[listItems.size()]);

        new AlertDialog.Builder(activity)
                .setTitle(R.string.action_searchChooseTitle)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Google")) {
                            editText.setText(".G ");
                        }
                        if (options[item].equals("Kidrex")) {
                            editText.setText(".R ");
                        }
                        if (options[item].equals("Kiddle")) {
                            editText.setText(".K ");
                        }
                        if (options[item].equals("Youtube")) {
                            editText.setText(".Y ");
                        }

                        editText.setSelection(editText.length());
                    }
                }).show();
    }

    public static void hideKeyboard(Activity activity, EditText editText, int i, String text, String hint) {
        editText.clearFocus();
        editText.setText("Search Here...");
        editText.setHint(hint);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        sharedPref.edit().putInt("keyboard", i).apply();
        activity.invalidateOptionsMenu();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showKeyboard(final Activity activity, final EditText editText, final int i, String text, String hint) {
        editText.requestFocus();
        editText.hasFocus();
        editText.setText(text);
        editText.setHint(hint);
        editText.setSelection(editText.length());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                sharedPref.edit().putInt("keyboard", i).apply();
                activity.invalidateOptionsMenu();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }
}