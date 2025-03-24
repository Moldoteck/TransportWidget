package com.example.transportwidged

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class BusTimetableWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_bus_timetable)

        // Set the adapter for the ListView
        val intent = Intent(context, BusTimetableWidgetService::class.java)
        views.setRemoteAdapter(R.id.bus_list, intent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
