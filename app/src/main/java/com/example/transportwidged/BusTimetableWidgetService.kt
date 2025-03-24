package com.example.transportwidged

import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class BusTimetableWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return BusTimetableRemoteViewsFactory()
    }
}

class BusTimetableRemoteViewsFactory : RemoteViewsService.RemoteViewsFactory {
    private val busSchedule = mutableListOf<Pair<String, String>>()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        // Populate mock data (this should be replaced with real data from JSON)
        busSchedule.clear()
        busSchedule.addAll(
            listOf(
                "25N" to "5 min",
                "42B" to "12 min",
                "10X" to "20 min",
                "30Y" to "25 min",
                "25N" to "5 min",
                "42B" to "12 min",
                "10X" to "20 min",
                "30Y" to "25 min",
                "25N" to "5 min",
                "42B" to "12 min",
                "10X" to "20 min",
                "30Y" to "25 min"
            )
        )
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
