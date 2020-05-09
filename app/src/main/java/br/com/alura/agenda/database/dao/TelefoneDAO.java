package br.com.alura.agenda.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import br.com.alura.agenda.model.Telefone;

@Dao
public interface TelefoneDAO {
    @Query("SELECT t.* FROM Telefone t JOIN Aluno a " +
            "ON t.alunoId = a.id " +
            "WHERE t.alunoId = :alunoId LIMIT 1")
    Telefone buscaPrimeiroTelefoneDoAluno(int alunoId);
}
