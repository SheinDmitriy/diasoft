DROP TABLE IF EXISTS tAudit;

CREATE TABLE tAudit (
  AuditID     int NOT NULL AUTO_INCREMENT,
  UserID      int NOT NULL,
  ActionType  TINYINT NOT NULL,
  ActionDate  timestamp NOT NULL  DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (AuditID),
  CONSTRAINT FK_tUser_tAudit_UserID FOREIGN KEY (UserID)
  REFERENCES tUser (UserID)
) ENGINE=InnoDB;