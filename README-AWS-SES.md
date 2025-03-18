# Implementación de AWS SES en Spring Boot

Este proyecto demuestra cómo integrar Amazon Simple Email Service (AWS SES) en una aplicación Spring Boot.

## Configuración

1. Añade la dependencia de AWS SES en tu `pom.xml`:
```xml
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-java-sdk-ses</artifactId>
    <version>1.12.621</version>
</dependency>
```

2. Configura las credenciales de AWS en tu `application.yml`:
```yaml
aws:
  ses:
    access-key: tu-access-key-aquí
    secret-key: tu-secret-key-aquí
    region: tu-región-aquí
    from-email: tu-email-verificado@dominio.com
```

## Componentes Principales

### 1. AwsSesConfig
Esta clase configura el cliente de AWS SES:
```java
@Configuration
public class AwsSesConfig {
    @Value("${aws.ses.access-key}")
    private String accessKey;

    @Value("${aws.ses.secret-key}")
    private String secretKey;

    @Value("${aws.ses.region}")
    private String region;

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
```

### 2. AwsMailSender
Esta clase maneja el envío de correos usando AWS SES:
```java
@Component
public class AwsMailSender {
    private final AmazonSimpleEmailService sesClient;
    private final String fromEmail;

    public AwsMailSender(AmazonSimpleEmailService sesClient,
                        @Value("${aws.ses.from-email}") String fromEmail) {
        this.sesClient = sesClient;
        this.fromEmail = fromEmail;
    }

    public void sendEmail(String to, String subject, String htmlBody, String transactionId) {
        try {
            SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(to))
                .withMessage(new Message()
                    .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBody)))
                    .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                .withSource(fromEmail);

            sesClient.sendEmail(request);
        } catch (Exception e) {
            throw new RuntimeException("Error sending email through AWS SES. Transaction ID: " + transactionId, e);
        }
    }
}
```

## Uso

Para usar el servicio de correo, inyecta `AwsMailSender` en tu servicio y utiliza el método `sendEmail`:

```java
@Service
public class TuServicio {
    private final AwsMailSender mailSender;

    public TuServicio(AwsMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreo() {
        mailSender.sendEmail(
            "destinatario@ejemplo.com",
            "Asunto del correo",
            "<h1>Contenido HTML del correo</h1>",
            "ID-TRANSACCION-123"
        );
    }
}
```

## Notas Importantes

1. Asegúrate de tener una cuenta de AWS y acceso a AWS SES.
2. Verifica tu dirección de correo electrónico en AWS SES antes de enviar correos.
3. Si estás en modo sandbox, solo podrás enviar correos a direcciones verificadas.
4. Para producción, solicita a AWS que te saque del modo sandbox.
5. Mantén tus credenciales de AWS seguras y nunca las compartas o subas a control de versiones.