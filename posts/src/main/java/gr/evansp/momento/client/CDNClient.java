package gr.evansp.momento.client;

import gr.evansp.momento.dto.AssetDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;

@FeignClient(name = "cdn-client", url = "${cdn.client.url}")
public interface CDNClient {

    @PostMapping(value = "cdn/v1/assets/upload", consumes = "multipart/form-data")
    ResponseEntity<AssetDto> upload(@RequestPart("file") MultipartFile file);
}
