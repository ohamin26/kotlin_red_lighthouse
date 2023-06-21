package kr.ac.kpu.red_lighthouse.placeReview

class PlaceReview (
    var address: String,
    var uid: String,
    var placePrice: String,
    var isLocalCurrency:Boolean,
    var placePhotos: Array<String>,
    var review : String,
    var dateOfReview:String,
) {
    constructor() : this("", "","",false, arrayOf(), "", "")
}