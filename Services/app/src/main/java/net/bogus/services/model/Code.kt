package net.bogus.services.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by burak on 12/10/17.
 */
@Parcelize
@SuppressLint("ParcelCreator")
data class Code (val hex:String) : Parcelable {}