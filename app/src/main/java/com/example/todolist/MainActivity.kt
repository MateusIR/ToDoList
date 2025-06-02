package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import java.util.Calendar



class MainActivity : AppCompatActivity() {

    private lateinit var banco: DBHelper
    private lateinit var layoutTarefas: LinearLayout
    private lateinit var editarTarefaLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        banco = DBHelper(this)

        val nome = findViewById<EditText>(R.id.edtNome)
        val descricao = findViewById<EditText>(R.id.edtDescricao)
        val botaoInserir = findViewById<Button>(R.id.btnInserir)
        val botaoTodas = findViewById<Button>(R.id.btnTodasTarefas)
        layoutTarefas = findViewById(R.id.layoutTarefas)
        val prazo = findViewById<EditText>(R.id.edtPrazo)
        prazo.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "%04d-%02d-%02d".format(selectedYear, selectedMonth + 1, selectedDay)
                prazo.setText(formattedDate)
            }, year, month, day)

            // Impede a seleção de datas anteriores a hoje
            datePickerDialog.datePicker.minDate = c.timeInMillis

            datePickerDialog.show()
        }



        // ao salvar edicao na tarefa, atualiza a home para ela ser carregada.
        editarTarefaLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                atualizarTarefas()
            }
        }

        botaoInserir.setOnClickListener {
            banco.inserirTarefa(nome.text.toString(), prazo.text.toString(), descricao.text.toString())
            nome.text.clear(); prazo.text.clear(); descricao.text.clear()
            atualizarTarefas()
        }

        botaoTodas.setOnClickListener {
            startActivity(Intent(this, TarefasActivity::class.java))
        }

        atualizarTarefas()
    }

    private fun atualizarTarefas() {
            layoutTarefas.removeAllViews()
            val tarefas = banco.obterTop3PorPrazo()
            val sdfInput = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val sdfOutput = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())

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
                val intent = Intent(this, EditarTarefaActivity::class.java)
                intent.putExtra("id", tarefa.id)
                editarTarefaLauncher.launch(intent)
            }

            view.findViewById<Button>(R.id.btnExcluir).setOnClickListener {
                banco.deletarTarefa(tarefa.id)
                atualizarTarefas()
            }

            layoutTarefas.addView(view)
        }
    }
}
