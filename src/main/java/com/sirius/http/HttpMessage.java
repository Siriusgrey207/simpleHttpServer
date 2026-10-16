package com.sirius.http;

public abstract class HttpMessage {
}

/*
* Notes:
* - The HTTP message class has been made abstract because the http request and response will have common
* components, which can be modeled with our abstract class.
* - The parts that are not common can be defined in the custom subclass implementations.
* */
