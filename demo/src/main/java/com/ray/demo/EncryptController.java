package com.ray.demo;

import com.ingrian.security.nae.*;
import com.ray.demo.entities.DecryptTextRequest;
import com.ray.demo.entities.DecryptTextResponse;
import com.ray.demo.entities.EncryptTextResponse;
import com.ray.demo.entities.EncryptTextRequest;
import io.akeyless.client.ApiClient;
import io.akeyless.client.ApiException;
import io.akeyless.client.Configuration;
import io.akeyless.client.api.V2Api;
import io.akeyless.client.model.Configure;
import io.akeyless.client.model.ConfigureOutput;
import io.akeyless.client.model.GetSecretValue;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;


@RestController
@Tag(name = "Encrypt API")
public class EncryptController {

    private NAESession initEncrypt() {

    ApiClient client = Configuration.getDefaultApiClient();
    client.setBasePath("https://web.ciphertrustmanager.local/akeyless-api/v2");
    V2Api api = new V2Api(client);
    Configure body = new Configure();
    body.accessId("<ACCESS ID>").accessKey("<ACCESS KEY>");
    ConfigureOutput out;
        Map<String, String> result = null;
        try {
            out = api.configure(body);
            String token = out.getToken();
            GetSecretValue secretBody = new GetSecretValue();
            List<String> secretName = List.of("CM Password");
            secretBody.setNames(secretName);
            secretBody.setToken(token);
            result = api.getSecretValue(secretBody);
            System.out.println(result);

        } catch (ApiException e) {
            System.err.println("Exception when calling V2Api#getSecretValue");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    String userName = "admin";
    String password = result.get("CM Password"); //Get CM Password from CSM
    System.setProperty("com.ingrian.security.nae.CADP_for_JAVA_Properties_Conf_Filename", "CADP_for_JAVA.properties");
    IngrianProvider builder = new IngrianProvider.Builder().addConfigFileInputStream(getClass().getClassLoader().getResourceAsStream("CADP_for_JAVA.properties")).build();
    NAESession session = NAESession.getSession(userName, password.toCharArray());

    return session;

    }

    @PostMapping("/encryptText")
    public EncryptTextResponse encryptText(@RequestBody EncryptTextRequest encryptBody) {
        byte[] outbuf = new byte[0];
        EncryptTextResponse response = new EncryptTextResponse(null, null);
        try {
            NAESession session = initEncrypt();
            NAEKey key = NAEKey.getSecretKey(encryptBody.getKeyName(), session);
            NAESecureRandom rng = new NAESecureRandom(session);
            byte[] iv = new byte[16];
            rng.nextBytes(iv);
            response.setIv(HexFormat.of().formatHex(iv));
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "IngrianProvider");
// initialize cipher to encrypt.
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
// encrypt data
            byte[] messagebuf = encryptBody.getMessage().getBytes();
            outbuf = encryptCipher.doFinal(messagebuf);
            response.setEncryptedHexText(HexFormat.of().formatHex(outbuf));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @PostMapping("/decryptText")
    public DecryptTextResponse decryptText (@RequestBody DecryptTextRequest request) {
        byte[] inbuf = HexFormat.of().parseHex(request.getEncryptedHexMessage());
        byte[] outbuf = new byte[0];
        DecryptTextResponse response = new DecryptTextResponse(null);
        try {
            NAESession session = initEncrypt();
            NAEKey key = NAEKey.getSecretKey(request.getKeyName(), session);
            NAESecureRandom rng = new NAESecureRandom(session);
            byte[] iv = HexFormat.of().parseHex(request.getIv());
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "IngrianProvider");
// initialize cipher to encrypt.
            decryptCipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
// encrypt data
            outbuf = decryptCipher.doFinal(inbuf);
            response.setMessage(new String(outbuf, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }


}
