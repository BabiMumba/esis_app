package com.BabiMumba.Esis_app.model

class poste_users_model {

    lateinit var users_id:String
    lateinit var id_poste:String
    lateinit var token_user:String
    lateinit var image_url:String
    lateinit var image_poste:String
    lateinit var message_texte:String
    lateinit var id_reser1:String
    lateinit var id_reser2:String
    lateinit var id_reser3:String
    lateinit var id_reser4:String



    constructor(){}
    constructor(
        image_url:String,
        id_poste:String,
        token_user:String,
        users_id: String,
        image_poste:String,
        message_texte:String,
        id_reser1:String,
        id_reser2:String,
        id_reser3:String,
        id_reser4:String,

    ) {
        this.image_url = image_url
        this.id_poste = id_poste
        this.token_user = token_user
        this.users_id = users_id
        this.image_poste = image_poste
        this.message_texte = message_texte
        this.id_reser1 = id_reser1
        this.id_reser2 = id_reser2
        this.id_reser3 = id_reser3
        this.id_reser4 = id_reser4
    }

}