package com.Esisalama.babim.admin.model

class model_logout {

    lateinit var nom:String
    lateinit var raison:String
    lateinit var profil:String
    lateinit var promotion:String
    lateinit var date:String

    constructor(){}
    constructor(
        nom: String,
        raison: String,
        profil:String,
        promotion:String,
        date:String,
    ) {
        this.nom = nom
        this.raison = raison
        this.profil = profil
        this.promotion = promotion
        this.date = date
    }

}