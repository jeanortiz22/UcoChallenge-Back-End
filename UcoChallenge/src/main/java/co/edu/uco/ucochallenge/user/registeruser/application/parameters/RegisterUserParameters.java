package co.edu.uco.ucochallenge.user.registeruser.application.parameters;

public record RegisterUserParameters(
        int idMin, int idMax,
        int firstNameMin, int firstNameMax,
        int secondNameMin, int secondNameMax,
        int firstSurnameMin, int firstSurnameMax,
        int secondSurnameMin, int secondSurnameMax,
        int emailMin, int emailMax, String emailPattern,
        int mobileMin, int mobileMax, String mobilePattern,
        String namePattern,
        int emailTtlMinutes, int smsTtlMinutes,
        int maxRetries,
        boolean selfRegisterEnabled
) {}
