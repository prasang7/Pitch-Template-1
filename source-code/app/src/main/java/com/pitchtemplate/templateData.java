package com.pitchtemplate;

/**
 * Created by Sid on 1/12/17.
 */

public class templateData {
    String EventName,EventType,timeDate,websiteURL,Address,Description,openClosed,targetAudience;
    String duration,organizersName,orgEmail,orgContact,sponsor,appCredits;

    public templateData(String eventName, String eventType, String timeDate, String websiteURL, String address, String description, String openClosed, String targetAudience, String duration, String organizersName, String orgEmail, String orgContact, String sponsor, String appCredits) {
        EventName = eventName;
        EventType = eventType;
        this.timeDate = timeDate;
        this.websiteURL = websiteURL;
        Address = address;
        Description = description;
        this.openClosed = openClosed;
        this.targetAudience = targetAudience;
        this.duration = duration;
        this.organizersName = organizersName;
        this.orgEmail = orgEmail;
        this.orgContact = orgContact;
        this.sponsor = sponsor;
        this.appCredits = appCredits;
    }

    public String getEventName() {
        return EventName;
    }

    public String getEventType() {
        return EventType;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public String getAddress() {
        return Address;
    }

    public String getDescription() {
        return Description;
    }

    public String getOpenClosed() {
        return openClosed;
    }

    public String getTargetAudience() {
        return targetAudience;
    }

    public String getDuration() {
        return duration;
    }

    public String getOrganizersName() {
        return organizersName;
    }

    public String getOrgEmail() {
        return orgEmail;
    }

    public String getOrgContact() {
        return orgContact;
    }

    public String getSponsor() {
        return sponsor;
    }

    public String getAppCredits() {
        return appCredits;
    }
}
