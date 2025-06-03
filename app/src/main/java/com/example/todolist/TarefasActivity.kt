package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TarefasActivity : AppCompatActivity() {

    private lateinit var banco: DBHelper
    private lateinit var layoutTodasTarefas: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefas)

        banco =DBHelper(this)
        layoutTodasTarefas = findViewById(R.id.layoutTodasTarefas)

        val btnVoltar = findViewById<Button>(R.id.btnVoltarHome)
        btnVoltar.setOnClickListener {
            finish()
        }
        carregarTodasTarefas()
    }

    private fun carregarTodasTarefas() {
        val tarefas = banco.obterTarefas()

        TarefaHelper.exibirTarefas(
            layoutInflater = layoutInflater,
            tarefas = tarefas,
            layout = layoutTodasTarefas,
            banco = banco,
            onEditar = { tarefa ->
                val intent = Intent(this, EditarTarefaActivity::class.java)
                intent.putExtra("id", tarefa.id)
                startActivity(intent)
            },
            onAtualizar = {
                carregarTodasTarefas()

            }
        )
    }

    override fun onResume() {
        super.onResume()
        carregarTodasTarefas()
    }
}
