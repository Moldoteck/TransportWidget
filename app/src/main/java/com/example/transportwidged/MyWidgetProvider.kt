package com.example.transportwidged

import android.content.Context
import android.widget.RemoteViews
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.transportwidged.R

class BusTimetableWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_bus_timetable)

            // Set click actions (currently non-functional)
            val leftIntent = Intent(context, BusTimetableWidget::class.java)
            val leftPendingIntent = PendingIntent.getBroadcast(context, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.button_left, leftPendingIntent)

            val rightIntent = Intent(context, BusTimetableWidget::class.java)
            val rightPendingIntent = PendingIntent.getBroadcast(context, 1, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.button_right, rightPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
