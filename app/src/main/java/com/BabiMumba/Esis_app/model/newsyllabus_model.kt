package com.BabiMumba.Esis_app.model

 data class newsyllabus_model(
     var nom_syllabus: String,
     var admin_assistant:String,
     var administrateur:String,
     var pochette: String,
     var mail_users: String,
     var id_user: String,
     var lien_pdf: String,
     var token_users: String,
     var nom_promotion: String,
     var description: String,
     var nom_prof: String,
     var lien_du_livre:String,
     var nom_user:String,
     var date_heure:String,
     var lien_profil:String,
     var id_reserve1:String,
     var id_reserve2:String,
     var id_reserve3:String,
     var id_reserve4:String,
     var like:Int =0,
     var download :Int= 0,
     var comment:Int =0,
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
         "",
         "",
         "",
         "",
         0,
         0,
         0
     )
 }