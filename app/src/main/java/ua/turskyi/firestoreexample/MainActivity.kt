package ua.turskyi.firestoreexample

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_TITLE = "title"
        private const val KEY_DESCRIPTION = "description"
    }

    private var editTextTitle: EditText? = null
    private var editTextDescription: EditText? = null
    private var textViewData: TextView? = null

    private val db = FirebaseFirestore.getInstance()
    private val notebookRef = db.collection("Notebook")
    private val noteRef =
        db.document("Notebook/My First Note")

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        textViewData = findViewById(R.id.text_view_data)
    }

    override fun onStart() {
        super.onStart()
/*        noteRef.addSnapshotListener(this, object : EventListener<DocumentSnapshot?> {

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

                    *//* working with object *//*
                    val note = documentSnapshot.toObject(Note::class.java)
                    val title = note?.title
                    val description = note?.description

                    textViewData!!.text =
                        getString(R.string.title_and_description, title, description)
                } else {
                    textViewData?.text = ""
                }
            }
        })*/

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
                   if (queryDocumentSnapshots != null) {
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
    }

    fun addNote(v: View?) {
        val title = editTextTitle!!.text.toString()
        val description = editTextDescription!!.text.toString()
        val note =
            Note(title, description)
        notebookRef.add(note)
    }

/*    fun saveNote(v: View?) {
        val title = editTextTitle!!.text.toString()
        val description = editTextDescription!!.text.toString()

//        val note: MutableMap<String, Any> = HashMap()
//        note[KEY_TITLE] = title
//        note[KEY_DESCRIPTION] = description

        *//* nicer way than above *//*
//        val note = hashMapOf(
//            KEY_TITLE to title,
//            KEY_DESCRIPTION to description
//        )

        *//* working with object *//*
        val note =
            Note(title, description)

//        db.collection("Notebook").document("My First Note").set(note)
        noteRef.set(note)
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
    }*/

/*    fun updateDescription(v: View?) {
        val description = editTextDescription!!.text.toString()

        *//* creates a new note if it does not exist *//*
//        val note: MutableMap<String, Any> = HashMap()
//        note[KEY_DESCRIPTION] = description
//        noteRef.set(note, SetOptions.merge())

        *//* does not create a new note if it does not exist *//*
        noteRef.update(KEY_DESCRIPTION, description)
    }

    fun deleteDescription(v: View?) {
//        val note: MutableMap<String, Any> = HashMap()
//        note[KEY_DESCRIPTION] = FieldValue.delete()
//        noteRef.update(note)

        *//* the same as above *//*
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete())
    }

    fun deleteNote(v: View?) {
        noteRef.delete()
    }*/


/*    fun loadNote(v: View?) {
        noteRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
//                    val title = documentSnapshot.getString(KEY_TITLE)
//                    val description =
//                        documentSnapshot.getString(KEY_DESCRIPTION)

        //                    val note: MutableMap<String, Any> =
        //                        documentSnapshot.data as MutableMap<String, Any>


                    *//* working with object *//*
                    val note = documentSnapshot.toObject(Note::class.java)
                    val title = note?.title
                    val description = note?.description

                    textViewData?.text = getString(R.string.title_and_description, title, description)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Document does not exist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@MainActivity, "Error!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, e.toString())
            }
    }*/

    fun loadNotes(v: View?) {
        notebookRef.get()
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
                    data += ("ID: " + documentId
                            + "\nTitle: " + title + "\nDescription: " + description + "\n\n")
                }
                textViewData!!.text = data
            }
    }
}
