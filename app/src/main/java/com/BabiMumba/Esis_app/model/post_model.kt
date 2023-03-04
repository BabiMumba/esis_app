package com.BabiMumba.Esis_app.model

class post_model {

    lateinit var nom:String
    lateinit var admin_assistant:String
    lateinit var administrateur:String
    lateinit var ad_mail:String
    lateinit var id_reserv1:String
    lateinit var id_reserv2:String
    lateinit var id_reserv3:String
    lateinit var id_poste:String
    lateinit var token_users:String
    lateinit var users_id:String
    lateinit var image_name_id:String
    lateinit var date:String
    lateinit var message:String
    lateinit var profil:String
    lateinit var image_poste:String
    var vue = 0
    var commentaire = 0


    constructor(){}
    constructor(
        nom: String,
        admin_assistant: String,
        administrateur: String,
        ad_mail: String,
        id_reserv1: String,
        id_reserv2: String,
        id_reserv3: String,
        id_post: String,
        token_users: String,
        users_id: String,
        image_name_id: String,
        date: String,
        message: String,
        profil:String,
        image_poste:String,
        vue:Int,
        commentaire:Int
    ) {
        this.nom = nom
        this.admin_assistant = admin_assistant
        this.administrateur = administrateur
        this.ad_mail = ad_mail
        this.id_reserv1 = id_reserv1
        this.id_reserv2 = id_reserv2
        this.id_reserv3 = id_reserv3
        this.id_poste = id_post
        this.token_users = token_users
        this.users_id = users_id
        this.date = date
        this.image_name_id = image_name_id
        this.message = message
        this.profil = profil
        this.image_poste = image_poste
        this.vue = vue
        this.commentaire = commentaire
    }

}