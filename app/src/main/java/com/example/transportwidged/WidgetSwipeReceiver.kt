package com.example.transportwidged

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class WidgetSwipeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, MyWidgetProvider::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

        for (widgetId in appWidgetIds) {
            when (intent.action) {
                "SWIPE_LEFT" -> MyWidgetProvider.updateWidget(context, appWidgetManager, widgetId, (MyWidgetProvider.currentPage + 1) % MyWidgetProvider.totalPages)
                "SWIPE_RIGHT" -> MyWidgetProvider.updateWidget(context, appWidgetManager, widgetId, (MyWidgetProvider.currentPage - 1 + MyWidgetProvider.totalPages) % MyWidgetProvider.totalPages)
            }
        }
    }
}
