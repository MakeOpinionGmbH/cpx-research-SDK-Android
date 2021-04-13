package com.makeopinion.cpxresearchlib.misc

fun<T> List<T>.isEqualTo(other: List<T>): Boolean {

    if (size != other.size) {
        return false
    }

    return zip(other).all { (x, y) -> x == y }
}