package com.example.procurement.RepositoryPattern;

public interface IRepository {

    void WriteFireStore();
    void ReadFireStore();
    void DeleteFireStore();
    void UpdateFireStore();

}
