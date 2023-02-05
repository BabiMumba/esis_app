package com.BabiMumba.Esis_app.model

class syllabus_model{
    lateinit var nom_syllabu: String
    lateinit var pochette: String
    lateinit var mail_users: String
    lateinit var id_user: String
    lateinit var lien_pdf: String
    lateinit var token_users: String
    lateinit var nom_promotion: String
    lateinit var description: String
    lateinit var nom_prof: String
    lateinit var lien_du_livre:String
    lateinit var nom_user:String
    lateinit var date_heure:String
    lateinit var lien_profil:String
    var like =0
    var download= 0
    var comment =0
    lateinit var id_reserve1:String
    lateinit var id_reserve2:String
    lateinit var id_reserve3:String
    lateinit var id_reserve4:String


    constructor(){}
    constructor(
        nom_syllabu: String,
        pochette: String,
        mail_users: String,
        id_user: String,
        lien_pdf: String,
        token_users: String,
        nom_promotion: String,
        description: String,
        nom_prof: String,
        lien_du_livre: String,
        nom_user:String,
        date_heure:String,
        lien_profil:String,
        like: Int,
        download: Int,
        comment: Int,
        id_reserve1: String,
        id_reserve2: String,
        id_reserve3: String,
        id_reserve4: String,
    ) {
        this.nom_syllabu = nom_syllabu
        this.pochette = pochette
        this.mail_users = mail_users
        this.id_user = id_user
        this.lien_pdf = lien_pdf
        this.token_users = token_users
        this.nom_promotion = nom_promotion
        this.description = description
        this.nom_prof = nom_prof
        this.lien_du_livre = lien_du_livre
        this.like = like
        this.lien_profil = lien_profil
        this.download = download
        this.comment = comment
        this.nom_user = nom_user
        this.date_heure = date_heure
        this.id_reserve1 = id_reserve1
        this.id_reserve2 = id_reserve2
        this.id_reserve3 = id_reserve3
        this.id_reserve4 = id_reserve4
    }

}