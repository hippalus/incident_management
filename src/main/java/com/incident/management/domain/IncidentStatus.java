package com.incident.management.domain;

public enum IncidentStatus implements ChainStatus {

    OPEN {
        @Override
        public IncidentStatus close() {
            return ChainStatus.cannotTransitTo(this, CLOSED);
        }

        @Override
        public IncidentStatus reopen() {
            return ChainStatus.cannotTransitTo(this, OPEN);
        }
    },

    ASSIGNED {
        @Override
        public IncidentStatus close() {
            return ChainStatus.cannotTransitTo(this, CLOSED);
        }
    },
    CLOSED {
        @Override
        public IncidentStatus assign() {
            return ChainStatus.cannotTransitTo(this, ASSIGNED);
        }

        @Override
        public IncidentStatus resolve() {
            return ChainStatus.cannotTransitTo(this, RESOLVED);
        }

        @Override
        public IncidentStatus close() {
            return ChainStatus.cannotTransitTo(this, CLOSED);
        }
    },
    RESOLVED


}
