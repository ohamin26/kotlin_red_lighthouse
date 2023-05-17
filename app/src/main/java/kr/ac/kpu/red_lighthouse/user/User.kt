package kr.ac.kpu.red_lighthouse.user

class User () {
    var user_id : String? = ""
    var user_email : String? = ""
    var user_nickname : String? = ""
    var user_dateOfRegist : String? = ""

    constructor(user_id:String,user_email:String,user_nickname:String,user_dateOfRegist:String) : this() {
        this.user_id = user_id
        this.user_email = user_email
        this.user_nickname = user_nickname
        this.user_dateOfRegist = user_dateOfRegist
    }

}