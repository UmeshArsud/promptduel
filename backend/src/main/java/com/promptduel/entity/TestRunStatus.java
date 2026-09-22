package com.promptduel.entity;

/**
 * Status of an individual test run (version × input combination).
 * Supports per-call failure isolation — one failed call does not abort the batch.
 */
public enum TestRunStatus {
    COMPLETED,
    FAILED
}
