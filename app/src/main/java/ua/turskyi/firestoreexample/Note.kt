package ua.turskyi.firestoreexample

class Note {
    var title: String? = null
        private set
    var description: String? = null
        private set
    var priority = 0
        private set

    constructor() { //empty constructor needed
    }

    constructor(title: String?, description: String?, priority: Int) {
        this.title = title
        this.description = description
        this.priority = priority
    }
}