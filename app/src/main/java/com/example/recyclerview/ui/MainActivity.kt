package com.example.recyclerview.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.R
import com.example.recyclerview.adapter.FilmesAdapter
import com.example.recyclerview.constants.Constants
import com.example.recyclerview.datasource.Datasource
import com.example.recyclerview.repository.FilmeRepository
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        insertTollbar()

        buttonCadastrarFilme.setOnClickListener(this)

    }

    override fun onResume() {
        iniciarRecyclerView()
        super.onResume()
    }

    private fun insertTollbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "My Movie App"
    }

    override fun onClick(v: View) {
        if (v.id == R.id.buttonCadastrarFilme) {
            val intent = Intent(this, CadastroFilmeActivity::class.java)
            intent.putExtra("operacao", Constants.OPERACAO_NOVO_CADASTRO)
            startActivity(intent)
        }
    }

    private fun iniciarRecyclerView(){
        recyclerViewFilmes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val repo = FilmeRepository(this)

        recyclerViewFilmes.adapter = FilmesAdapter(repo.getFilmes())
    }


}