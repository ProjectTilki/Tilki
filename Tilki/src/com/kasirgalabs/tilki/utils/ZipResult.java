package com.kasirgalabs.tilki.utils;

import java.io.File;
import java.util.Objects;

public class ZipResult {

    private final File resultFile;
    private final int resultStatus;

    public ZipResult(File resultName, int resultStatus) {
        this.resultFile = resultName;
        this.resultStatus = resultStatus;
    }

    public File getResultName() {
        return this.resultFile;
    }

    public int getResultStatus() {
        return this.resultStatus;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) {
            return true;
        }
        if(object == null) {
            return false;
        }
        if(this.getClass() != object.getClass()) {
            return false;
        }
        ZipResult zipResult = (ZipResult) object;
        return this.resultFile.equals(zipResult.getResultName())
                && this.resultStatus == zipResult.getResultStatus();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.resultFile);
        hash = 53 * hash + this.resultStatus;
        return hash;
    }
}
