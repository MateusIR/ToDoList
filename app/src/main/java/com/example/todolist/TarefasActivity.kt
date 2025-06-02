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
        carregarTarefas()
    }

    private fun carregarTarefas() {
        layoutTodasTarefas.removeAllViews()
        val tarefas = banco.obterTarefas()

        // Conversores de data
        val sdfInput = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val sdfOutput = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())

        tarefas.forEach { tarefa ->
            val view = layoutInflater.inflate(R.layout.item_tarefa, null)
            view.findViewById<TextView>(R.id.txtNome).text = tarefa.nome
            view.findViewById<TextView>(R.id.txtDescricao).text = tarefa.descricao

            // Formatar data de exibição
            val prazoFormatado = try {
                sdfOutput.format(sdfInput.parse(tarefa.prazo)!!)
            } catch (e: Exception) {
                tarefa.prazo // fallback caso erro
            }

            view.findViewById<TextView>(R.id.txtPrazo).text = prazoFormatado

            view.findViewById<Button>(R.id.btnEditar).setOnClickListener {
                val intent = Intent(this, EditarTarefaActivity::class.java)
                intent.putExtra("id", tarefa.id)
                startActivity(intent)
            }

            view.findViewById<Button>(R.id.btnExcluir).setOnClickListener {
                banco.deletarTarefa(tarefa.id)
                carregarTarefas()
            }

            layoutTodasTarefas.addView(view)
        }
    }


    override fun onResume() {
        super.onResume()
        carregarTarefas()
    }
}
