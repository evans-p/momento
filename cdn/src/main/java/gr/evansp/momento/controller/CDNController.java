package gr.evansp.momento.controller;

import gr.evansp.momento.bean.FileWithContentType;
import gr.evansp.momento.dto.AssetDto;
import gr.evansp.momento.model.Asset;
import gr.evansp.momento.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller
 */
@RestController
@RequestMapping("cdn/v1/assets/")
public class CDNController {

	private final AssetService service;

	@Autowired
	public CDNController(AssetService service) {
		this.service = service;
	}

	@PostMapping("upload")
	public ResponseEntity<AssetDto> upload(@RequestParam("file") MultipartFile file) {
		Asset asset = service.uploadAsset(file);
		return new ResponseEntity<>(AssetDto.of(asset), HttpStatus.OK);
	}

	@GetMapping("{fileName}")
	public ResponseEntity<FileSystemResource> getFile(@PathVariable String fileName) {
		FileWithContentType bean = service.getFileByName(fileName);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(bean.contentType());

		return ResponseEntity.ok()
				       .headers(headers)
				       .contentLength(bean.file().length())
				       .body(new FileSystemResource(bean.file()));
	}
}
