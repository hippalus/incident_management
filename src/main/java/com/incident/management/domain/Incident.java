package com.incident.management.domain;

import com.google.common.base.Preconditions;
import com.incident.management.domain.exceptions.PropertyRequiredException;
import lombok.Data;

import java.io.Serializable;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

import static com.incident.management.domain.Incident.RelatedIncident.RelationshipType;

@Data
public class Incident {

    private final String title;
    private final Priority priority;
    private final User createdBy;
    private final String description;
    private IncidentNumber id;
    private IncidentStatus status = IncidentStatus.OPEN;
    private Blob content;
    private AssigneeID assignee;
    private Resolution resolution;
    private Set<RelatedIncident> related = new HashSet<>();
    private IncidentVersion fixVersion;
    private IncidentVersion occurredIn;

    private Incident(String title, Priority priority, User createdBy, String description, IncidentNumber id, Blob content, AssigneeID assignee) {
        this.title = title;
        this.priority = priority;
        this.createdBy = createdBy;
        this.description = description;
        this.id = id;
        this.content = content;
        this.assignee = assignee;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void assignTo(AssigneeID assignee) {
        Preconditions.checkArgument(assignee != null, "AssigneeID can not be null!");
        this.status = status.assign();
        this.assignee = assignee;
    }

    public void fixedIn(IncidentVersion version) {
        Preconditions.checkArgument(version != null, "Incident version can not be null!");
        this.status = status.resolve();
        this.resolution = Resolution.FIXED;
        this.fixVersion = version;
    }

    public void duplicateOf(IncidentNumber original) {
        Preconditions.checkArgument(original != null);
        this.status = status.resolve();
        this.resolution = Resolution.DUPLICATE;
        related.add(Incident.RelatedIncident.duplicateOf(this, original));
    }

    public boolean isDuplicateOf(IncidentNumber incidentNum) {
        return hasRelationshipTo(incidentNum, RelationshipType.DUPLICATES);
    }

    public boolean hasRelationshipTo(IncidentNumber incidentNumber, RelationshipType relationshipType) {
        Preconditions.checkArgument(incidentNumber != null);
        Preconditions.checkArgument(relationshipType != null);
        return related.stream()
                .anyMatch(relatedIncident ->
                        this == relatedIncident.source
                                && relatedIncident.target.equals(incidentNumber)
                                && relatedIncident.type == relationshipType);
    }

    public void cannotReproduce() {
        this.status = status.resolve();
        resolution = Resolution.CANNOT_REPRODUCE;
    }

    public void wontFix(String reason) {
        Preconditions.checkArgument(reason != null);
        Preconditions.checkArgument(!reason.isEmpty());
        this.status = status.resolve();
        this.resolution = Resolution.WONT_FIX;
    }

    public void close() {
        this.status = status.close();

    }

    public void reopen(IncidentVersion reopenVersion) {
        Preconditions.checkArgument(reopenVersion != null);
        this.status = status.reopen();
        this.occurredIn = reopenVersion;
        this.assignee = null;
    }

    public void blockedBy(IncidentNumber blocker) {
        Preconditions.checkArgument(blocker != null);
        related.add(RelatedIncident.blockedBy(this, blocker));
    }

    public void relatedTo(IncidentNumber other) {
        Preconditions.checkArgument(other != null);
        this.related.add(RelatedIncident.relatedTo(this, other));
    }


    protected static class RelatedIncident implements Serializable {

        private Incident source;
        private IncidentNumber target;
        private RelationshipType type;

        private RelatedIncident() {
        }

        private RelatedIncident(Incident source, IncidentNumber target, RelationshipType type) {
            this.source = source;
            this.target = target;
            this.type = type;
        }

        public static RelatedIncident duplicateOf(Incident source, IncidentNumber target) {
            return new RelatedIncident(source, target, RelationshipType.DUPLICATES);
        }

        public static RelatedIncident blockedBy(Incident source, IncidentNumber target) {
            return new RelatedIncident(source, target, RelationshipType.BLOCKED_BY);
        }

        public static RelatedIncident relatedTo(Incident source, IncidentNumber target) {
            return new RelatedIncident(source, target, RelationshipType.RELATED_TO);
        }

        public enum RelationshipType {
            BLOCKED_BY, RELATED_TO, DUPLICATES
        }


    }


    public static class Builder {

        private IncidentNumber id;
        private String title;
        private Priority priority;
        private User createdBy;
        private String description;
        private Blob content;
        private AssigneeID assigneeID;


        public Builder withId(IncidentNumber id) {
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
