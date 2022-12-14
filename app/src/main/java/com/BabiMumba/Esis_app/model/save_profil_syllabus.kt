package com.BabiMumba.Esis_app.model

class save_profil_syllabus {

    lateinit var nom_livre:String
    lateinit var lien_livre:String
    lateinit var id_poste:String

    constructor(){}
    constructor(
        lien_livre:String,
        nom_livre: String,
        id_poste: String,
    ) {
        this.lien_livre = lien_livre
        this.nom_livre = nom_livre
        this.id_poste = id_poste

    }

}