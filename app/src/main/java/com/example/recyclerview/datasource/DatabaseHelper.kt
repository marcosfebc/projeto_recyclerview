package com.example.recyclerview.datasource

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_FILME)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("drop table ${DatabaseDefinitions.Filme.TABLE_NAME}")
        db.execSQL(CREATE_TABLE_FILME)

        val ALTER_TABLE = "ALTER TABLE ${DatabaseDefinitions.Filme.TABLE_NAME} " +
                "ADD COLUMN ${DatabaseDefinitions.Filme.Columns.IMAGEM} BLOB"
        db.execSQL(ALTER_TABLE)
    }

    companion object {
        private const val DATABASE_NAME = "filme.db"
        private const val DATABASE_VERSION = 1

        private const val CREATE_TABLE_FILME = "CREATE TABLE ${DatabaseDefinitions.Filme.TABLE_NAME} (" +
            "${DatabaseDefinitions.Filme.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${DatabaseDefinitions.Filme.Columns.TITULO} TEXT, " +
                    "${DatabaseDefinitions.Filme.Columns.PRODUTORA} TEXT, " +
                        "${DatabaseDefinitions.Filme.Columns.NOTA} REAL, " +
                            "${DatabaseDefinitions.Filme.Columns.DISPONIBILIDADE} TEXT, " +
                                "${DatabaseDefinitions.Filme.Columns.ASSISTIDO} INTEGER, " +
                                    "${DatabaseDefinitions.Filme.Columns.IMAGEM} BLOB);"
    }
}