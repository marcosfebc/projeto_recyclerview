package com.example.recyclerview.datasource

class DatabaseDefinitions {

    object Filme {
        const val TABLE_NAME = "tbl_filme"

        object Columns {
            const val ID = "id"
            const val TITULO = "titulo"
            const val PRODUTORA = "produtora"
            const val NOTA = "nota"
            const val DISPONIBILIDADE = "disponibilidade"
            const val ASSISTIDO = "assistido"
            const val IMAGEM = "imagem"
        }
    }

}