package br.com.alura.agenda.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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
                            .build();
                }
            }
        }
        return instance;
    }
}