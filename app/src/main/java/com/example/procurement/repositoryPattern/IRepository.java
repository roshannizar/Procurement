package com.example.procurement.repositoryPattern;

public interface IRepository {

    void WriteFireStore();
    void ReadFireStore();
    void DeleteFireStore();
    void UpdateFireStore();

}
