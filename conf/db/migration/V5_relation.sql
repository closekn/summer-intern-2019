-- 紐付け定義 (sample)
--------------
CREATE TABLE "relation" (
  "id"              INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  "organization_id" INT      NOT NULL,
  "facility_id"     INT      NOT NULL,
  "updated_at"  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  "created_at"  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 紐付け情報 (sample)
INSERT INTO "relation" ("organization_id", "facility_id") VALUES ('1', '1');
INSERT INTO "relation" ("organization_id", "facility_id") VALUES ('1', '2');
INSERT INTO "relation" ("organization_id", "facility_id") VALUES ('2', '3');
INSERT INTO "relation" ("organization_id", "facility_id") VALUES ('2', '4');
