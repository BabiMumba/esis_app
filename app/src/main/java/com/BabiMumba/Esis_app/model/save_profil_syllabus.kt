package com.BabiMumba.Esis_app.model

 data class save_profil_syllabus(

     var lien_livre:String,
     var nom_livre: String,
     var id_poste: String,
    var  promotion_: String,
    var  id_res1: String,
    var  id_res2: String,
    var  id_res3: String,

 ) {
     constructor():this("","","","","","","")

}