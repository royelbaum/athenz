//
// This file generated by rdl 1.5.2. Do not modify!
//

package com.yahoo.athenz.zms;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yahoo.rdl.*;

//
// MemberRole -
//
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberRole {
    public String roleName;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String domainName;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String memberName;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Timestamp expiration;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Timestamp reviewReminder;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Boolean active;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String auditRef;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String requestPrincipal;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Timestamp requestTime;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Integer systemDisabled;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String pendingState;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String trustRoleName;

    public MemberRole setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }
    public String getRoleName() {
        return roleName;
    }
    public MemberRole setDomainName(String domainName) {
        this.domainName = domainName;
        return this;
    }
    public String getDomainName() {
        return domainName;
    }
    public MemberRole setMemberName(String memberName) {
        this.memberName = memberName;
        return this;
    }
    public String getMemberName() {
        return memberName;
    }
    public MemberRole setExpiration(Timestamp expiration) {
        this.expiration = expiration;
        return this;
    }
    public Timestamp getExpiration() {
        return expiration;
    }
    public MemberRole setReviewReminder(Timestamp reviewReminder) {
        this.reviewReminder = reviewReminder;
        return this;
    }
    public Timestamp getReviewReminder() {
        return reviewReminder;
    }
    public MemberRole setActive(Boolean active) {
        this.active = active;
        return this;
    }
    public Boolean getActive() {
        return active;
    }
    public MemberRole setAuditRef(String auditRef) {
        this.auditRef = auditRef;
        return this;
    }
    public String getAuditRef() {
        return auditRef;
    }
    public MemberRole setRequestPrincipal(String requestPrincipal) {
        this.requestPrincipal = requestPrincipal;
        return this;
    }
    public String getRequestPrincipal() {
        return requestPrincipal;
    }
    public MemberRole setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
        return this;
    }
    public Timestamp getRequestTime() {
        return requestTime;
    }
    public MemberRole setSystemDisabled(Integer systemDisabled) {
        this.systemDisabled = systemDisabled;
        return this;
    }
    public Integer getSystemDisabled() {
        return systemDisabled;
    }
    public MemberRole setPendingState(String pendingState) {
        this.pendingState = pendingState;
        return this;
    }
    public String getPendingState() {
        return pendingState;
    }
    public MemberRole setTrustRoleName(String trustRoleName) {
        this.trustRoleName = trustRoleName;
        return this;
    }
    public String getTrustRoleName() {
        return trustRoleName;
    }

    @Override
    public boolean equals(Object another) {
        if (this != another) {
            if (another == null || another.getClass() != MemberRole.class) {
                return false;
            }
            MemberRole a = (MemberRole) another;
            if (roleName == null ? a.roleName != null : !roleName.equals(a.roleName)) {
                return false;
            }
            if (domainName == null ? a.domainName != null : !domainName.equals(a.domainName)) {
                return false;
            }
            if (memberName == null ? a.memberName != null : !memberName.equals(a.memberName)) {
                return false;
            }
            if (expiration == null ? a.expiration != null : !expiration.equals(a.expiration)) {
                return false;
            }
            if (reviewReminder == null ? a.reviewReminder != null : !reviewReminder.equals(a.reviewReminder)) {
                return false;
            }
            if (active == null ? a.active != null : !active.equals(a.active)) {
                return false;
            }
            if (auditRef == null ? a.auditRef != null : !auditRef.equals(a.auditRef)) {
                return false;
            }
            if (requestPrincipal == null ? a.requestPrincipal != null : !requestPrincipal.equals(a.requestPrincipal)) {
                return false;
            }
            if (requestTime == null ? a.requestTime != null : !requestTime.equals(a.requestTime)) {
                return false;
            }
            if (systemDisabled == null ? a.systemDisabled != null : !systemDisabled.equals(a.systemDisabled)) {
                return false;
            }
            if (pendingState == null ? a.pendingState != null : !pendingState.equals(a.pendingState)) {
                return false;
            }
            if (trustRoleName == null ? a.trustRoleName != null : !trustRoleName.equals(a.trustRoleName)) {
                return false;
            }
        }
        return true;
    }

    //
    // sets up the instance according to its default field values, if any
    //
    public MemberRole init() {
        if (active == null) {
            active = true;
        }
        return this;
    }
}
