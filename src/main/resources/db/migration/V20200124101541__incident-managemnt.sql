create table public.incident (id binary(255) not null, assignee binary(255),
 createdAt timestamp, createdBy binary(255), description varchar(255), fixVersion binary(255),
  occurredIn binary(255), priority integer, resolution varchar(255), status varchar(255), title varchar(255), primary key (id));

create table public.related_incident (id bigint not null, target_incident_id binary(255), type varchar(255), source_incident_id binary(255), primary key (id));

 alter table public.related_incident add constraint FKt7hds3l0johwgkkg0qrdosbli foreign key (source_incident_id) references incident;