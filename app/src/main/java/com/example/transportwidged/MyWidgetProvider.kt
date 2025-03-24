package com.example.transportwidged

import android.app.PendingIntent
import androidx.core.content.ContextCompat
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.transportwidged.R
class MyWidgetProvider : AppWidgetProvider() {

        override fun onUpdate(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
        ) {
            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_page)

                // Set up the ListView to use RemoteViewsService
                val intent = Intent(context, WidgetListService::class.java)
                views.setRemoteAdapter(R.id.widget_list, intent)

                // Set up an empty view in case the list is empty
//                views.setEmptyView(R.id.widget_list, R.id.widget_empty)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }


    companion object {
        public var totalPages = 1 // Default 1 page
        public var currentPage = 0

        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, pageIndex: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            // Simulate data fetching (Replace this with actual network fetching)
            val dataPages = fetchDynamicData()
            totalPages = dataPages.size
            currentPage = pageIndex.coerceIn(0, totalPages - 1)

            // Clear ViewFlipper before adding new views
            views.removeAllViews(R.id.widget_viewflipper)

            // Dynamically add pages
            for (data in dataPages) {
                val pageView = RemoteViews(context.packageName, R.layout.widget_page)
                populatePage(context, pageView, data,)
                views.addView(R.id.widget_viewflipper, pageView)
            }

            // Set the correct page
            views.setDisplayedChild(R.id.widget_viewflipper, currentPage)

            // Update page indicator
            updatePageIndicator(context, views, currentPage, totalPages)

            // Add swipe intents
            val leftIntent = Intent(context, WidgetSwipeReceiver::class.java).apply { action = "SWIPE_LEFT" }
            val rightIntent = Intent(context, WidgetSwipeReceiver::class.java).apply { action = "SWIPE_RIGHT" }

            val pendingLeft = PendingIntent.getBroadcast(context, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT
                   or PendingIntent.FLAG_MUTABLE)
            val pendingRight = PendingIntent.getBroadcast(context, 1, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT
                    or PendingIntent.FLAG_MUTABLE)

            views.setOnClickPendingIntent(R.id.widget_viewflipper, pendingLeft)
            views.setOnClickPendingIntent(R.id.widget_viewflipper, pendingRight)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun fetchDynamicData(): List<List<Pair<String, String>>> {

            return listOf(
                listOf(
                    Pair("Item 1", "Description 1"),
                    Pair("Item 2", "Description 2"),
                    Pair("Item 2", "Description 2"),
                    Pair("Item 2", "Description 2")),
                listOf(
                    Pair("Item 1", "Description 1"),
                    Pair("Item 3", "Description 3")),
                listOf(
                    Pair("Item 1", "Description 1"),
                    Pair("Item 2", "Description 2")),
            ) // Simulated multiple pages
        }

        private fun populatePage(context: Context, pageView: RemoteViews, data: List<Pair<String, String>>) {
            pageView.removeAllViews(R.id.widget_list) // Ensure this ID exists in XML

            for ((left, right) in data) {
                val itemView = RemoteViews(context.packageName, R.layout.widget_list_item) // ✅ FIXED!
                itemView.setTextViewText(R.id.item_left, left)
                itemView.setTextViewText(R.id.item_right, right)
                pageView.addView(R.id.widget_list, itemView) // Ensure widget_list exists in XML
            }
        }

        private fun updatePageIndicator(context: Context, views: RemoteViews, currentPage: Int, totalPages: Int) {
            views.removeAllViews(R.id.widget_page_indicator)
            for (i in 0 until totalPages) {
                val dot = RemoteViews(context.packageName, R.layout.widget_page_dot)
                val color = if (i == currentPage) R.color.teal_700 else R.color.gray
                dot.setInt(R.id.dot, "setColorFilter", ContextCompat.getColor(context, color))
                views.addView(R.id.widget_page_indicator, dot)
            }
        }
    }
}
