package com.ms.core.common.constants;

import java.util.UUID;

public class ConfigConstants {

	public static final UUID APPLICATION_ID = UUID.randomUUID();
	public static final String IMAGE_UPLOAD_LOCATION = "${image.upload.location}";
    public static final String IMAGE_PROFILE_UPLOAD_LOCATION = "profile_images";
    public static final String IMAGE_COVER_UPLOAD_LOCATION = "cover_images";
    public static final String SYSTEM_INTERNAL_AUTH_KEY = "${system-internal-auth-key}";

    // Assuming you are sending email through relay.jangosmtp.net
    public static final String EMAIL_SMTP_HOST = "${email.smtp.host}";
    public static final String EMAIL_SMTP_PORT = "${email.smtp.port}";
    public static final String EMAIL_USERNAME = "${email.userName}";
    public static final String EMAIL_PASSWORD = "${email.password}";

}
