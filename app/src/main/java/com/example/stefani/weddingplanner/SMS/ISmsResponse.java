package com.example.stefani.weddingplanner.SMS;


public interface ISmsResponse {
    void smsResponse(String phoneNumber, Integer numberOfGuests);

    void smsErrorResponse(String phoneNumber);
}
