package com.example.transportwidged

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.transportwidged.R

class MyRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return MyRemoteViewsFactory(this.applicationContext)
    }
}

class MyRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var data: List<Pair<String, String>> = listOf()

    override fun onCreate() {}
    override fun onDataSetChanged() {
//        data = fetchDataFromWeb() // Replace with actual function
        data = listOf(
            Pair("Item 1", "Description 1"),
            Pair("Item 2", "Description 2"),
            Pair("Item 3", "Description 3"),
            Pair("Item 4", "Description 4")
        )
    }
    override fun onDestroy() {}

    override fun getCount(): Int = data.size

    override fun getViewAt(position: Int): RemoteViews {
        val item = data[position]
        val views = RemoteViews(context.packageName, R.layout.widget_list_item)
        views.setTextViewText(R.id.item_left, item.first)
        views.setTextViewText(R.id.item_right, item.second)
        return views
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
}
