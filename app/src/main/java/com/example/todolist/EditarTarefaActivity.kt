package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import java.util.Calendar



class EditarTarefaActivity : AppCompatActivity() {

    private lateinit var banco: DBHelper
    private var idTarefa: Int = -1

    private lateinit var edtNome: EditText
    private lateinit var edtPrazo: EditText
    private lateinit var edtDescricao: EditText
    private lateinit var btnSalvar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_tarefa)

        banco = DBHelper(this)

        edtNome = findViewById(R.id.edtEditarNome)
        edtPrazo = findViewById(R.id.edtEditarPrazo)
        edtPrazo.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "%04d-%02d-%02d".format(selectedYear, selectedMonth + 1, selectedDay)
                edtPrazo.setText(formattedDate)
            }, year, month, day)

            // Bloqueia datas anteriores à hoje
            datePickerDialog.datePicker.minDate = c.timeInMillis
            datePickerDialog.show()
        }

        edtDescricao = findViewById(R.id.edtEditarDescricao)
        btnSalvar = findViewById(R.id.btnSalvarEdicao)

        idTarefa = intent.getIntExtra("id", -1)

        val btnVoltar = findViewById<Button>(R.id.btnVoltarHome)
        btnVoltar.setOnClickListener {
            finish()
        }
        if (idTarefa != -1) {
            val tarefa = banco.obterTarefas().find { it.id == idTarefa }
            tarefa?.let {
                edtNome.setText(it.nome)
                edtPrazo.setText(it.prazo)
                edtDescricao.setText(it.descricao)
            }
        }

        btnSalvar.setOnClickListener {
            banco.atualizarTarefa(
                idTarefa,
                edtNome.text.toString(),
                edtPrazo.text.toString(),
                edtDescricao.text.toString()
            )
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}
