package com.example.estafeprojects12025

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.estafeprojects12025.databinding.AddeditBinding

class AddEdit: Activity(), View.OnClickListener {
    private lateinit var binding: AddeditBinding

    //create DBHandler Object
    val dbh = DBHandler(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddeditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // set on click listener
        binding.btnSave.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        // read value from mainactivity passed on
        val extras = intent.extras
        if (extras != null) {
            val id: Int = extras.getInt("ID")
            // get employees based on ID
            val employee = dbh.getEmployee(id)
            // fill details on activity
            binding.etID.setText(employee.id.toString())
            binding.etName.setText(employee.name)
            binding.etMobile.setText(employee.mobile)
            binding.etAddress.setText(employee.address)
            binding.etEmail.setText(employee.email)
            binding.ivImageFile.setImageResource(
                this.resources.getIdentifier(
                    employee.imageFile, "drawable", "com.example.estafeprojects12025"
                )
            )
        } else {
            binding.etID.setText("")
            binding.etName.setText("")
            binding.etMobile.setText("")
            binding.etAddress.setText("")
            binding.etEmail.setText("")
            binding.ivImageFile.setImageResource(
                this.resources.getIdentifier(
                    "", "drawable",
                    "com.example.estafeprojects12025"
                )
            )
        }

    }

    override fun onClick(btn: View) {
        when (btn.id) {
            R.id.btnSave -> {
                // read id if no value ise 0
                val cid: Int = binding.etID.text.toString().toIntOrNull() ?: 0
                // decide add or edit
                if (cid == 0) {
                    addEmployee()
                } else
                    editEmployee(cid)
            }

            R.id.btnCancel -> {
                // cancel code
                goBack()

            }
        }
    }

    private fun goBack() {
        // go back to main activity
        val mainact = Intent(this, MainActivity::class.java)
        // start another activity
        startActivity(mainact)
    }

    private fun addEmployee() {
        // read values from activity and assign to employee object
        val employee = Person()
        employee.name = binding.etName.text.toString()
        employee.address = binding.etAddress.text.toString()
        employee.mobile = binding.etMobile.text.toString()
        employee.email = binding.etEmail.text.toString()
        employee.imageFile = binding.etImageFile.text.toString()

        dbh.addEmployee(employee)
        // cal dbhandler function to add
        Toast.makeText(this, "Employee has been added", Toast.LENGTH_LONG).show()
        goBack()

    }


    private fun editEmployee(cid: Int) {
        // create an employee object and fill it with new value
        val employee = Person()
        employee.id = binding.etID.text.toString().toInt()
        employee.name = binding.etName.text.toString()
        employee.address = binding.etAddress.text.toString()
        employee.mobile = binding.etMobile.text.toString()
        employee.email = binding.etEmail.text.toString()
        employee.imageFile = binding.etImageFile.text.toString()
        // call dbhandler function to update
        dbh.updateEmployee(employee)
        // display confirmation
        Toast.makeText(this, "Employee details updated", Toast.LENGTH_LONG).show()
        goBack()

    }
}
