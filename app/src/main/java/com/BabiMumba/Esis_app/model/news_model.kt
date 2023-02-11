package com.BabiMumba.Esis_app.model

class news_model {
    lateinit var titre:String
    lateinit var message:String
    lateinit var date:String
    lateinit var image:String
    constructor(){}
    constructor(
        nom: String,
        administrateur: String,
        mail: String,
        profil:String,
    ) {
        this.titre = nom
        this.message = administrateur
        this.date = mail
        this.image = profil

    }
}