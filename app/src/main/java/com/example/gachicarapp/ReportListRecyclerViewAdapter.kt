// MyItemRecyclerViewAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gachicarapp.databinding.ListItemBinding
import com.example.gachicarapp.retrofit.response.DriveReport
import com.example.gachicarapp.retrofit.response.UserReportList

class ReportListRecyclerViewAdapter(private var driveReports: List<DriveReport>) :
    RecyclerView.Adapter<ReportListRecyclerViewAdapter.DriveReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriveReportViewHolder {
        val binding =
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DriveReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriveReportViewHolder, position: Int) {
        holder.bind(driveReports[position])
    }

    override fun getItemCount(): Int {
        return driveReports.size
    }

    fun updateData(newData: UserReportList) {
        driveReports = newData.report
        notifyDataSetChanged()
    }

    inner class DriveReportViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(driveReport: DriveReport) {
            with(binding) {
                tvStartLocationItem.text = driveReport.departure
                tvDestinationItem.text = driveReport.destination
                tvStartTimeItem.text = driveReport.startTime
                tvEndTimeItem.text = driveReport.endTime
            }
        }
    }

}
