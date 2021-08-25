package com.practics.sqlitefirstpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), RVListener {

    private val TAG = "MyLog"

    private var addBtn: Button? = null
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var recycler: RecyclerView
    private var noRecords: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById<RecyclerView>(R.id.recycler)
       // recycler.isNestedScrollingEnabled = true

        etName = findViewById(R.id.editTextName)
        etEmail = findViewById(R.id.editTextEmail)
        noRecords = findViewById(R.id.no_records)

        addBtn = findViewById(R.id.add_button)
        addBtn?.setOnClickListener {
            Log.d(TAG, "click on add btn")
            addRecord()
        }

        setupListOfDataIntoRV()
    }

    //ф-я для отображения списка данных через ресайклервью
    private fun setupListOfDataIntoRV() {

        if (getItemsList().size > 0) {
            Log.d(TAG, "call setupListOfDataIntoRV() ${getItemsList().size}")
            recycler.visibility = View.VISIBLE
            noRecords?.visibility = View.GONE

            //подключаем лэйаут менеджер
            val manager = LinearLayoutManager(this)
            //manager.isSmoothScrollbarEnabled = true
            recycler.layoutManager = manager

            //создаем класс адаптер, передаем конткст и список
            val itemAdapter = RVAdapter(this, getItemsList(), this)
            //передаем созданный адаптер, чтобы заинфлейтить айтемы
            recycler.adapter = itemAdapter
        } else {
            recycler.visibility = View.GONE
            noRecords?.visibility = View.VISIBLE
        }
    }

    //ф-я чтобы получить лист айтемов, которые добавлены в таблицу БД
    private fun getItemsList(): ArrayList<PersonData> {
        //создаем экземпляр класса DatabaseHandler
        val databaseHandler = DatabaseHandler(this)
        Log.d(TAG, "call getItemsList() ${databaseHandler.viewPerson()}")
        //вызываем его метод для считывания записей
        return databaseHandler.viewPerson()
    }

    //ф-я для сохраненя записей person в БД
    private fun addRecord() {
        Log.d(TAG, "call addRecord()")
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val databaseHandler = DatabaseHandler(this)

        if (name.isNotEmpty() && email.isNotEmpty()) {
            val status = databaseHandler.addPerson(PersonData(0, name, email))

            if (status > -1) {
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_SHORT).show()
                etName.text?.clear()
                etEmail.text?.clear()

                setupListOfDataIntoRV()
            }
        } else {
            Toast.makeText(applicationContext, "Name or email cannot be blank", Toast.LENGTH_SHORT).show()
        }
    }

    override fun deleteRecord(personData: PersonData) {
        val databaseHandler = DatabaseHandler(this)

        val status = databaseHandler.deletePerson(PersonData(personData.id, "", ""))
        if (status > -1) {
            Toast.makeText(applicationContext, "Record delete successfully", Toast.LENGTH_SHORT).show()
            setupListOfDataIntoRV()
        }

    }
}