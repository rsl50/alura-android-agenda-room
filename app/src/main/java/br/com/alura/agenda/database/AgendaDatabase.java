package br.com.alura.agenda.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

@Database(entities = {Aluno.class}, version = 2, exportSchema = false)
public abstract class AgendaDatabase extends RoomDatabase {

    private static volatile AgendaDatabase instance = null;
    private static final String NOME_BANCO_DE_DADOS = "agenda.db";

    public abstract AlunoDAO getRoomAlunoDAO();

    // Método que centraliza a criação da instância da classe facilitando seu reuso
    public static AgendaDatabase getInstance(Context context) {

        // Criação do Database via Singleton
        // https://developer.android.com/training/data-storage/room/
        // https://en.wikipedia.org/wiki/Singleton_pattern
        if (instance == null) {
            synchronized(AgendaDatabase.class) {
                if (instance == null) {
                    instance = Room
                            .databaseBuilder(context, AgendaDatabase.class, NOME_BANCO_DE_DADOS)
                            .allowMainThreadQueries()
                            .addMigrations(new Migration(1, 2) {
                                //explicando: new Migration(versaoAtualDoDatabase, novaVersaoDoDatabase)
                                @Override
                                public void migrate(@NonNull SupportSQLiteDatabase database) {
                                    database.execSQL("ALTER TABLE aluno ADD COLUMN sobrenome TEXT");
                                }
                            })
                            .build();
                }
            }
        }
        return instance;
    }
}