//
// This file generated by rdl 1.5.2. Do not modify!
//

package com.yahoo.athenz.zms;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;
import com.yahoo.rdl.*;

//
// TopLevelDomain - Top Level Domain object. The required attributes include
// the name of the domain and list of domain administrators.
//
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopLevelDomain {
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String description;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String org;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Boolean enabled;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Boolean auditEnabled;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String account;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer ypmId;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String applicationId;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String certDnsDomain;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer memberExpiryDays;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer tokenExpiryMins;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer serviceCertExpiryMins;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer roleCertExpiryMins;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String signAlgorithm;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer serviceExpiryDays;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer groupExpiryDays;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String userAuthorityFilter;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String azureSubscription;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String gcpProject;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String gcpProjectNumber;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Map<String, TagValueList> tags;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String businessService;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer memberPurgeExpiryDays;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String productId;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer featureFlags;
    public String name;
    public List<String> adminUsers;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public DomainTemplateList templates;

    public TopLevelDomain setDescription(String description) {
        this.description = description;
        return this;
    }
    public String getDescription() {
        return description;
    }
    public TopLevelDomain setOrg(String org) {
        this.org = org;
        return this;
    }
    public String getOrg() {
        return org;
    }
    public TopLevelDomain setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    public Boolean getEnabled() {
        return enabled;
    }
    public TopLevelDomain setAuditEnabled(Boolean auditEnabled) {
        this.auditEnabled = auditEnabled;
        return this;
    }
    public Boolean getAuditEnabled() {
        return auditEnabled;
    }
    public TopLevelDomain setAccount(String account) {
        this.account = account;
        return this;
    }
    public String getAccount() {
        return account;
    }
    public TopLevelDomain setYpmId(Integer ypmId) {
        this.ypmId = ypmId;
        return this;
    }
    public Integer getYpmId() {
        return ypmId;
    }
    public TopLevelDomain setApplicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }
    public String getApplicationId() {
        return applicationId;
    }
    public TopLevelDomain setCertDnsDomain(String certDnsDomain) {
        this.certDnsDomain = certDnsDomain;
        return this;
    }
    public String getCertDnsDomain() {
        return certDnsDomain;
    }
    public TopLevelDomain setMemberExpiryDays(Integer memberExpiryDays) {
        this.memberExpiryDays = memberExpiryDays;
        return this;
    }
    public Integer getMemberExpiryDays() {
        return memberExpiryDays;
    }
    public TopLevelDomain setTokenExpiryMins(Integer tokenExpiryMins) {
        this.tokenExpiryMins = tokenExpiryMins;
        return this;
    }
    public Integer getTokenExpiryMins() {
        return tokenExpiryMins;
    }
    public TopLevelDomain setServiceCertExpiryMins(Integer serviceCertExpiryMins) {
        this.serviceCertExpiryMins = serviceCertExpiryMins;
        return this;
    }
    public Integer getServiceCertExpiryMins() {
        return serviceCertExpiryMins;
    }
    public TopLevelDomain setRoleCertExpiryMins(Integer roleCertExpiryMins) {
        this.roleCertExpiryMins = roleCertExpiryMins;
        return this;
    }
    public Integer getRoleCertExpiryMins() {
        return roleCertExpiryMins;
    }
    public TopLevelDomain setSignAlgorithm(String signAlgorithm) {
        this.signAlgorithm = signAlgorithm;
        return this;
    }
    public String getSignAlgorithm() {
        return signAlgorithm;
    }
    public TopLevelDomain setServiceExpiryDays(Integer serviceExpiryDays) {
        this.serviceExpiryDays = serviceExpiryDays;
        return this;
    }
    public Integer getServiceExpiryDays() {
        return serviceExpiryDays;
    }
    public TopLevelDomain setGroupExpiryDays(Integer groupExpiryDays) {
        this.groupExpiryDays = groupExpiryDays;
        return this;
    }
    public Integer getGroupExpiryDays() {
        return groupExpiryDays;
    }
    public TopLevelDomain setUserAuthorityFilter(String userAuthorityFilter) {
        this.userAuthorityFilter = userAuthorityFilter;
        return this;
    }
    public String getUserAuthorityFilter() {
        return userAuthorityFilter;
    }
    public TopLevelDomain setAzureSubscription(String azureSubscription) {
        this.azureSubscription = azureSubscription;
        return this;
    }
    public String getAzureSubscription() {
        return azureSubscription;
    }
    public TopLevelDomain setGcpProject(String gcpProject) {
        this.gcpProject = gcpProject;
        return this;
    }
    public String getGcpProject() {
        return gcpProject;
    }
    public TopLevelDomain setGcpProjectNumber(String gcpProjectNumber) {
        this.gcpProjectNumber = gcpProjectNumber;
        return this;
    }
    public String getGcpProjectNumber() {
        return gcpProjectNumber;
    }
    public TopLevelDomain setTags(Map<String, TagValueList> tags) {
        this.tags = tags;
        return this;
    }
    public Map<String, TagValueList> getTags() {
        return tags;
    }
    public TopLevelDomain setBusinessService(String businessService) {
        this.businessService = businessService;
        return this;
    }
    public String getBusinessService() {
        return businessService;
    }
    public TopLevelDomain setMemberPurgeExpiryDays(Integer memberPurgeExpiryDays) {
        this.memberPurgeExpiryDays = memberPurgeExpiryDays;
        return this;
    }
    public Integer getMemberPurgeExpiryDays() {
        return memberPurgeExpiryDays;
    }
    public TopLevelDomain setProductId(String productId) {
        this.productId = productId;
        return this;
    }
    public String getProductId() {
        return productId;
    }
    public TopLevelDomain setFeatureFlags(Integer featureFlags) {
        this.featureFlags = featureFlags;
        return this;
    }
    public Integer getFeatureFlags() {
        return featureFlags;
    }
    public TopLevelDomain setName(String name) {
        this.name = name;
        return this;
    }
    public String getName() {
        return name;
    }
    public TopLevelDomain setAdminUsers(List<String> adminUsers) {
        this.adminUsers = adminUsers;
        return this;
    }
    public List<String> getAdminUsers() {
        return adminUsers;
    }
    public TopLevelDomain setTemplates(DomainTemplateList templates) {
        this.templates = templates;
        return this;
    }
    public DomainTemplateList getTemplates() {
        return templates;
    }

    @Override
    public boolean equals(Object another) {
        if (this != another) {
            if (another == null || another.getClass() != TopLevelDomain.class) {
                return false;
            }
            TopLevelDomain a = (TopLevelDomain) another;
            if (description == null ? a.description != null : !description.equals(a.description)) {
                return false;
            }
            if (org == null ? a.org != null : !org.equals(a.org)) {
                return false;
            }
            if (enabled == null ? a.enabled != null : !enabled.equals(a.enabled)) {
                return false;
            }
            if (auditEnabled == null ? a.auditEnabled != null : !auditEnabled.equals(a.auditEnabled)) {
                return false;
            }
            if (account == null ? a.account != null : !account.equals(a.account)) {
                return false;
            }
            if (ypmId == null ? a.ypmId != null : !ypmId.equals(a.ypmId)) {
                return false;
            }
            if (applicationId == null ? a.applicationId != null : !applicationId.equals(a.applicationId)) {
                return false;
            }
            if (certDnsDomain == null ? a.certDnsDomain != null : !certDnsDomain.equals(a.certDnsDomain)) {
                return false;
            }
            if (memberExpiryDays == null ? a.memberExpiryDays != null : !memberExpiryDays.equals(a.memberExpiryDays)) {
                return false;
            }
            if (tokenExpiryMins == null ? a.tokenExpiryMins != null : !tokenExpiryMins.equals(a.tokenExpiryMins)) {
                return false;
            }
            if (serviceCertExpiryMins == null ? a.serviceCertExpiryMins != null : !serviceCertExpiryMins.equals(a.serviceCertExpiryMins)) {
                return false;
            }
            if (roleCertExpiryMins == null ? a.roleCertExpiryMins != null : !roleCertExpiryMins.equals(a.roleCertExpiryMins)) {
                return false;
            }
            if (signAlgorithm == null ? a.signAlgorithm != null : !signAlgorithm.equals(a.signAlgorithm)) {
                return false;
            }
            if (serviceExpiryDays == null ? a.serviceExpiryDays != null : !serviceExpiryDays.equals(a.serviceExpiryDays)) {
                return false;
            }
            if (groupExpiryDays == null ? a.groupExpiryDays != null : !groupExpiryDays.equals(a.groupExpiryDays)) {
                return false;
            }
            if (userAuthorityFilter == null ? a.userAuthorityFilter != null : !userAuthorityFilter.equals(a.userAuthorityFilter)) {
                return false;
            }
            if (azureSubscription == null ? a.azureSubscription != null : !azureSubscription.equals(a.azureSubscription)) {
                return false;
            }
            if (gcpProject == null ? a.gcpProject != null : !gcpProject.equals(a.gcpProject)) {
                return false;
            }
            if (gcpProjectNumber == null ? a.gcpProjectNumber != null : !gcpProjectNumber.equals(a.gcpProjectNumber)) {
                return false;
            }
            if (tags == null ? a.tags != null : !tags.equals(a.tags)) {
                return false;
            }
            if (businessService == null ? a.businessService != null : !businessService.equals(a.businessService)) {
                return false;
            }
            if (memberPurgeExpiryDays == null ? a.memberPurgeExpiryDays != null : !memberPurgeExpiryDays.equals(a.memberPurgeExpiryDays)) {
                return false;
            }
            if (productId == null ? a.productId != null : !productId.equals(a.productId)) {
                return false;
            }
            if (featureFlags == null ? a.featureFlags != null : !featureFlags.equals(a.featureFlags)) {
                return false;
            }
            if (name == null ? a.name != null : !name.equals(a.name)) {
                return false;
            }
            if (adminUsers == null ? a.adminUsers != null : !adminUsers.equals(a.adminUsers)) {
                return false;
            }
            if (templates == null ? a.templates != null : !templates.equals(a.templates)) {
                return false;
            }
        }
        return true;
    }
}
