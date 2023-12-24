package com.example.khaier.factory;

import com.example.khaier.models.ApiCustomResponse;

public interface ResponseFactory<T> {
    ApiCustomResponse<?>createResponse(T data,String message);
}
