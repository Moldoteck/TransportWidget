package com.example.transportwidged

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class FetchDataWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val context = applicationContext
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, MyWidgetProvider::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

        for (widgetId in appWidgetIds) {
            MyWidgetProvider.updateWidget(context, appWidgetManager, widgetId, MyWidgetProvider.currentPage)
        }

        return Result.success()
    }
}
