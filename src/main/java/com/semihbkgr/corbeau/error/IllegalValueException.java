package com.semihbkgr.corbeau.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IllegalValueException extends PersistenceException {

    private String table;
    private String column;
    private Object value;

    public IllegalValueException(String message, String table, String column, Object value) {
        super(message);
        this.table = table;
        this.column = column;
        this.value = value;
    }

}
