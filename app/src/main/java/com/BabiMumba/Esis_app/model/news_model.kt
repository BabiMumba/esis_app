package com.BabiMumba.Esis_app.model

class news_model(
    var titre:String,
     var mail:String,
     var message:String,
     var date:String,
     var image:String,
     var id_doc:String,
     var autor:String,
     var promot:String,
     var all_promotion:Boolean
) {
       constructor():this("","","","","","","", "",false)
}