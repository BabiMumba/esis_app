package com.Esisalama.babim.admin.model

class modeluser {

    lateinit var prenom:String
    lateinit var admin_assistant:String
    lateinit var administrateur:String
    lateinit var mail:String
    lateinit var profil:String
    lateinit var promotion:String
    lateinit var sexe:String

    constructor(){}
    constructor(
        prenom: String,
        admin_assistant: String,
        administrateur: String,
        mail: String,
        profil:String,
        promotion:String,
        sexe:String,
    ) {
        this.prenom = prenom
        this.admin_assistant = admin_assistant
        this.administrateur = administrateur
        this.mail = mail
        this.profil = profil
        this.promotion = promotion
        this.sexe = sexe
    }

}