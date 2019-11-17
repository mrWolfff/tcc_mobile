package com.example.tcc_mobile.interfaces;

import com.example.tcc_mobile.classes.Demandas;

public interface Actions {

    public void undo();

    public void toast(Demandas demandas);

    public void edit(int position);

}