package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Nish on 28-02-2016.
 */
public class WatchFace {
    private static final String TIME_FORMAT_WITHOUT_SECONDS = "HH:mm";
    private static final String TIME_FORMAT_WITH_SECONDS = "HH:mm:ss";
    private static final String DATE_FORMAT = "EEE, MMM dd, yyyy";
    private static int DATE_AND_TIME_DEFAULT_COLOUR = Color.WHITE;
    private static int BACKGROUND_DEFAULT_COLOUR = Color.BLACK;

    private final Paint timePaint;
    private final Paint datePaint;
    private final Paint backgroundPaint;
    private final Calendar c;

    private Paint weatherIconPaint;
    private Bitmap weatherIconBitmap;
    private Bitmap grayWeatherIconBitmap;
    private String highTemp;
    private String lowTemp;
    private boolean ambient;

    private boolean shouldShowSeconds = true;
    private static int backgroundColour = BACKGROUND_DEFAULT_COLOUR;
    private static int dateAndTimeColour = DATE_AND_TIME_DEFAULT_COLOUR;

    public static WatchFace newInstance(Context context) {

        backgroundColour = context.getResources().getColor(R.color.primary);

        Paint timePaint = new Paint();
        timePaint.setColor(DATE_AND_TIME_DEFAULT_COLOUR);
        timePaint.setTextSize(context.getResources().getDimension(R.dimen.time_size));
        timePaint.setAntiAlias(true);

        Paint datePaint = new Paint();
        datePaint.setColor(DATE_AND_TIME_DEFAULT_COLOUR);
        datePaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
        datePaint.setAntiAlias(true);

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColour);

        Paint weatherIconPaint = new Paint();

        return new WatchFace(context, timePaint, datePaint, backgroundPaint, weatherIconPaint, Calendar.getInstance());
    }

    WatchFace(Context context, Paint timePaint, Paint datePaint, Paint backgroundPaint, Paint weatherIconPaint, Calendar c) {
        this.timePaint = timePaint;
        this.datePaint = datePaint;
        this.backgroundPaint = backgroundPaint;
        this.weatherIconPaint = weatherIconPaint;
        this.ambient = false;
        this.c = c;
        c.setTimeZone(TimeZone.getDefault());
        updateWeatherDetails(context, 0, 0, null);
    }

    public void updateWeatherDetails(Context context, double high, double low, String icon  ){
        if(icon == null){
            icon = "ic_clear";
        }
        int resId = context.getResources().getIdentifier(icon , "drawable", context.getPackageName());
        int resIdBw = context.getResources().getIdentifier(icon + "_bw" , "drawable", context.getPackageName());

        this.weatherIconBitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        this.grayWeatherIconBitmap = BitmapFactory.decodeResource(context.getResources(), resIdBw);
        this.highTemp = String.format("%1.0f",high) + "° C";
        this.lowTemp = String.format("%1.0f",low) + "° C";
    }

    public void draw(Canvas canvas, Rect bounds) {
        c.setTime(new Date());
        canvas.drawRect(0, 0, bounds.width(), bounds.height(), backgroundPaint);

        SimpleDateFormat tf = new SimpleDateFormat(shouldShowSeconds ? TIME_FORMAT_WITH_SECONDS : TIME_FORMAT_WITHOUT_SECONDS);
        String timeText = tf.format(c.getTime());
        float timeXOffset = computeXOffset(timeText, timePaint, bounds);
        float timeYOffset = computeTimeYOffset(timeText, timePaint, bounds);
        canvas.drawText(timeText, timeXOffset, timeYOffset, timePaint);

        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String dateText = df.format(c.getTime());
        float dateXOffset = computeXOffset(dateText, datePaint, bounds);
        float dateYOffset = computeDateYOffset(dateText, datePaint);
        canvas.drawText(dateText, dateXOffset, timeYOffset + dateYOffset, datePaint);

        float xOffset;
        float yOffset = timeYOffset + dateYOffset;

        yOffset += getTextHeight(dateText, datePaint);
        xOffset = (bounds.width() - (weatherIconBitmap.getWidth() + 20 + datePaint.measureText(highTemp)  )) /2;

        if(ambient) {
            canvas.drawBitmap(grayWeatherIconBitmap, xOffset, yOffset, weatherIconPaint);
        } else {
            canvas.drawBitmap(weatherIconBitmap, xOffset, yOffset, weatherIconPaint);
        }

        xOffset += weatherIconBitmap.getWidth() + 5;
        yOffset = yOffset + weatherIconBitmap.getHeight() /2;
        canvas.drawText(highTemp, xOffset , yOffset -5, datePaint);
        yOffset += getTextHeight(highTemp, datePaint);
        canvas.drawText(lowTemp, xOffset , yOffset +5, datePaint);
    }

    private float computeXOffset(String text, Paint paint, Rect watchBounds) {
        float centerX = watchBounds.exactCenterX();
        float timeLength = paint.measureText(text);
        return centerX - (timeLength / 2.0f);
    }

    private float computeTimeYOffset(String timeText, Paint timePaint, Rect watchBounds) {
        float centerY = watchBounds.exactCenterY();
        Rect textBounds = new Rect();
        timePaint.getTextBounds(timeText, 0, timeText.length(), textBounds);
        return centerY;
    }

    private float computeDateYOffset(String dateText, Paint datePaint) {
        Rect textBounds = new Rect();
        datePaint.getTextBounds(dateText, 0, dateText.length(), textBounds);
        return textBounds.height() + 10.0f;
    }

    private float getTextHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    public void setAntiAlias(boolean antiAlias) {
        timePaint.setAntiAlias(antiAlias);
        datePaint.setAntiAlias(antiAlias);
        weatherIconPaint.setAntiAlias(antiAlias);
    }

    public void setShowSeconds(boolean showSeconds) {
        shouldShowSeconds = showSeconds;
    }

    public void restoreBackgroundColour() {
        backgroundPaint.setColor(backgroundColour);
    }

    public void updateBackgroundColourToDefault() {
        backgroundPaint.setColor(BACKGROUND_DEFAULT_COLOUR);
    }

    public void updateDateAndTimeColourToDefault() {
        timePaint.setColor(DATE_AND_TIME_DEFAULT_COLOUR);
        datePaint.setColor(DATE_AND_TIME_DEFAULT_COLOUR);
        ambient = true;
    }

    public void restoreDateAndTimeColour() {
        timePaint.setColor(dateAndTimeColour);
        datePaint.setColor(dateAndTimeColour);
        ambient = false;
    }
}