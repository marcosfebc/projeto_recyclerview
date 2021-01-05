package com.example.recyclerview.datasource
import com.example.recyclerview.model.Filme

class Datasource {

    companion object {

        fun getFilmes(): ArrayList<Filme>{
            var filmes = ArrayList<Filme>()

            filmes.add(Filme(100, "O som do coração", "", 5.0f, "HBO"))
            filmes.add(Filme(100, "O som do coração", "", 5.0f, "HBO"))

            return filmes

        }

    }

}