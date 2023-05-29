package kr.ac.kpu.red_lighthouse.user

data class User (
    var userId : String,
    var userEmail : String,
    var userNickname : String,
    var userDateOfRegist : String)
{
    constructor() : this("","","","")
}