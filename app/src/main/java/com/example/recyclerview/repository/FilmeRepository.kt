package com.example.recyclerview.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.recyclerview.datasource.DatabaseDefinitions
import com.example.recyclerview.datasource.DatabaseHelper
import com.example.recyclerview.model.Filme
import java.io.ByteArrayOutputStream


class FilmeRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun save(filme: Filme) : Int{
        //Colocar o banco em modo escrita
        val db = dbHelper.writableDatabase

        //Criar um mapa com os valores que serão inseridos
        val valores = ContentValues()
        valores.put(DatabaseDefinitions.Filme.Columns.TITULO, filme.titulo)
        valores.put(DatabaseDefinitions.Filme.Columns.PRODUTORA, filme.produtora)
        valores.put(DatabaseDefinitions.Filme.Columns.NOTA, filme.notaFilme)
        valores.put(DatabaseDefinitions.Filme.Columns.DISPONIBILIDADE, filme.disponibilidade)
        valores.put(DatabaseDefinitions.Filme.Columns.ASSISTIDO, filme.assistido)

        //Transformar o Bitmap em um array de Bytes
        val stream = ByteArrayOutputStream()
                filme.imagem!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val imageArray = stream.toByteArray()
        valores.put(DatabaseDefinitions.Filme.Columns.IMAGEM, imageArray)

        //Inserir os dados no banco
        val id = db.insert(DatabaseDefinitions.Filme.TABLE_NAME, null, valores)

        return id.toInt()
    }

    fun update(filme: Filme) : Int{
        //Colocar o banco de dados em modo escrita
        val db = dbHelper.writableDatabase

        //Criar um mapa com os valores que serão atualizados no banco
        val valores = ContentValues().apply {
            put(DatabaseDefinitions.Filme.Columns.TITULO, filme.titulo)
            put(DatabaseDefinitions.Filme.Columns.PRODUTORA, filme.produtora)
            put(DatabaseDefinitions.Filme.Columns.NOTA, filme.notaFilme)
            put(DatabaseDefinitions.Filme.Columns.DISPONIBILIDADE, filme.disponibilidade)
            put(DatabaseDefinitions.Filme.Columns.ASSISTIDO, filme.assistido)
        }

        //Criar o critério para a cláusula WHERE
        val selection = "${DatabaseDefinitions.Filme.Columns.ID} = ?"

        //Criar um array com a lista de argumentos que serão utilizados para executar a atualização
        val selectionArgs = arrayOf(filme.id.toString())

        //Chamar o método uptade do sqlite
        val count = db.update(
            DatabaseDefinitions.Filme.TABLE_NAME,
            valores,
            selection,
            selectionArgs
        )

        return count
    }

    fun delete(id: Int) : Int{
        //Colocar o banco de dados, no modo "escrita"
        val db = dbHelper.writableDatabase

        //Definir cláusula WHERE da excluir
        val selection = "${DatabaseDefinitions.Filme.Columns.ID} = ?"

        //Definir os argumentos que serão usados pela instrução delete
        val selectionArgs = arrayOf(id.toString())

        //Execute o comando de exclusão
        val deletedRows = db.delete(DatabaseDefinitions.Filme.TABLE_NAME, selection, selectionArgs)

        return deletedRows
    }

    fun getFilmes() : ArrayList<Filme>{

        //Colocar o banco de dados em modo leitura
        val db = dbHelper.readableDatabase

        //Definir os campos que serão devolvidos na consulta
        val projection = arrayOf(DatabaseDefinitions.Filme.Columns.ID,
                                                DatabaseDefinitions.Filme.Columns.TITULO,
                                                    DatabaseDefinitions.Filme.Columns.PRODUTORA,
                                                        DatabaseDefinitions.Filme.Columns.NOTA,
                                                            DatabaseDefinitions.Filme.Columns.IMAGEM)

        //Definir a ordem de exibição da lista
        val sortOrder = "${DatabaseDefinitions.Filme.Columns.TITULO} ASC"

        val cursor = db.query(DatabaseDefinitions.Filme.TABLE_NAME,
            projection, null, null, null, null, sortOrder)

        var filmes = ArrayList<Filme>()

        if(cursor != null){
            while (cursor.moveToNext()){
                var filme = Filme(
                    id = cursor.getInt(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.ID)),
                    titulo = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.TITULO)),
                    produtora = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.PRODUTORA)),
                    notaFilme = cursor.getFloat(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.NOTA)),
                    imagem = byteArrayToBitmap(cursor.getBlob(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.IMAGEM)))
                )

                filmes.add(filme)
            }
        }

        return filmes

    }







    fun getFilme(id: Int) : Filme{

        //Colocar o banco de dados em modo leitura
        val db = dbHelper.readableDatabase

        //Definir os campos que serão devolvidos na consulta
        val projection = arrayOf(DatabaseDefinitions.Filme.Columns.ID,
            DatabaseDefinitions.Filme.Columns.TITULO,
            DatabaseDefinitions.Filme.Columns.PRODUTORA,
            DatabaseDefinitions.Filme.Columns.NOTA,
            DatabaseDefinitions.Filme.Columns.DISPONIBILIDADE,
            DatabaseDefinitions.Filme.Columns.ASSISTIDO,
            DatabaseDefinitions.Filme.Columns.IMAGEM
        )

        //Definir o filtro

        val selection = "${DatabaseDefinitions.Filme.Columns.ID} = ?"

        //Definir o valor do argumento

        val selectionArgs = arrayOf(id.toString())

        //Definir a ordem de exibição da lista

        val cursor = db.query(DatabaseDefinitions.Filme.TABLE_NAME,
            projection, selection, selectionArgs, null, null, null)

        var filme = Filme()

        if(cursor != null){
            cursor.moveToNext()
                    filme.id = cursor.getInt(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.ID))
                    filme.titulo = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.TITULO))
                    filme.produtora = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.PRODUTORA))
                    filme.notaFilme = cursor.getFloat(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.NOTA))
                    filme.disponibilidade = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.DISPONIBILIDADE))
                    filme.assistido = cursor.getInt(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.ASSISTIDO)) == 1

                    val imagem = cursor.getBlob(cursor.getColumnIndex(DatabaseDefinitions.Filme.Columns.IMAGEM))

                    if(imagem != null){
                        filme.imagem = byteArrayToBitmap(imagem)
                    }
        }

        println("************${filme.titulo}")

        return filme

    }

    private fun byteArrayToBitmap(imagem: ByteArray) : Bitmap{
        val bitmap = BitmapFactory.decodeByteArray(imagem, 0, imagem.size)
        return bitmap
    }

}