package br.com.calves.cookspringboot.database.structure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.calves.cookspringboot.cook.Helper;
import lombok.Data;

/**
 * Created by clezio on 17/01/2017.
 */
//@Data
public class ModelDesign {

    private String modelName;

    private String columnName;

    private String displayField;

    private List<Attribute> attributeList;

    private List<ModelDesign> manyToOneList;

    private List<ModelDesign> oneToManyList;

    private List<ModelDesign> oneToOneList;

    private List<ModelDesign> manyToManyList;

    private String packageRepository;
    
    private String packageService;
    
    private String packageModel;
    
    private String packageDTO;
    
    private String packageException;
    
    private String packageController;

    public ModelDesign(String modelName) {
        this.modelName = modelName;
        this.attributeList = new ArrayList<Attribute>();
        this.manyToOneList = new ArrayList<ModelDesign>();
        this.oneToManyList = new ArrayList<ModelDesign>();
        this.oneToOneList = new ArrayList<ModelDesign>();
        this.manyToManyList = new ArrayList<ModelDesign>();
    }

    public ModelDesign(String modelName, String columnName, Attribute attributePrimaryKey, String displayField) {
        this(modelName);
        this.columnName = columnName;
        this.attributeList.add(attributePrimaryKey);
        this.displayField = displayField;
    }

    public String getControllerName(){
        return getControllerSimpleName() + "Controller";
    }

    public String getControllerSimpleName(){
        return Helper.getInstance().pluralize(this.getModelName());
    }

    public String getModelNameHumanize(){
        return Helper.getInstance().humanize(Helper.getInstance().pluralize(this.getModelName()));
    }

    public String getModelNameHumanizeSingularize(){
        return Helper.getInstance().singularize(Helper.getInstance().humanize(
                Helper.getInstance().pluralize(this.getModelName()
                )));
    }

    public String getModelNameVariable(){
        return Character.toLowerCase(this.getModelName().charAt(0)) + this.getModelName().substring(1);
    }

    public String getResourceName() {
        return getControllerSimpleName().replaceAll("(?=[A-Z])","-").toLowerCase().substring(1);
    }

    public String getModelNameVariableList(){
        String modelName = this.getModelName();
        modelName = Helper.getInstance().pluralize(modelName.substring(0,1).toLowerCase() + "" + modelName.substring(1));
        return modelName;
    }

    public Attribute getPrimaryKey(){
        for(Attribute attribute : attributeList){
            if(attribute.getPrimaryKey()){
                return attribute;
            }
        }
        return null;
    }

    public Set<String> getListaModelImports(){
        Set<String> importList = new HashSet<String>();

        for(ModelDesign md : this.getManyToOneList()){
            importList.add(md.getModelName());
        }
        for(ModelDesign md : this.getOneToManyList()){
            importList.add(md.getModelName());
        }
        for(ModelDesign md : this.getOneToOneList()){
            importList.add(md.getModelName());
        }
        for(ModelDesign md : this.getManyToManyList()){
            importList.add(md.getModelName());
        }
        return importList;
    }
    
    public List<Attribute> getAttributeDatesList() {
        List<Attribute> attributeDatesList = new ArrayList<Attribute>();
        for(Attribute attribute : getAttributeList()){
            if(attribute.getType().toLowerCase().contains(TableDesign.DATE)){
                attributeDatesList.add(attribute);
            }
        }
        return attributeDatesList;
    }

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDisplayField() {
		return displayField;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	public List<Attribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<Attribute> attributeList) {
		this.attributeList = attributeList;
	}

	public List<ModelDesign> getManyToOneList() {
		return manyToOneList;
	}

	public void setManyToOneList(List<ModelDesign> manyToOneList) {
		this.manyToOneList = manyToOneList;
	}

	public List<ModelDesign> getOneToManyList() {
		return oneToManyList;
	}

	public void setOneToManyList(List<ModelDesign> oneToManyList) {
		this.oneToManyList = oneToManyList;
	}

	public List<ModelDesign> getOneToOneList() {
		return oneToOneList;
	}

	public void setOneToOneList(List<ModelDesign> oneToOneList) {
		this.oneToOneList = oneToOneList;
	}

	public List<ModelDesign> getManyToManyList() {
		return manyToManyList;
	}

	public void setManyToManyList(List<ModelDesign> manyToManyList) {
		this.manyToManyList = manyToManyList;
	}

	public String getPackageRepository() {
		return packageRepository;
	}

	public void setPackageRepository(String packageRepository) {
		this.packageRepository = packageRepository;
	}

	public String getPackageService() {
		return packageService;
	}

	public void setPackageService(String packageService) {
		this.packageService = packageService;
	}

	public String getPackageModel() {
		return packageModel;
	}

	public void setPackageModel(String packageModel) {
		this.packageModel = packageModel;
	}

	public String getPackageDTO() {
		return packageDTO;
	}

	public void setPackageDTO(String packageDTO) {
		this.packageDTO = packageDTO;
	}

	public String getPackageException() {
		return packageException;
	}

	public void setPackageException(String packageException) {
		this.packageException = packageException;
	}

	public String getPackageController() {
		return packageController;
	}

	public void setPackageController(String packageController) {
		this.packageController = packageController;
	}
    
	
}
