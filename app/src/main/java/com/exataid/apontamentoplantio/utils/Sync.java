package com.exataid.apontamentoplantio.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;

import androidx.annotation.Nullable;

import com.exataid.apontamentoplantio.APDatabase;
import com.exataid.apontamentoplantio.banco.modelos.ApontamentoPlantio;
import com.exataid.apontamentoplantio.banco.modelos.CargaDeMuda;
import com.exataid.apontamentoplantio.banco.modelos.ConfigWS;
import com.exataid.apontamentoplantio.banco.modelos.Fazendas;
import com.exataid.apontamentoplantio.banco.modelos.Funcionario;
import com.exataid.apontamentoplantio.banco.modelos.LogSync;
import com.exataid.apontamentoplantio.banco.modelos.Procedencia;
import com.exataid.apontamentoplantio.banco.modelos.SistemaPlantio;
import com.exataid.apontamentoplantio.banco.modelos.Talhao;
import com.exataid.apontamentoplantio.banco.modelos.TipoPlantio;
import com.exataid.apontamentoplantio.banco.modelos.Variedade;
import com.exataid.apontamentoplantio.utils.interfaces.endpoints.repos.CadastroRepo;
import com.exataid.apontamentoplantio.utils.interfaces.endpoints.repos.EnvioRepo;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sync extends Service implements Runnable {
    static int fazendasTerminou = 0;
    static int talhoesTerminou = 0;
    static int funcionariosTerminou = 0;
    static int tipoTerminou = 0;
    static int sistemaPlantioTerminou = 0;
    static int variedadeTerminou = 0;
    static int procedenciaTerminou = 0;
    static int eRegistros = 0;
    private static List<ApontamentoPlantio> apontamentos;
    private final Context context;
    private static ApontamentoPlantio pacoteApontamentoPlantio;

    public Sync(Context ctx) {
        context = ctx;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        // ValidaModulos(context);
        AtualizaCadastrosSistema(context);
    }

    public static String retornaDataAgora() {
        String Data;
        Calendar calendario = Calendar.getInstance();
        SimpleDateFormat formata = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK);
        Data = formata.format(calendario.getTime());
        return Data;
    }

    public static void AtualizaCadastrosSistema(Context context) {
        ConfigWS configWS = null;
        try {
            configWS = APDatabase.getInstance(context).getConfigWSDAO().buscarUm().get();
            CadastroRepo repo = null;
            EnvioRepo envioRepo = null;
            try {
                repo = CadastroRepo.getInstance(configWS);
                envioRepo = EnvioRepo.getInstance(configWS);
            } catch (Exception e) {
                return;
            }
//            CADASTRO DE FAZENDAS
            repo.getService().baixarTodasFazendas().enqueue(new Callback<List<Fazendas>>() {
                @Override
                public void onResponse(Call<List<Fazendas>> call, Response<List<Fazendas>> response) {
                    try {
                        if (response.body() != null) {
                            List<Fazendas> resposta = response.body();
                            APDatabase.getInstance(context).getFazendasDAO().inserir(resposta).get();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_FAZENDAS",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(resposta.size())),
                                            "FINALIZADO")
                            ).get();
                            fazendasTerminou = 1;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        try {
                            e.printStackTrace();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_FAZENDAS",
                                            retornaDataAgora(),
                                            0L,
                                            Arrays.toString(e.getStackTrace()))
                            ).get();
                        } catch (ExecutionException | InterruptedException y) {
                            y.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Fazendas>> call, Throwable t) {

                }
            });
//            CADASTRO TALHOES
            repo.getService().baixarTodosTalhoes().enqueue(new Callback<List<Talhao>>() {
                @Override
                public void onResponse(Call<List<Talhao>> call, Response<List<Talhao>> response) {
                    try {
                        if (response.body() != null) {
                            List<Talhao> resposta = response.body();
                            APDatabase.getInstance(context).getTalhaoDAO().inserir(resposta).get();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_TALHÕES",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(resposta.size())),
                                            "FINALIZADO")
                            ).get();
                            talhoesTerminou = 1;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        try {
                            e.printStackTrace();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_TALHÕES",
                                            retornaDataAgora(),
                                            0L,
                                            Arrays.toString(e.getStackTrace()))
                            ).get();
                        } catch (ExecutionException | InterruptedException y) {
                            y.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Talhao>> call, Throwable t) {

                }
            });
//            CADASTRO FUNCIONÁRIOS
            repo.getService().baixarTodosFuncionarios().enqueue(new Callback<List<Funcionario>>() {
                @Override
                public void onResponse(Call<List<Funcionario>> call, Response<List<Funcionario>> response) {
                    try {
                        if (response.isSuccessful()) {
                            List<Funcionario> resposta = response.body();
                            List<Funcionario> ativos = resposta.stream().filter(funcionario -> funcionario.getAtivo().equalsIgnoreCase("Ativo")).collect(Collectors.toList());
                            List<Funcionario> inativos = resposta.stream().filter(funcionario -> funcionario.getAtivo().equalsIgnoreCase("Inativo")).collect(Collectors.toList());
                            APDatabase.getInstance(context).getFuncionarioDAO().inserir(ativos).get();
                            if (inativos.size() > 0) {
                                APDatabase.getInstance(context).getFuncionarioDAO().excluir(inativos).get();
                            }
                            APDatabase.getInstance(context).getFuncionarioDAO().inserir(resposta).get();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_FUNCIONÁRIOS",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(resposta.size())),
                                            "FINALIZADO")
                            ).get();
                            funcionariosTerminou = 1;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        try {
                            e.printStackTrace();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_FUNCIONÁRIOS",
                                            retornaDataAgora(),
                                            0L,
                                            Arrays.toString(e.getStackTrace()))
                            ).get();
                        } catch (ExecutionException | InterruptedException y) {
                            y.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Funcionario>> call, Throwable t) {

                }
            });
//           CADASTROS_TIPOS_PLANTIO
            repo.getService().baixarTodosTiposPlantio().enqueue(new Callback<List<TipoPlantio>>() {
                @Override
                public void onResponse(Call<List<TipoPlantio>> call, Response<List<TipoPlantio>> response) {
                    try {
                        if (response.body() != null) {
                            List<TipoPlantio> resposta = response.body();
                            APDatabase.getInstance(context).getTipoPlantioDAO().inserir(resposta).get();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_TIPOS_DE_PLANTIO",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(resposta.size())),
                                            "FINALIZADO")
                            ).get();
                            tipoTerminou = 1;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        try {
                            e.printStackTrace();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_TIPOS_DE_PLANTIO",
                                            retornaDataAgora(),
                                            0L,
                                            Arrays.toString(e.getStackTrace()))
                            ).get();
                        } catch (ExecutionException | InterruptedException y) {
                            y.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<TipoPlantio>> call, Throwable t) {

                }
            });

            //           CADASTROS_VARIEDADES
            repo.getService().baixarTodasVariedades().enqueue(new Callback<List<Variedade>>() {
                @Override
                public void onResponse(Call<List<Variedade>> call, Response<List<Variedade>> response) {
                    try {
                        if (response.body() != null) {
                            List<Variedade> resposta = response.body();
                            APDatabase.getInstance(context).getVariedadeDAO().inserir(resposta).get();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_VARIEDADES",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(resposta.size())),
                                            "FINALIZADO")
                            ).get();
                            variedadeTerminou = 1;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        try {
                            e.printStackTrace();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_VARIEDADES",
                                            retornaDataAgora(),
                                            0L,
                                            Arrays.toString(e.getStackTrace()))
                            ).get();
                        } catch (ExecutionException | InterruptedException y) {
                            y.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Variedade>> call, Throwable t) {

                }
            });

            //           CADASTROS_SISTEMA_PLANTIO
            repo.getService().baixarTodosSistemaPlantio().enqueue(new Callback<List<SistemaPlantio>>() {
                @Override
                public void onResponse(Call<List<SistemaPlantio>> call, Response<List<SistemaPlantio>> response) {
                    try {
                        if (response.body() != null) {
                            List<SistemaPlantio> resposta = response.body();
                            APDatabase.getInstance(context).getSistemaPlantioDAO().inserir(resposta).get();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_SISTEMAS_DE_PLANTIO",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(resposta.size())),
                                            "FINALIZADO")
                            ).get();
                            sistemaPlantioTerminou = 1;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        try {
                            e.printStackTrace();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_SISTEMAS_DE_PLANTIO",
                                            retornaDataAgora(),
                                            0L,
                                            Arrays.toString(e.getStackTrace()))
                            ).get();
                        } catch (ExecutionException | InterruptedException y) {
                            y.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<SistemaPlantio>> call, Throwable t) {

                }
            });

//           CADASTROS_PROCEDENCIA
            repo.getService().baixarTodasProcedencias().enqueue(new Callback<List<Procedencia>>() {
                @Override
                public void onResponse(Call<List<Procedencia>> call, Response<List<Procedencia>> response) {
                    try {
                        if (response.body() != null) {
                            List<Procedencia> resposta = response.body();
                            APDatabase.getInstance(context).getProcedenciaDAO().inserir(resposta).get();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_PROCEDENCIAS",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(resposta.size())),
                                            "FINALIZADO")
                            ).get();
                            sistemaPlantioTerminou = 1;
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        try {
                            e.printStackTrace();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "CADASTROS_PROCEDENCIAS",
                                            retornaDataAgora(),
                                            0L,
                                            Arrays.toString(e.getStackTrace()))
                            ).get();
                        } catch (ExecutionException | InterruptedException y) {
                            y.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Procedencia>> call, Throwable t) {

                }
            });

            APDatabase db = APDatabase.getInstance(context.getApplicationContext());
            //ENVIO DE REGISTRO/APONTAMENTO
            if (ConfigGerais.UsuarioLogado != null) {
                apontamentos = APDatabase.getInstance(context).getApontamentoPlantioDAO().buscarPorNaoEnviado("N", ConfigGerais.UsuarioLogado.getNome()).get();
                List<CargaDeMuda> cargaMuda = APDatabase.getInstance(context).getCargaDeMudaDAO().buscarPorApontamento("N", ConfigGerais.UsuarioLogado.getNome()).get();
                if (apontamentos.size() == 0 & cargaMuda.size() == 0) return;
                for (ApontamentoPlantio re : apontamentos) {
                    re.setCargaDeMuda(cargaMuda);
                    pacoteApontamentoPlantio = re;
                }

                envioRepo.getService().enviarRegistros(pacoteApontamentoPlantio).enqueue(new Callback<ApontamentoPlantio>() {
                    @Override
                    public void onResponse(Call<ApontamentoPlantio> call, Response<ApontamentoPlantio> response) {
                        if (response.body() != null) {
                            if (apontamentos.size() == 0) return;
                            for (int i = 0; i <= apontamentos.size(); i++) {
                                ApontamentoPlantio apontamentoPlantioOK = apontamentos.stream().filter(u -> u.getEnviado().equalsIgnoreCase("N")).findFirst().orElse(null);
                                if (apontamentoPlantioOK != null) {
                                    apontamentoPlantioOK.setEnviado("S");
                                    apontamentoPlantioOK.setDataEnviado(U_Data_Hora.retornaData(0, U_Data_Hora.YYYY_MM_DD_HH_MM_SS_SSS));
                                    db.getApontamentoPlantioDAO().atualizar(apontamentoPlantioOK);
                                }
                            }
                            if (cargaMuda.size() == 0) return;
                            for (int i = 0; i <= cargaMuda.size(); i++) {
                                CargaDeMuda cargas = cargaMuda.stream().filter(u -> "N".equalsIgnoreCase(u.getEnviado())).findFirst().orElse(null);
                                if (cargas != null) {
                                    cargas.setEnviado("S");
                                    cargas.setDataEnviado(U_Data_Hora.retornaData(0, U_Data_Hora.YYYY_MM_DD_HH_MM_SS_SSS));
                                    db.getCargaDeMudaDAO().atualizar(cargas);
                                }
                            }
                            try {
                                APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                        new LogSync(
                                                "APONTAMENTOS_PLANTIOS",
                                                retornaDataAgora(),
                                                Long.parseLong(String.valueOf(apontamentos.size())),
                                                "FINALIZADO")
                                ).get();
                                APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                        new LogSync(
                                                "APONTAMENTOS_CARGAS",
                                                retornaDataAgora(),
                                                Long.parseLong(String.valueOf(cargaMuda.size())),
                                                "FINALIZADO")
                                ).get();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApontamentoPlantio> call, Throwable t) {
                        t.printStackTrace();
                        try {
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "APONTAMENTOS_PLANTIOS",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(apontamentos.size())), "FALHOU")
                            ).get();
                            APDatabase.getInstance(context).getLogSyncDAO().inserir(
                                    new LogSync(
                                            "APONTAMENTOS_CARGAS",
                                            retornaDataAgora(),
                                            Long.parseLong(String.valueOf(cargaMuda.size())), "FALHOU")
                            ).get();

                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (ExecutionException | InterruptedException u) {
            u.printStackTrace();
        }
    }
}
