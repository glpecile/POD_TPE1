package ar.edu.itba.pod.models;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ReticketingReport {
    @Getter
    private final int success;
    @Getter
    private final List<FailureTicket> failure;

    public ReticketingReport(int success, @NonNull List<FailureTicket> failure) {
        this.success = success;
        this.failure = failure;
    }

    public static class FailureTicket {
        @Getter
        private final String passenger;
        @Getter
        private final String flightCode;

        public FailureTicket(String passenger, String flightCode) {
            this.passenger = passenger;
            this.flightCode = flightCode;
        }
    }
}
