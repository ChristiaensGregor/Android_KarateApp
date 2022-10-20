package com.gregorchristiaens.karatelessons.domain

class Lesson() {
    var id: String = ""
    var date: String = ""
    var type: String = ""
    var location: String = ""
    var expired: Boolean = false

    constructor(
        _id: String,
        _date: String,
        _type: String,
        _location: String,
        _expired: Boolean,
    ) : this() {
        id = _id
        date = _date
        type = _type
        location = _location
        expired = _expired
    }
}