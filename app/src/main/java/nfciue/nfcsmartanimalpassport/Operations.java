package nfciue.nfcsmartanimalpassport;

import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ASLI on 28.3.2018.
 */

public class Operations implements Serializable {
    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    private String operationType;
    private String operatorId;
    private Date operationDate;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private String comment;

    public Operations(String operationType, String operatorId, Date operationDate, String comment) {
        this.operationType = operationType;
        this.operatorId = operatorId;
        this.operationDate = operationDate;
        this.comment = comment;
    }

    public Operations() {
    }
}
