package com.nixonsu.extensions

import java.net.http.HttpResponse

fun HttpResponse<*>.reasonPhrase(): String {
    return when(this.statusCode()) {
        200 -> "OK"
        201 -> "Created"
        204 -> "No Content"
        400 -> "Bad Request"
        401 -> "Unauthorized"
        403 -> "Forbidden"
        404 -> "Not Found"
        500 -> "Internal Server Error"
        else -> "Unknown Phrase For Status Code"
    }
}