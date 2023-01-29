package br.com.calves.cookspringboot.database;

import java.util.Arrays;

public enum DatabaseTypeEnum {

	POSTGRES("postgresql"), MYSQL("mysql"), SQL_SERVER("sqlserver");

	public String value;

	DatabaseTypeEnum(String value) {
		this.value = value;
	}

	public static DatabaseTypeEnum fromValue(String value) {
		return Arrays.stream(values()).filter(e -> e.value.contentEquals(value)).findFirst().orElse(null);
	}

}
