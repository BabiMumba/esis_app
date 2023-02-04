package com.BabiMumba.Esis_app.model

class save_profil_syllabus {

    lateinit var nom_livre:String
    lateinit var lien_livre:String
    lateinit var id_poste:String
    lateinit var promotion_:String
    lateinit var id_res1:String
    lateinit var id_res2:String
    lateinit var id_res3:String


    constructor(){}
    constructor(
        lien_livre:String,
        nom_livre: String,
        id_poste: String,
        promotion_: String,
        id_res1: String,
        id_res2: String,
        id_res3: String,
    ) {
        this.lien_livre = lien_livre
        this.nom_livre = nom_livre
        this.id_poste = id_poste
        this.promotion_ = promotion_
        this.id_res1 = id_res1
        this.id_res2 = id_res2
        this.id_res3 = id_res3

    }

}