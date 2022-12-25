package com.BabiMumba.Esis_app.admin.model

class modeluser {

    lateinit var nom:String
    lateinit var mail:String
    lateinit var profil:String
    lateinit var administrateur:String

    constructor(){}
    constructor(
        nom: String,
        administrateur: String,
        mail: String,
        profil:String
    ) {
        this.nom = nom
        this.administrateur = administrateur
        this.mail = mail
        this.profil = profil
    }

}