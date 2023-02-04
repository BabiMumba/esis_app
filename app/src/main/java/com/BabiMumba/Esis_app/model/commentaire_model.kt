package com.BabiMumba.Esis_app.model

class commentaire_model {
    lateinit var nom:String
    lateinit var date:String
    lateinit var commentaire:String
    lateinit var profil:String
    lateinit var id_reserve1:String
    lateinit var id_reserve2:String
    lateinit var id_reserve3:String
    lateinit var id_reserve4:String


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