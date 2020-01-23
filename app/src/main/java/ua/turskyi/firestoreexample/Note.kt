package ua.turskyi.firestoreexample

class Note {
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