package com.example.member.domain;

public record Address(String address) {

    public static Address of(String address) {
        return new Address(address);
    }
}
