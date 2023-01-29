package br.com.calves.cookspringboot.database.structure;

import br.com.calves.cookspringboot.cook.Helper;

/**
 * Created by clezio on 19/08/16.
 */
public class Attribute {

	public static final String VARCHAR = "varchar";

	private String name;

	private String type;

	private Integer maxLenght;

	private Boolean required;

	private Boolean primaryKey;

	private ForeingKey foreingKey;

	private Boolean displayField;

	public Attribute(String name, String type, Integer maxLenght, Boolean required) {
		this.name = name;
		this.type = type;
		this.maxLenght = maxLenght;
		this.required = required;
		this.primaryKey = Boolean.FALSE;
	}

	public Attribute(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getTypeJava() {
		switch (type) {
		case "BIT":
			return "Boolean";
		case "VARCHAR":
			return "String";
		case "SMALLINT":
		case "INTEGER":
		case "INT":
			return "Integer";
		case "BIGINT":
			return "Long";
		case "REAL":
		case "FLOAT":
		case "DOUBLE":
			return "BigDecimal";
		case "DATE":
		case "TIMESTAMP":
		case "DATETIME":
			return "Date";
		case "BLOB":
			return "java.sql.Blob";
		default:
			break;
		}
		System.out.println("Datatype for " + type + " not found");
		return null;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMaxLenght() {
		return maxLenght;
	}

	public void setMaxLenght(Integer maxLenght) {
		this.maxLenght = maxLenght;
	}

	public Boolean getRequired() {
		return required;
	}

	public Boolean isRequiredAttribute() {
		return !getPrimaryKey() && getRequired();
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public ForeingKey getForeingKey() {
		return foreingKey;
	}

	public void setForeingKey(ForeingKey foreingKey) {
		this.foreingKey = foreingKey;
	}

	public boolean isForeingKey() {
		return this.getForeingKey() != null;
	}

	public Boolean getDisplayField() {
		return displayField;
	}

	public void setDisplayField(Boolean displayField) {
		this.displayField = displayField;
	}

	public String getNameHumanize() {
		return Helper.getInstance().humanize(this.getName());
	}

	public Boolean isDateType() {
		return getType().equalsIgnoreCase("date");
	}

	public String getNameVariable() {
		return Character.toLowerCase(this.getNameModelize().charAt(0)) + this.getNameModelize().substring(1);
	}

	public String getNameModelize() {
		return Helper.getInstance().modelize(this.getName());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Attribute attribute = (Attribute) o;

		return name != null ? name.equals(attribute.name) : attribute.name == null;

	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public Boolean isDefineSize() {
		return this.getMaxLenght() != null && this.getMaxLenght() > 0 && this.getTypeJava().equals("String");
	}
}
