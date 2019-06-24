package marketplaces.backend.backendrestapi.config.exceptions.constants;

import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessageBody;
import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;

public final class ExceptionMessages {

    public static final String ERROR_PEFIX = "ERROR_";
    public static final ApiExceptionMessage ERROR_NOT_FOUND = new ApiExceptionMessage(
            ERROR_PEFIX.concat("NOT_FOUND"),
            new ApiExceptionMessageBody(
                    "",
                    "",
                    ""
            )
    );

    public static final ApiExceptionMessage ERROR_DOCUMENT_NOT_FOUND = new ApiExceptionMessage(
            ERROR_PEFIX.concat("DOCUMENT_NOT_FOUND"),
            new ApiExceptionMessageBody(
                    "Ce document n'existe pas !",
                    "",
                    "This document not found !"
            )
    );

    public static final ApiExceptionMessage ERROR_OBJECT_ID_NOT_VALID = new ApiExceptionMessage(
            ERROR_PEFIX.concat("OBJECT_ID_NOT_VALID"),
            new ApiExceptionMessageBody(
                    "Cet id n'est pas correct !",
                    "",
                    "This id is not valid !"
            )
    );

    public static final ApiExceptionMessage ERROR_USER_SMALL_THEN_4 = new ApiExceptionMessage(
            ERROR_PEFIX.concat("USER_SMALL_THEN_4"),
            new ApiExceptionMessageBody(
                    "Le nom doit être supérieur à 4 !!",
                    "",
                    "The name must be bigger then 4 caracters !!"
            )
    );

    public static final ApiExceptionMessage ERROR_PASS_SMALL_THEN_8 = new ApiExceptionMessage(
            ERROR_PEFIX.concat("PASS_SMALL_THEN_8"),
            new ApiExceptionMessageBody(
                    "le mot de passe doit être supérieur à 8 !!",
                    "",
                    "The password must be bigger then 8 caracters !!"
            )
    );

    public static final ApiExceptionMessage ERROR_FIELD_NULL = new ApiExceptionMessage(
            ERROR_PEFIX.concat("FIELD_NULL"),
            new ApiExceptionMessageBody(
                    "Veuillez remplir tous les champs !!",
                    "",
                    "Try to send all fields !!"
            )
    );

    public static final ApiExceptionMessage ERROR_INVALID_MAIL = new ApiExceptionMessage(
            ERROR_PEFIX.concat("INVALID_MAIL"),
            new ApiExceptionMessageBody(
                    "Adresse email non valide !!",
                    "",
                    "Address email not valid !!"
            )
    );

    public static final ApiExceptionMessage ERROR_INVALID_PHONE_NUMBER = new ApiExceptionMessage(
            ERROR_PEFIX.concat("INVALID_PHONE_NUMBER"),
            new ApiExceptionMessageBody(
                    "Un numéro du télephone incorrect !!",
                    "",
                    "The number phone in incorrect !!"
            )
    );

    public static final ApiExceptionMessage ERROR_EXISTING_USERNAME = new ApiExceptionMessage(
            ERROR_PEFIX.concat("EXISTING_USERNAME"),
            new ApiExceptionMessageBody(
                    "Veuillez changer ce nom d'utilisatuer !!",
                    "",
                    "You need to choose another username!!"
            )
    );

    public static final ApiExceptionMessage ERROR_EXISTING_MAIL = new ApiExceptionMessage(
            ERROR_PEFIX.concat("EXISTING_MAIL"),
            new ApiExceptionMessageBody(
                    "Veuillez changer cette adresse mail!!",
                    "",
                    "You need to pass another address mail!!"
            )
    );

    public static final ApiExceptionMessage ERROR_EXISTING_PHONE = new ApiExceptionMessage(
            ERROR_PEFIX.concat("EXISTING_PHONE"),
            new ApiExceptionMessageBody(
                    "Veuillez changer ce numero du telephone!!",
                    "",
                    "You need to pass another phone number l!!"
            )
    );

    public static final ApiExceptionMessage ERROR_UNKNOWN_EXCEPTION = new ApiExceptionMessage(
            ERROR_PEFIX.concat("UNKNOWN_EXCEPTION"),
            new ApiExceptionMessageBody(
                    "Le systeme a rencontre des probleme durant le traitement de votre requete !!",
                    "",
                    "The system was have difficult to trait your request !!"
            )
    );
}
