package ${tableDesign.getPackageDTO()};

<#if tableDesign.getAttributeDatesList()?has_content>
import java.util.Date;
</#if>
<#if tableDesign.getOneToManyList()?has_content || tableDesign.getManyToManyList()?has_content>
import java.util.List;
</#if>

import lombok.Data;

@Data
public class ${tableDesign.getNameModelize()}DTO {

    <#list tableDesign.getAttributeList() as attribute>
    private ${attribute.getTypeJava()} ${attribute.getNameVariable()};

    </#list>
    <#list tableDesign.getManyToOneList() as attr>
    private ${attr.getTableNameModelize()}DTO ${attr.getTableNameVariable()};

    </#list>
    <#list tableDesign.getOneToManyList() as attr>
    private List<${attr.getTableNameModelize()}DTO> ${attr.getTableNameCollections()};

    </#list>
    <#list tableDesign.getManyToManyList() as attr>
    private List<${attr.getManyToOne().getTableNameModelize()}DTO> ${attr.getManyToOne().getTableNameCollections()};

    </#list>
}