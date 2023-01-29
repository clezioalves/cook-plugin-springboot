package br.com.calves.cookspringboot.database.structure;

import java.util.ArrayList;
import java.util.List;

import br.com.calves.cookspringboot.cook.Helper;
import lombok.Data;

/**
 * Created by clezio on 19/08/16.
 */
@Data
public class TableDesign {

    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String DATE = "date";

    private String name;
    
    private String packageModel;
    
    private String packageDTO;

    private String displayField;

    private Boolean timestamps;

    private List<Attribute> attributeList;

    private List<ForeingKey> manyToOneList;

    private List<ForeingKey> oneToManyList;

    private List<ForeingKey> oneToOneList;

    private List<ForeingKey> manyToManyList;

    public TableDesign(String name) {
        this.name = name;
        this.attributeList = new ArrayList<Attribute>();
        this.manyToOneList = new ArrayList<ForeingKey>();
        this.oneToManyList = new ArrayList<ForeingKey>();
        this.oneToOneList = new ArrayList<ForeingKey>();
        this.manyToManyList = new ArrayList<ForeingKey>();
    }

    public Attribute getPrimaryKey(){
        for(Attribute attribute: this.getAttributeList()){
            if(attribute.getPrimaryKey()){
                return attribute;
            }
        }
        return null;
    }

    public String getNameModelize(){
        return Helper.getInstance().modelize(this.getName());
    }

    public String getNameSingularize(){
        return Helper.getInstance().singularize(this.getName());
    }

    public List<RuleAttributeName> getRuleAttributeNameList() {
        List<RuleAttributeName> attributeNameList = new ArrayList<RuleAttributeName>();
        for(Attribute attribute : this.getAttributeList()){
            if(attribute.getName().equals(CREATED_AT) || attribute.getName().equals(UPDATED_AT)){
                continue;
            }
            StringBuilder rules = new StringBuilder();
            String attributeName = null;
            ForeingKey foreingKey = this.getForeingKeyByNameColumn(attribute.getName());
            if(attribute.getRequired() && !attribute.getPrimaryKey()){
                attributeName = attribute.getName();
                if(foreingKey != null){
                    attributeName = foreingKey.getTableNameVariable();
                }
                rules.append("required|");
            }
            if(attribute.getType().equalsIgnoreCase(Attribute.VARCHAR)){
                rules.append("max:"+attribute.getMaxLenght()+"|");
            }

            if(attributeName != null && rules.length() > 0){
                attributeNameList.add(new RuleAttributeName(attributeName, rules.substring(0,rules.length() - 1).toString()));
            }

        }
        return attributeNameList;
    }

    public List<String> getFillableAttributeNameList() {
        List<String> attributeNameList = new ArrayList<String>();
        for(Attribute attribute : this.getAttributeList()){
            if(attribute.getName().equals(CREATED_AT) ||
                    attribute.getName().equals(UPDATED_AT) ||
                    this.getForeingKeyByNameColumn(attribute.getName()) != null ||
                    attribute.getPrimaryKey()){
                continue;
            }
            attributeNameList.add(attribute.getName());
        }
        return attributeNameList;
    }

    public List<Attribute> getAttributeDatesList() {
        List<Attribute> attributeDatesList = new ArrayList<Attribute>();
        for(Attribute attribute : getAttributeList()){
        	if(attribute.getType().toLowerCase().contains(DATE)){
                attributeDatesList.add(attribute);
            }
        }
        return attributeDatesList;
    }



    private ForeingKey getForeingKeyByNameColumn(String columnName){
        for(ForeingKey foreingKey : this.getManyToOneList()){
            if(columnName.equals(foreingKey.getColumnName())){
                return foreingKey;
            }
        }
        for(ForeingKey foreingKey : this.getOneToOneList()){
            if(columnName.equals(foreingKey.getColumnName())){
                return foreingKey;
            }
        }
        return null;
    }

	public boolean isRequiredAttribute() {
		return getAttributeList().stream().filter(a -> a.isRequiredAttribute()).findAny().isPresent();
	}
	
	public boolean isDefineSize() {
		return getAttributeList().stream().filter(a -> a.isDefineSize()).findAny().isPresent();
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableDesign that = (TableDesign) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TableDesign{" +
                "name='" + name + '\'' +
                '}';
    }
}
