
 create table incident (id binary(255) not null, assignee binary(255),
 content blob, created_at timestamp, created_by binary(255),
  description varchar(255), fix_version binary(255),
   occurred_in binary(255), priority integer,
   resolution varchar(255), status varchar(255), title varchar(255), primary key (id));

create table related_incident (type varchar(255) not null
                                       , target binary(255) not null, source_ticket_number binary(255) not null,
                                        primary key (type, target, source_ticket_number));

 alter table related_incident add constraint FK9t13i0fbmyrdi84r18qemwbu2 foreign key (source_ticket_number) references incident