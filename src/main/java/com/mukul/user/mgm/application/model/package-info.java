@TypeDef(
        name = "encryptedString",
        typeClass = EncryptedStringType.class,
        parameters = {
                @Parameter(name = "encryptorRegisteredName", value = "strongHibernateStringEncryptor"),
        }
)
package com.mukul.user.mgm.application.model;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.jasypt.hibernate4.type.EncryptedStringType;