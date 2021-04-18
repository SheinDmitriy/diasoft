DROP TABLE IF EXISTS tAccessToken;

CREATE TABLE tAccessToken (
  TokenID     int NOT NULL AUTO_INCREMENT,
  UserID      int NOT NULL,
  AuditID     int NOT NULL,
  ExpireDate   timestamp NOT NULL,
  PRIMARY KEY (TokenID),
  CONSTRAINT FK_tUser_tAccessToken_UserID FOREIGN KEY (UserID)
  REFERENCES tUser (UserID),

  CONSTRAINT FK_tAudit_tAccessToken_AuditID FOREIGN KEY (AuditID)
  REFERENCES tAudit (AuditID)
) ENGINE=InnoDB;