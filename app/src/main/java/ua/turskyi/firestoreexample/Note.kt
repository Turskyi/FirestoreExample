package ua.turskyi.firestoreexample

import com.google.firebase.firestore.Exclude


class Note {
    @get:Exclude
    var documentId: String? = null
    var title: String? = null
        private set
    var description: String? = null
        private set

    constructor() { //public no-arg constructor needed
    }

    constructor(title: String?, description: String?) {
        this.title = title
        this.description = description
    }

}