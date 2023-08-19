package com.nixonsu.exceptions

class HttpResponseException(statusCode: Int, message: String) : Exception(message)
