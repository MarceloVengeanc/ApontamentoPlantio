package com.exataid.apontamentoplantio;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;

import com.exataid.apontamentoplantio.banco.dao.AutenticaLoginDAO;
import com.exataid.apontamentoplantio.banco.dao.CargaDeMudaDAO;
import com.exataid.apontamentoplantio.banco.dao.ColetorDAO;
import com.exataid.apontamentoplantio.banco.dao.ConfigWSDAO;
import com.exataid.apontamentoplantio.banco.dao.FazendasDAO;
import com.exataid.apontamentoplantio.banco.dao.FuncionarioDAO;
import com.exataid.apontamentoplantio.banco.dao.LogSyncDAO;
import com.exataid.apontamentoplantio.banco.dao.ApontamentoPlantioDAO;
import com.exataid.apontamentoplantio.banco.dao.ProcedenciaDAO;
import com.exataid.apontamentoplantio.banco.dao.SincDAO;
import com.exataid.apontamentoplantio.banco.dao.SistemaPlantioDAO;
import com.exataid.apontamentoplantio.banco.dao.TalhaoDAO;
import com.exataid.apontamentoplantio.banco.dao.TipoPlantioDAO;
import com.exataid.apontamentoplantio.banco.dao.VariedadeDAO;
import com.exataid.apontamentoplantio.banco.modelos.ApontamentoPlantio;
import com.exataid.apontamentoplantio.banco.modelos.AutenticaLogin;
import com.exataid.apontamentoplantio.banco.modelos.CargaDeMuda;
import com.exataid.apontamentoplantio.banco.modelos.Coletor;
import com.exataid.apontamentoplantio.banco.modelos.ConfigWS;
import com.exataid.apontamentoplantio.banco.modelos.Fazendas;
import com.exataid.apontamentoplantio.banco.modelos.Funcionario;
import com.exataid.apontamentoplantio.banco.modelos.LogSync;
import com.exataid.apontamentoplantio.banco.modelos.Procedencia;
import com.exataid.apontamentoplantio.banco.modelos.Sinc;
import com.exataid.apontamentoplantio.banco.modelos.SistemaPlantio;
import com.exataid.apontamentoplantio.banco.modelos.Talhao;
import com.exataid.apontamentoplantio.banco.modelos.TipoPlantio;
import com.exataid.apontamentoplantio.banco.modelos.Variedade;
import com.exataid.apontamentoplantio.utils.Conversores;

@Database(
        entities = {
                Fazendas.class,
                Talhao.class,
                Funcionario.class,
                ApontamentoPlantio.class,
                SistemaPlantio.class,
                TipoPlantio.class,
                Variedade.class,
                Procedencia.class,
                CargaDeMuda.class,
                ConfigWS.class,
                Sinc.class,
                LogSync.class,
                Coletor.class,
                AutenticaLogin.class

        },
        version = 5, exportSchema = false
)
@TypeConverters({Conversores.class})
public abstract class APDatabase extends RoomDatabase {
    public static final String AP_DATABASE = "apontamentoPlantio_db";
    private static APDatabase INSTANCE;

    public abstract FazendasDAO getFazendasDAO();

    public abstract TalhaoDAO getTalhaoDAO();

    public abstract FuncionarioDAO getFuncionarioDAO();

    public abstract ApontamentoPlantioDAO getApontamentoPlantioDAO();
    public abstract SistemaPlantioDAO getSistemaPlantioDAO();
    public abstract TipoPlantioDAO getTipoPlantioDAO();
    public abstract VariedadeDAO getVariedadeDAO();
    public abstract ProcedenciaDAO getProcedenciaDAO();
    public abstract CargaDeMudaDAO getCargaDeMudaDAO();
    public abstract ConfigWSDAO getConfigWSDAO();

    public abstract SincDAO getSincDAO();

    public abstract LogSyncDAO getLogSyncDAO();

    public abstract ColetorDAO getColetorDAO();

    public abstract AutenticaLoginDAO getAutenticaLoginDAO();

    public Migration[] migrations = {};

    public static APDatabase getInstance(Context ctx) {
        return Room.databaseBuilder(ctx, APDatabase.class, AP_DATABASE)
                .fallbackToDestructiveMigration().build();
//                .addMigrations(Migrations.MIGRATION_3_4).build();
    }
}
