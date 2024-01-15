package com.exataid.apontamentoplantio;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.exataid.apontamentoplantio.APDatabase;

public class Migrations {
    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `AP_CARGADEMUDA` ADD COLUMN `enviado` TEXT DEFAULT 'N'");
        }
    };
}
