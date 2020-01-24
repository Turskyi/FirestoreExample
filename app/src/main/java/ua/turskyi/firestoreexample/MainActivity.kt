package ua.turskyi.firestoreexample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val db = FirebaseFirestore.getInstance()
    private val notebookRef = db.collection("Notebook")
    private var adapter: NoteAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        val buttonAddNote: FloatingActionButton = findViewById(R.id.button_add_note)
        buttonAddNote.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    NewNoteActivity::class.java
                )
            )
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val query = notebookRef.orderBy(
            "priority",
            Query.Direction.DESCENDING
        )
        val options =
            FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note::class.java)
                .build()
        adapter = NoteAdapter(options)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }
}