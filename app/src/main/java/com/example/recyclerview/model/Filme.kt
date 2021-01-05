package com.example.recyclerview.model

import android.graphics.Bitmap

data class Filme(

    var id: Int = 0,
    var titulo: String = "",
    var produtora: String = "",
    var notaFilme: Float = 0.0f,
    var disponibilidade: String = "",
    var assistido: Boolean = false,
    var imagem: Bitmap? = null
)


