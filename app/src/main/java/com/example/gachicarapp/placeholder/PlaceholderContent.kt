// PlaceholderContent.kt

import com.example.gachicarapp.retrofit.response.Reservation

object PlaceholderContent {

    val ITEMS: MutableList<Reservation> = ArrayList()

    fun addItems(reservations: List<Reservation>) {
        ITEMS.clear()
        ITEMS.addAll(reservations)
    }
}
