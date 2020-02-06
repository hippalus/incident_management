package com.incident.management.domain;

import com.google.common.base.Preconditions;
import com.incident.management.domain.exceptions.PropertyRequiredException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import static com.incident.management.domain.Incident.RelatedIncident.RelationshipType;
import static com.incident.management.domain.Incident.RelatedIncident.RelationshipType.*;

@Data
@Entity
@Table(name = "incident")
@EqualsAndHashCode(exclude = "related")
public class Incident implements Serializable {

    @Id
    private IncidentNumber id;
    @Column
    private String title;
    @Column
    private Priority priority;
    @Column
    private User createdBy;
    @Column
    private String description;
    @Enumerated(EnumType.STRING)
    private IncidentStatus status = IncidentStatus.OPEN;
    @Lob
    private transient Blob content;
    @Column
    private AssigneeID assignee;
    @Enumerated(EnumType.STRING)
    private Resolution resolution;

    @OneToMany(mappedBy = "source", cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    private Set<RelatedIncident> related = new HashSet<>();
    @Column
    private IncidentVersion fixVersion;
    @Column
    private IncidentVersion occurredIn;
    @Column
    private LocalDateTime createdAt;


    protected Incident() {
    }

    private Incident(Builder builder) {
        this.title = builder.title;
        this.priority = builder.priority;
        this.createdBy = builder.createdBy;
        this.description = builder.description;
        this.id = builder.id;
        this.content = builder.content;
        this.assignee = builder.assignee;
        this.createdAt=builder.createdAt;
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
        return hasRelationshipTo(incidentNum, DUPLICATES);
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

    public void markAsBlockerFor(IncidentNumber blocked) {
        Preconditions.checkArgument(blocked!=null);
        related.add(RelatedIncident.markAsBlockerFor(this,blocked));
    }


    public void relatedTo(IncidentNumber other) {
        Preconditions.checkArgument(other != null);
        this.related.add(RelatedIncident.relatedTo(this, other));
    }

    public void markAsBeingDuplicatedBy(IncidentNumber duplicate) {
    related.add(RelatedIncident.duplicateBy(this,duplicate));
    }

    @Entity
    @Getter@Setter
    @Table(name = "related_incident")
    public static class RelatedIncident implements Serializable {
        private static final long serialVersionUID = 1905122041950251207L;

        public enum RelationshipType {
            BLOCKED_BY, RELATED_TO, DUPLICATED_BY, BLOCKER, DUPLICATES
        }

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long  id;

        @ManyToOne(cascade = {CascadeType.ALL})
        @JoinColumn(name = "source_incident_id")
        private Incident source;

        @Column(name = "target_incident_id")
        private IncidentNumber target;

        @Enumerated(EnumType.STRING)
        private RelationshipType type;

        protected RelatedIncident() {
        }

        private RelatedIncident(Incident source, IncidentNumber target, RelationshipType type) {
            this.source = source;
            this.target = target;
            this.type = type;
        }

        public static RelatedIncident duplicateOf(Incident source, IncidentNumber target) {
            return new RelatedIncident(source, target, DUPLICATES);
        }

        public static RelatedIncident blockedBy(Incident source, IncidentNumber target) {
            return new RelatedIncident(source, target, BLOCKED_BY);
        }

        public static RelatedIncident markAsBlockerFor(Incident source, IncidentNumber blocked) {
            return new RelatedIncident(source,blocked, BLOCKER);
        }

        public static RelatedIncident relatedTo(Incident source, IncidentNumber target) {
            return new RelatedIncident(source, target, RELATED_TO);
        }

        public static RelatedIncident duplicateBy(Incident source, IncidentNumber target) {
            return new RelatedIncident(source,target, DUPLICATED_BY);
        }

        @Override
        public String toString() {
            return "";
        }

    }


    public static class Builder {

        private IncidentNumber id;
        private String title;
        private Priority priority;
        private User createdBy;
        private String description;
        private Blob content;
        private AssigneeID assignee;
        private LocalDateTime createdAt;


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
            this.assignee = assigneeID;
            return this;
        }

        public Builder withCreateAt(LocalDateTime createdAt) {
            this.createdAt=createdAt;
            return this;
        }

        public Incident build() {
            checkProperty();
            return new Incident(this);
        }

        private void checkProperty() {
            if (title == null) throw new PropertyRequiredException(this.getClass().getName(), "title");
            if (description == null) throw new PropertyRequiredException(this.getClass().getName(), "description");
            if (priority == null) throw new PropertyRequiredException(this.getClass().getName(), "priority");
            if (createdBy == null) throw new PropertyRequiredException(this.getClass().getName(), "createdBy");
        }
    }


}
