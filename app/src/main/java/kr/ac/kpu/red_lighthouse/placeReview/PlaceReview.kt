package kr.ac.kpu.red_lighthouse.placeReview

class PlaceReview (
    var address: String,// 명소 사업자 등록증
    var placePrice: String,
    var isLocalCurrency:Boolean,
    var placePhotos: Array<String>,
    var review : String,
    var dateOfReview:String,
) {
    constructor() : this("", "", false, arrayOf(), "", "")
}