package util;

import com.google.common.base.Objects;

/**
 * Used for putting LDBN/NBN classes and storing changes made to those nodes.
 *
 * @author devinmcgloin
 * @version 8/16/15.
 */
public final class Record {
    final String operation;
    final String key;
    final String valOne;
    final String valTwo;


    public Record(String operation, String second, String valOne) {
        this.operation = operation;
        this.key = second;
        this.valOne = valOne;
        this.valTwo = null;
    }

    public Record(String key, String second) {
        this.operation = key;
        this.key = second;
        this.valOne = null;
        this.valTwo = null;
    }

    public Record(String operation, String key, String valOne, String valTwo) {
        this.operation = operation;
        this.key = key;
        this.valOne = valOne;
        this.valTwo = valTwo;
    }

    public String getOperation() {
        return operation;
    }

    public String getKey() {
        return key;
    }

    public String getVal() {
        return valOne;
    }

    public String getOldVal() {
        return valOne;
    }

    public String getNewVal() {
        return valTwo;
    }


    @Override
    public String toString() {
        return "Record{" +
                "operation='" + operation + '\'' +
                ", key='" + key + '\'' +
                ", valOne='" + valOne + '\'' +
                ", valTwo='" + valTwo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equal(operation, record.operation) &&
                Objects.equal(key, record.key) &&
                Objects.equal(valOne, record.valOne) &&
                Objects.equal(valTwo, record.valTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(operation, key, valOne, valTwo);
    }
}
