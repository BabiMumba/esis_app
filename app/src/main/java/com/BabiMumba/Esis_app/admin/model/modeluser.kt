package com.BabiMumba.Esis_app.admin.model

class modeluser {

    lateinit var nom:String
    lateinit var admin_assistant:String
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
        this.admin_assistant = administrateur
        this.mail = mail
        this.profil = profil
        this.promotion = promotion
        this.sexe = sexe
    }

}