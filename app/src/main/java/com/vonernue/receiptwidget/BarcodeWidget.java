package com.vonernue.receiptwidget;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.os.Bundle;
import android.R.color;

import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BarcodeWidgetConfigureActivity BarcodeWidgetConfigureActivity}
 */
public class BarcodeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = BarcodeWidgetConfigureActivity.loadTitlePref(context, appWidgetId);

        Bundle awo = appWidgetManager.getAppWidgetOptions( appWidgetId);
        int h = awo.getInt("appWidgetMaxHeight");
        int w = awo.getInt("appWidgetMaxWidth");


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.barcode_widget);

        // Generate Barcode
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(widgetText.toString(), BarcodeFormat.CODE_39, 800, 200, null);
            Bitmap bitmap = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Bitmap.Config.ARGB_8888);
            for (int i = 0; i < bitMatrix.getWidth(); i++) {
                for (int j = 0; j < bitMatrix.getHeight(); j++) {
                    bitmap.setPixel(i, j, bitMatrix.get(i, j) ? ContextCompat.getColor(context, color.system_neutral2_800) : ContextCompat.getColor(context, color.system_accent2_100));
                }
            }
            views.setBitmap(R.id.barcodeImage, "setImageBitmap", bitmap);
            views.setTextViewText(R.id.barcodeText, widgetText);
        }catch (WriterException e) {
            e.printStackTrace();
        }



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BarcodeWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}