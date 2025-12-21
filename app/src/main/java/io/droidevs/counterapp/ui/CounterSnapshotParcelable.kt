package io.droidevs.counterapp.ui

import android.os.Parcel
import android.os.Parcelable

data class CounterSnapshotParcelable(
    val id: String,
    val name: String,
    val currentCount: Int,
    val categoryId: String,
    val createdAtMillis: Long,
    val lastUpdatedAtMillis: Long,
    val canIncrease: Boolean,
    val canDecrease: Boolean,
    val orderAnchorAt: Long
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeInt(currentCount)
        parcel.writeString(categoryId)
        parcel.writeLong(createdAtMillis)
        parcel.writeLong(lastUpdatedAtMillis)
        parcel.writeByte(if (canIncrease) 1 else 0)
        parcel.writeByte(if (canDecrease) 1 else 0)
        parcel.writeLong(orderAnchorAt)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CounterSnapshotParcelable> {
        override fun createFromParcel(parcel: Parcel): CounterSnapshotParcelable {
            return CounterSnapshotParcelable(parcel)
        }

        override fun newArray(size: Int): Array<CounterSnapshotParcelable?> {
            return arrayOfNulls(size)
        }
    }
}
