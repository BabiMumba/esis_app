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
        profil:String,
        id_reserve1:String,
        id_reserve2:String,
        id_reserve3:String,
        id_reserve4:String,
    ) {
        this.nom = nom
        this.date = date
        this.commentaire = commentaire
        this.profil = profil
        this.id_reserve1 = id_reserve1
        this.id_reserve2 = id_reserve2
        this.id_reserve3 = id_reserve3
        this.id_reserve4 = id_reserve4
    }

}