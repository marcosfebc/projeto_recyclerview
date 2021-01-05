package com.example.recyclerview.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import com.example.recyclerview.R
import com.example.recyclerview.constants.Constants
import com.example.recyclerview.datasource.DatabaseDefinitions
import com.example.recyclerview.model.Filme
import com.example.recyclerview.repository.FilmeRepository
import kotlinx.android.synthetic.main.activity_cadastro_filme.*
import kotlinx.android.synthetic.main.layout_lista_filmes.*
import kotlinx.android.synthetic.main.toolbar.*

class CadastroFilmeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var adapter : ArrayAdapter<CharSequence>
    private lateinit var operacao: String
    private var bitmap: Bitmap? = null

    private val ABRIR_GALERIA = 5000
    private val ABRIR_CAMERA = 6000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_filme)

        preencherSpinnerDisponibilidade()

        insertToolbar()

        //Preencher o valor da variável operacao
        operacao = intent.getStringExtra("operacao")

        if (operacao != Constants.OPERACAO_NOVO_CADASTRO){
            preecherFormulario()
        }


        buttonAbrirGaleria.setOnClickListener(this)
        buttonAbrirCamera.setOnClickListener(this)
    }

    override fun onClick(view: View) {

        val id = view.id

        when (id){
            R.id.buttonAbrirGaleria -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, ABRIR_GALERIA)
            }
            R.id.buttonAbrirCamera -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, ABRIR_CAMERA)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        imageViewFotoDoFilme.scaleType = ImageView.ScaleType.CENTER_CROP

        if(data != null && requestCode == ABRIR_GALERIA){
            val inputStream = contentResolver.openInputStream(data.data!!)
            bitmap = BitmapFactory.decodeStream(inputStream)
            imageViewFotoDoFilme.setImageBitmap(bitmap)
        }else if(data != null && requestCode == ABRIR_CAMERA){
            bitmap = data.extras!!.get("data") as Bitmap
            imageViewFotoDoFilme.setImageBitmap(bitmap)
        }
    }

    private fun preencherSpinnerDisponibilidade() {
        adapter = ArrayAdapter.createFromResource(this, R.array.disponibilidades, android.R.layout.simple_spinner_dropdown_item)
        spinnerDisponibilidade.adapter = adapter
    }

    private fun preecherFormulario() {

        var  filme = Filme()
        var id = intent.getIntExtra("id", 0)

        val repository = FilmeRepository(this)

        filme = repository.getFilme(id)

        editTextNomeDoFilme.setText(filme.titulo)
        editTextProdutoraDoFilme.setText(filme.produtora)

        //Selecionar a disponibilidade do filme na lista
        val position = adapter.getPosition(filme.disponibilidade)
        spinnerDisponibilidade.setSelection(position)

        checkboxAssistido.isChecked = filme.assistido
        ratingBarNotaDoFilme.rating = filme.notaFilme

        if (filme.imagem != null){
            imageViewFotoDoFilme.scaleType = ImageView.ScaleType.CENTER_CROP
            imageViewFotoDoFilme.setImageBitmap(filme.imagem)
        }else{
            imageViewFotoDoFilme.setImageResource(R.drawable.ic_insert_photo_white_24)
        }

    }

    private fun insertToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar!!.title = intent.getStringExtra("operacao")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filme, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.menu_cancelar -> {
                alert()
            }
            R.id.menu_salvar -> {
                if (validarFormulario() && operacao == Constants.OPERACAO_NOVO_CADASTRO) {
                    salvarFilme()
                } else {
                    atualizarFilme()
                }
            }
            else -> {
                onBackPressed()
            }
        }

        return true
    }

    private fun salvarFilme() {
        //Criar um objeto filme
        val filme = Filme(
            titulo = editTextNomeDoFilme.text.toString(),
            produtora = editTextProdutoraDoFilme.text.toString(),
            notaFilme = ratingBarNotaDoFilme.rating,
            disponibilidade = spinnerDisponibilidade.selectedItem.toString(),
            assistido =  checkboxAssistido.isChecked,
            imagem = bitmap
        )

        //Criar uma instância do repositório
        val repo = FilmeRepository(this)
        val id = repo.save(filme)

        //Exibir  confirmação de inclusão de registro
        if (id > 0) {
            val builderDialog = AlertDialog.Builder(this)
            builderDialog.setTitle("Sucesso!")
            builderDialog.setMessage("Seu filme foi gravado com sucesso!\n\nDeseja cadastrar outro filme?")
            builderDialog.setIcon(R.drawable.ic_done_green_24dp)

            builderDialog.setPositiveButton("Sim") {_,_->
                limparFormulario()
            }

            builderDialog.setNegativeButton("Não") {_,_->
                onBackPressed()
            }

            builderDialog.show()
        }
    }

    private fun atualizarFilme() {
        //Criar um objeto filme
        val filme = Filme(
            id = intent.getIntExtra("id", 0),
            titulo = editTextNomeDoFilme.text.toString(),
            produtora = editTextProdutoraDoFilme.text.toString(),
            notaFilme = ratingBarNotaDoFilme.rating,
            disponibilidade = spinnerDisponibilidade.selectedItem.toString(),
            assistido =  checkboxAssistido.isChecked
        )

        //Criar uma instância do repositório
        val repo = FilmeRepository(this)
        val count = repo.update(filme)

        //Exibir confirmação de inclusão de registro
        if (count > 0) {
            val builderDialog = AlertDialog.Builder(this)
            builderDialog.setTitle("Sucesso!")
            builderDialog.setMessage("Seu filme foi atualizado com sucesso!")
            builderDialog.setIcon(R.drawable.ic_done_green_24dp)

            builderDialog.setPositiveButton("OK") {_,_->
                onBackPressed()
            }

            builderDialog.show()
        }
    }

    private fun limparFormulario() {
        editTextNomeDoFilme.setText("")
        editTextProdutoraDoFilme.setText("")
        checkboxAssistido.isChecked = false
        editTextNomeDoFilme.requestFocus()
    }

    private fun validarFormulario() : Boolean {

        var valida = true

        if (editTextNomeDoFilme.length() < 3){
            tilNomeDoFilme.isErrorEnabled = true
            tilNomeDoFilme.error = "Título do Filme é obrigatório!"
            valida = false
        }else{
            tilNomeDoFilme.isErrorEnabled = false
            tilNomeDoFilme.error = null
        }

        if (editTextProdutoraDoFilme.length() < 3){
            tilProdutoraDoFilme.isErrorEnabled = true
            tilProdutoraDoFilme.error = "Produtora do filme é obrigatório!"
            valida = false
        }else{
            tilProdutoraDoFilme.isErrorEnabled = false
            tilProdutoraDoFilme.error = null
        }

        return valida

    }

    private fun alert() {
        var builderDialog = AlertDialog.Builder(this)
        builderDialog.setTitle("Cancelar Cadastro")
        builderDialog.setMessage("Tem certeza que deseja cancelar o cadastro do seu filme?")
        builderDialog.setIcon(R.drawable.ic__help_red_24dp)

        builderDialog.setPositiveButton("Sim") {_,_ ->
            onBackPressed()
        }

        builderDialog.setNegativeButton("Não") { _, _ ->
            editTextNomeDoFilme.requestFocus()
        }

        builderDialog.show()
    }

}


