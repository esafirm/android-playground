package com.esafirm.androidplayground.conductor.sharedtransition

import android.os.Parcel
import android.os.Parcelable

data class SharedItem(val imageRes: Int, val text: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imageRes)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SharedItem> {
        override fun createFromParcel(parcel: Parcel): SharedItem {
            return SharedItem(parcel)
        }

        override fun newArray(size: Int): Array<SharedItem?> {
            return arrayOfNulls(size)
        }
    }
}
