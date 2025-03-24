package com.example.transportwidged

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class WidgetListFactory(private val context: Context, intent: Intent) :
    RemoteViewsService.RemoteViewsFactory {
    private val items = listOf(
        "Item 1" to "Description 1",
        "Item 2" to "Description 2",
        "Item 3" to "Description 3",
        "Item 4" to "Description 4",
        "Item 5" to "Description 5"
    )
    // This method is required, but you can leave it empty if you don't need a special loading view
    override fun getLoadingView(): RemoteViews? {
        // Return a simple loading view (you can customize this as needed)
//        return RemoteViews(context.packageName, R.layout.widget_loading_layout)
        return null
    }
    override fun onCreate() {
        // Initialize any data or resources here
    }

    override fun onDataSetChanged() {
        // Optionally, update data if necessary
    }

    override fun onDestroy() {
        // Cleanup if needed
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val pair = items[position]
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_list_item)

        // Bind data to the views
        remoteViews.setTextViewText(R.id.item_left, pair.first)
        remoteViews.setTextViewText(R.id.item_right, pair.second)

//        // Set a click action if needed
//        val intent = Intent(context, YourActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        remoteViews.setOnClickPendingIntent(R.id.widget_item, pendingIntent)

        return remoteViews
    }

    override fun getViewTypeCount(): Int {
        return 1 // Single type of item layout
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}
