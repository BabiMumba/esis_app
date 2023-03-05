package com.BabiMumba.Esis_app.model

class news_model {
    lateinit var titre:String
    lateinit var mail:String
    lateinit var message:String
    lateinit var date:String
    lateinit var image:String
    lateinit var id_doc:String
    lateinit var autor:String
    lateinit var promot:String
    constructor(){}
    constructor(
        titre: String,
        mail: String,
        message: String,
        date: String,
        image:String,
        id_doc:String,
        autor:String,
        promot:String,
    ) {
        this.titre = titre
        this.mail = mail
        this.message = message
        this.date = date
        this.image = image
        this.id_doc = id_doc
        this.autor = autor
        this.promot = promot

    }
}