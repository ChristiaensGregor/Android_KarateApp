package com.gregorchristiaens.karatelessons.domain

class Lesson() {
    var id: String = ""
    var date: String = ""
    var type: String = ""
    var location: String = ""
    var expired: Boolean = false
    var participants: ArrayList<String> = arrayListOf()

    constructor(
        _id: String,
        _date: String,
        _type: String,
        _location: String,
        _expired: Boolean,
        _participants: ArrayList<String>,
    ) : this() {
        id = _id
        date = _date
        type = _type
        location = _location
        expired = _expired
        participants = _participants
    }
}