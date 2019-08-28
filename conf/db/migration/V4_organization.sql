-- 組織定義 (sample)
--------------
CREATE TABLE "organization" (
  "id"           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
  "location_id"  VARCHAR(8)   NOT NULL,
  "kanziName"    VARCHAR(255) NOT NULL,
  "furiganaName" VARCHAR(255) NOT NULL,
  "englishName"  VARCHAR(255) NOT NULL,
  "address"      VARCHAR(255) NOT NULL,
  "description"  TEXT         DEFAULT NULL,
  "updated_at"   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  "created_at"   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 組織情報 (sample)
INSERT INTO "organization" ("location_id", "kanziName", "furiganaName", "englishName", "address", "description") VALUES ('22100', '仮組織1', 'かりそしき1', 'test-organization1', '静岡県静岡市1-1', '仮の静岡の組織です');
INSERT INTO "organization" ("location_id", "kanziName", "furiganaName", "englishName", "address", "description") VALUES ('22100', '仮組織2', 'かりそしき2', 'test-organization2', '静岡県静岡市1-2', '仮の静岡の組織です');
INSERT INTO "organization" ("location_id", "kanziName", "furiganaName", "englishName", "address", "description") VALUES ('22100', '仮組織3', 'かりそしき3', 'test-organization3', '静岡県静岡市1-3', '仮の静岡の組織です');
INSERT INTO "organization" ("location_id", "kanziName", "furiganaName", "englishName", "address", "description") VALUES ('22100', '仮組織4', 'かりそしき4', 'test-organization4', '静岡県静岡市1-4', '仮の静岡の組織です');
INSERT INTO "organization" ("location_id", "kanziName", "furiganaName", "englishName", "address", "description") VALUES ('22100', '仮組織5', 'かりそしき5', 'test-organization5', '静岡県静岡市1-5', '仮の静岡の組織です');
INSERT INTO "organization" ("location_id", "kanziName", "furiganaName", "englishName", "address", "description") VALUES ('22100', '仮組織6', 'かりそしき6', 'test-organization6', '静岡県静岡市1-6', '仮の静岡の組織です');
INSERT INTO "organization" ("location_id", "kanziName", "furiganaName", "englishName", "address", "description") VALUES ('22100', '仮組織7', 'かりそしき7', 'test-organization7', '静岡県静岡市1-7', '仮の静岡の組織です');
INSERT INTO "organization" ("location_id", "kanziName", "furiganaName", "englishName", "address", "description") VALUES ('22100', '仮組織8', 'かりそしき8', 'test-organization8', '静岡県静岡市1-8', '仮の静岡の組織です');

