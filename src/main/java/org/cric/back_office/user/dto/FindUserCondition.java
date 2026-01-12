package org.cric.back_office.user.dto;


import java.time.LocalDate;

public record FindUserCondition(String email, String name, String affiliation, String position, String phoneNumber, LocalDate birth) {
}
