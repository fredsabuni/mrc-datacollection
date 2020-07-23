package com.mrc.reports.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.mrc.reports.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

    //Getting Authorization-Token
    public static String getAuthorizationToken() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        // Encode data on your side using BASE64
        byte[] data = currentDate.getBytes("UTF-8");
        String encodedDate = Base64.encodeToString(data, Base64.DEFAULT);
        //Apply SHA1
        String encryptedDate = SHA1(encodedDate);
        //TODO:Delete this after testing
        Log.d("SHA1", encryptedDate);
        return encodedDate;

    }

    public static boolean isValid(String string) {
//        Log.d(TAG, "isValid");
        return (string != null && !string.trim().isEmpty()) ? true : false;
    }

    public static File writeImage(byte[] jpeg) {
        File dir = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera");
        if (!dir.exists())
            dir.mkdir();
        File photo = new File(dir, "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmSS", Locale.ENGLISH).format(new Date()) + ".jpg");
        if (photo.exists()) {
            photo.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(photo.getPath());
            fos.write(jpeg);
            fos.close();
        } catch (Exception ignored) {
        }
        return photo;
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    //Convert Bitmap to base64
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    //Convert base64 to bitmap
    public static Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


    //Strip Html tags
    public static Spanned stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }


    /**
     * Get from and to start date for datePicker
     * @param from a boolean value expressing if is a to or from start date required
     *             if from start date : reduce one hour from the current datetime,
     *             if to start date : add one day from today this is under the assumption that
     *             minimum date difference required is one day.
     * @return
     */
    public static Date getPickerStartDate(boolean from){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        if(from) {
            calendar.add(Calendar.HOUR_OF_DAY, -1);
            calendar.add(Calendar.YEAR, -18);
        } else
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }


    /**
     * Get from and to start date for datePicker
     * @param from a boolean value expressing if is a to or from start date required
     *             if from start date : reduce one hour from the current datetime,
     *             if to start date : add one day from today this is under the assumption that
     *             minimum date difference required is one day.
     * @return
     */
    public static Date getPickerDate(boolean from){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        if(from) {
            calendar.add(Calendar.HOUR_OF_DAY, -1);
        } else
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    /**
     * Check if we have internet connection
     * @param context
     * @return
     */
    public static boolean isInternetAvailable(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        Toast.makeText(context,context.getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        return false;
    }

    public static String getDateInString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);

    }

    public static String getBase64ImageFile(String img_path){
        File imgFile = new File(img_path);
        String encoded = "";
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        }
        return encoded;


    }

    /**
     * Hide soft input keybord
     * @param context
     * @param view
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    //Validate Email Address
    public static boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    public static String getCompatibility(String compatibility){

        String firstPart;
        if (compatibility.contains(".")) {
        String[] parts = compatibility.split(Pattern.quote("."));
        firstPart = parts[0];
        }else {
            throw new IllegalArgumentException("String " + compatibility + " does not contain .");
        }
        return firstPart + "%";
    }


    // The public static function which can be called from other classes
    public static void changeStatusBar(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            activity.getWindow().setStatusBarColor(
                    changeColor(
                            ContextCompat.getColor(activity, color)));
        }

    }


    // Code to change the color supplied (mostly color of toolbar)
    private static int changeColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

}
