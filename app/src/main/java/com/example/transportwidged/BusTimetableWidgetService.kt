package com.example.transportwidged

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
        transformStops()
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
    data class Stop(val stop_id: Int, var stop_name: String, val stop_lat: Float, val stop_lon: Float, val
    location_type: Int, val stop_code: String);


    data class Trip(val route_id: Int, var trip_id: String, var trip_headsign: String, val direction_id: Int, val
    block_id: Int, val shape_id: String,
                    var shape: Triple<Float, Float, Int>? = null);


    data class Shape(var shape_id: String, var shape_pt_lat: Float, val shape_pt_lon: Float, val
    shape_pt_sequence: Int);


    fun transformStops(){

        val inputStream = context.resources.openRawResource(R.raw.stops)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val listType = object : TypeToken<List<Stop>>() {}.type
        var stopsList: List<Stop> = Gson().fromJson(jsonString, listType)
        for (el in stopsList){
            el.stop_name=cleanStopName(el.stop_name)
//            Log.e("tamper", el.stop_name)
        }


        val tripStream = context.resources.openRawResource(R.raw.trips)
        val tripjsonString = tripStream.bufferedReader().use { it.readText() }

        val triplistType = object : TypeToken<List<Trip>>() {}.type
        var tripsList: List<Trip> = Gson().fromJson(tripjsonString, triplistType)
        for (el in tripsList){
            el.trip_headsign=cleanStopName(el.trip_headsign)
            Log.e("tamper head", el.trip_headsign)
        }



        val shapeStream = context.resources.openRawResource(R.raw.shape)
        val shapejsonString = shapeStream.bufferedReader().use { it.readText() }

        val shapelistType = object : TypeToken<List<Shape>>() {}.type
        var shapeList: List<Shape> = Gson().fromJson(shapejsonString, shapelistType)
        var shapeMap: MutableMap<String, MutableList<Shape>> = mutableMapOf()
        for (el in shapeList){
            if(shapeMap.containsKey(el.shape_id)) {
                shapeMap[el.shape_id]?.add(el)
            }else {
                shapeMap[el.shape_id] = mutableListOf(el)
            }
        }
        for (el in tripsList){
            if(shapeMap.containsKey(el.shape_id)){
                Log.e("tamper head", shapeMap[el.shape_id].toString())
//                el.shape=
            }
        }




    }
    fun cleanStopName(stopName: String): String {
        var name = stopName
            .replace('ă', 'a')
            .replace('î', 'i')
            .replace('â', 'a')
            .replace('ș', 's')
            .replace('ț', 't')
            .trimEnd()  // removes trailing whitespace

        if (name.endsWith("Sos")) {
            name = name.replace("Sos", "Sosire")
        } else if (name.endsWith("Sos.")) {
            name = name.replace("Sos.", "Sosire")
        }

        return name
    }
}

// din stops - de scos toate diacriticile (ă cu a)
// din trip headsign - la fel

//# Create a mapping of stop_id to stop_name
//stop_dict = {stop["stop_id"]: stop["stop_name"] for stop in stops}
//
//for trip in trips:
//# print(f"Trip: {trip['trip_headsign']}")
//
//# Get stops for this trip, sorted by stop_sequence
//trip_stops = sorted(
//[st for st in stop_trip if st["trip_id"] == trip["trip_id"]],
//key=lambda x: x["stop_sequence"]
//)
//trip_stops = [stop_dict[st["stop_id"]] for st in trip_stops]
//
//trip_stops = [trip_stops[0],trip_stops[-1]]
//trip_stop=trip_stops[1]
//tram = trip['trip_headsign']
//if tram != trip_stop:
//print(f"  - {tram, trip_stop}")
//
//# for stop in trip_stops:
//#     print(f"  - {stop_dict.get(stop['stop_id'], 'Unknown Stop')}")




//// get routers
////get trips.
////get stops
////get stop-trip
////for each trip create a structure
//{
//    //trip data
//    //shape:[lat, lon, stop_id]//-1 by default
//}
//for each trip stop
//find closest point index
//find closest line segment between 2 shape points index
//get min between these
//place after this point with corresponding stop id
//
////
//manually map between stop names and trip headsign to have same values
//
////for each bus
//get corresponding trip id (from route ID get it's name')
//get closest point
//if point is station and bus is between this point and next one segment,
//get next station point in this trip
//else - get next station point
////calculate distance between bus and that point
////calculate arrival time?
//
//// can start with - get all stations between this bus and desired station
//
//
//
////stops:[stop id]
