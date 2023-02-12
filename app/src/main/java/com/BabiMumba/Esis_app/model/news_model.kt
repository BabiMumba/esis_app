package com.BabiMumba.Esis_app.model

class news_model {
    lateinit var titre:String
    lateinit var message:String
    lateinit var date:String
    lateinit var image:String
    lateinit var id_doc:String
    lateinit var autor:String
    constructor(){}
    constructor(
        titre: String,
        message: String,
        date: String,
        image:String,
        id_doc:String,
        autor:String,
    ) {
        this.titre = titre
        this.message = message
        this.date = date
        this.image = image
        this.id_doc = id_doc
        this.autor = autor

    }
}