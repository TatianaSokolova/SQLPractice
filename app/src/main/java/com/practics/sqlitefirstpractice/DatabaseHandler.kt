package com.practics.sqlitefirstpractice

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "SOME_PERSONS"
private const val DATABASE_VERSION = 1
private const val TABLE_CONTACTS = "PersonsTable"

private const val KEY_ID = "_id"
private const val KEY_NAME = "name"
private const val KEY_EMAIL = "email"

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        //create table with fields
        //CREATE TABLE persons table(_id INTEGER PRIMARY KEY, name TEXT, email TEXT)
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }

    //ф-я для добавления данных в таблицу
    fun addPerson(person: PersonData): Long {
        //database that will be used for reading and writing
        val db = this.writableDatabase

        val contentValues = ContentValues()
        /**
         * объект ContentValues - представляет словарь, который содержит набор пар "ключ-значение".
         * Для добавления в этот словарь нового объекта применяется метод put.
         * Первый параметр метода - это ключ, а второй - значение
         */
        contentValues.put(KEY_NAME, person.name) // значения из PersonData class
        contentValues.put(KEY_EMAIL, person.email) // добавляем зачения в колонки

        // создаем ряд
        val success = db.insert(TABLE_CONTACTS, null, contentValues)

        db.close()
        return success
    }

    //ф-я для чтения записей из базы данных в виде листа
    fun viewPerson(): ArrayList<PersonData> {

        val personList = ArrayList<PersonData>()

        //Query to select all the records from the table
        val selectQuery = "SELECT * FROM " + TABLE_CONTACTS

        val db = this.readableDatabase

        //курсор - механизм для обхода записей в БД (~ итератор)
        var cursor: Cursor? = null

        try {
            //Runs the provided SQL and returns a cursor over the result set.
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if (cursor.moveToFirst()) { //  test whether the query returned an empty set
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))

                val person = PersonData(id = id, name = name, email = email)
                personList.add(person)
            } while (cursor.moveToNext())
        }
        return personList
    }

    //ф-я для обновления БД
    fun updatePerson(person: PersonData): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_NAME, person.name)
        contentValues.put(KEY_EMAIL, person.email)

        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + person.id, null)
        //whereClause: проверяем что id в БД соответствует изменяемому

        db.close()
        return success
    }

    //ф-я для удаления данных из БД
    fun deletePerson(person: PersonData): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, person.id)

        //delete row
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + person.id, null)

        db.close()
        return success
    }
}