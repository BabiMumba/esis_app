package com.BabiMumba.Esis_app.model

class commentaire_poste_model {
    lateinit var nom:String
    lateinit var date:String
    lateinit var commentaire:String
    lateinit var profil:String

    lateinit var id_reserv1:String
    lateinit var id_reserv2:String
    lateinit var id_reserv3:String
    lateinit var id_reserv4:String


    constructor(){}
    constructor(
        nom: String,
        date: String,
        commentaire: String,
        profil:String,
        id_reserv1:String,
        id_reserv2:String,
        id_reserv3:String,
        id_reserv4:String,
    ) {
        this.nom = nom
        this.date = date
        this.commentaire = commentaire
        this.profil = profil
        this.id_reserv1 = id_reserv1
        this.id_reserv2 = id_reserv2
        this.id_reserv3 = id_reserv3
        this.id_reserv4 = id_reserv4
    }

}