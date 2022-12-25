package com.BabiMumba.Esis_app.admin.model

class model_logout {

    lateinit var nom:String
    lateinit var date:String
    lateinit var profil:String
    lateinit var promotion:String
    lateinit var Raison:String

    constructor(){}
    constructor(
        nom: String,
        date: String,
        profil:String,
        promotion:String,
        Raison:String,
    ) {
        this.nom = nom
        this.date = date
        this.profil = profil
        this.promotion = promotion
        this.Raison = Raison
    }

}