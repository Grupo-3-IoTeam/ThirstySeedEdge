package com.thristyseed.platform.edgeservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/device-data")
public class DeviceController {

    @Value("${backend.url}")
    private String backendUrl;

    private final RestTemplate restTemplate = new RestTemplate();


    @PostMapping
    public ResponseEntity<String> receiveDeviceData(@RequestBody Map<String, Object> data) {
        System.out.println("Datos recibidos: " + data);

        if (data.containsKey("temperature") && (Double) data.get("temperature") > 30) {
            System.out.println("Advertencia: temperatura alta");
        }

        try {
            restTemplate.postForObject(backendUrl + "/api/plots", data, String.class);

            restTemplate.postForObject(backendUrl + "/api/nodes", data, String.class);

            return ResponseEntity.ok("Datos enviados al servidor central");
        } catch (Exception e) {
            System.err.println("Error al enviar los datos al back end: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al conectar con el servidor central");
        }
    }
}
