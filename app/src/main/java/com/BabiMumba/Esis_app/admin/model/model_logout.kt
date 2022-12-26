package com.BabiMumba.Esis_app.admin.model

class model_logout {

    lateinit var nom:String
    lateinit var Raison:String
    lateinit var profil:String
    lateinit var promotion:String
    lateinit var Date:String

    constructor(){}
    constructor(
        nom: String,
        Raison: String,
        profil:String,
        promotion:String,
        Date:String,
    ) {
        this.nom = nom
        this.Raison = Raison
        this.profil = profil
        this.promotion = promotion
        this.Date = Date
    }

}