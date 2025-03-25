package com.example.transportwidged

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews

class BusTimetableWidget : AppWidgetProvider() {

    companion object {
        private const val PREFS_NAME = "BusTimetableWidgetPrefs"
        private const val KEY_CURRENT_STATION = "currentStation"
        private const val DEFAULT_STATION = "Station A"
    }

    private fun getCurrentStation(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CURRENT_STATION, DEFAULT_STATION) ?: DEFAULT_STATION
    }

    private fun setCurrentStation(context: Context, station: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CURRENT_STATION, station).apply()
    }


    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val currentStation = getCurrentStation(context)
        val views = RemoteViews(context.packageName, R.layout.widget_bus_timetable)

        // Set the station name dynamically
        views.setTextViewText(R.id.station_name, currentStation)

        // Set up the button click intents
        val leftIntent = Intent(context, BusTimetableWidget::class.java).apply {
            action = "CHANGE_STATION_LEFT"
        }
        val leftPendingIntent = PendingIntent.getBroadcast(context, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        views.setOnClickPendingIntent(R.id.button_left, leftPendingIntent)

        val rightIntent = Intent(context, BusTimetableWidget::class.java).apply {
            action = "CHANGE_STATION_RIGHT"
        }
        val rightPendingIntent = PendingIntent.getBroadcast(context, 1, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        views.setOnClickPendingIntent(R.id.button_right, rightPendingIntent)

        // Pass current station in the intent to the service
        val intent = Intent(context, BusTimetableWidgetService::class.java).apply {
            putExtra("currentStation", currentStation)  // Pass the current station to the service
        }
        views.setRemoteAdapter(R.id.bus_list, intent)

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val currentStation = getCurrentStation(context)
        val newStation = when (intent.action) {
            "CHANGE_STATION_LEFT" -> if (currentStation == "Station A") "Station B" else "Station A"
            "CHANGE_STATION_RIGHT" -> if (currentStation == "Station B") "Station A" else "Station B"
            else -> currentStation
        }

        // Persist the new station value
        setCurrentStation(context, newStation)

        // Trigger the widget update
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, BusTimetableWidget::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.bus_list)
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }
}
