package com.example.carros;

import com.example.carros.api.upload.UploadInput;
import com.example.carros.api.upload.UploadOutput;
import com.example.carros.domain.upload.FirebaseStorageService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarrosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UploadTest {

    @Autowired
    protected TestRestTemplate rest;

    @Autowired
    private FirebaseStorageService service;

    private TestRestTemplate basicAuth() { return rest.withBasicAuth("admin", "123");}

    private UploadInput getUploadInput(){
        UploadInput upload = new UploadInput();
        upload.setFileName("nome.txt");
        upload.setBase64("SmFpbmUgUGl6emFuaQ==");
        upload.setMimeType("text/plain");
        return upload;
    }

    @Test
    public void testUploadFirebase(){
        String url = service.upload(getUploadInput());

        ResponseEntity<String> urlResponse = rest.getForEntity(url, String.class);
        System.out.println(urlResponse);
        assertEquals(HttpStatus.OK, urlResponse.getStatusCode());
    }

    @Test
    public void testUploadAPI(){
        UploadInput uploadInput = getUploadInput();

        //Insert
        ResponseEntity<UploadOutput> responseEntity = basicAuth().postForEntity("/api/v1/upload", uploadInput, UploadOutput.class);
        System.out.println(responseEntity);

        //Verifica ser criou
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        UploadOutput out = responseEntity.getBody();
        assertNotNull(out);
        System.out.println(out);

        String url = out.getUrl();

        //Faz o get na URL
        ResponseEntity<String> response = basicAuth().getForEntity(url, String.class);
        System.out.println(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }



}
