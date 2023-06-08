package kr.ac.kpu.red_lighthouse.placeReview

class PlaceReview (
    var reviewId: String,
    var placeBizReNo: String,// 명소 사업자 등록증
    var placePrice: String,
    var isLocalCurrency:Boolean,
    var placePhotos: Array<String>,
    var tag : Array<String>,
    var dateOfReview:String,
) {
    constructor() : this("", "", "", false, arrayOf(), arrayOf(), "")
}