package com.example.transportwidged

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class BusTimetableWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        val prefs = applicationContext.getSharedPreferences("BusTimetableWidgetPrefs", Context.MODE_PRIVATE)
        val currentStation = prefs.getString("currentStation", "Station A") ?: "Station A"
        return BusTimetableRemoteViewsFactory(applicationContext, currentStation)

       // return BusTimetableRemoteViewsFactory(currentStation)  // Pass currentStation to the factory
    }
}

class BusTimetableRemoteViewsFactory(
    private val context: Context,
    private val currentStation: String
) : RemoteViewsService.RemoteViewsFactory {
private val busSchedule = mutableListOf<Pair<String, String>>()
    // Mock data stored here
    private val mockData = mutableMapOf(
        "Station A" to listOf("25N" to "5 min", "42B" to "12 min", "10X" to "20 min", "42B" to "12 min", "10X" to "20 min", "42B" to "12 min", "10X" to "20 min"),
        "Station B" to listOf("50A" to "3 min", "88C" to "15 min", "12Z" to "30 min")
    )
    override fun onCreate() {
        busSchedule.clear()
        busSchedule.addAll(mockData[currentStation] ?: emptyList())
    }

    override fun onDataSetChanged() {
        // Populate mock data (this should be replaced with real data from JSON)
        val prefs = context.getSharedPreferences("BusTimetableWidgetPrefs", Context.MODE_PRIVATE)
        val currentStation = prefs.getString("currentStation", "Station A") ?: "Station A"

        busSchedule.clear()
        busSchedule.addAll(mockData[currentStation] ?: emptyList())
    }

    override fun onDestroy() {
        busSchedule.clear()
    }

    override fun getCount(): Int = busSchedule.size

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews("com.example.transportwidged", R.layout.widget_list_item)
        val (route, time) = busSchedule[position]

        views.setTextViewText(R.id.route_name, route)
        views.setTextViewText(R.id.arrival_time, time)

        return views
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
}
