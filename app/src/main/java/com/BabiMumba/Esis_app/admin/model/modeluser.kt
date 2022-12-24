package com.BabiMumba.Esis_app.admin.model

class modeluser {

    lateinit var nom:String
    lateinit var mail:String
    lateinit var profil:String


    constructor(){}
    constructor(
        nom: String,
        mail: String,
        profil:String
    ) {
        this.nom = nom
        this.mail = mail
        this.profil = profil
    }

}