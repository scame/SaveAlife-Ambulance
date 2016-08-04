package com.example.scame.savealife.presentation.presenters;


public interface Presenter<T> {

    void setView(T view);

    void resume();

    void pause();

    void destroy();
}
