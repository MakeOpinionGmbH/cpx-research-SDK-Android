package com.makeopinion.cpxresearchlib.misc

fun String.prefixedDot(): String {
    return if (this.first().toString() == "#") {
        this.replaceFirst("#", ".")
    } else {
        StringBuilder()
            .append(".")
            .append(this)
            .toString()
    }
}