package mx.tecm.ciudadhidalgo.practica

import android.view.View

interface ItemListener {
    fun onClick (v: View?, position: Int)
    fun onEdit (v: View?, position: Int)
    fun onDel (v: View?, position: Int)
}