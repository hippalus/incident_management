package com.incident.management.domain;

import com.google.common.base.Preconditions;
import com.incident.management.domain.exceptions.PropertyRequiredException;
import lombok.Data;

import java.sql.Blob;

@Data
public class Incident {

    private final String title;
    private final Priority priority;
    private final User createdBy;
    private final String description;
    private Long id;
    private IncidentStatus status= IncidentStatus.OPEN;
    private Blob content;
    private AssigneeID assigneeID;
    private Resolution resolution;

    private IncidentVersion fixVersion;

    private Incident(String title, Priority priority, User createdBy, String description, Long id, Blob content, AssigneeID assigneeID) {
        this.title = title;
        this.priority = priority;
        this.createdBy = createdBy;
        this.description = description;
        this.id = id;
        this.content = content;
        this.assigneeID = assigneeID;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void assignTo(AssigneeID assigneeID) {
        Preconditions.checkArgument(assigneeID != null, "AssigneeID can not be null!");
        this.status = status.assign();
        this.assigneeID = assigneeID;
    }

    public void fixedIn(IncidentVersion version) {
        Preconditions.checkArgument(version!=null,"Incident version can not be null!");
        this.status = status.resolved();
        this.resolution=Resolution.FIXED;
        this.fixVersion=version;
    }



    public static class Builder {

        private Long id;
        private String title;
        private Priority priority;
        private User createdBy;
        private String description;
        private Blob content;
        private AssigneeID assigneeID;


        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withPriority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder withCreatedBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withContent(Blob content) {
            this.content = content;
            return this;
        }

        public Builder withAssignee(AssigneeID assigneeID) {
            this.assigneeID = assigneeID;
            return this;
        }

        public Incident build() {
            checkProperty();
            return new Incident(title, priority, createdBy, description, this.id, content, assigneeID);
        }

        private void checkProperty() {
            if (title == null) throw new PropertyRequiredException(this.getClass().getName(), "title");
            if (description == null) throw new PropertyRequiredException(this.getClass().getName(), "description");
            if (priority == null) throw new PropertyRequiredException(this.getClass().getName(), "category");
            if (createdBy == null) throw new PropertyRequiredException(this.getClass().getName(), "createdBy");
        }

    }


}
