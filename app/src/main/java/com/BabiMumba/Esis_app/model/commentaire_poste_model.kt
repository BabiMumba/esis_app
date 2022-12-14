package com.BabiMumba.Esis_app.model

class commentaire_poste_model {
    lateinit var nom:String
    lateinit var date:String
    lateinit var commentaire:String
    lateinit var profil:String


    constructor(){}
    constructor(
        nom: String,
        date: String,
        commentaire: String,
        profil:String
    ) {
        this.nom = nom
        this.date = date
        this.commentaire = commentaire
        this.profil = profil
    }

}