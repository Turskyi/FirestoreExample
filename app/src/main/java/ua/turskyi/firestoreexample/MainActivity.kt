package ua.turskyi.firestoreexample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val TAG = "MainActivity"
    }

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

    override fun onStart() {
        super.onStart()
        notebookRef.addSnapshotListener(this, object : EventListener<QuerySnapshot?> {
           override fun onEvent(
                queryDocumentSnapshots: QuerySnapshot?,
                e: FirebaseFirestoreException?
            ) {
                e?.let{
                    Log.d(TAG,e.toString())
                    return
                }
                for (dc in queryDocumentSnapshots?.documentChanges!!) {
                    val documentSnapshot: DocumentSnapshot = dc.document
                    val id = documentSnapshot.id
                    val oldIndex = dc.oldIndex
                    val newIndex = dc.newIndex
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> textViewData!!.append(
                            "\nAdded: " + id +
                                    "\nOld Index: " + oldIndex + "New Index: " + newIndex
                        )
                        DocumentChange.Type.MODIFIED -> textViewData!!.append(
                            "\nModified: " + id +
                                    "\nOld Index: " + oldIndex + "New Index: " + newIndex
                        )
                        DocumentChange.Type.REMOVED -> textViewData!!.append(
                            "\nRemoved: " + id +
                                    "\nOld Index: " + oldIndex + "New Index: " + newIndex
                        )
                    }
                }
            }
        })
    }

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
