package jae.KidsPortal.Browser.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jae on 9/18/2017.
 */

public class Utils_Checker {
    private static final String eng = "english";
    private static final String tag = "tagalog";
    private static final Set<String> xWords = new HashSet<>();

    public static void init(final Context context){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    loadFromAssets(context);
                } catch (IOException ignored) {

                }
                return null;
            }
        }.execute();
    }

    private static void loadFromAssets(Context context) throws IOException {
        InputStream stream = context.getAssets().open(eng);
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) xWords.add(line);

        stream = context.getAssets().open(tag);
        inputStreamReader = new InputStreamReader(stream);
        bufferedReader = new BufferedReader(inputStreamReader);
        while ((line = bufferedReader.readLine()) != null) xWords.add(line);

        bufferedReader.close();
        inputStreamReader.close();
        stream.close();
    }

    public static boolean isxWord(String x) {

        if(xWords.contains(x)) {
        return true;
        }else {
        return false;
        }

    }


    }




