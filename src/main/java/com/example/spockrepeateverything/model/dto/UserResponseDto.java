package com.example.spockrepeateverything.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {


    private String name;
    private String lastname;
    private String username;


}
