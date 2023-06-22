package kr.ac.kpu.red_lighthouse.placeReview

import android.content.SharedPreferences

class PlaceReview(
    var placeName : String,
    var address: String,
    var uid: String,
    var userName: String,
    var placePrice: String,
    var isLocalCurrency:Boolean,
    var placePhotos1: String,
    var review: String,
    var dateOfReview:String,
) {
    constructor() : this("","","", "","",false,"", "", "")
}