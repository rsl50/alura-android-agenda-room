package br.com.alura.agenda.database.dao;

import android.arch.persistence.room.Dao;

import br.com.alura.agenda.model.Telefone;

@Dao
public interface TelefoneDAO {
    Telefone buscaPrimeiroTelefoneDoAluno();
}
