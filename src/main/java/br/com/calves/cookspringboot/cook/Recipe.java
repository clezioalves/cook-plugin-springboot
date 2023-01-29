package br.com.calves.cookspringboot.cook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.calves.cookspringboot.database.DatabaseFactory;
import br.com.calves.cookspringboot.database.IDatabase;
import br.com.calves.cookspringboot.database.ResourceUtil;
import br.com.calves.cookspringboot.database.structure.Attribute;
import br.com.calves.cookspringboot.database.structure.ForeingKey;
import br.com.calves.cookspringboot.database.structure.ModelDesign;
import br.com.calves.cookspringboot.database.structure.TableDesign;
import br.com.calves.cookspringboot.utils.FileUtilPlugin;
import br.com.calves.cookspringboot.utils.PrintUtilPlugin;
import cook.core.FreemarkerWrapper;
import cook.core.IFCook;
import cook.core.ResultProcess;
import cook.core.ui.Screen;
import cook.util.FileUtil;
import cook.util.PrintUtil;

/**
 * Created by clezio on 08/08/16.
 */
public class Recipe implements IFCook {

	private static final String PUBLIC_STATIC_VOID_MAIN = "public static void main";
	public static final String MODEL = "model";
	public static final String CONTROLLER_REST = "controller-rest";
	public static final String CONTROLLER = "controller";
	public static final String REPOSITORY = "repository";
	public static final String SERVICE = "service";
	public static final String TEMPLATE = "template";

	public static final String DATABASE_CONFIG_FILE = "src/main/resources/application.properties";
	public static final String Q = "q";
	public static final String Y = "y";
	public static final String N = "n";
	public static final String ENTER_A_NUMBER_FROM_THE_LIST_ABOVE_OR_Q_TO_EXIT = "Enter a number from the list above, or 'q' to exit: ";
	public static final String POSSIBLE_MODELS_BASED_ON_CURRENT_DATABASE_DEFINED_IN_FILE_ENV = "Possible Models based on current database defined in file \""
			+ DATABASE_CONFIG_FILE + "\"";
	public static final String POSSIBLE_CONTROLLERS_BASED_ON_CURRENT_DATABASE_DEFINED_IN_FILE_ENV = "Possible Controllers based on current database defined in file \""
			+ DATABASE_CONFIG_FILE + "\"";
	public static final String POSSIBLE_SERVICES_BASED_ON_CURRENT_DATABASE_DEFINED_IN_FILE_ENV = "Possible Services based on current database defined in file \""
			+ DATABASE_CONFIG_FILE + "\"";
	public static final String POSSIBLE_TEMPLATES_BASED_ON_CURRENT_DATABASE_DEFINED_IN_FILE_ENV = "Possible Templates based on current database defined in file \""
			+ DATABASE_CONFIG_FILE + "\"";
	public static final String INVALID_OPTION = "Invalid option";
	public static final String TABLE = "TABLE";
	public static final String MODEL_CREATED_SUCCESSFULLY = "Model created successfully!";
	public static final String SERVICE_CREATED_SUCCESSFULLY = "Service created successfully!";
	public static final String CONTROLLER_CREATED_SUCCESSFULLY = "Controller created successfully!";
	public static final String REPOSITORY_CREATED_SUCCESSFULLY = "Repository created successfully!";
	public static final String TEMPLATES_CREATED_SUCCESSFULLY = "Templates created successfully!";
	public static final String OPERATION_CANCELED = "Operation canceled!";
	public static final String PLEASE_CONFIRM_THE_FOLLOWING_ASSOCIATIONS = "Please confirm the following associations:";
	public static final String PLEASE_SELECT_DISPLAY_FIELD = "A displayField could not be automatically detected, so would you like to choose one?";
	public static final String BELONGS_TO_LABEL = " belongsTo ";
	public static final String HAS_ONE_LABEL = " hasOne ";
	public static final String HAS_MANY_LABEL = " hasMany ";
	public static final String HAS_AND_BELONGS_TO_MANY_LABEL = " hasAndBelongsToMany ";
	public static final String NONE = "None";
	public static final String LANGUAGE_NAME_IN_TABLE = "Language name in table?";
	public static final String ESCAPE = "\\";
	public static final String THE_FILENAME = "The filename \"";
	public static final String ALREADY_EXISTS_REPLACE_THE_EXISTING_FILE_Y_N = "\" already exists. Replace the existing file? (y/n)";
	public static final String PATH_SRC = "src" + File.separator + "main" + File.separator + "java";
	public static final String PATH_MODEL = "model";
	public static final String PATH_DTO = "dto";
	public static final String PATH_EXCEPTION = "exception";
	public static final String PATH_CONTROLLER = "controller";
	public static final String PATH_REPOSITORY = "repository";
	public static final String PATH_SERVICE = "service";
	public static final String PATH_REQUESTS = "Http" + File.separator + "Requests";
	public static final String PATH_CONFIG = "config";
	public static final String CONTROLLER_SUFIX = "Controller";
	public static final String REPOSITORY_SUFIX = "Repository";
	public static final String SERVICE_SUFIX = "Service";
	public static final String REGEX_EXTRACT_RELATIONSHIPS = "public function (\\w+).*" + System.lineSeparator()
			+ ".*(->(\\w+)\\('(.*\\\\([\\w+]+))').*;";
	public static final String REGEX_EXTRACT_PRIMARYKEY = ".*primaryKey.*'(.*)'";
	public static final String REGEX_EXTRACT_DISPLAY_FIELD = ".*private\\s.*\\s(nome|name);";
	public static final String BELONGS_TO = "belongsTo";
	public static final String BELONGS_TO_MANY = "belongsToMany";
	public static final String HAS_ONE = "hasOne";
	public static final String HAS_MANY = "hasMany";
	public static final String NOME = "nome";
	public static final String NAME = "name";
	private static final String CREATED = "Created";
	private static final String UPDATED = "Updated";
	private static final String PATH_VENDOR = "vendor";
	private static final String PATH_LARAVEL_COLLECTIVE = "laravelcollective";
	private static final String PATH_HTML = "html";
	private static final String PATH_RESOURCES = "resources";
	private static final String PATH_VIEW = "views";
	public static final String RESOURCES_TEMPLATE_BOOTSTRAP1_ZIP = "https://github.com/clezioalves/cook-plugin-laravel/raw/master/others-resources/template-bootstrap1.zip";
	public static final String DOWNLOAD_ADDITIONAL_RESOURCE = "Downloading additional resource";
	private static final java.lang.String REGEX_EXTRACT_ITEM_MENU = "(.*)(<!--.*inject:itemMenu.*-->)";

	private IDatabase database;

	private String action;

	private String path;

	private Integer success;

	private List<String> changeHistory;

	public static void main(String args[]) throws Exception {
		Helper.getInstance().configureInflector(Inflector.PT_BR);
		Recipe cook = new Recipe();
		cook.printHeader();
		cook.start(args);
		cook.validDirectory();
		// Executa o procedimento
		ResultProcess out = cook.cook();

		if (!out.isERROR()) {

			PrintUtil.outn("");
			PrintUtil.outn(out.getMESSAGE());
			cook.end();

		} else {

			PrintUtil.outn("");
			PrintUtil
					.outn(PrintUtil.getRedFont() + "--- ERRO ----------------------------------------------------------"
							+ PrintUtil.getColorReset());
			PrintUtil.outn("" + out.getMESSAGE());
			PrintUtil
					.outn(PrintUtil.getRedFont() + "-------------------------------------------------------------------"
							+ PrintUtil.getColorReset());
			PrintUtil.outn("");
			PrintUtil.outn(PrintUtil.getRedFont() + "Ho no!!! The plugin did not run correctly. Try again."
					+ PrintUtil.getColorReset());
			PrintUtil.outn("");
			return;
		}
		Screen.end();

	}

	public Recipe() {
		changeHistory = new ArrayList<String>();
	}

	public String getVersion() {
		return "1.0";
	}

	public void printHeader() {
		// To create => https://patorjk.com/software/taag/#p=display&f=Ogre&t=Springboot
		PrintUtil.outn(PrintUtil.getGreenFont() + "" + PrintUtil.getColorReset());
		PrintUtil.outn(PrintUtil.getGreenFont() + " __            _             _                 _   " + PrintUtil.getColorReset());
		PrintUtil.outn(PrintUtil.getGreenFont() + "/ _\\_ __  _ __(_)_ __   __ _| |__   ___   ___ | |_ " + PrintUtil.getColorReset());
		PrintUtil.outn(PrintUtil.getGreenFont() + "\\ \\| '_ \\| '__| | '_ \\ / _` | '_ \\ / _ \\ / _ \\| __|" + PrintUtil.getColorReset());
		PrintUtil.outn(PrintUtil.getGreenFont() + "_\\ \\ |_) | |  | | | | | (_| | |_) | (_) | (_) | |_ " + PrintUtil.getColorReset());
		PrintUtil.outn(PrintUtil.getGreenFont() + "\\__/ .__/|_|  |_|_| |_|\\__, |_.__/ \\___/ \\___/ \\__|" + PrintUtil.getColorReset());
		PrintUtil.outn(PrintUtil.getGreenFont() + "   |_|                 |___/                       " + PrintUtil.getColorReset());
		PrintUtil.outn(PrintUtil.getGreenFont() + "" + PrintUtil.getColorReset());
		PrintUtil.outn("Springboot plugin generator. Version " + getVersion());
		PrintUtil.outn("");
	}

	public void printHelp() {
		PrintUtil.outn("Use: cook springboot [action]");
		PrintUtil.outn("");
		PrintUtil.outn("Available actions:");
		PrintUtil.outn("~~~~~~~~~~~~~~~~~~");
		PrintUtil.outn(MODEL);
		PrintUtil.outn(REPOSITORY);
		PrintUtil.outn(SERVICE);
//		PrintUtil.outn(CONTROLLER_REST);
		PrintUtil.outn(CONTROLLER);
//		PrintUtil.outn(TEMPLATE);
	}

	public boolean start(String[] param) {
		// Valid in param
		if (param.length == 1 || param[1].equals("")) {
			printHelp(); // show help
			PrintUtil.outn("");
			return false;
		}
		if (!(param[1].toLowerCase().equals(MODEL) || param[1].toLowerCase().equals(CONTROLLER_REST)
				|| param[1].toLowerCase().equals(REPOSITORY) || param[1].toLowerCase().equals(CONTROLLER)
				|| param[1].toLowerCase().equals(SERVICE) || param[1].toLowerCase().equals(TEMPLATE))) {
			printHelp();
			PrintUtil.outn("");
			return false;
		}

		this.action = param[1].toLowerCase();
		return true;
	}

	public boolean validDirectory() {
		return validDirectory(FileUtil.getPromptPath());
	}

	private boolean validDirectory(String path) {
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		this.path = path;
		boolean valid = true;
		// get the path of user execute script
		if (!new File(path + DATABASE_CONFIG_FILE).exists()) {
			String directory = PrintUtil.inString("Enter the full path to the application: ");
			if (directory.length() > 0) {
				PrintUtil.outn("Invalid directory");
				valid = validDirectory(directory);
			} else {
				valid = false;
			}
		}
		return valid;
	}

	public ResultProcess cook() {
		ResultProcess resultProcess = new ResultProcess();
		ResourceUtil conf = ResourceUtil.getInstance();
		try {
			conf.loadProperties(this.path + DATABASE_CONFIG_FILE);
			this.database = new DatabaseFactory().getDataBase(conf.getDbType(), conf.getDbHost(), conf.getDbPort(),
					conf.getDbName(), conf.getDbUser(), conf.getDbPassword());

			if (this.action.equals(MODEL)) {
				resultProcess = this.buildModel();
			} else if (this.action.equals(REPOSITORY)) {
				resultProcess = this.buildRepository();
			} else if (this.action.equals(SERVICE)) {
				resultProcess = this.buildService();
			} else if (this.action.equals(CONTROLLER)) {
				resultProcess = this.buildController(Boolean.FALSE);
			} else if (this.action.equals(CONTROLLER_REST)) {
				resultProcess = this.buildController(Boolean.TRUE);
			} else if (this.action.equals(TEMPLATE)) {
				resultProcess = this.buildTemplate();
			} else {
				resultProcess.setResultProcess(ResultProcess.ERROR, "Action not found");
			}
			
			if (!resultProcess.isERROR()) {
				printMissingDependency();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.success = resultProcess.getINFO();
		return resultProcess;
	}

	private void printMissingDependency() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try (InputStream is = new FileInputStream(new File(this.path + File.separator + "pom.xml"))) {

			// parse XML file
			DocumentBuilder db = dbf.newDocumentBuilder();

			// read from a project's resources folder
			Document doc = db.parse(is);

			if (doc.hasChildNodes()) {
				int countDeps = 0;
				List<String> lines = new ArrayList<String>();
				if (!findDependency(doc.getChildNodes(), "javax.persistence-api")) {
					countDeps++;
					lines.add("<dependency>");
					lines.add("\t<groupId>javax.persistence</groupId>");
					lines.add("\t<artifactId>javax.persistence-api</artifactId>");
					lines.add("\t<version>2.2</version>");
					lines.add("</dependency>");
					lines.add("");
				}
				if (!findDependency(doc.getChildNodes(), "modelmapper")) {
					countDeps++;
					lines.add("<dependency>");
					lines.add("\t<groupId>org.modelmapper</groupId>");
					lines.add("\t<artifactId>modelmapper</artifactId>");
					lines.add("\t<version>3.1.0</version>");
					lines.add("</dependency>");
					lines.add("");
				}
				if (!findDependency(doc.getChildNodes(), "validation-api")) {
					countDeps++;
					lines.add("<dependency>");
					lines.add("\t<groupId>javax.validation</groupId>");
					lines.add("\t<artifactId>validation-api</artifactId>");
					lines.add("\t<version>2.0.1.Final</version>");
					lines.add("</dependency>");
					lines.add("");
				}
				
				if (countDeps > 0) {
					PrintUtilPlugin.outn("");
					PrintUtilPlugin.outn("Add dependenc" + (countDeps > 1 ? "ies" : "y") + " to your project:");
					lines.forEach(l -> PrintUtilPlugin.printLineYellow(l));
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public void end() {
		if (success.equals(1)) {
			PrintUtil.outn("");
			for (String h : changeHistory) {
				PrintUtilPlugin.printLineYellow(h);
			}
		}
	}

	public ResultProcess buildModel() {
		ResultProcess out = new ResultProcess();
		try {
			configureInflector();
			List<String> tableList = getTableList();

			PrintUtilPlugin.outn(POSSIBLE_MODELS_BASED_ON_CURRENT_DATABASE_DEFINED_IN_FILE_ENV);
			// Model list
			int cont = 0;
			for (String table : tableList) {
				PrintUtilPlugin.printLineYellow("[" + (cont++) + "] " + this.getModelName(table));
			}
			String option = this.inputOptions(cont);
			String tableName = tableList.get(Integer.valueOf(option));
			TableDesign tableDesign = getTableDesign(tableName);

			//
			String fileName = getFileNameModelByTableName(tableDesign.getName());
			Boolean generateFile = checkConfirmFileExists(fileName);
			if (generateFile) {
				FreemarkerWrapper.getInstance().addVar("tableDesign", tableDesign);
				
				// Model
				String content = FreemarkerWrapper.getInstance().parseTemplate("model.ftl");
				FileUtilPlugin.saveToPath(fileName, content);
				this.updateHistory(CREATED, fileName);
				
				// DTO
				content = FreemarkerWrapper.getInstance().parseTemplate("dto.ftl");
				FileUtilPlugin.saveToPath(getFileNameDTO(tableName), content);
				
				out.setResultProcess(ResultProcess.SUCESS, MODEL_CREATED_SUCCESSFULLY);
			} else {
				out.setResultProcess(ResultProcess.WARNING, OPERATION_CANCELED);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			out.setResultProcess(ResultProcess.ERROR, "Error, " + ex.getMessage());
		}
		return out;
	}

	public ResultProcess buildController(Boolean resource) {
		ResultProcess out = new ResultProcess();
		try {
			configureInflector();
			List<String> tableList = getTableListWithService();

			PrintUtilPlugin.outn(POSSIBLE_CONTROLLERS_BASED_ON_CURRENT_DATABASE_DEFINED_IN_FILE_ENV);
			// Controllers list
			int cont = 0;
			for (String table : tableList) {
				PrintUtilPlugin.printLineYellow("[" + (cont++) + "] " + this.getControllerName(table));
			}
			String option = this.inputOptions(cont);
			String tableName = tableList.get(Integer.valueOf(option));
			ModelDesign modelDesign = getModelDesign(tableName);

			String fileName = getFileNameController(tableName);
			Boolean generateFile = checkConfirmFileExists(fileName);
			if (generateFile) {
				FreemarkerWrapper.getInstance().addVar("modelDesign", modelDesign);
				String content = null;
				if (resource) {
					content = FreemarkerWrapper.getInstance().parseTemplate("controller-resource.ftl");
				} else {
					content = FreemarkerWrapper.getInstance().parseTemplate("controller.ftl");
				}
				FileUtilPlugin.saveToPath(fileName, content);
				this.updateHistory(CREATED, fileName);
				if (!resource) {
					FreemarkerWrapper.getInstance().addVar("modelDesignName", modelDesign.getModelName());
				}
				out.setResultProcess(ResultProcess.SUCESS, CONTROLLER_CREATED_SUCCESSFULLY);
			} else {
				out.setResultProcess(ResultProcess.WARNING, OPERATION_CANCELED);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			out.setResultProcess(ResultProcess.ERROR, "Error, " + ex.getMessage());
		}
		return out;
	}

	public ResultProcess buildRepository() {
		ResultProcess out = new ResultProcess();
		try {
			configureInflector();
			List<String> tableList = getTableListWithModel();

			PrintUtilPlugin.outn(POSSIBLE_CONTROLLERS_BASED_ON_CURRENT_DATABASE_DEFINED_IN_FILE_ENV);
			// Controllers list
			int cont = 0;
			for (String table : tableList) {
				PrintUtilPlugin.printLineYellow("[" + (cont++) + "] " + this.getRepositoryName(table));
			}
			String option = this.inputOptions(cont);
			String tableName = tableList.get(Integer.valueOf(option));
			ModelDesign modelDesign = getModelDesign(tableName);

			String fileName = getFileNameRepository(tableName);
			Boolean generateFile = checkConfirmFileExists(fileName);
			if (generateFile) {
				FreemarkerWrapper.getInstance().addVar("modelDesign", modelDesign);
				String content = null;
				content = FreemarkerWrapper.getInstance().parseTemplate("repository.ftl");
				FileUtilPlugin.saveToPath(fileName, content);
				this.updateHistory(CREATED, fileName);
				out.setResultProcess(ResultProcess.SUCESS, REPOSITORY_CREATED_SUCCESSFULLY);
			} else {
				out.setResultProcess(ResultProcess.WARNING, OPERATION_CANCELED);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			out.setResultProcess(ResultProcess.ERROR, "Error, " + ex.getMessage());
		}
		return out;
	}

	public ResultProcess buildService() {
		ResultProcess out = new ResultProcess();
		try {
			configureInflector();
			List<String> tableList = getTableListWithRepository();

			PrintUtilPlugin.outn(POSSIBLE_SERVICES_BASED_ON_CURRENT_DATABASE_DEFINED_IN_FILE_ENV);
			// Controllers list
			int cont = 0;
			for (String table : tableList) {
				PrintUtilPlugin.printLineYellow("[" + (cont++) + "] " + this.getServiceName(table));
			}
			String option = this.inputOptions(cont);
			String tableName = tableList.get(Integer.valueOf(option));
			ModelDesign modelDesign = getModelDesign(tableName);

			String fileName = getFileNameService(tableName);
			Boolean generateFile = checkConfirmFileExists(fileName);
			if (generateFile) {

				// Exception
				FreemarkerWrapper.getInstance().addVar("modelDesign", modelDesign);
				String content = FreemarkerWrapper.getInstance().parseTemplate("entity-not-found-exception.ftl");
				FileUtilPlugin.saveToPath(getFileNameException(tableName), content);

				// Service
				FreemarkerWrapper.getInstance().addVar("modelDesign", modelDesign);
				content = FreemarkerWrapper.getInstance().parseTemplate("service.ftl");
				FileUtilPlugin.saveToPath(fileName, content);

				this.updateHistory(CREATED, fileName);
				out.setResultProcess(ResultProcess.SUCESS, SERVICE_CREATED_SUCCESSFULLY);
			} else {
				out.setResultProcess(ResultProcess.WARNING, OPERATION_CANCELED);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			out.setResultProcess(ResultProcess.ERROR, "Error, " + ex.getMessage());
		}
		return out;
	}

	public ResultProcess buildTemplate() {
		ResultProcess out = new ResultProcess();
		try {
			Boolean fileLaravelCollectiveExists = checkFileExists(getFileNameLaravelCollective());
			if (!fileLaravelCollectiveExists) {
				String message = this.getMessageComponentLaravelCollective();
				out.setResultProcess(ResultProcess.ERROR, message);
			} else {
				configureInflector();
				List<String> tableList = getTableListWithModel();

				PrintUtilPlugin.outn(POSSIBLE_TEMPLATES_BASED_ON_CURRENT_DATABASE_DEFINED_IN_FILE_ENV);
				// Controllers list
				int cont = 0;
				for (String table : tableList) {
					PrintUtilPlugin.printLineYellow("[" + (cont++) + "] " + this.getModelName(table));
				}
				String option = this.inputOptions(cont);
				String tableName = tableList.get(Integer.valueOf(option));
				ModelDesign modelDesign = getModelDesign(tableName);
				FreemarkerWrapper.getInstance().addVar("modelDesign", modelDesign);
				FreemarkerWrapper.getInstance().addVar("lang", Helper.getInstance().getLang());
				String content = null;

				List<TemplateViewEnum> templateViewEnumList = Arrays.asList(TemplateViewEnum.values());
				for (TemplateViewEnum templateViewEnum : templateViewEnumList) {
					String fileName = getFileNameTemplate(modelDesign.getResourceName(),
							templateViewEnum.getValor() + ".php");
					Boolean generateFile = checkConfirmFileExists(fileName);
					if (generateFile) {
						content = FreemarkerWrapper.getInstance().parseTemplate(templateViewEnum.getValor() + ".ftl");
						FileUtilPlugin.saveToPath(fileName, content);
						this.updateHistory(CREATED, fileName);
					}
				}

				String fileNameAppBlade = getFileNameAppBlade();
				if (!FileUtil.fileExist(fileNameAppBlade)) {
					PrintUtilPlugin.outn(DOWNLOAD_ADDITIONAL_RESOURCE);
					FileUtilPlugin.importTemplate(RESOURCES_TEMPLATE_BOOTSTRAP1_ZIP, this.path);
				}

				createItemMenu(modelDesign, fileNameAppBlade);

				out.setResultProcess(ResultProcess.SUCESS, TEMPLATES_CREATED_SUCCESSFULLY);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			out.setResultProcess(ResultProcess.ERROR, "Error, " + ex.getMessage());
		}
		return out;
	}

	private String getFileNameAppBlade() {
		return this.path + PATH_RESOURCES + File.separator + PATH_VIEW + File.separator + "app.blade.php";
	}

	private String getFileNameTemplate(String resourceName, String template) {
		String controllerPath = this.path + PATH_RESOURCES + File.separator + PATH_VIEW + File.separator + resourceName
				+ File.separator;
		return controllerPath + template;
	}

	private String getMessageComponentLaravelCollective() {
		StringBuilder sb = new StringBuilder(
				"Please install component Forms & HTML in https://laravelcollective.com or follow the below steps before to continue:");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("Step 1 - Run this command:");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("\tcomposer require \"laravelcollective/html\"");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("Step 2 - Open config/app.php and add this line to service providers array:");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("\tCollective\\Html\\HtmlServiceProvider::class,");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("Step 3 - Next, add following line of code to aliases array.");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("\t'Form' => Collective\\Html\\FormFacade::class,");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("\t'Html' => Collective\\Html\\HtmlFacade::class,");
		return sb.toString();
	}

	private void createItemMenu(ModelDesign modelDesign, String fileNameAppBlade) throws Exception {
		String menuContent = readFile(fileNameAppBlade);
		Pattern p = Pattern.compile(REGEX_EXTRACT_ITEM_MENU);
		Matcher m = p.matcher(menuContent);
		if (m.find()) {
			String route = "url('" + modelDesign.getResourceName() + "')";
			if (!menuContent.contains(route)) {
				String injectItemMenuSection = m.group(1) + m.group(2);
				FreemarkerWrapper.getInstance().addVar("padLeft", m.group(1));
				FreemarkerWrapper.getInstance().addVar("route", route);
				FreemarkerWrapper.getInstance().addVar("humanizeName", modelDesign.getModelNameHumanize());
				String contentItemMenu = FreemarkerWrapper.getInstance().parseTemplate("item-menu.ftl");
				menuContent = menuContent.replace(injectItemMenuSection, contentItemMenu);
				FileUtil.saveToPath(fileNameAppBlade, menuContent);
				updateHistory(UPDATED, fileNameAppBlade);
			}
		}
	}

	private Boolean checkConfirmFileExists(String fileName) {
		Boolean generateFile = true;
		if (checkFileExists(fileName)) {
			String[] simpleName = fileName.split(ESCAPE + File.separator);
			PrintUtilPlugin.printLineYellowGreenYellow(THE_FILENAME, simpleName[simpleName.length - 1],
					ALREADY_EXISTS_REPLACE_THE_EXISTING_FILE_Y_N);
			generateFile = this.inputConfirm(N);
		}
		return generateFile;
	}

	private boolean checkFileExists(String fileName) {
		return FileUtil.fileExist(fileName);
	}

	private void configureInflector() {
		PrintUtilPlugin.outn(LANGUAGE_NAME_IN_TABLE);
		PrintUtilPlugin.printLineYellow("[0] " + Inflector.EN);
		PrintUtilPlugin.printLineYellow("[1] " + Inflector.PT_BR);
		if (this.inputOptions(2).equals("0")) {
			Helper.getInstance().configureInflector(Inflector.EN);
		} else {
			Helper.getInstance().configureInflector(Inflector.PT_BR);
		}
	}

	private TableDesign getTableDesign(String tableName) throws Exception {
		String option = null;
		TableDesign tableDesign = new TableDesign(tableName);
		tableDesign.setPackageModel(getPackageBase() + "." + PATH_MODEL);
		tableDesign.setPackageDTO(getPackageBase() + "." + PATH_DTO);
		tableDesign.setAttributeList(getAttributeList(tableName));
		tableDesign.setTimestamps(getTimestamps(tableDesign));
		PrintUtilPlugin.outn(PLEASE_CONFIRM_THE_FOLLOWING_ASSOCIATIONS);
		// ManyToOneList
		for (ForeingKey fk : this.getManyToOneList(tableName)) {
			PrintUtilPlugin.printLineYellowGreenYellow(this.getModelName(tableName), BELONGS_TO_LABEL,
					this.getModelName(fk.getTableName()) + "? (y/n)");
			if (this.inputConfirm(Y)) {
				tableDesign.getAttributeList().removeIf(a -> a.getName().equals(fk.getColumnName()));
				tableDesign.getManyToOneList().add(fk);
			}
		}

		// OneToManyList
		for (ForeingKey fk : this.getOneToManyAndManyToManyList(tableName)) {
			if (fk.getManyToOne() != null) {
				PrintUtilPlugin.printLineYellowGreenYellow("[0] " + this.getModelName(tableName),
						HAS_AND_BELONGS_TO_MANY_LABEL, this.getModelName(fk.getManyToOne().getTableName()));
				PrintUtilPlugin.printLineYellowGreenYellow("[1] " + this.getModelName(tableName), HAS_MANY_LABEL,
						this.getModelName(fk.getTableName()));
				PrintUtilPlugin.printLineYellow("[2] " + NONE);
				option = this.inputOptions(3);
				if (Integer.valueOf(option) == 0) {
					tableDesign.getManyToManyList().add(fk);
				} else if (Integer.valueOf(option) == 1) {
					tableDesign.getOneToManyList().add(fk);
				}
			} else {
				PrintUtilPlugin.printLineYellowGreenYellow("[0] " + this.getModelName(tableName), HAS_MANY_LABEL,
						this.getModelName(fk.getTableName()));
				PrintUtilPlugin.printLineYellowGreenYellow("[1] " + this.getModelName(tableName), HAS_ONE_LABEL,
						this.getModelName(fk.getTableName()));
				PrintUtilPlugin.printLineYellow("[2] " + NONE);
				option = this.inputOptions(3);
				if (Integer.valueOf(option) == 0) {
					tableDesign.getOneToManyList().add(fk);
				} else if (Integer.valueOf(option) == 1) {
					tableDesign.getOneToOneList().add(fk);
				}
			}
		}
		String displayField = null;
		for (Attribute attribute : tableDesign.getAttributeList()) {
			if (attribute.getName().equalsIgnoreCase(NAME) || attribute.getName().equalsIgnoreCase(NOME)) {
				displayField = attribute.getName();
				break;
			}
		}
		if (displayField == null) {
			PrintUtilPlugin.outn(PLEASE_SELECT_DISPLAY_FIELD);
			int cont = 0;
			for (Attribute attribute : tableDesign.getAttributeList()) {
				PrintUtilPlugin.printLineYellow("[" + (cont++) + "] " + attribute.getName());
			}
			option = this.inputOptions(tableDesign.getAttributeList().size());
			displayField = tableDesign.getAttributeList().get(Integer.valueOf(option)).getName();
		}
		tableDesign.setDisplayField(displayField);
		return tableDesign;
	}

	private String getPackageBase() {
		String pathBaseCreation = getPathBaseCreation();
		if (!pathBaseCreation.isEmpty()) {
			pathBaseCreation = pathBaseCreation.replaceAll("(\\\\|\\/)", ".");
			return pathBaseCreation.substring(0, pathBaseCreation.length() - 1);
		}
		return "";
	}

	private Boolean getTimestamps(TableDesign tableDesign) {
		Boolean existsCreatedAt = Boolean.FALSE;
		Boolean existsUpdatedAt = Boolean.FALSE;
		for (Attribute attribute : tableDesign.getAttributeList()) {
			if (attribute.getName().equals(TableDesign.CREATED_AT)) {
				existsCreatedAt = Boolean.TRUE;
			} else if (attribute.getName().equals(TableDesign.UPDATED_AT)) {
				existsUpdatedAt = Boolean.TRUE;
			}
		}
		return existsCreatedAt && existsUpdatedAt;
	}

	private ModelDesign getModelDesign(String tableName) throws Exception {
		ModelDesign modelDesign = new ModelDesign(getModelName(tableName));
		modelDesign.setAttributeList(getAttributeControllerList(tableName));
		modelDesign.setPackageRepository(getPackageBase() + "." + PATH_REPOSITORY);
		modelDesign.setPackageService(getPackageBase() + "." + PATH_SERVICE);
		modelDesign.setPackageModel(getPackageBase() + "." + PATH_MODEL);
		modelDesign.setPackageDTO(getPackageBase() + "." + PATH_DTO);
		modelDesign.setPackageException(getPackageBase() + "." + PATH_EXCEPTION);
		modelDesign.setPackageController(getPackageBase() + "." + PATH_CONTROLLER);
		String modelContent = readFile(getFileNameModelByTableName(tableName));
		Matcher mDisplayField = Pattern.compile(REGEX_EXTRACT_DISPLAY_FIELD).matcher(modelContent);
		String displayField = "id";
		if (mDisplayField.find()) {
			displayField = mDisplayField.group(1);
		}
		modelDesign.setDisplayField(displayField);
		
		Matcher mClassName = Pattern.compile(".*private\\s(.*)\\s(.*);").matcher(modelContent);

		while (mClassName.find()) {
			String dataType = mClassName.group(1);
			String simpleNameModel = null;
			String relationType = null;
			String attributeName = mClassName.group(2);
			Matcher mDataType = Pattern.compile(".*<(.*)>.*|(.*)").matcher(dataType);
			if (mDataType.find()) {
				simpleNameModel = Optional.ofNullable(mDataType.group(1)).orElse(mDataType.group(2));
				relationType = mDataType.group(1) != null ? HAS_MANY : BELONGS_TO;
			}

			String fileNameModelrelation = getFileNameModelByModelName(simpleNameModel);
			if (!FileUtil.fileExist(fileNameModelrelation)) {
				continue;
			}
			String primaryKey = "id";
			String modelContentAssociation = readFile(fileNameModelrelation);
			Pattern p2 = Pattern.compile(REGEX_EXTRACT_PRIMARYKEY);
			Matcher m2 = p2.matcher(modelContentAssociation);
			if (m2.find()) {
				primaryKey = m2.group(1);
			}

			mDisplayField = Pattern.compile(REGEX_EXTRACT_DISPLAY_FIELD).matcher(modelContent);
			displayField = "id";
			if (mDisplayField.find()) {
				displayField = mDisplayField.group(1);
			}

			Attribute attributePrimaryKey = new Attribute(primaryKey);
			attributePrimaryKey.setPrimaryKey(Boolean.TRUE);

			if (BELONGS_TO.equals(relationType)) {
				modelDesign.getManyToOneList()
						.add(new ModelDesign(simpleNameModel, attributeName, attributePrimaryKey, displayField));
			} else if (BELONGS_TO_MANY.equals(relationType)) {
				modelDesign.getManyToManyList()
						.add(new ModelDesign(simpleNameModel, attributeName, attributePrimaryKey, displayField));
			} else if (HAS_ONE.equals(relationType)) {
				modelDesign.getOneToOneList()
						.add(new ModelDesign(simpleNameModel, attributeName, attributePrimaryKey, displayField));
			} else if (HAS_MANY.equals(relationType)) {
				modelDesign.getOneToManyList()
						.add(new ModelDesign(simpleNameModel, attributeName, attributePrimaryKey, displayField));
			} else {
				continue;
			}
		}
		return modelDesign;
	}

	private String getModelName(String input) {
		return Helper.getInstance().modelize(input);
	}

	private String getModelDTOName(String input) {
		return Helper.getInstance().modelize(input) + "DTO";
	}

	private String getControllerName(String input) {
//		return Helper.getInstance().pluralize(this.getModelName(input)) + CONTROLLER_SUFIX;
		return Helper.getInstance().modelize(input) + CONTROLLER_SUFIX;
	}

	private String getRepositoryName(String input) {
		return Helper.getInstance().modelize(input) + REPOSITORY_SUFIX;
	}

	private String getServiceName(String input) {
		return Helper.getInstance().modelize(input) + SERVICE_SUFIX;
	}

	private String inputOptions(int numberMaxOptions) {
		String option = null;
		Boolean isValid = null;
		do {
			option = PrintUtilPlugin.inString(ENTER_A_NUMBER_FROM_THE_LIST_ABOVE_OR_Q_TO_EXIT);
			isValid = Q.equalsIgnoreCase(option) || (isNumber(option) && Integer.valueOf(option) < numberMaxOptions);
			if (!isValid) {
				PrintUtilPlugin.printLineRed(INVALID_OPTION);
			}
		} while (option.isEmpty() || !isValid);

		if (Q.equalsIgnoreCase(option)) {
			System.exit(0);
		}
		return option;
	}

	private Boolean inputConfirm(String defaultOption) {
		String option = null;
		Boolean isValid = null;
		do {
			option = PrintUtilPlugin.inString("[" + defaultOption + "] > ");
			if (option.isEmpty()) {
				option = defaultOption;
			}
			isValid = Y.equalsIgnoreCase(option) || N.equalsIgnoreCase(option);
			if (!isValid) {
				PrintUtilPlugin.printLineRed(INVALID_OPTION);
			}
		} while (option.isEmpty() || !isValid);

		if (Y.equalsIgnoreCase(option)) {
			return true;
		}
		return false;
	}

	private boolean isNumber(String value) {
		return value.matches("[0-9]+");
	}

	private List<String> getTableList() throws SQLException, ClassNotFoundException {
		List<String> tableList = new ArrayList<String>();
		// Open connection
		this.database.openConnection();
		// List tables
		ResultSet rs = null;
		try {
			rs = database.getConnection().getMetaData().getTables(null, null, "%", new String[] { TABLE });
			while (rs.next()) {
				tableList.add(rs.getString(DatabaseFactory.TABLE_NAME));
			}
		} finally {
			// Close connection
			DatabaseFactory.close(rs);
			this.database.closeConnection();
		}
		return tableList;
	}

	private List<String> getTableListWithModel() throws Exception {
		List<String> tableList = new ArrayList<String>();
		List<String> tableListTmp = new ArrayList<String>();
		// Open connection
		this.database.openConnection();
		// List tables
		ResultSet rs = null;
		try {
			rs = database.getConnection().getMetaData().getTables(null, null, "%", new String[] { TABLE });
			while (rs.next()) {
				tableListTmp.add(rs.getString(DatabaseFactory.TABLE_NAME));
			}
		} finally {
			DatabaseFactory.close(rs);
			// Close connection
			this.database.closeConnection();
		}

		for (String tableName : tableListTmp) {
			if (new File(getFileNameModelByTableName(tableName)).exists()) {
				tableList.add(tableName);
			}
		}

		return tableList;
	}

	private List<String> getTableListWithRepository() throws Exception {
		List<String> tableList = new ArrayList<String>();
		List<String> tableListTmp = new ArrayList<String>();
		// Open connection
		this.database.openConnection();
		// List tables
		ResultSet rs = null;
		try {
			rs = database.getConnection().getMetaData().getTables(null, null, "%", new String[] { TABLE });
			while (rs.next()) {
				tableListTmp.add(rs.getString(DatabaseFactory.TABLE_NAME));
			}
		} finally {
			DatabaseFactory.close(rs);
			// Close connection
			this.database.closeConnection();
		}

		for (String tableName : tableListTmp) {
			if (new File(getFileNameRepositoryByTableName(tableName)).exists()) {
				tableList.add(tableName);
			}
		}

		return tableList;
	}

	private List<String> getTableListWithService() throws Exception {
		List<String> tableList = new ArrayList<String>();
		List<String> tableListTmp = new ArrayList<String>();
		// Open connection
		this.database.openConnection();
		// List tables
		ResultSet rs = null;
		try {
			rs = database.getConnection().getMetaData().getTables(null, null, "%", new String[] { TABLE });
			while (rs.next()) {
				tableListTmp.add(rs.getString(DatabaseFactory.TABLE_NAME));
			}
		} finally {
			DatabaseFactory.close(rs);
			// Close connection
			this.database.closeConnection();
		}

		for (String tableName : tableListTmp) {
			if (new File(getFileNameServiceByTableName(tableName)).exists()) {
				tableList.add(tableName);
			}
		}

		return tableList;
	}

	private List<Attribute> getAttributeList(String tableName) throws SQLException, ClassNotFoundException {
		Attribute primaryKey = getPrimaryKey(tableName);
		List<Attribute> attributeList = new ArrayList<Attribute>();
		// Open connection
		this.database.openConnection();
		// List attributes
		ResultSet rs = null;
		try {
			rs = database.getConnection().getMetaData().getColumns(null, null, tableName, "%");
			while (rs.next()) {
				Attribute attribute = new Attribute(rs.getString(DatabaseFactory.COLUMN_NAME),
						rs.getString(DatabaseFactory.TYPE_NAME), rs.getInt(DatabaseFactory.COLUMN_SIZE),
						rs.getInt(DatabaseFactory.NULLABLE) == 0);
				attribute.setPrimaryKey(attribute.equals(primaryKey));
				attributeList.add(attribute);
			}
		} finally {
			DatabaseFactory.close(rs);
			// Close connection
			this.database.closeConnection();
		}
		return attributeList;
	}

	private List<Attribute> getAttributeControllerList(String tableName) throws SQLException, ClassNotFoundException {
		List<Attribute> attributeList = getAttributeList(tableName);
		Set<Attribute> attributeListRemove = new HashSet<Attribute>();

		for (ForeingKey fk : this.getManyToOneList(tableName)) {
			for (Attribute attribute : attributeList) {
				if (fk.getColumnName().equals(attribute.getName())) {
					attributeListRemove.add(attribute);
				}
			}
		}
		for (ForeingKey fk : this.getOneToManyAndManyToManyList(tableName)) {
			for (Attribute attribute : attributeList) {
				if (fk.getColumnName().equals(attribute.getName())) {
					attributeListRemove.add(attribute);
				}
			}
		}
		for (Attribute attribute : attributeList) {
			if (TableDesign.CREATED_AT.equals(attribute.getName())
					|| TableDesign.UPDATED_AT.equals(attribute.getName())) {
				attributeListRemove.add(attribute);
			}
		}
		attributeList.removeAll(attributeListRemove);
		return attributeList;
	}

	private Attribute getPrimaryKey(String tableName) throws SQLException, ClassNotFoundException {
		Attribute primaryKey = null;
		// Open connection
		this.database.openConnection();
		// List attributes
		ResultSet rs = null;
		try {
			rs = database.getConnection().getMetaData().getPrimaryKeys(database.getConnection().getCatalog(), null,
					tableName);
			if (rs.next()) {
				primaryKey = new Attribute(rs.getString(DatabaseFactory.COLUMN_NAME));
			}
		} finally {
			DatabaseFactory.close(rs);
			// Close connection
			this.database.closeConnection();
		}
		return primaryKey;
	}

	private List<ForeingKey> getManyToOneList(String tableName) throws SQLException, ClassNotFoundException {
		List<ForeingKey> manyToOneList = new ArrayList<ForeingKey>();
		// Open connection
		this.database.openConnection();
		ResultSet rs = null;
		try {
			rs = database.getConnection().getMetaData().getImportedKeys(database.getConnection().getCatalog(), null,
					tableName);
			while (rs.next()) {
				manyToOneList.add(new ForeingKey(rs.getString(DatabaseFactory.PKTABLE_NAME),
						rs.getString(DatabaseFactory.FKCOLUMN_NAME), rs.getString(DatabaseFactory.PKCOLUMN_NAME)));
			}
		} finally {
			DatabaseFactory.close(rs);
			// Close connection
			this.database.closeConnection();
		}
		return manyToOneList;
	}

	private List<ForeingKey> getOneToManyAndManyToManyList(String tableName)
			throws SQLException, ClassNotFoundException {
		List<ForeingKey> oneToManyList = new ArrayList<ForeingKey>();
		// Open connection
		this.database.openConnection();
		try {
			// OneToMany
			ResultSet rs = null;
			try {
				rs = database.getConnection().getMetaData().getExportedKeys(database.getConnection().getCatalog(), null,
						tableName);
				while (rs.next()) {
					oneToManyList.add(new ForeingKey(rs.getString(DatabaseFactory.FKTABLE_NAME),
							rs.getString(DatabaseFactory.FKCOLUMN_NAME), rs.getString(DatabaseFactory.PKCOLUMN_NAME)));
				}
			} finally {
				DatabaseFactory.close(rs);
			}
			// ManyToOne
			for (ForeingKey fk : oneToManyList) {
				rs = database.getConnection().getMetaData().getImportedKeys(database.getConnection().getCatalog(), null,
						fk.getTableName());
				try {
					while (rs.next()) {
						if (rs.getString(DatabaseFactory.PKTABLE_NAME).equals(tableName)) {
							continue;
						}
						fk.setManyToOne(new ForeingKey(rs.getString(DatabaseFactory.PKTABLE_NAME),
								rs.getString(DatabaseFactory.FKCOLUMN_NAME),
								rs.getString(DatabaseFactory.PKCOLUMN_NAME),
								rs.getString(DatabaseFactory.FKTABLE_NAME)));
					}
				} finally {
					DatabaseFactory.close(rs);
				}
			}
		} finally {
			// Close connection
			this.database.closeConnection();
		}
		return oneToManyList;
	}

	private String readFile(String filename) throws Exception {
		return new String(Files.readAllBytes(Paths.get(filename)));
	}

	private String getFileNameModelByTableName(String name) {
		String modelPath = this.path + PATH_SRC + File.separator + getPathBaseCreation() + PATH_MODEL + File.separator;
		return modelPath + this.getModelName(name) + ".java";
	}

	private String getFileNameRepositoryByTableName(String name) {
		String modelPath = this.path + PATH_SRC + File.separator + getPathBaseCreation() + PATH_REPOSITORY
				+ File.separator;
		return modelPath + getRepositoryName(name) + ".java";
	}

	private String getFileNameServiceByTableName(String name) {
		String modelPath = this.path + PATH_SRC + File.separator + getPathBaseCreation() + PATH_SERVICE
				+ File.separator;
		return modelPath + getServiceName(name) + ".java";
	}

	private String getFileNameModelByModelName(String name) {
		String modelPath = this.path + PATH_SRC + File.separator + getPathBaseCreation() + PATH_MODEL + File.separator;
		return modelPath + name + ".java";
	}

	private String getFileNameController(String name) {
		String controllerPath = this.path + PATH_SRC + File.separator + getPathBaseCreation() + PATH_CONTROLLER
				+ File.separator;
		return controllerPath + this.getControllerName(name) + ".java";
	}

	private String getFileNameRepository(String name) {
		String repositoryPath = this.path + PATH_SRC + File.separator + getPathBaseCreation() + File.separator
				+ PATH_REPOSITORY + File.separator;
		return repositoryPath + this.getRepositoryName(name) + ".java";
	}

	private String getFileNameService(String name) {
		String servicePath = this.path + PATH_SRC + File.separator + getPathBaseCreation() + File.separator
				+ PATH_SERVICE + File.separator;
		return servicePath + this.getServiceName(name) + ".java";
	}

	private String getFileNameDTO(String name) {
		String dtoPath = this.path + PATH_SRC + File.separator + getPathBaseCreation() + File.separator + PATH_DTO
				+ File.separator;
		return dtoPath + this.getModelDTOName(name) + ".java";
	}

	private String getFileNameException(String name) {
		String exceptionPath = this.path + PATH_SRC + File.separator + getPathBaseCreation() + File.separator
				+ PATH_EXCEPTION + File.separator;
		return exceptionPath + "EntityNotFoundException.java";
	}

	public String getFileNameLaravelCollective() {
		return this.path + PATH_VENDOR + File.separator + PATH_LARAVEL_COLLECTIVE + File.separator + PATH_HTML
				+ File.separator + "composer.json";
	}

	public String getFileNameAppConfig() {
		return this.path + PATH_CONFIG + File.separator + "app.php";
	}

	private void updateHistory(String action, String file) {
		changeHistory.add(action + " " + file);
	}

	public String getPathBaseCreation() {
		File file = searchFile(
				new File(this.path + File.separator + "src" + File.separator + "main" + File.separator + "java"),
				PUBLIC_STATIC_VOID_MAIN);
		if (file != null) {
			return file.getParent().replaceAll("(\\\\|\\/)", "\\" + File.separator)
					.split("src\\" + File.separator + "main\\" + File.separator + "java\\" + File.separator)[1]
					+ File.separator;
		}
		return "";
	}

	private static File searchFile(File file, String search) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				File found = searchFile(f, search);
				if (found != null)
					return found;
			}
		} else {
			try (Stream<String> stream = Files.lines(Paths.get(file.getPath()))) {
				if (stream.filter(lines -> lines.contains(search)).findAny().isPresent()) {
					return file;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			if (file.getName().contains(search)) {
				return file;
			}
		}
		return null;
	}

	private static boolean findDependency(NodeList nodeList, String searchArtifactId) {

		boolean hasDependency = false;
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.getNodeName().equals("dependency")) {
					for (int x = 0; x < tempNode.getChildNodes().getLength(); x++) {
						Node attrNode = tempNode.getChildNodes().item(x);
						if ("artifactId".equals(attrNode.getNodeName())
								&& searchArtifactId.equals(attrNode.getFirstChild().getNodeValue())) {
							return true;
						}
					}
				}

				if (tempNode.hasChildNodes()) {
					hasDependency = findDependency(tempNode.getChildNodes(), searchArtifactId);
				}
			}

			if (hasDependency) {
				break;
			}

		}

		return hasDependency;
	}
}
