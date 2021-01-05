package com.esafirm.androidplayground.others

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row

class ParcelController : BaseController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Parcel") {
                val bundle = Bundle().apply {
                    putParcelable(
                        "key",
                        ArrayOfSpecialOfferViewModel(arrayListOf(SpecialOfferViewModel()))
                    )
                }

                val item = bundle.getParcelable<ArrayOfSpecialOfferViewModel>("key")
                Logger.log(item!!.specialOffers.first().item)
            }
            logger()
        }
    }
}

data class SpecialOfferViewModel(
    val item: String = "a"
)

class ArrayOfSpecialOfferViewModel() : Parcelable {

    var specialOffers = ArrayList<SpecialOfferViewModel>()

    constructor(parcel: Parcel) : this(
        arrayListOf<SpecialOfferViewModel>().apply {
            parcel.readList(this as List<*>, SpecialOfferViewModel::class.java.classLoader)
        })

    constructor(specialOffers: ArrayList<SpecialOfferViewModel>) : this() {
        this.specialOffers = specialOffers
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(specialOffers as List<*>?)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArrayOfSpecialOfferViewModel> {
        override fun createFromParcel(parcel: Parcel): ArrayOfSpecialOfferViewModel {
            return ArrayOfSpecialOfferViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ArrayOfSpecialOfferViewModel?> {
            return arrayOfNulls(size)
        }
    }

    fun setOffers(specialOffers: ArrayList<SpecialOfferViewModel>) {
        this.specialOffers = specialOffers
    }
}
