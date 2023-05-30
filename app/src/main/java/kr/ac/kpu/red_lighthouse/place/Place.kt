package kr.ac.kpu.red_lighthouse.place

class Place(
    var placeId: String,
    var placeName : String,
    var placeAddress: String,
    var placeOpening:String,
    var placeCall:String,
) {
    constructor():this("","","","","")
}