package com.nixonsu.exception

class HttpResponseException(statusCode: Int, message: String) : Exception(message)
