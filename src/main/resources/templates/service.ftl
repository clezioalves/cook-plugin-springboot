package ${modelDesign.getPackageService()};

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${modelDesign.getPackageDTO()}.${modelDesign.getModelName()}DTO;
import ${modelDesign.getPackageException()}.EntityNotFoundException;
import ${modelDesign.getPackageModel()}.${modelDesign.getModelName()};
import ${modelDesign.getPackageRepository()}.${modelDesign.getModelName()}Repository;

@Service
public class ${modelDesign.getModelName()}Service {

    @Autowired
    private ${modelDesign.getModelName()}Repository ${modelDesign.getModelNameVariable()}Repository;

    @Autowired
    private ModelMapper modelMapper;

    public Iterable<${modelDesign.getModelName()}DTO> findAll() {
        List<${modelDesign.getModelName()}DTO> ${modelDesign.getModelNameVariable()}DTOList = new ArrayList<>();
    	${modelDesign.getModelNameVariable()}Repository.findAll().forEach(${modelDesign.getModelNameVariable()} -> ${modelDesign.getModelNameVariable()}DTOList.add(convertEntityToDto(${modelDesign.getModelNameVariable()})));
        return ${modelDesign.getModelNameVariable()}DTOList;
    }

    public Optional<${modelDesign.getModelName()}DTO> findById(${modelDesign.getPrimaryKey().getTypeJava()} id) {
        Optional<${modelDesign.getModelName()}> ${modelDesign.getModelNameVariable()}Optional = ${modelDesign.getModelNameVariable()}Repository.findById(id);
        return ${modelDesign.getModelNameVariable()}Optional.map(this::convertEntityToDto);
    }

    @Transactional
    public ${modelDesign.getModelName()}DTO save(${modelDesign.getModelName()}DTO ${modelDesign.getModelNameVariable()}DTO) {
        ${modelDesign.getModelName()} ${modelDesign.getModelNameVariable()} = this.convertDtoToEntity(${modelDesign.getModelNameVariable()}DTO);
        return this.convertEntityToDto(${modelDesign.getModelNameVariable()}Repository.save(${modelDesign.getModelNameVariable()}));
    }

    @Transactional
    public void delete(${modelDesign.getPrimaryKey().getTypeJava()} id){
        ${modelDesign.getModelNameVariable()}Repository.deleteById(id);
    }

    @Transactional
    public ${modelDesign.getModelName()}DTO update(${modelDesign.getPrimaryKey().getTypeJava()} id, ${modelDesign.getModelName()}DTO ${modelDesign.getModelNameVariable()}DTO){
        ${modelDesign.getModelName()} saved${modelDesign.getModelName()} = ${modelDesign.getModelNameVariable()}Repository
            .findById(id).orElseThrow(() -> new EntityNotFoundException("${modelDesign.getModelName()}", id));
        ${modelDesign.getModelName()} ${modelDesign.getModelNameVariable()}ToUpdate = this.convertDtoToEntity(${modelDesign.getModelNameVariable()}DTO);

        <#list modelDesign.getAttributeList() as attribute>
        <#if !attribute.getPrimaryKey()>
        saved${modelDesign.getModelName()}.set${attribute.getNameVariable()?cap_first}(${modelDesign.getModelNameVariable()}ToUpdate.get${attribute.getNameVariable()?cap_first}());
        </#if>
        </#list>
        <#list modelDesign.getOneToManyList() as modelRelation>
        saved${modelDesign.getModelName()}.set${modelRelation.getModelNameVariableList()?cap_first}(${modelDesign.getModelNameVariable()}ToUpdate.get${modelRelation.getModelNameVariableList()?cap_first}());
        </#list>
        <#list modelDesign.getManyToOneList() as modelRelation>
        saved${modelDesign.getModelName()}.set${modelRelation.getColumnName()?cap_first}(${modelDesign.getModelNameVariable()}ToUpdate.get${modelRelation.getColumnName()?cap_first}());
        </#list>

        return this.convertEntityToDto(${modelDesign.getModelNameVariable()}Repository.save(saved${modelDesign.getModelName()}));
    }

    private ${modelDesign.getModelName()}DTO convertEntityToDto(${modelDesign.getModelName()} ${modelDesign.getModelNameVariable()}){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        ${modelDesign.getModelName()}DTO ${modelDesign.getModelNameVariable()}DTO = new ${modelDesign.getModelName()}DTO();
        ${modelDesign.getModelNameVariable()}DTO = modelMapper.map(${modelDesign.getModelNameVariable()}, ${modelDesign.getModelName()}DTO.class);
        return ${modelDesign.getModelNameVariable()}DTO;
    }

    private ${modelDesign.getModelName()} convertDtoToEntity(${modelDesign.getModelName()}DTO ${modelDesign.getModelNameVariable()}DTO){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        ${modelDesign.getModelName()} ${modelDesign.getModelNameVariable()} = new ${modelDesign.getModelName()}();
        ${modelDesign.getModelNameVariable()} = modelMapper.map(${modelDesign.getModelNameVariable()}DTO, ${modelDesign.getModelName()}.class);
        return ${modelDesign.getModelNameVariable()};
    }
}