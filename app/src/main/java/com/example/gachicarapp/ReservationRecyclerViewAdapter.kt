// MyItemRecyclerViewAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gachicarapp.databinding.FragmentReservationListItemBinding
import com.example.gachicarapp.retrofit.response.Reservation

class ReservationRecyclerViewAdapter(private var reservations: List<Reservation>) :
    RecyclerView.Adapter<ReservationRecyclerViewAdapter.ReservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding =
            FragmentReservationListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(reservations[position])
    }

    override fun getItemCount(): Int {
        return reservations.size
    }

    fun updateData(newData: List<Reservation>) {
        reservations = newData
        notifyDataSetChanged()
    }

    inner class ReservationViewHolder(private val binding: FragmentReservationListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reservation: Reservation) {
            with(binding) {
                tvUserItem.text = reservation.userName
                tvDestinationItem.text = reservation.destination
                tvStartTimeItem.text = reservation.startTime
                tvEndTimeItem.text = reservation.endTime
            }
        }
    }

}
