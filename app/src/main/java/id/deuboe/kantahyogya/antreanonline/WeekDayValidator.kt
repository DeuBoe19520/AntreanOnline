package id.deuboe.kantahyogya.antreanonline

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import java.util.*

class WeekDayValidator : CalendarConstraints.DateValidator {
    private val utc =
            Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val CREATOR: Parcelable.Creator<WeekDayValidator?> =
            object : Parcelable.Creator<WeekDayValidator?> {
                override fun createFromParcel(source: Parcel): WeekDayValidator {
                    return WeekDayValidator()
                }
                override fun newArray(size: Int): Array<WeekDayValidator?> {
                    return arrayOfNulls(size)
                }
            }
    override fun writeToParcel(dest: Parcel?, flags: Int) {
    }
    override fun isValid(date: Long): Boolean {
        utc.timeInMillis = date
        val dayOfWeek = utc[Calendar.DAY_OF_WEEK]
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
    }
    override fun describeContents(): Int {
        return 0
    }

    override fun hashCode(): Int {
        val hashedFields = arrayOf<Any>()
        return hashedFields.contentHashCode()
    }
}