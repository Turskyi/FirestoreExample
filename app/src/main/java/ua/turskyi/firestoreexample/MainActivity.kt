package ua.turskyi.firestoreexample

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity(R.layout.activity_main) {

/*    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_TITLE = "title"
        private const val KEY_DESCRIPTION = "description"
    }*/

    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private var editTextPriority: EditText? = null
    private var textViewData: TextView? = null

    private val db = FirebaseFirestore.getInstance()
    private val notebookRef = db.collection("Notebook")
/*    private val noteRef = db.document("Notebook/My First Note")*/

    private var lastResult: DocumentSnapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        editTextPriority = findViewById(R.id.edit_text_priority)
        textViewData = findViewById(R.id.text_view_data)
    }

  /*  override fun onStart() {
        super.onStart()
        notebookRef.addSnapshotListener(this, object : EventListener<QuerySnapshot?> {
           override fun onEvent(
                queryDocumentSnapshots: QuerySnapshot?,
                e: FirebaseFirestoreException?
            ) {
                if (e != null) {
                    return
                }
                var data = ""
               queryDocumentSnapshots?.let {
                   for (documentSnapshot in queryDocumentSnapshots) {
                       val note =
                           documentSnapshot.toObject(
                               Note::class.java
                           )
                       note.documentId = documentSnapshot.id
                       val documentId = note.documentId
                       val title = note.title
                       val description = note.description
                       val priority = note.priority
                       data += ("ID: " + documentId
                               + "\nTitle: " + title + "\nDescription: " + description
                               + "\nPriority: " + priority + "\n\n")
                   }
               }
                textViewData!!.text = data
            }
        })
    }*/

/*    override fun onStart() {
        super.onStart()
*//*        noteRef.addSnapshotListener(this, object : EventListener<DocumentSnapshot?> {

            override fun onEvent(
                documentSnapshot: DocumentSnapshot?,
                e: FirebaseFirestoreException?
            ) {
                if (e != null) {
                    Toast.makeText(this@MainActivity, "Error while loading!", Toast.LENGTH_SHORT)
                        .show()
                    Log.d(TAG, e.toString())
                    return
                }
                if (documentSnapshot?.exists()!!) {
//                    val title = documentSnapshot.getString(KEY_TITLE)
//                    val description = documentSnapshot.getString(KEY_DESCRIPTION)

                    *//**//* working with object *//**//*
                    val note = documentSnapshot.toObject(Note::class.java)
                    val title = note?.title
                    val description = note?.description

                    textViewData!!.text =
                        getString(R.string.title_and_description, title, description)
                } else {
                    textViewData?.text = ""
                }
            }
        })*//*

        notebookRef.addSnapshotListener(
            this,
            object : EventListener<QuerySnapshot> {
               override fun onEvent(
                    queryDocumentSnapshots: QuerySnapshot?,
                    e: FirebaseFirestoreException?
                ) {
                    if (e != null) {
                        return
                    }
                    var data = ""
                   queryDocumentSnapshots?.let {
                       for (documentSnapshot in queryDocumentSnapshots) {
                           val note =
                               documentSnapshot.toObject(
                                   Note::class.java
                               )
                           note.documentId = documentSnapshot.id
                           val documentId = note.documentId
                           val title = note.title
                           val description = note.description
                           data += ("ID: " + documentId
                                   + "\nTitle: " + title + "\nDescription: " + description + "\n\n")
                       }
                   }
                    textViewData!!.text = data
                }
            })
    }*/

    fun addNote(v: View?) {
        val title = editTextTitle!!.text.toString()
        val description = editTextDescription!!.text.toString()
        if (editTextPriority!!.length() == 0) {
            editTextPriority!!.setText("0")
        }
        val priority = editTextPriority!!.text.toString().toInt()
        val note =
            Note(title, description, priority)
        notebookRef.add(note)
    }

    fun loadNotes(v: View?) {
        val query: Query = if (lastResult == null) {
            notebookRef.orderBy("priority")
                .limit(3)
        } else {
            notebookRef.orderBy("priority")
                .startAfter(lastResult!!)
                .limit(3)
        }
        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                var data = ""
                for (documentSnapshot in queryDocumentSnapshots) {
                    val note =
                        documentSnapshot.toObject(
                            Note::class.java
                        )
                    note.documentId = documentSnapshot.id
                    val documentId = note.documentId
                    val title = note.title
                    val description = note.description
                    val priority = note.priority
                    data += ("ID: " + documentId
                            + "\nTitle: " + title + "\nDescription: " + description
                            + "\nPriority: " + priority + "\n\n")
                }
                if (queryDocumentSnapshots.size() > 0) {
                    data += "___________\n\n"
                    textViewData!!.append(data)
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                }
            }
    }
}
