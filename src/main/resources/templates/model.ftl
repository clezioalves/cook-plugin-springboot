package ${tableDesign.getPackageModel()};

<#if tableDesign.getAttributeDatesList()?has_content>
import java.util.Date;
</#if>
<#if tableDesign.getOneToManyList()?has_content || tableDesign.getManyToManyList()?has_content>
import java.util.List;
</#if>

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

<#if tableDesign.getManyToOneList()?has_content || tableDesign.getManyToManyList()?has_content || tableDesign.getOneToManyList()?has_content>
import javax.persistence.JoinColumn;
</#if>
<#if tableDesign.getManyToManyList()?has_content || tableDesign.getOneToManyList()?has_content>
import javax.persistence.JoinTable;
</#if>
<#if tableDesign.getManyToOneList()?has_content>
import javax.persistence.ManyToOne;
</#if>
<#if tableDesign.getManyToManyList()?has_content>
import javax.persistence.ManyToMany;
</#if>
<#if tableDesign.getOneToManyList()?has_content>
import javax.persistence.OneToMany;
</#if>
import javax.persistence.Table;
<#if tableDesign.isRequiredAttribute()>
import javax.validation.constraints.NotNull;
</#if>
<#if tableDesign.isDefineSize()>
import javax.validation.constraints.Size;
</#if>

import lombok.Data;

@Data
@Entity
@Table(name = "${tableDesign.getName()}")
public class ${tableDesign.getNameModelize()} {

    <#list tableDesign.getAttributeList() as attribute>
    <#if !attribute.isForeingKey()>
    <#if attribute.getPrimaryKey()>
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    </#if>
    <#if attribute.isRequiredAttribute()>
    @NotNull
    </#if>
    <#if attribute.isDefineSize()>
    @Size(max = ${attribute.getMaxLenght()})
    </#if>
    @Column(name = "${attribute.getName()}")
    private ${attribute.getTypeJava()} ${attribute.getNameVariable()};

    </#if>
    </#list>
    <#list tableDesign.getManyToOneList() as attr>
    /**
     * Get the ${attr.getTableNameVariable()} record associated with the ${tableDesign.getNameSingularize()}.
     */
    @ManyToOne
    @JoinColumn(name="${attr.getColumnName()}")
    private ${attr.getTableNameModelize()} ${attr.getTableNameVariable()};

    </#list>
    <#list tableDesign.getOneToManyList() as attr>
    /**
     * Get the ${attr.getTableNameCollections()} for the ${tableDesign.getNameSingularize()}.
     */
    @OneToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "${attr.getColumnName()}"))
    private List<${attr.getTableNameModelize()}> ${attr.getTableNameCollections()};

    </#list>
    <#list tableDesign.getManyToManyList() as attr>
    /**
     * The ${attr.getManyToOne().getTableNameCollections()} that belong to the ${tableDesign.getNameSingularize()}.
     */
    @ManyToMany
    @JoinTable(name = "${attr.getManyToOne().getFkTableName()}", joinColumns = @JoinColumn(name = "${attr.getColumnName()}"), inverseJoinColumns = @JoinColumn(name = "${attr.getManyToOne().getColumnName()}"))
    private List<${attr.getManyToOne().getTableNameModelize()}> ${attr.getManyToOne().getTableNameCollections()};

    </#list>
}