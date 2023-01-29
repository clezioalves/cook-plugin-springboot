package ${modelDesign.getPackageRepository()};

import org.springframework.data.repository.CrudRepository;

import ${modelDesign.getPackageModel()}.${modelDesign.getModelName()};

public interface ${modelDesign.getModelName()}Repository extends CrudRepository<${modelDesign.getModelName()}, ${modelDesign.getPrimaryKey().getTypeJava()}> {

}