package com.BabiMumba.Esis_app.model

class news_model {
    lateinit var nom:String
    lateinit var administrateur:String
    lateinit var mail:String
    lateinit var profil:String
    lateinit var promotion:String
    lateinit var sexe:String

    constructor(){}
    constructor(
        nom: String,
        administrateur: String,
        mail: String,
        profil:String,
        promotion:String,
        sexe:String,
    ) {
        this.nom = nom
        this.administrateur = administrateur
        this.mail = mail
        this.profil = profil
        this.promotion = promotion
        this.sexe = sexe
    }
}