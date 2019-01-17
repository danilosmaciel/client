package com.dan.projeto.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

public class CarregaBanco {
    private final String NOME_BANCO = "projetodb";

    public BancoProjeto getDb(Context contexto){
        return Room
                .databaseBuilder(contexto, BancoProjeto.class, NOME_BANCO)
                .allowMainThreadQueries()
                .build();
    }
}
