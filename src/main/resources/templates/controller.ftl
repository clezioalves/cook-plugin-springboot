package ${modelDesign.getPackageController()};

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ${modelDesign.getPackageDTO()}.${modelDesign.getModelName()}DTO;
import ${modelDesign.getPackageService()}.${modelDesign.getModelName()}Service;

@RestController
@RequestMapping("/${modelDesign.getModelNameVariableList()}")
public class ${modelDesign.getModelName()}Controller {

	@Autowired
	private ${modelDesign.getModelName()}Service ${modelDesign.getModelNameVariable()}Service;

	@GetMapping
	public ResponseEntity<Iterable<${modelDesign.getModelName()}DTO>> findAll${modelDesign.getModelName()}() {
		try {
			return new ResponseEntity<>(${modelDesign.getModelNameVariable()}Service.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<${modelDesign.getModelName()}DTO> get${modelDesign.getModelName()}ById(@PathVariable("id") Integer id) {
		Optional<${modelDesign.getModelName()}DTO> ${modelDesign.getModelNameVariable()}Data = ${modelDesign.getModelNameVariable()}Service.findById(id);
		if (${modelDesign.getModelNameVariable()}Data.isPresent()) {
			return new ResponseEntity<>(${modelDesign.getModelNameVariable()}Data.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<${modelDesign.getModelName()}DTO> create${modelDesign.getModelName()}(@RequestBody ${modelDesign.getModelName()}DTO ${modelDesign.getModelNameVariable()}DTO) {
		try {
			${modelDesign.getModelName()}DTO _${modelDesign.getModelNameVariable()} = ${modelDesign.getModelNameVariable()}Service.save(${modelDesign.getModelNameVariable()}DTO);
			return new ResponseEntity<>(_${modelDesign.getModelNameVariable()}, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<${modelDesign.getModelName()}DTO> update${modelDesign.getModelName()}(@PathVariable("id") Integer id, @RequestBody ${modelDesign.getModelName()}DTO ${modelDesign.getModelNameVariable()}DTO) {
		try {
			${modelDesign.getModelName()}DTO _${modelDesign.getModelNameVariable()} = ${modelDesign.getModelNameVariable()}Service.update(id, ${modelDesign.getModelNameVariable()}DTO);
			return new ResponseEntity<>(_${modelDesign.getModelNameVariable()}, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<HttpStatus> delete${modelDesign.getModelName()}(@PathVariable("id") Integer id) {
		try {
			${modelDesign.getModelNameVariable()}Service.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}