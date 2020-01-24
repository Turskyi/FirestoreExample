package ua.turskyi.firestoreexample

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private var editTextPriority: EditText? = null
    private var editTextTags: EditText? = null
    private var textViewData: TextView? = null
    private val db = FirebaseFirestore.getInstance()
    private val notebookRef = db.collection("Notebook")
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        editTextPriority = findViewById(R.id.edit_text_priority)
        editTextTags = findViewById(R.id.edit_text_tags)
        textViewData = findViewById(R.id.text_view_data)
        updateNestedValue()
    }

    fun addNote(v: View?) {
        val title = editTextTitle!!.text.toString()
        val description = editTextDescription!!.text.toString()
        if (editTextPriority!!.length() == 0) {
            editTextPriority!!.setText("0")
        }
        val priority = editTextPriority!!.text.toString().toInt()
        val tagInput = editTextTags!!.text.toString()
        val tagArray = tagInput.split("\\s*,\\s*").toTypedArray()
        val tags: MutableMap<String, Boolean> = HashMap()
        for (tag in tagArray) {
            tags[tag] = true
        }
        val note =
            Note(title, description, priority, tags)
        notebookRef.add(note)
    }

    fun loadNotes(v: View?) {
        notebookRef.whereEqualTo("tags.tag1", true).get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                var data = ""
                for (documentSnapshot in queryDocumentSnapshots) {
                    val note =
                        documentSnapshot.toObject(
                            Note::class.java
                        )
                    note.documentId = documentSnapshot.id
                    val documentId = note.documentId
                    data += "ID: $documentId"
                    for (tag in note.tags!!.keys) {
                        data += "\n-$tag"
                    }
                    data += "\n\n"
                }
                textViewData!!.text = data
            }
    }

    private fun updateNestedValue() {
        notebookRef.document("kPj6ZqQ4v85WvLAvXCT5")
            .update("tags.tag1.nested1.nested2", true)
    }
}