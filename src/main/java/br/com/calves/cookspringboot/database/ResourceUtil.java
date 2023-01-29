package br.com.calves.cookspringboot.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by clezio on 06/11/22.
 */
public class ResourceUtil{

    private String dbHost;

    private String dbName;

    private String dbUser;

    private String dbPassword;

    private Integer dbPort;

    private DatabaseTypeEnum dbType;

    private static ResourceUtil instance = null;

    private Properties bundle;

    private ResourceUtil() {
        bundle = new Properties();
    }

    public static ResourceUtil getInstance(){
        if(instance == null){
            instance = new ResourceUtil();
        }
        return instance;
    }

    public void loadProperties(String fullPath) throws IOException {
        InputStream is = null;
        try{
            is = new java.io.FileInputStream(fullPath);
            bundle.load(is);
            String url = this.bundle.getProperty("spring.datasource.url");
            
            Pattern p = Pattern.compile("(.*):(.*):\\/\\/(.*):(\\d+)\\/(.*)");
            Matcher m = p.matcher(url);
            if (m.find()) {
				this.dbType = DatabaseTypeEnum.fromValue(m.group(2));
	            this.dbHost = m.group(3);
	            this.dbName = m.group(5).split("\\?")[0];
	            this.dbPort = Integer.valueOf(m.group(4));
	            this.dbUser = this.bundle.getProperty("spring.datasource.username");
	            this.dbPassword = this.bundle.getProperty("spring.datasource.password");
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                    ;
                }
            }
        }
    }

    public String getDbHost() {
        return dbHost;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public Integer getDbPort() {
        return dbPort;
    }

    public DatabaseTypeEnum getDbType() {
        return dbType;
    }
}
