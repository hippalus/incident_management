package com.incident.management.domain;

import com.incident.management.domain.exceptions.PropertyRequiredException;
import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import java.sql.Blob;

@Data
@AllArgsConstructor
public class Incident {
    private Long id;
    private final String title;
    private final Category category;
    private final User createdBy;
    private final String description;
    private IncidentStatus status=IncidentStatus.OPEN;
    private Blob content;

    public static Builder builder(){

        return new Builder();
    }
    public static class Builder{

        private Long id;
        private String title;
        private Category category;
        private User createdBy;
        private String description;
        private IncidentStatus status=IncidentStatus.OPEN;
        private Blob content;


        public Builder withId(Long id){
            this.id=id;
            return this;
        }
        public  Builder withTitle(String title){
            this.title=title;
            return this;
        }
        public  Builder withCategory( Category category){
            this.category=category;
            return this;
        }
        public  Builder withCreatedBy(User createdBy){
            this.createdBy=createdBy;
            return this;
        }
        public  Builder withDescription(String description){
            this.description=description;
            return this;
        }
        public  Builder withContent(Blob content){
            this.content=content;
            return this;
        }
        public  Builder withStatus(IncidentStatus status){
            this.status=status;
            return this;
        }

        public Incident build(){
            checkProperty();
            return new Incident(this.id,title,category,createdBy,description,status,content);
      }

        private void checkProperty() {
            if (title==null) throw new PropertyRequiredException(this.getClass().getName(),"title");
            if (description==null) throw new PropertyRequiredException(this.getClass().getName(),"description");
            if (category==null) throw new PropertyRequiredException(this.getClass().getName(),"category");
            if (createdBy==null) throw new PropertyRequiredException(this.getClass().getName(),"createdBy");
        }

    }


}
