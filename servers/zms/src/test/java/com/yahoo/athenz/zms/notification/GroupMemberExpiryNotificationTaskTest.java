/*
 * Copyright The Athenz Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yahoo.athenz.zms.notification;

import com.yahoo.athenz.common.server.notification.*;
import com.yahoo.athenz.zms.*;
import com.yahoo.athenz.zms.store.AthenzDomain;
import com.yahoo.rdl.Timestamp;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.*;

import static com.yahoo.athenz.common.ServerCommonConsts.USER_DOMAIN_PREFIX;
import static com.yahoo.athenz.common.server.notification.NotificationServiceConstants.*;
import static com.yahoo.athenz.common.server.notification.impl.MetricNotificationService.*;
import static com.yahoo.athenz.zms.notification.ZMSNotificationManagerTest.getNotificationManager;
import static org.mockito.ArgumentMatchers.any;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertEquals;

public class GroupMemberExpiryNotificationTaskTest {
    @Test
    public void testSendGroupMemberExpiryRemindersException() {

        DBService dbsvc = Mockito.mock(DBService.class);
        NotificationService mockNotificationService =  Mockito.mock(NotificationService.class);
        NotificationServiceFactory testfact = () -> mockNotificationService;

        // we're going to throw an exception when called

        Mockito.when(dbsvc.getGroupExpiryMembers(1)).thenThrow(new IllegalArgumentException());
        NotificationManager notificationManager = getNotificationManager(dbsvc, testfact);

        GroupMemberExpiryNotificationTask groupMemberExpiryNotificationTask = new GroupMemberExpiryNotificationTask(dbsvc, USER_DOMAIN_PREFIX, new NotificationToEmailConverterCommon(null));
        // to make sure we're not creating any notifications, we're going
        // to configure our mock to throw an exception

        Mockito.when(mockNotificationService.notify(any())).thenThrow(new IllegalArgumentException());

        try {
            groupMemberExpiryNotificationTask.getNotifications();
            fail();
        } catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
        notificationManager.shutdown();
    }

    @Test
    public void testSendGroupMemberExpiryRemindersEmptySet() {

        DBService dbsvc = Mockito.mock(DBService.class);
        NotificationService mockNotificationService =  Mockito.mock(NotificationService.class);
        NotificationServiceFactory testfact = () -> mockNotificationService;
        NotificationManager notificationManager = getNotificationManager(dbsvc, testfact);

        // to make sure we're not creating any notifications, we're going
        // to configure our mock to throw an exception

        Mockito.when(mockNotificationService.notify(any())).thenThrow(new IllegalArgumentException());

        GroupMemberExpiryNotificationTask groupMemberExpiryNotificationTask = new GroupMemberExpiryNotificationTask(dbsvc, USER_DOMAIN_PREFIX, new NotificationToEmailConverterCommon(null));
        assertEquals(groupMemberExpiryNotificationTask.getNotifications(), new ArrayList<>());

        notificationManager.shutdown();
    }
    @Test
    public void testSendGroupMemberExpiryReminders() {

        DBService dbsvc = Mockito.mock(DBService.class);
        NotificationToEmailConverterCommon notificationToEmailConverterCommon = new NotificationToEmailConverterCommon(null);
        NotificationService mockNotificationService =  Mockito.mock(NotificationService.class);
        NotificationServiceFactory testfact = () -> mockNotificationService;

        List<GroupMember> memberGroups = new ArrayList<>();
        memberGroups.add(new GroupMember().setGroupName("group1")
                .setDomainName("athenz1")
                .setMemberName("user.joe")
                .setExpiration(Timestamp.fromMillis(100)));
        DomainGroupMember domainGroupMember = new DomainGroupMember()
                .setMemberName("user.joe")
                .setMemberGroups(memberGroups);
        Map<String, DomainGroupMember> expiryMembers = new HashMap<>();
        expiryMembers.put("user.joe", domainGroupMember);

        // we're going to return null for our first thread which will
        // run during init call and then the real data for the second
        // call

        Mockito.when(dbsvc.getGroupExpiryMembers(1))
                .thenReturn(null)
                .thenReturn(expiryMembers);

        NotificationManager notificationManager = getNotificationManager(dbsvc, testfact);

        ZMSTestUtils.sleep(1000);

        AthenzDomain domain = new AthenzDomain("athenz1");
        List<RoleMember> roleMembers = new ArrayList<>();
        roleMembers.add(new RoleMember().setMemberName("user.jane"));
        Role adminRole = new Role().setName("athenz1:role.admin").setRoleMembers(roleMembers);
        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        domain.setRoles(roles);

        Mockito.when(dbsvc.getRolesByDomain("athenz1")).thenReturn(domain.getRoles());

        List<Notification> notifications = new GroupMemberExpiryNotificationTask(dbsvc, USER_DOMAIN_PREFIX, notificationToEmailConverterCommon)
                .getNotifications();

        // we should get 2 notifications - one for user and one for domain
        assertEquals(notifications.size(), 2);

        // Verify contents of notifications is as expected
        Notification expectedFirstNotification = new Notification();
        expectedFirstNotification.addRecipient("user.joe");
        expectedFirstNotification.addDetails(NOTIFICATION_DETAILS_ROLES_LIST, "athenz1;group1;1970-01-01T00:00:00.100Z");
        expectedFirstNotification.addDetails("member", "user.joe");
        expectedFirstNotification.setNotificationToEmailConverter(new GroupMemberExpiryNotificationTask.GroupExpiryPrincipalNotificationToEmailConverter(notificationToEmailConverterCommon));
        expectedFirstNotification.setNotificationToMetricConverter(new GroupMemberExpiryNotificationTask.GroupExpiryPrincipalNotificationToToMetricConverter());

        Notification expectedSecondNotification = new Notification();
        expectedSecondNotification.addRecipient("user.jane");
        expectedSecondNotification.addDetails(NOTIFICATION_DETAILS_MEMBERS_LIST, "user.joe;group1;1970-01-01T00:00:00.100Z");
        expectedSecondNotification.addDetails("domain", "athenz1");
        expectedSecondNotification.setNotificationToEmailConverter(new GroupMemberExpiryNotificationTask.GroupExpiryDomainNotificationToEmailConverter(notificationToEmailConverterCommon));
        expectedSecondNotification.setNotificationToMetricConverter(new GroupMemberExpiryNotificationTask.GroupExpiryDomainNotificationToMetricConverter());

        assertEquals(notifications.get(0), expectedFirstNotification);
        assertEquals(notifications.get(1), expectedSecondNotification);

        notificationManager.shutdown();
    }

    @Test
    public void testSendGroupMemberExpiryRemindersNoValidDomain() {

        DBService dbsvc = Mockito.mock(DBService.class);

        List<GroupMember> memberGroups = new ArrayList<>();
        memberGroups.add(new GroupMember().setGroupName("group1")
                .setDomainName("athenz1")
                .setMemberName("user.joe")
                .setExpiration(Timestamp.fromMillis(100)));
        DomainGroupMember domainGroupMember = new DomainGroupMember()
                .setMemberGroups(memberGroups);
        Map<String, DomainGroupMember> expiryMembers = new HashMap<>();
        expiryMembers.put("user.joe", domainGroupMember);

        // we're going to return null for our first thread which will
        // run during init call and then the real data for the second
        // call

        Mockito.when(dbsvc.getGroupExpiryMembers(1))
                .thenReturn(null)
                .thenReturn(expiryMembers);

        ZMSTestUtils.sleep(1000);

        // we're going to return not found domain always

        Mockito.when(dbsvc.getAthenzDomain("athenz1", false)).thenReturn(null);

        List<Notification> notifications = new GroupMemberExpiryNotificationTask(dbsvc, USER_DOMAIN_PREFIX, new NotificationToEmailConverterCommon(null)).getNotifications();

        // we should get 0 notifications
        assertEquals(notifications, new ArrayList<>());
    }

    @Test
    public void testGetEmailBody() {
        NotificationToEmailConverterCommon notificationToEmailConverterCommon = new NotificationToEmailConverterCommon(null);
        System.setProperty("athenz.notification_workflow_url", "https://athenz.example.com/workflow");
        System.setProperty("athenz.notification_support_text", "#Athenz slack channel");
        System.setProperty("athenz.notification_support_url", "https://link.to.athenz.channel.com");

        Map<String, String> details = new HashMap<>();
        details.put("domain", "dom1");
        details.put("group", "group1");
        details.put("member", "user.member1");
        details.put("reason", "test reason");
        details.put("requester", "user.requester");

        Notification notification = new Notification();
        notification.setDetails(details);
        GroupMemberExpiryNotificationTask.GroupExpiryDomainNotificationToEmailConverter converter = new GroupMemberExpiryNotificationTask.GroupExpiryDomainNotificationToEmailConverter(notificationToEmailConverterCommon);
        NotificationEmail notificationAsEmail = converter.getNotificationAsEmail(notification);

        String body = notificationAsEmail.getBody();
        assertNotNull(body);
        assertFalse(body.contains("user.member1"));

        // Make sure support text and url do not appear

        assertFalse(body.contains("slack"));
        assertFalse(body.contains("link.to.athenz.channel.com"));

        // now set the correct expiry members details
        // with one bad entry that should be skipped

        details.put(NOTIFICATION_DETAILS_MEMBERS_LIST,
                "user.joe;group1;2020-12-01T12:00:00.000Z|user.jane;group1;2020-12-01T12:00:00.000Z|user.bad;group3");

        NotificationEmail notificationAsEmailWithMembers = converter.getNotificationAsEmail(notification);
        body = notificationAsEmailWithMembers.getBody();
        assertNotNull(body);
        assertTrue(body.contains("user.joe"));
        assertTrue(body.contains("user.jane"));
        assertTrue(body.contains("group1"));
        assertTrue(body.contains("2020-12-01T12:00:00.000Z"));

        // make sure the bad entries are not included

        assertFalse(body.contains("user.bad"));
        assertFalse(body.contains("group3"));

        // Make sure support text and url do not appear

        assertFalse(body.contains("slack"));
        assertFalse(body.contains("link.to.athenz.channel.com"));

        // now try the expiry groups reminder
        notification = new Notification();
        notification.setDetails(details);
        details.put(NOTIFICATION_DETAILS_ROLES_LIST,
                "athenz1;group1;2020-12-01T12:00:00.000Z|athenz2;group2;2020-12-01T12:00:00.000Z");
        GroupMemberExpiryNotificationTask.GroupExpiryPrincipalNotificationToEmailConverter principalConverter = new GroupMemberExpiryNotificationTask.GroupExpiryPrincipalNotificationToEmailConverter(notificationToEmailConverterCommon);
        NotificationEmail principalNotificationAsEmail = principalConverter.getNotificationAsEmail(notification);

        body = principalNotificationAsEmail.getBody();
        assertNotNull(body);
        assertTrue(body.contains("athenz1"));
        assertTrue(body.contains("athenz2"));
        assertTrue(body.contains("group1"));
        assertTrue(body.contains("group2"));
        assertTrue(body.contains("2020-12-01T12:00:00.000Z"));

        // Make sure support text and url do not appear

        assertFalse(body.contains("slack"));
        assertFalse(body.contains("link.to.athenz.channel.com"));

        System.clearProperty("athenz.notification_workflow_url");
        System.clearProperty("notification_support_text");
        System.clearProperty("notification_support_url");
    }

    @Test
    public void testGetEmailSubject() {
        Notification notification = new Notification();
        NotificationToEmailConverterCommon notificationToEmailConverterCommon = new NotificationToEmailConverterCommon(null);
        GroupMemberExpiryNotificationTask.GroupExpiryDomainNotificationToEmailConverter converter = new GroupMemberExpiryNotificationTask.GroupExpiryDomainNotificationToEmailConverter(notificationToEmailConverterCommon);
        NotificationEmail notificationAsEmail = converter.getNotificationAsEmail(notification);
        String subject = notificationAsEmail.getSubject();
        assertEquals(subject, "Athenz Domain Group Member Expiration Notification");

        notification = new Notification();
        GroupMemberExpiryNotificationTask.GroupExpiryPrincipalNotificationToEmailConverter principalConverter = new GroupMemberExpiryNotificationTask.GroupExpiryPrincipalNotificationToEmailConverter(notificationToEmailConverterCommon);
        notificationAsEmail = principalConverter.getNotificationAsEmail(notification);
        subject = notificationAsEmail.getSubject();
        assertEquals(subject, "Athenz Group Member Expiration Notification");
    }

    @Test
    public void testGetNotificationAsMetric() {
        Timestamp currentTimeStamp = Timestamp.fromCurrentTime();
        Timestamp twentyDaysFromNow = ZMSTestUtils.addDays(currentTimeStamp, 20);
        Timestamp twentyFiveDaysFromNow = ZMSTestUtils.addDays(currentTimeStamp, 25);

        Map<String, String> details = new HashMap<>();
        details.put(NOTIFICATION_DETAILS_DOMAIN, "dom1");
        details.put(NOTIFICATION_DETAILS_MEMBERS_LIST,
                "user.joe;group1;" + twentyFiveDaysFromNow + "|user.jane;group1;" + twentyDaysFromNow + "|user.bad;group3");

        Notification notification = new Notification();
        notification.setDetails(details);

        GroupMemberExpiryNotificationTask.GroupExpiryDomainNotificationToMetricConverter domainConverter = new GroupMemberExpiryNotificationTask.GroupExpiryDomainNotificationToMetricConverter();

        final NotificationMetric notificationAsMetrics = domainConverter.getNotificationAsMetrics(notification, currentTimeStamp);

        final String[] expectedRecord1 = new String[] {
                METRIC_NOTIFICATION_TYPE_KEY, "domain_group_membership_expiry",
                METRIC_NOTIFICATION_DOMAIN_KEY, "dom1",
                METRIC_NOTIFICATION_MEMBER_KEY, "user.joe",
                METRIC_NOTIFICATION_GROUP_KEY, "group1",
                METRIC_NOTIFICATION_EXPIRY_DAYS_KEY, "25"
        };

        final String[] expectedRecord2 = new String[] {
                METRIC_NOTIFICATION_TYPE_KEY, "domain_group_membership_expiry",
                METRIC_NOTIFICATION_DOMAIN_KEY, "dom1",
                METRIC_NOTIFICATION_MEMBER_KEY, "user.jane",
                METRIC_NOTIFICATION_GROUP_KEY, "group1",
                METRIC_NOTIFICATION_EXPIRY_DAYS_KEY, "20"
        };

        final List<String[]> expectedAttributes = new ArrayList<>();
        expectedAttributes.add(expectedRecord1);
        expectedAttributes.add(expectedRecord2);

        assertEquals(new NotificationMetric(expectedAttributes), notificationAsMetrics);

        details.put(NOTIFICATION_DETAILS_ROLES_LIST,
                "athenz1;group1;" + twentyFiveDaysFromNow + "|athenz2;group2;" + twentyDaysFromNow);
        details.put(NOTIFICATION_DETAILS_MEMBER, "user.joe");

        notification = new Notification();
        notification.setDetails(details);

        GroupMemberExpiryNotificationTask.GroupExpiryPrincipalNotificationToToMetricConverter principalConverter = new GroupMemberExpiryNotificationTask.GroupExpiryPrincipalNotificationToToMetricConverter();

        final NotificationMetric notificationAsMetricsPrincipal = principalConverter.getNotificationAsMetrics(notification, currentTimeStamp);

        final String[] expectedRecord3 = new String[] {
                METRIC_NOTIFICATION_TYPE_KEY, "principal_group_membership_expiry",
                METRIC_NOTIFICATION_MEMBER_KEY, "user.joe",
                METRIC_NOTIFICATION_DOMAIN_KEY, "athenz1",
                METRIC_NOTIFICATION_GROUP_KEY, "group1",
                METRIC_NOTIFICATION_EXPIRY_DAYS_KEY, "25"
        };

        final String[] expectedRecord4 = new String[] {
                METRIC_NOTIFICATION_TYPE_KEY, "principal_group_membership_expiry",
                METRIC_NOTIFICATION_MEMBER_KEY, "user.joe",
                METRIC_NOTIFICATION_DOMAIN_KEY, "athenz2",
                METRIC_NOTIFICATION_GROUP_KEY, "group2",
                METRIC_NOTIFICATION_EXPIRY_DAYS_KEY, "20"
        };

        final List<String[]> expectedAttributesPrincipal = new ArrayList<>();
        expectedAttributesPrincipal.add(expectedRecord3);
        expectedAttributesPrincipal.add(expectedRecord4);

        assertEquals(new NotificationMetric(expectedAttributesPrincipal), notificationAsMetricsPrincipal);
    }
}
