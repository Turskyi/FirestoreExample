package ua.turskyi.firestoreexample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_TITLE = "title"
        private const val KEY_DESCRIPTION = "description"
    }

    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
    }

    fun saveNote(v: View?) {
        val title = editTextTitle!!.text.toString()
        val description = editTextDescription!!.text.toString()

//        val note: MutableMap<String, Any> = HashMap()
//        note[KEY_TITLE] = title
//        note[KEY_DESCRIPTION] = description

        /* nicer way than above */
        val note = hashMapOf(
            KEY_TITLE to title,
            KEY_DESCRIPTION to description
        )

        db.collection("Notebook").document("My First Note").set(note)
            .addOnSuccessListener {
                Toast.makeText(
                    this@MainActivity,
                    "Note saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@MainActivity, "Error!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, e.toString())
            }
    }
}
