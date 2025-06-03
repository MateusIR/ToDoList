package com.example.todolist;

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

object TarefaHelper {
    fun exibirTarefas(
        layoutInflater: LayoutInflater,
        tarefas: List<Tarefa>,
        layout: LinearLayout,
        banco: DBHelper,
        onEditar: (Tarefa) -> Unit,
        onAtualizar: () -> Unit
    ) {
        layout.removeAllViews()

        val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfOutput = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        tarefas.forEach { tarefa ->
                val view = layoutInflater.inflate(R.layout.item_tarefa, null)
            view.findViewById<TextView>(R.id.txtNome).text = tarefa.nome
            view.findViewById<TextView>(R.id.txtDescricao).text = tarefa.descricao

            val prazoFormatado = try {
                sdfOutput.format(sdfInput.parse(tarefa.prazo)!!)
            } catch (e: Exception) {
                tarefa.prazo
            }
            view.findViewById<TextView>(R.id.txtPrazo).text = prazoFormatado

            view.findViewById<Button>(R.id.btnEditar).setOnClickListener {
                onEditar(tarefa)
            }

            view.findViewById<Button>(R.id.btnExcluir).setOnClickListener {
                banco.deletarTarefa(tarefa.id)
                onAtualizar()
            }

            layout.addView(view)
        }
    }
}
