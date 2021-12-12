package com.iwbfly.myhttp.retry;

import com.iwbfly.myhttp.exceptions.RetryableException;
import com.iwbfly.myhttp.reflection.MyhttpRequest;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @auther: pangyajun
 * @create: 2021/11/25 21:30
 **/
public interface Retryer {

    int continueRetry(RetryableException ex) throws Throwable;
    Retryer clone();
    Retryer clone(int maxAttempts, long period);

    class Default implements Retryer {

        private final int maxAttempts;
        private final long period;
        int attempt;

        public Default() {
            this(600, 3);
        }

        public Default( int maxAttempts,long period) {
            this.period = period;
            this.maxAttempts = maxAttempts;
            this.attempt = 0;
        }

        public int continueRetry(RetryableException e) {
            if (attempt++ >= maxAttempts) {
                throw e;
            }
            long interval = nextMaxInterval();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
                throw e;
            }
            return attempt;
        }
        /**
         * Calculates the time interval to a retry attempt. <br>
         * The interval increases exponentially with each attempt, at a rate of nextInterval *= 1.5
         * (where 1.5 is the backoff factor), to the maximum interval.
         *
         * @return time in nanoseconds from now until the next attempt.
         */
        long nextMaxInterval() {
            long interval = (long) (period * Math.pow(1.5, attempt - 1));
            return interval ;
        }

        @Override
        public Retryer clone() {
            return new Default(maxAttempts,period);
        }

        @Override
        public Retryer clone(int maxAttempts, long period) {
            return new Default(maxAttempts,period);
        }
    }

    class Never implements Retryer {

        @Override
        public int continueRetry(RetryableException e) throws Throwable {
            throw e;
        }

        @Override
        public Retryer clone() {
            return this;
        }

        @Override
        public Retryer clone(int maxAttempts, long period) {
            return this;
        }
    }
    /**
     * Implementation that never retries request. It propagates the RetryableException.
     */
    Retryer NEVER_RETRY = new Never();

    Retryer DEFAULT = new Default();



}
