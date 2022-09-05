//
// This file generated by rdl 1.5.2. Do not modify!
//
package com.yahoo.athenz.zms;

import com.yahoo.rdl.*;
import jakarta.ws.rs.core.Response;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//
// ZMSHandler is the interface that the service implementation must implement
//
public interface ZMSHandler { 
    Domain getDomain(ResourceContext context, String domain);
    DomainList getDomainList(ResourceContext context, Integer limit, String skip, String prefix, Integer depth, String account, Integer productId, String roleMember, String roleName, String subscription, String tagKey, String tagValue, String businessService, String modifiedSince);
    Domain postTopLevelDomain(ResourceContext context, String auditRef, TopLevelDomain detail);
    Domain postSubDomain(ResourceContext context, String parent, String auditRef, SubDomain detail);
    Domain postUserDomain(ResourceContext context, String name, String auditRef, UserDomain detail);
    void deleteTopLevelDomain(ResourceContext context, String name, String auditRef);
    void deleteSubDomain(ResourceContext context, String parent, String name, String auditRef);
    void deleteUserDomain(ResourceContext context, String name, String auditRef);
    void putDomainMeta(ResourceContext context, String name, String auditRef, DomainMeta detail);
    void putDomainSystemMeta(ResourceContext context, String name, String attribute, String auditRef, DomainMeta detail);
    void putDomainTemplate(ResourceContext context, String name, String auditRef, DomainTemplate domainTemplate);
    void putDomainTemplateExt(ResourceContext context, String name, String template, String auditRef, DomainTemplate domainTemplate);
    DomainTemplateList getDomainTemplateList(ResourceContext context, String name);
    void deleteDomainTemplate(ResourceContext context, String name, String template, String auditRef);
    DomainMetaStoreValidValuesList getDomainMetaStoreValidValuesList(ResourceContext context, String attributeName, String userName);
    AuthHistoryDependencies getAuthHistoryDependencies(ResourceContext context, String domainName);
    Response deleteExpiredMembers(ResourceContext context, Integer purgeResources, String auditRef, Boolean returnObj);
    DomainDataCheck getDomainDataCheck(ResourceContext context, String domainName);
    void putEntity(ResourceContext context, String domainName, String entityName, String auditRef, Entity entity);
    Entity getEntity(ResourceContext context, String domainName, String entityName);
    void deleteEntity(ResourceContext context, String domainName, String entityName, String auditRef);
    EntityList getEntityList(ResourceContext context, String domainName);
    RoleList getRoleList(ResourceContext context, String domainName, Integer limit, String skip);
    Roles getRoles(ResourceContext context, String domainName, Boolean members, String tagKey, String tagValue);
    Role getRole(ResourceContext context, String domainName, String roleName, Boolean auditLog, Boolean expand, Boolean pending);
    Response putRole(ResourceContext context, String domainName, String roleName, String auditRef, Boolean returnObj, Role role);
    void deleteRole(ResourceContext context, String domainName, String roleName, String auditRef);
    Membership getMembership(ResourceContext context, String domainName, String roleName, String memberName, String expiration);
    DomainRoleMembers getOverdueReview(ResourceContext context, String domainName);
    DomainRoleMembers getDomainRoleMembers(ResourceContext context, String domainName);
    DomainRoleMember getPrincipalRoles(ResourceContext context, String principal, String domainName);
    Response putMembership(ResourceContext context, String domainName, String roleName, String memberName, String auditRef, Boolean returnObj, Membership membership);
    void deleteMembership(ResourceContext context, String domainName, String roleName, String memberName, String auditRef);
    void deletePendingMembership(ResourceContext context, String domainName, String roleName, String memberName, String auditRef);
    void putDefaultAdmins(ResourceContext context, String domainName, String auditRef, DefaultAdmins defaultAdmins);
    void putRoleSystemMeta(ResourceContext context, String domainName, String roleName, String attribute, String auditRef, RoleSystemMeta detail);
    void putRoleMeta(ResourceContext context, String domainName, String roleName, String auditRef, RoleMeta detail);
    void putMembershipDecision(ResourceContext context, String domainName, String roleName, String memberName, String auditRef, Membership membership);
    Response putRoleReview(ResourceContext context, String domainName, String roleName, String auditRef, Boolean returnObj, Role role);
    Groups getGroups(ResourceContext context, String domainName, Boolean members, String tagKey, String tagValue);
    Group getGroup(ResourceContext context, String domainName, String groupName, Boolean auditLog, Boolean pending);
    Response putGroup(ResourceContext context, String domainName, String groupName, String auditRef, Boolean returnObj, Group group);
    void deleteGroup(ResourceContext context, String domainName, String groupName, String auditRef);
    GroupMembership getGroupMembership(ResourceContext context, String domainName, String groupName, String memberName, String expiration);
    DomainGroupMember getPrincipalGroups(ResourceContext context, String principal, String domainName);
    Response putGroupMembership(ResourceContext context, String domainName, String groupName, String memberName, String auditRef, Boolean returnObj, GroupMembership membership);
    void deleteGroupMembership(ResourceContext context, String domainName, String groupName, String memberName, String auditRef);
    void deletePendingGroupMembership(ResourceContext context, String domainName, String groupName, String memberName, String auditRef);
    void putGroupSystemMeta(ResourceContext context, String domainName, String groupName, String attribute, String auditRef, GroupSystemMeta detail);
    void putGroupMeta(ResourceContext context, String domainName, String groupName, String auditRef, GroupMeta detail);
    void putGroupMembershipDecision(ResourceContext context, String domainName, String groupName, String memberName, String auditRef, GroupMembership membership);
    Response putGroupReview(ResourceContext context, String domainName, String groupName, String auditRef, Boolean returnObj, Group group);
    DomainGroupMembership getPendingDomainGroupMembersList(ResourceContext context, String principal, String domainName);
    PolicyList getPolicyList(ResourceContext context, String domainName, Integer limit, String skip);
    Policies getPolicies(ResourceContext context, String domainName, Boolean assertions, Boolean includeNonActive);
    Policy getPolicy(ResourceContext context, String domainName, String policyName);
    Response putPolicy(ResourceContext context, String domainName, String policyName, String auditRef, Boolean returnObj, Policy policy);
    void deletePolicy(ResourceContext context, String domainName, String policyName, String auditRef);
    Assertion getAssertion(ResourceContext context, String domainName, String policyName, Long assertionId);
    Assertion putAssertion(ResourceContext context, String domainName, String policyName, String auditRef, Assertion assertion);
    Assertion putAssertionPolicyVersion(ResourceContext context, String domainName, String policyName, String version, String auditRef, Assertion assertion);
    void deleteAssertion(ResourceContext context, String domainName, String policyName, Long assertionId, String auditRef);
    void deleteAssertionPolicyVersion(ResourceContext context, String domainName, String policyName, String version, Long assertionId, String auditRef);
    AssertionConditions putAssertionConditions(ResourceContext context, String domainName, String policyName, Long assertionId, String auditRef, AssertionConditions assertionConditions);
    AssertionCondition putAssertionCondition(ResourceContext context, String domainName, String policyName, Long assertionId, String auditRef, AssertionCondition assertionCondition);
    void deleteAssertionConditions(ResourceContext context, String domainName, String policyName, Long assertionId, String auditRef);
    void deleteAssertionCondition(ResourceContext context, String domainName, String policyName, Long assertionId, Integer conditionId, String auditRef);
    PolicyList getPolicyVersionList(ResourceContext context, String domainName, String policyName);
    Policy getPolicyVersion(ResourceContext context, String domainName, String policyName, String version);
    Response putPolicyVersion(ResourceContext context, String domainName, String policyName, PolicyOptions policyOptions, String auditRef, Boolean returnObj);
    void setActivePolicyVersion(ResourceContext context, String domainName, String policyName, PolicyOptions policyOptions, String auditRef);
    void deletePolicyVersion(ResourceContext context, String domainName, String policyName, String version, String auditRef);
    Response putServiceIdentity(ResourceContext context, String domain, String service, String auditRef, Boolean returnObj, ServiceIdentity detail);
    ServiceIdentity getServiceIdentity(ResourceContext context, String domain, String service);
    void deleteServiceIdentity(ResourceContext context, String domain, String service, String auditRef);
    ServiceIdentities getServiceIdentities(ResourceContext context, String domainName, Boolean publickeys, Boolean hosts);
    ServiceIdentityList getServiceIdentityList(ResourceContext context, String domainName, Integer limit, String skip);
    PublicKeyEntry getPublicKeyEntry(ResourceContext context, String domain, String service, String id);
    void putPublicKeyEntry(ResourceContext context, String domain, String service, String id, String auditRef, PublicKeyEntry publicKeyEntry);
    void deletePublicKeyEntry(ResourceContext context, String domain, String service, String id, String auditRef);
    void putServiceIdentitySystemMeta(ResourceContext context, String domain, String service, String attribute, String auditRef, ServiceIdentitySystemMeta detail);
    void putTenancy(ResourceContext context, String domain, String service, String auditRef, Tenancy detail);
    void deleteTenancy(ResourceContext context, String domain, String service, String auditRef);
    void putTenant(ResourceContext context, String domain, String service, String tenantDomain, String auditRef, Tenancy detail);
    void deleteTenant(ResourceContext context, String domain, String service, String tenantDomain, String auditRef);
    TenantResourceGroupRoles putTenantResourceGroupRoles(ResourceContext context, String domain, String service, String tenantDomain, String resourceGroup, String auditRef, TenantResourceGroupRoles detail);
    TenantResourceGroupRoles getTenantResourceGroupRoles(ResourceContext context, String domain, String service, String tenantDomain, String resourceGroup);
    void deleteTenantResourceGroupRoles(ResourceContext context, String domain, String service, String tenantDomain, String resourceGroup, String auditRef);
    ProviderResourceGroupRoles putProviderResourceGroupRoles(ResourceContext context, String tenantDomain, String provDomain, String provService, String resourceGroup, String auditRef, ProviderResourceGroupRoles detail);
    ProviderResourceGroupRoles getProviderResourceGroupRoles(ResourceContext context, String tenantDomain, String provDomain, String provService, String resourceGroup);
    void deleteProviderResourceGroupRoles(ResourceContext context, String tenantDomain, String provDomain, String provService, String resourceGroup, String auditRef);
    Access getAccess(ResourceContext context, String action, String resource, String domain, String checkPrincipal);
    Access getAccessExt(ResourceContext context, String action, String resource, String domain, String checkPrincipal);
    ResourceAccessList getResourceAccessList(ResourceContext context, String principal, String action);
    Response getSignedDomains(ResourceContext context, String domain, String metaOnly, String metaAttr, Boolean master, Boolean conditions, String matchingTag);
    Response getJWSDomain(ResourceContext context, String name, Boolean signatureP1363Format, String matchingTag);
    UserToken getUserToken(ResourceContext context, String userName, String serviceNames, Boolean header);
    UserToken optionsUserToken(ResourceContext context, String userName, String serviceNames);
    ServicePrincipal getServicePrincipal(ResourceContext context);
    ServerTemplateList getServerTemplateList(ResourceContext context);
    Template getTemplate(ResourceContext context, String template);
    DomainTemplateDetailsList getDomainTemplateDetailsList(ResourceContext context, String name);
    DomainTemplateDetailsList getServerTemplateDetailsList(ResourceContext context);
    UserList getUserList(ResourceContext context, String domainName);
    void deleteUser(ResourceContext context, String name, String auditRef);
    void deleteDomainRoleMember(ResourceContext context, String domainName, String memberName, String auditRef);
    Quota getQuota(ResourceContext context, String name);
    void putQuota(ResourceContext context, String name, String auditRef, Quota quota);
    void deleteQuota(ResourceContext context, String name, String auditRef);
    Status getStatus(ResourceContext context);
    DomainRoleMembership getPendingDomainRoleMembersList(ResourceContext context, String principal, String domainName);
    UserAuthorityAttributeMap getUserAuthorityAttributeMap(ResourceContext context);
    Stats getStats(ResourceContext context, String name);
    Stats getSystemStats(ResourceContext context);
    void putDomainDependency(ResourceContext context, String domainName, String auditRef, DependentService service);
    void deleteDomainDependency(ResourceContext context, String domainName, String service, String auditRef);
    ServiceIdentityList getDependentServiceList(ResourceContext context, String domainName);
    DependentServiceResourceGroupList getDependentServiceResourceGroupList(ResourceContext context, String domainName);
    DomainList getDependentDomainList(ResourceContext context, String service);
    Info getInfo(ResourceContext context);
    Schema getRdlSchema(ResourceContext context);
    ResourceContext newResourceContext(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, String apiName);
    void recordMetrics(ResourceContext ctx, int httpStatus);
    void publishChangeMessage(ResourceContext ctx, int httpStatus);
}
