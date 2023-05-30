package kr.ac.kpu.red_lighthouse.placeReview

class PlaceReview (
    var ReviewId: String,
    var placeAddress: String,
    var placePrice: String,
    var isLocalCurrency:Boolean,
    var placePhotos: Array<String>,
    var tag : Array<String>,
    var dateOfReview:String,
) {
    constructor() : this("", "", "", false, arrayOf(), arrayOf(), "")
}