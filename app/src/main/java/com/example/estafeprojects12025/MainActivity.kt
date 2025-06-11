package com.example.estafeprojects12025

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.estafeprojects12025.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // create DBHandler object
    private val dbh = DBHandler(this,null)
    //create arrays to store person object and IDs
    private var personList:MutableList<Person> = arrayListOf()
    private var idList:MutableList<Int> = arrayListOf()
    // add Menu
    private val menuAdd = Menu.FIRST+1
    private val menuEdit = Menu.FIRST+2
    private val menuDelete = Menu.FIRST+3



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // register for context menu
        registerForContextMenu(binding.lvEmployees)
        //Load the existing data in Listview widget
        initAdapter()


    }

         private fun initAdapter() {try {
        // clear arraylist
        idList.clear()
        personList.clear()
        // get all person and put them into arraylist
        for (person: Person in dbh.getAllPersons())
        {
            // read and add to arraylist
            idList.add(person.id)
            personList.add(person)
        }
        // create arrayAdapter
        val adp = CustomAdapter(this, 0, personList as ArrayList<Person>)
        // Assign to Listview
        binding.lvEmployees.adapter = adp
    } catch (ex: Exception) {
        Toast.makeText(this, "Problem: ${ex.message.toString()}", Toast.LENGTH_LONG).show()
    }

}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE,menuAdd,Menu.NONE,"ADD")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // this will start addedit activity
        val goAddEdit = Intent(this,AddEdit::class.java)
        startActivity(goAddEdit)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu.add(Menu.NONE,menuEdit,Menu.NONE,"EDIT")
        menu.add(Menu.NONE,menuDelete,Menu.NONE,"DELETE")
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
       // adapter context menu info object
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        // code for edit and delete
        when (item.itemId){
            menuEdit->{
                // create intent and start activity with data passed on
                val addEdit = Intent(this,AddEdit::class.java)
                addEdit.putExtra("ID",idList[info.position])
                startActivity(addEdit)
            }
            menuDelete->{
                // call delete function from dbhandler
                dbh.deleteEmployee(idList[info.position])
                // refresh list view
                initAdapter()

            }

        }
        return super.onContextItemSelected(item)
    }
}