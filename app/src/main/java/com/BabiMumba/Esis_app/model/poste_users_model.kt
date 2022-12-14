package com.BabiMumba.Esis_app.model

class poste_users_model {

    lateinit var users_id:String
    lateinit var id_poste:String
    lateinit var token_user:String

    lateinit var image_url:String
    lateinit var image_poste:String
    lateinit var message_texte:String



    constructor(){}
    constructor(
        image_url:String,
        id_poste:String,
        token_user:String,
        users_id: String,
        image_poste:String,
        message_texte:String,
    ) {
        this.image_url = image_url
        this.id_poste = id_poste
        this.token_user = token_user
        this.users_id = users_id
        this.image_poste = image_poste
        this.message_texte = message_texte
    }

}