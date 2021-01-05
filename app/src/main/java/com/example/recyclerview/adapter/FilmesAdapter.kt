package com.example.recyclerview.adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.R
import com.example.recyclerview.constants.Constants
import com.example.recyclerview.model.Filme
import com.example.recyclerview.repository.FilmeRepository
import com.example.recyclerview.ui.CadastroFilmeActivity
import kotlinx.android.synthetic.main.layout_lista_filmes.view.*

class FilmesAdapter(var listaFilmes: ArrayList<Filme>): RecyclerView.Adapter<FilmesAdapter.FilmeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_lista_filmes, parent, false)

        return FilmeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaFilmes.size
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        val filme = listaFilmes[position]
        holder.bind(filme, position)
    }

    inner class FilmeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(filme: Filme, position: Int){
            itemView.textNomeDoFilme.text = filme.titulo
            itemView.textDisponibilidade.text = filme.disponibilidade
            itemView.notaFilme.rating = filme.notaFilme
            itemView.image.setImageBitmap(filme.imagem)

            itemView.buttonDetalhes.setOnClickListener{
                val intent = Intent(itemView.context, CadastroFilmeActivity::class.java)
                intent.putExtra("operacao", Constants.OPERACAO_CONSULTAR)
                intent.putExtra("id", filme.id)
                itemView.context.startActivity(intent)
            }

            itemView.setOnLongClickListener {

                AlertDialog.Builder(itemView.context)
                    .setTitle("Exclusão")
                    .setMessage("Confirma a exclusão do filme ${filme.titulo}")
                    .setPositiveButton("SIM"){dialog, which ->
                        val repo = FilmeRepository(itemView.context)
                        repo.delete(filme.id)
                        listaFilmes.removeAt(position)
                        notifyDataSetChanged()
                        Toast.makeText(itemView.context, "Filme excluído com sucesso", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("NÃO"){dialog, which ->

                    }
                    .show()

                true
            }

        }
    }

}