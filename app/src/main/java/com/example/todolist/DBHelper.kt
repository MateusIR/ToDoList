package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context) :
        SQLiteOpenHelper(context, "tarefas.db", null, 1) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("""
            CREATE TABLE tarefas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                prazo TEXT,
                descricao TEXT
            )
        """.trimIndent())
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS tarefas")
            onCreate(db)
        }

        fun inserirTarefa(nome: String, prazo: String, descricao: String) {
            val db = writableDatabase
            val valores = ContentValues().apply {
                put("nome", nome)
                put("prazo", prazo)
                put("descricao", descricao)
            }
            db.insert("tarefas", null, valores)
        }

        fun obterTarefas(): List<Tarefa> {
            val lista = mutableListOf<Tarefa>()
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM tarefas ORDER BY prazo ASC", null)
            while (cursor.moveToNext()) {
                lista.add(
                    Tarefa(
                        id = cursor.getInt(0),
                        nome = cursor.getString(1),
                        prazo = cursor.getString(2),
                        descricao = cursor.getString(3)
                    )
                )
            }
            cursor.close()
            return lista
        }

        fun atualizarTarefa(id: Int, nome: String, prazo: String, descricao: String) {
            val db = writableDatabase
            val valores = ContentValues().apply {
                put("nome", nome)
                put("prazo", prazo)
                put("descricao", descricao)
            }
            db.update("tarefas", valores, "id = ?", arrayOf(id.toString()))
        }

        fun deletarTarefa(id: Int) {
            val db = writableDatabase
            db.delete("tarefas", "id = ?", arrayOf(id.toString()))
        }

        fun obterTop3PorPrazo(): List<Tarefa> {
            return obterTarefas().take(3)
        }
    }

