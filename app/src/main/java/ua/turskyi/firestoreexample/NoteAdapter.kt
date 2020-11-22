package ua.turskyi.firestoreexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot

class NoteAdapter(options: FirestoreRecyclerOptions<Note?>) :
    FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder>(options) {
    private var listener: OnItemClickListener? = null
    override fun onBindViewHolder(
        holder: NoteHolder,
        position: Int,
        model: Note
    ) {
        holder.textViewTitle.text = model.title
        holder.textViewDescription.text = model.description
        holder.textViewPriority.text = model.priority.toString()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.note_item,
            parent, false
        )
        return NoteHolder(v)
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

   inner class NoteHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        var textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        var textViewPriority: TextView = itemView.findViewById(R.id.text_view_priority)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener?.onItemClick(snapshots.getSnapshot(position), position)
                }
            }
        }
    }

    interface OnItemClickListener {
       fun onItemClick(documentSnapshot: DocumentSnapshot?, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}