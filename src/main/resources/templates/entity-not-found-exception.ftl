package ${modelDesign.getPackageException()};

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String entityName, Object entityId) {
		super(entityName + " " + entityId + " does not exist.");
	}
}
