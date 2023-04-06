package com.BabiMumba.Esis_app.model

data class post_model (

     var nom:String,
     var admin_assistant:String,
     var administrateur:String,
     var ad_mail:String,
     var token_users:String,
     var users_id:String,
     var image_name_id:String,
     var date:String,
     var message:String,
     var profil:String,
     var image_poste:String,
     var id_reserv1:String,
     var id_reserv2:String,
     var id_reserv3:String,
     var id_poste:String,
    var vue:Int = 0,
    var nb_comment:Int = 0

){
    constructor():this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        0
    )
}

