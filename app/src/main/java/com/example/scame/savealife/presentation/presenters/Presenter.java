package com.example.scame.savealife.presentation.presenters;


import com.example.scame.savealife.data.repository.IFileDataManager;

public interface Presenter<T> {

    void setView(T view);

    void resume(IFileDataManager dataManager);

    void pause();

    void destroy();
}
